package com.tiv.inventory.transfer.consumer;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.TopicConstants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AgentConsumer {

    @Resource
    private CompiledGraph compiledGraph;

    @KafkaListener(topics = {TopicConstants.PRODUCT_SALE_TOPIC}, groupId = "group-1")
    public void consume(List<String> messages, Acknowledgment acknowledgment) {
        log.info("AgentConsumer--consume--batch size: {}", messages.size());
        try {
            for (String message : messages) {
                log.info("AgentConsumer--consume--message: {}", message);
                // 解析消息
                JSONObject jsonObject = JSONUtil.parseObj(message);
                Integer productId = jsonObject.getInt(Constants.PRODUCT_ID);
                String threadId = IdUtil.simpleUUID();
                Map<String, Object> inputs = Map.of(
                        Constants.PRODUCT_ID, productId,
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
