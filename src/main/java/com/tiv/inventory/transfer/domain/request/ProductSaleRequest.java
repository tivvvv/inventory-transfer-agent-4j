package com.tiv.inventory.transfer.domain.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductSaleRequest {

    private Long warehouseId;

    private Long productId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date saleDate;

    private Long quantity;

    private BigDecimal revenue;

}
