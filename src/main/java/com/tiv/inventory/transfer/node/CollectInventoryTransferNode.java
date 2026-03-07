package com.tiv.inventory.transfer.node;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.domain.dto.InventoryTransferData;
import com.tiv.inventory.transfer.service.TransferOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 采集商品库存历史调拨数据节点
 */
@Slf4j
@AllArgsConstructor
public class CollectInventoryTransferNode implements NodeAction {

    private final TransferOrderService transferOrderService;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取商品id
        String productId = state.value(Constants.PRODUCT_ID, "");
        if (StrUtil.isBlank(productId)) {
            return Map.of();
        }

        // 2. 查询商品历史库存调拨数据
        List<InventoryTransferData> inventoryTransferData = transferOrderService.collectInventoryTransferDataByProductId(Long.parseLong(productId));
        String inventoryTransferDataJson = JSONUtil.toJsonStr(inventoryTransferData);
        log.info("apply--inventoryTransferDataJson: {}", inventoryTransferDataJson);

        return Map.of(Constants.INVENTORY_TRANSFER_DATA, inventoryTransferDataJson);
    }

}
