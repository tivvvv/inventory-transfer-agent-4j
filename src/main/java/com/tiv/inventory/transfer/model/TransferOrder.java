package com.tiv.inventory.transfer.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 调拨单信息表
 *
 * @TableName transfer_order
 */
@Data
@TableName(value = "transfer_order")
public class TransferOrder {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调拨单编号
     */
    private String orderCode;

    /**
     * 调出方仓库ID
     */
    private Long sourceWarehouseId;

    /**
     * 调入方仓库ID
     */
    private Long targetWarehouseId;

    /**
     * 处理状态:0-待审核 1-已审核 2-调拨中 3-已完成 4-已取消
     */
    private Integer status;

    /**
     * 调拨方式:0-人工 1-智能推荐
     */
    private Integer transferType;

    /**
     * 计划调拨执行日期
     */
    private Date transferDate;

    /**
     * 调拨原因
     */
    private String comment;

    /**
     * 制单人
     */
    private String createdBy;

    /**
     * 审核人
     */
    private String approvedBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

}