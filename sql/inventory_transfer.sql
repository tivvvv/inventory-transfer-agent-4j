CREATE TABLE warehouse
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_code VARCHAR(50)  NOT NULL UNIQUE COMMENT '仓库编码',
    warehouse_name VARCHAR(100) NOT NULL COMMENT '仓库名称',
    location       VARCHAR(255) DEFAULT NULL COMMENT '仓库地址',
    manager        VARCHAR(50)  DEFAULT NULL COMMENT '仓库负责人',
    status         TINYINT      DEFAULT 1 COMMENT '启用状态:1-启用 0-停用',
    created_time   DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time   DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='仓库信息表';

CREATE TABLE product
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    product_code VARCHAR(50)  NOT NULL UNIQUE COMMENT '商品编码',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    spec         VARCHAR(100) DEFAULT NULL COMMENT '商品规格',
    unit         VARCHAR(20)  DEFAULT '件' COMMENT '计量单位',
    category     VARCHAR(50)  DEFAULT NULL COMMENT '商品分类',
    status       TINYINT      DEFAULT 1 COMMENT '商品状态:1-启用 0-停用',
    created_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='商品信息表';

CREATE TABLE sale_record
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_id BIGINT         NOT NULL COMMENT '仓库ID',
    product_id   BIGINT         NOT NULL COMMENT '商品ID',
    sale_date    DATE           NOT NULL COMMENT '销售日期',
    quantity     DECIMAL(18, 2) NOT NULL COMMENT '销售出库数量',
    revenue      DECIMAL(18, 2) DEFAULT 0 COMMENT '销售收入金额',
    created_time DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) COMMENT ='商品销售记录表';

CREATE TABLE inventory
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_id    BIGINT NOT NULL COMMENT '仓库ID',
    product_id      BIGINT NOT NULL COMMENT '商品ID',
    quantity        DECIMAL(18, 2) DEFAULT 0 COMMENT '库存总量',
    locked_quantity DECIMAL(18, 2) DEFAULT 0 COMMENT '被锁定的库存数量',
    created_time    DATETIME       DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time    DATETIME       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_ware_prod (warehouse_id, product_id)
) COMMENT ='库存信息表';

CREATE TABLE transfer_order
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    order_code          VARCHAR(50) NOT NULL UNIQUE COMMENT '调拨单编号',
    source_warehouse_id BIGINT      NOT NULL COMMENT '调出方仓库ID',
    target_warehouse_id BIGINT      NOT NULL COMMENT '调入方仓库ID',
    status              TINYINT     DEFAULT 0 COMMENT '处理状态:0-待审核 1-已审核 2-调拨中 3-已完成 4-已取消',
    transfer_type       TINYINT     DEFAULT 0 COMMENT '调拨方式:0-人工 1-智能推荐',
    transfer_date       DATE        DEFAULT NULL COMMENT '计划调拨执行日期',
    comment             TEXT COMMENT '调拨原因',
    created_by          VARCHAR(50) DEFAULT NULL COMMENT '制单人',
    approved_by         VARCHAR(50) DEFAULT NULL COMMENT '审核人',
    created_time        DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time        DATETIME    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT ='调拨单信息表';

CREATE TABLE transfer_order_item
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    transfer_order_id BIGINT         NOT NULL COMMENT '调拨单ID',
    product_id        BIGINT         NOT NULL COMMENT '调拨商品ID',
    transfer_quantity DECIMAL(18, 2) NOT NULL COMMENT '计划调拨数量',
    actual_quantity   DECIMAL(18, 2) DEFAULT 0 COMMENT '实际接收确认数量',
    remark            VARCHAR(255)   DEFAULT NULL COMMENT '备注信息'
) COMMENT ='调拨单商品明细表';

CREATE TABLE inventory_record
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    warehouse_id    BIGINT         NOT NULL COMMENT '仓库ID',
    product_id      BIGINT         NOT NULL COMMENT '商品ID',
    change_type     VARCHAR(50)    NOT NULL COMMENT '库存变更类型:IN-入库 OUT-出库 TRANSFER_OUT-调拨出库 TRANSFER_IN-调拨入库',
    quantity_change DECIMAL(18, 2) NOT NULL COMMENT '库存变化量:正值为增加 负值为减少',
    order_code      VARCHAR(50)  DEFAULT NULL COMMENT '调拨单编号',
    created_time    DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    remark          VARCHAR(255) DEFAULT NULL COMMENT '备注信息'
) COMMENT ='库存变动记录表';