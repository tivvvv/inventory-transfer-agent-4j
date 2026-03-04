-- 创建仓库样例数据
INSERT INTO warehouse (warehouse_code, warehouse_name, location, manager)
VALUES ('WH001', '华北仓', '北京', '张三'),
       ('WH002', '华东仓', '上海', '李四'),
       ('WH003', '华南仓', '⼴州', '王五');

-- 创建商品样例数据
INSERT INTO product (product_code, product_name, spec, unit, category)
VALUES ('P001', '智能⼿机', '6.5英⼨ 128G', '台', '电⼦产品'),
       ('P002', '蓝⽛⽿机', '⼊⽿式', '副', '电⼦产品'),
       ('P003', '办公椅', '可调节靠背', '张', '家具'),
       ('P004', '显示器', '27⼨ IPS', '台', '电⼦产品'),
       ('P005', '机械键盘', '104键 RGB', '个', '外设');

-- 创建库存样例数据
INSERT INTO inventory (warehouse_id, product_id, quantity, locked_quantity)
SELECT w.id, p.id, FLOOR(RAND() * 200 + 100), 0
FROM warehouse w,
     product p;

-- 创建销售样例数据
SET @start_date = '2024-01-01';
SET @end_date = '2026-02-28';
INSERT INTO sale_record (warehouse_id, product_id, sale_date, quantity, revenue)
SELECT *
FROM (SELECT w.id                                                            AS warehouse_id,
             p.id                                                            AS product_id,
             DATE_ADD('2024-01-01', INTERVAL (FLOOR(RAND() * 12 * 3)) MONTH) AS sale_date,
             FLOOR(RAND() * 500 + 50)                                        AS quantity,
             ROUND(RAND() * 50000 + 5000, 2)                                 AS revenue
      FROM warehouse w,
           product p,
           (SELECT 1
            FROM dual
            UNION
            SELECT 2
            UNION
            SELECT 3
            UNION
            SELECT 4) q) t
WHERE t.sale_date BETWEEN @start_date AND @end_date;

-- 创建调拨单样例数据
INSERT INTO transfer_order (order_code,
                            source_warehouse_id,
                            target_warehouse_id,
                            status,
                            transfer_type,
                            transfer_date,
                            created_by)
SELECT t.order_code,
       t.source_warehouse_id,
       t.target_warehouse_id,
       t.status,
       t.transfer_type,
       t.transfer_date,
       t.created_by
FROM (SELECT CONCAT('TO', LPAD(FLOOR(RAND() * 99999), 5, '0'))         AS order_code,
             FLOOR(1 + RAND() * 3)                                     AS source_warehouse_id,
             FLOOR(1 + RAND() * 3)                                     AS target_warehouse_id,
             3                                                         AS status,
             1                                                         AS transfer_type,
             DATE_ADD('2023-01-01', INTERVAL FLOOR(RAND() * 36) MONTH) AS transfer_date,
             '系统⾃动'                                                 AS created_by
      FROM (SELECT 1
            FROM dual
            UNION
            SELECT 2
            UNION
            SELECT 3
            UNION
            SELECT 4
            UNION
            SELECT 5) tmp) t
WHERE t.source_warehouse_id != t.target_warehouse_id;

-- 创建调拨单商品明细样例数据
INSERT INTO transfer_order_item (transfer_order_id, product_id, transfer_quantity, actual_quantity, remark)
SELECT o.id,
       p.id,
       FLOOR(RAND() * 50 + 10),
       FLOOR(RAND() * 50 + 10),
       '季度调拨'
FROM transfer_order o,
     product p
WHERE o.status = 3
ORDER BY o.id
LIMIT 50;

-- 创建库存变动记录样例数据
INSERT INTO inventory_record (warehouse_id, product_id, change_type, quantity_change, order_code, remark)
SELECT t.source_warehouse_id,
       i.product_id,
       'TRANSFER_OUT',
       -titem.transfer_quantity,
       o.order_code,
       '系统自动调拨出库'
FROM transfer_order o
         JOIN transfer_order_item titem ON o.id = titem.transfer_order_id
         JOIN inventory i ON titem.product_id = i.product_id
         JOIN transfer_order t ON o.id = t.id;

INSERT INTO inventory_record (warehouse_id, product_id, change_type, quantity_change, order_code, remark)
SELECT t.target_warehouse_id,
       i.product_id,
       'TRANSFER_IN',
       titem.transfer_quantity,
       o.order_code,
       '系统自动调拨⼊库'
FROM transfer_order o
         JOIN transfer_order_item titem ON o.id = titem.transfer_order_id
         JOIN inventory i ON titem.product_id = i.product_id
         JOIN transfer_order t ON o.id = t.id;