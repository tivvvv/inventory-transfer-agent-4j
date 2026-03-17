package com.tiv.inventory.transfer.consumer;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.RedisConstants;
import com.tiv.inventory.transfer.constant.TopicConstants;
import com.tiv.inventory.transfer.domain.request.ProductSaleRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class AgentConsumer {

    @Resource
    private CompiledGraph compiledGraph;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @KafkaListener(topics = {TopicConstants.PRODUCT_SALE_TOPIC}, groupId = "group-1")
    public void consume(List<String> messages, Acknowledgment acknowledgment) {
        log.info("AgentConsumer--consume--batch size: {}", messages.size());
        try {
            for (String message : messages) {
                log.info("AgentConsumer--consume--message: {}", message);
                // 解析消息
                ProductSaleRequest request = JSONUtil.toBean(message, ProductSaleRequest.class);
                String redisKey = String.format("%s:%s:%s:%s", RedisConstants.PRODUCT_SALE_KEY, request.getProductId(), request.getWarehouseId(), request.getSaleDate());
                Boolean result = redisTemplate.opsForValue().setIfAbsent(redisKey, message, 1L, TimeUnit.DAYS);
                if (BooleanUtil.isFalse(result)) {
                    log.info("AgentConsumer--consume--message has been consumed, message: {}", message);
                    acknowledgment.acknowledge();
                    return;
                }
                String threadId = IdUtil.simpleUUID();
                Map<String, Object> inputs = Map.of(
                        Constants.PRODUCT_ID, String.valueOf(request.getProductId()),
                        Constants.THREAD_ID, threadId);
                RunnableConfig runnableConfig = RunnableConfig.builder()
                        .threadId(threadId)
                        .build();
                OverAllState overAllState = compiledGraph.call(inputs, runnableConfig).get();
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            log.error("AgentConsumer--consume--fail, error: {}", e.getMessage());
        }
        log.info("AgentConsumer--consume--end");
    }

}
