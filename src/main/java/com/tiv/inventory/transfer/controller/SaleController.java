package com.tiv.inventory.transfer.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.tiv.inventory.transfer.constant.Constants;
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
    private CompiledGraph compiledGraph;

    @PostMapping("/product")
    public Map<String, Object> saleProduct(@RequestBody String productId) {
        OverAllState overAllState = compiledGraph.call(Map.of(Constants.PRODUCT_ID, productId)).get();
        return overAllState.data();
    }

}
