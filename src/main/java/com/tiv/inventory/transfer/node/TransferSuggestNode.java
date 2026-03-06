package com.tiv.inventory.transfer.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * 调拨建议节点
 */
@Slf4j
@AllArgsConstructor
public class TransferSuggestNode implements NodeAction {

    private final ChatClient client;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取商品销售数据和库存调拨数据
        String productId = state.value(Constants.PRODUCT_ID, "");
        String saleRecordData = state.value(Constants.SALE_RECORD_DATA, "");
        String productInventoryData = state.value(Constants.PRODUCT_INVENTORY_DATA, "");
        String inventoryTransferData = state.value(Constants.INVENTORY_TRANSFER_DATA, "");

        // 2. 调用大模型生成调拨建议
        Flux<String> flux = client.prompt().user(t -> t.text("""
                        商品id:{productId}
                        商品销售数据:{saleRecordData}
                        商品剩余库存:{productInventoryData}
                        商品历史库存调拨数据:{inventoryTransferData}
                        帮我生成一份调拨建议
                        """).params(Map.of(Constants.PRODUCT_ID, productId,
                        Constants.SALE_RECORD_DATA, saleRecordData,
                        Constants.PRODUCT_INVENTORY_DATA, productInventoryData,
                        Constants.INVENTORY_TRANSFER_DATA, inventoryTransferData)))
                .stream()
                .content();
        StringBuilder sb = new StringBuilder();
        flux.doOnNext(sb::append).blockLast();
        log.info("ai调拨建议: {}", sb);
        return Map.of();
    }

}
