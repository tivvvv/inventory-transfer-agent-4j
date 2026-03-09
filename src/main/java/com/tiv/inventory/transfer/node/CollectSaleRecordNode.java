package com.tiv.inventory.transfer.node;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.domain.dto.SaleRecordData;
import com.tiv.inventory.transfer.service.SaleRecordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 采集商品历史销售数据节点
 */
@Slf4j
@AllArgsConstructor
public class CollectSaleRecordNode implements NodeAction {

    private final SaleRecordService saleRecordService;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取商品id
        String productId = state.value(Constants.PRODUCT_ID, "");
        if (StrUtil.isBlank(productId)) {
            return Map.of();
        }

        // 2. 查询商品历史销售数据
        List<SaleRecordData> saleRecordData = saleRecordService.collectSaleRecordDataByProductId(Long.parseLong(productId));
        String saleRecordDataJson = JSONUtil.toJsonStr(saleRecordData);
        log.info("CollectSaleRecordNode--apply--saleRecordDataJson: {}", saleRecordDataJson);

        return Map.of(Constants.SALE_RECORD_DATA, saleRecordDataJson);
    }

}
