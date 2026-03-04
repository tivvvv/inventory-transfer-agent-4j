package com.tiv.inventory.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiv.inventory.transfer.domain.dto.SaleRecordData;
import com.tiv.inventory.transfer.model.SaleRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SaleRecordMapper extends BaseMapper<SaleRecord> {

    @Select("""
            SELECT sr.product_id,
                   p.product_code,
                   p.product_name,
                   YEAR(sr.sale_date) AS year,
                   QUARTER(sr.sale_date) AS quarter,
                   sr.warehouse_id,
                   w.warehouse_code,
                   w.warehouse_name,
                   SUM(sr.quantity) AS totalSaleQuantity
            FROM sale_record sr
            LEFT JOIN product p ON sr.product_id = p.id
            LEFT JOIN warehouse w ON sr.warehouse_id = w.id
            WHERE sr.product_id = #{productId}
            GROUP BY sr.product_id, YEAR(sr.sale_date), QUARTER(sr.sale_date), sr.warehouse_id
            ORDER BY YEAR(sr.sale_date), QUARTER(sr.sale_date), sr.warehouse_id
            """)
    List<SaleRecordData> collectSaleRecordDataByProductId(@Param("productId") Long productId);

}