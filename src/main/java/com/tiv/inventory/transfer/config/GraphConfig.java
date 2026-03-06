package com.tiv.inventory.transfer.config;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.AsyncNodeAction;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.NodeConstants;
import com.tiv.inventory.transfer.node.CollectInventoryTransferNode;
import com.tiv.inventory.transfer.node.CollectProductInventoryNode;
import com.tiv.inventory.transfer.node.CollectSaleRecordNode;
import com.tiv.inventory.transfer.node.TransferSuggestNode;
import com.tiv.inventory.transfer.service.InventoryService;
import com.tiv.inventory.transfer.service.SaleRecordService;
import com.tiv.inventory.transfer.service.TransferOrderService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 图配置类
 */
@Configuration
public class GraphConfig {

    @Resource
    private SaleRecordService saleRecordService;

    @Resource
    private InventoryService inventoryService;

    @Resource
    private TransferOrderService transferOrderService;

    @Bean
    public CompiledGraph graph(ChatClient.Builder chatClientBuilder) throws GraphStateException {

        // 全局状态机
        KeyStrategyFactory keyStrategyFactory = () -> Map.of(Constants.PRODUCT_ID, new ReplaceStrategy(),
                Constants.SALE_RECORD_DATA, new ReplaceStrategy(),
                Constants.PRODUCT_INVENTORY_DATA, new ReplaceStrategy(),
                Constants.INVENTORY_TRANSFER_DATA, new ReplaceStrategy());
        StateGraph stateGraph = new StateGraph(Constants.INVENTORY_TRANSFER_GRAPH, keyStrategyFactory);

        // 定义节点
        stateGraph.addNode(NodeConstants.COLLECT_SALE_RECORD_NODE, AsyncNodeAction.node_async(new CollectSaleRecordNode(saleRecordService)));
        stateGraph.addNode(NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE, AsyncNodeAction.node_async(new CollectProductInventoryNode(inventoryService)));
        stateGraph.addNode(NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE, AsyncNodeAction.node_async(new CollectInventoryTransferNode(transferOrderService)));
        stateGraph.addNode(NodeConstants.TRANSFER_SUGGEST_NODE, AsyncNodeAction.node_async(new TransferSuggestNode(chatClientBuilder.build())));

        // 定义边
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_SALE_RECORD_NODE);
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE);
        stateGraph.addEdge(StateGraph.START, NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE);

        stateGraph.addEdge(NodeConstants.COLLECT_SALE_RECORD_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);
        stateGraph.addEdge(NodeConstants.COLLECT_PRODUCT_INVENTORY_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);
        stateGraph.addEdge(NodeConstants.COLLECT_INVENTORY_TRANSFER_NODE, NodeConstants.TRANSFER_SUGGEST_NODE);

        stateGraph.addEdge(NodeConstants.TRANSFER_SUGGEST_NODE, StateGraph.END);

        return stateGraph.compile();
    }

}
