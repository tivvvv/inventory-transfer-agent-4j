package com.tiv.inventory.transfer.controller;

import cn.hutool.core.util.IdUtil;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.tiv.inventory.transfer.common.BusinessResponse;
import com.tiv.inventory.transfer.constant.Constants;
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
    private CompiledGraph compiledGraph;

    @PostMapping("/product")
    public BusinessResponse<Map<String, Object>> saleProduct(@RequestBody String productId) {
        String threadId = IdUtil.simpleUUID();
        Map<String, Object> inputs = Map.of(
                Constants.PRODUCT_ID, productId,
                Constants.THREAD_ID, threadId);
        RunnableConfig runnableConfig = RunnableConfig.builder()
                .threadId(threadId)
                .build();
        OverAllState overAllState = compiledGraph.call(inputs, runnableConfig).get();
        return ResultUtils.success(overAllState.data());
    }

}
