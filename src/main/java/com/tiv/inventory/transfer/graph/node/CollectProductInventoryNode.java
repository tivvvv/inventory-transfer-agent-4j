package com.tiv.inventory.transfer.graph.node;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.model.Inventory;
import com.tiv.inventory.transfer.service.InventoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 采集商品剩余库存数据节点
 */
@Slf4j
@AllArgsConstructor
public class CollectProductInventoryNode implements NodeAction {

    private final InventoryService inventoryService;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取商品id
        String productId = state.value(Constants.PRODUCT_ID, "");
        if (StrUtil.isBlank(productId)) {
            return Map.of();
        }
        // 2. 查询商品剩余库存
        LambdaQueryWrapper<Inventory> lambdaQueryWrapper = Wrappers.lambdaQuery(Inventory.class)
                .eq(Inventory::getProductId, productId);
        List<Inventory> productInventoryData = inventoryService.list(lambdaQueryWrapper);
        String productInventoryDataJson = JSONUtil.toJsonStr(productInventoryData);
        log.info("CollectProductInventoryNode--apply--productInventoryDataJson: {}", productInventoryDataJson);

        return Map.of(Constants.PRODUCT_INVENTORY_DATA, productInventoryDataJson);
    }

}
