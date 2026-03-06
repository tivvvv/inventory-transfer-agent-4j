package com.tiv.inventory.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiv.inventory.transfer.domain.dto.InventoryTransferData;
import com.tiv.inventory.transfer.model.TransferOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TransferOrderMapper extends BaseMapper<TransferOrder> {

    @Select("""
            SELECT
            toi.product_id,
            p.product_code,
            p.product_name,
            YEAR(t.transfer_date) as year,
            QUARTER(t.transfer_date) as quarter,
            t.source_warehouse_id,
            sw.warehouse_code as sourceWarehouseCode,
            sw.warehouse_name as sourceWarehouseName,
            t.target_warehouse_id,
            tw.warehouse_code as targetWarehouseCode,
            tw.warehouse_name as targetWarehouseName,
            SUM(toi.transfer_quantity) as totalTransferQty
            FROM transfer_order t
            LEFT JOIN transfer_order_item toi ON t.id = toi.transfer_order_id
            LEFT JOIN product p ON toi.product_id = p.id
            LEFT JOIN warehouse sw ON t.source_warehouse_id = sw.id
            LEFT JOIN warehouse tw ON t.target_warehouse_id = tw.id
            WHERE t.status = 3 AND toi.product_id = #{productId}
            GROUP BY toi.product_id, YEAR(t.transfer_date), QUARTER(t.transfer_date), t.source_warehouse_id, t.target_warehouse_id
            ORDER BY YEAR(t.transfer_date), QUARTER(t.transfer_date), t.source_warehouse_id, t.target_warehouse_id
            """)
    List<InventoryTransferData> collectInventoryTransferDataByProductId(Long productId);

}