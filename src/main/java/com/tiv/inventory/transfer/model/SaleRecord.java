package com.tiv.inventory.transfer.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品销售记录表
 *
 * @TableName sale_record
 */
@Data
@TableName(value = "sale_record")
public class SaleRecord {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 销售日期
     */
    private Date saleDate;

    /**
     * 销售出库数量
     */
    private BigDecimal quantity;

    /**
     * 销售收入金额
     */
    private BigDecimal revenue;

    /**
     * 创建时间
     */
    private Date createdTime;

}