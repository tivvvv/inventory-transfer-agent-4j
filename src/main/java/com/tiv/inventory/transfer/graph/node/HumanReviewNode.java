package com.tiv.inventory.transfer.graph.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.NodeConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 人工审核节点
 */
@Slf4j
public class HumanReviewNode implements NodeAction {

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        Map<String, Object> data = state.humanFeedback().data();
        Boolean approval = (Boolean) data.getOrDefault(Constants.APPROVAL, false);

        String nextNode = StateGraph.END;
        if (approval) {
            nextNode = NodeConstants.CREATE_TRANSFER_ORDER_NODE;
        }

        log.info("HumanReviewNode--apply--approval: {}", approval);
        return Map.of(Constants.HUMAN_REVIEW_NEXT_STEP, nextNode);
    }

}
