package com.tiv.inventory.transfer.domain.dto;

import lombok.Data;

@Data
public class TransferSuggestItem {

    /**
     * 调拨商品ID
     */
    private Long productId;

    /**
     * 计划调拨数量
     */
    private Long transferQuantity;

    /**
     * 备注信息
     */
    private String remark;

}