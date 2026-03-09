package com.tiv.inventory.transfer.node;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.domain.dto.TransferSuggest;
import com.tiv.inventory.transfer.service.TransferOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 创建调拨单节点
 */
@Slf4j
@AllArgsConstructor
public class CreateTransferOrderNode implements NodeAction {

    private final TransferOrderService transferOrderService;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String transferSuggestFormattedData = state.value(Constants.TRANSFER_SUGGEST_FORMATTED_DATA, "");
        TransferSuggest transferSuggest = JSONUtil.toBean(transferSuggestFormattedData, TransferSuggest.class);

        log.info("CreateTransferOrderNode--apply--transferSuggest: {}", transferSuggest);
        transferOrderService.createTransferOrder(transferSuggest);
        return Map.of();
    }

}
