package com.tiv.inventory.transfer.controller;

import com.tiv.inventory.transfer.common.BusinessResponse;
import com.tiv.inventory.transfer.domain.request.ProductSaleRequest;
import com.tiv.inventory.transfer.service.SaleRecordService;
import com.tiv.inventory.transfer.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 销售控制器
 */
@Slf4j
@RestController
@RequestMapping("/sale")
public class SaleController {

    @Resource
    private SaleRecordService saleRecordService;

    @PostMapping("/product")
    public BusinessResponse<Map<String, Object>> saleProduct(@RequestBody ProductSaleRequest productSaleRequest) {
        saleRecordService.sale(productSaleRequest);
        return ResultUtils.success();
    }

}
