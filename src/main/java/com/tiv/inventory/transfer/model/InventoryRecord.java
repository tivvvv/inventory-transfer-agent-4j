package com.tiv.inventory.transfer.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存变动记录表
 *
 * @TableName inventory_record
 */
@Data
@TableName(value = "inventory_record")
public class InventoryRecord {

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
     * 库存变更类型:IN-入库 OUT-出库 TRANSFER_OUT-调拨出库 TRANSFER_IN-调拨入库
     */
    private String changeType;

    /**
     * 库存变化量:正值为增加 负值为减少
     */
    private BigDecimal quantityChange;

    /**
     * 调拨单编号
     */
    private String orderCode;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 备注信息
     */
    private String remark;

}