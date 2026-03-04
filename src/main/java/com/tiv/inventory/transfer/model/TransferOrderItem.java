package com.tiv.inventory.transfer.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 调拨单商品明细表
 *
 * @TableName transfer_order_item
 */
@Data
@TableName(value = "transfer_order_item")
public class TransferOrderItem {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调拨单ID
     */
    private Long transferOrderId;

    /**
     * 调拨商品ID
     */
    private Long productId;

    /**
     * 计划调拨数量
     */
    private Long transferQuantity;

    /**
     * 实际接收确认数量
     */
    private Long actualQuantity;

    /**
     * 备注信息
     */
    private String remark;

}