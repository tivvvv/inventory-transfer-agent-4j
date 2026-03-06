package com.tiv.inventory.transfer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryTransferData {

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 年份
     */
    private String year;

    /**
     * 季度
     */
    private Integer quarter;

    /**
     * 调出方仓库ID
     */
    private Long sourceWarehouseId;

    /**
     * 调出方仓库编码
     */
    private String sourceWarehouseCode;

    /**
     * 调出方仓库名称
     */
    private String sourceWarehouseName;

    /**
     * 调入方仓库ID
     */
    private Long targetWarehouseId;

    /**
     * 调入方仓库编码
     */
    private String targetWarehouseCode;

    /**
     * 调入方仓库名称
     */
    private String targetWarehouseName;

    /**
     * 总调拨数量
     */
    private Long totalTransferQuantity;

}
