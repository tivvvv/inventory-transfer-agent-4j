package com.tiv.inventory.transfer.domain.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TransferSuggest {

    /**
     * 调出方仓库ID
     */
    private Long sourceWarehouseId;

    /**
     * 调入方仓库ID
     */
    private Long targetWarehouseId;

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
     * 调拨明细
     */
    List<TransferSuggestItem> items;

}