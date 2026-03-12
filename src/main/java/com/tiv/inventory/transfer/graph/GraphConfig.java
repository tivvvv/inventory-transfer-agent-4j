package com.tiv.inventory.transfer.graph;

import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.action.AsyncEdgeAction;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.checkpoint.config.SaverConfig;
import com.alibaba.cloud.ai.graph.checkpoint.constant.SaverEnum;
import com.alibaba.cloud.ai.graph.checkpoint.savers.RedisSaver;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.NodeConstants;
import com.tiv.inventory.transfer.graph.edge.ReviewEdge;
import com.tiv.inventory.transfer.graph.node.*;
import com.tiv.inventory.transfer.graph.saver.TtlRedisSaver;
import com.tiv.inventory.transfer.service.EmailService;
import com.tiv.inventory.transfer.service.InventoryService;
import com.tiv.inventory.transfer.service.SaleRecordService;
import com.tiv.inventory.transfer.service.TransferOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 图配置类
 */
@Slf4j
@Configuration
public class GraphConfig {

    @Resource
    private SaleRecordService saleRecordService;

    @Resource
    private InventoryService inventoryService;

    @Resource
    private TransferOrderService transferOrderService;

    @Resource
    private EmailService emailService;

    @Resource
    private RedissonClient redissonClient;

    @Value("${notification.email.receiver}")
    private String receiver;

    @Value("${graph.checkpoint.ttl}")
    private Long checkpointTtl;

    @Bean
    public CompiledGraph graph(ChatClient.Builder chatClientBuilder) throws GraphStateException {
        // 全局状态机
        StateGraph stateGraph = registerStateGraph();

        // 定义节点
        stateGraph.addNode(NodeConstants.COLLECT_SALE_RECORD_NODE, AsyncNodeAction.node_async(new CollectSaleRecordNode(saleRecordService)));
        stateGraph.addNode(NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE, AsyncNodeAction.node_async(new CollectProductInventoryNode(inventoryService)));
        stateGraph.addNode(NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE, AsyncNodeAction.node_async(new CollectInventoryTransferNode(transferOrderService)));
        stateGraph.addNode(NodeConstants.TRANSFER_SUGGEST_NODE, AsyncNodeAction.node_async(new TransferSuggestNode(chatClientBuilder.build())));
        stateGraph.addNode(NodeConstants.PROCESS_SUGGEST_FORMAT_NODE, AsyncNodeAction.node_async(new ProcessSuggestFormatNode(chatClientBuilder.build())));
        stateGraph.addNode(NodeConstants.NOTIFY_NODE, AsyncNodeAction.node_async(new NotifyNode(emailService, receiver)));
        stateGraph.addNode(NodeConstants.CREATE_TRANSFER_ORDER_NODE, AsyncNodeAction.node_async(new CreateTransferOrderNode(transferOrderService)));
        stateGraph.addNode(NodeConstants.HUMAN_REVIEW_NODE, AsyncNodeAction.node_async(new HumanReviewNode()));

        // 定义边
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_SALE_RECORD_NODE);
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE);
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE);

        stateGraph.addEdge(NodeConstants.COLLECT_SALE_RECORD_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);
        stateGraph.addEdge(NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);
        stateGraph.addEdge(NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);

        stateGraph.addEdge(NodeConstants.TRANSFER_SUGGEST_NODE, NodeConstants.PROCESS_SUGGEST_FORMAT_NODE);

        stateGraph.addEdge(NodeConstants.PROCESS_SUGGEST_FORMAT_NODE, NodeConstants.NOTIFY_NODE);

        stateGraph.addEdge(NodeConstants.NOTIFY_NODE, NodeConstants.HUMAN_REVIEW_NODE);

        stateGraph.addConditionalEdges(NodeConstants.HUMAN_REVIEW_NODE, AsyncEdgeAction.edge_async(new ReviewEdge()),
                Map.of(NodeConstants.CREATE_TRANSFER_ORDER_NODE, NodeConstants.CREATE_TRANSFER_ORDER_NODE,
                        StateGraph.END, StateGraph.END));

        stateGraph.addEdge(NodeConstants.CREATE_TRANSFER_ORDER_NODE, StateGraph.END);

        // 持久化配置
        RedisSaver redisSaver = new TtlRedisSaver(redissonClient, checkpointTtl, TimeUnit.HOURS);
        SaverConfig saverConfig = SaverConfig.builder()
                .register(SaverEnum.REDIS.getValue(), redisSaver)
                .build();

        // 编译配置
        CompileConfig compileConfig = CompileConfig.builder()
                .interruptBefore(NodeConstants.HUMAN_REVIEW_NODE)
                .saverConfig(saverConfig)
                .build();

        CompiledGraph compiledGraph = stateGraph.compile(compileConfig);
        GraphRepresentation plantUml = compiledGraph.getGraph(GraphRepresentation.Type.PLANTUML, "AI智能库存调拨Agent UML类图", true);
        log.info("GraphConfig--graph--plantUml: {}", plantUml.content());

        return compiledGraph;
    }

    private StateGraph registerStateGraph() {
        KeyStrategyFactory keyStrategyFactory = () -> Map.of(
                Constants.PRODUCT_ID, new ReplaceStrategy(),
                Constants.SALE_RECORD_DATA, new ReplaceStrategy(),
                Constants.PRODUCT_INVENTORY_DATA, new ReplaceStrategy(),
                Constants.INVENTORY_TRANSFER_DATA, new ReplaceStrategy(),
                Constants.TRANSFER_SUGGEST_RAW_DATA, new ReplaceStrategy(),
                Constants.TRANSFER_SUGGEST_FORMATTED_DATA, new ReplaceStrategy(),
                Constants.HUMAN_REVIEW_NEXT_STEP, new ReplaceStrategy(),
                Constants.THREAD_ID, new ReplaceStrategy());
        return new StateGraph(Constants.INVENTORY_TRANSFER_GRAPH, keyStrategyFactory);
    }

}
