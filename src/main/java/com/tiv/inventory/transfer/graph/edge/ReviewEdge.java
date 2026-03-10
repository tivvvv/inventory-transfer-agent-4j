package com.tiv.inventory.transfer.graph.edge;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.constant.NodeConstants;

/**
 * 人工审核条件边
 */
public class ReviewEdge implements EdgeAction {

    @Override
    public String apply(OverAllState state) throws Exception {
        String humanReviewNextStep = state.value(Constants.HUMAN_REVIEW_NEXT_STEP, "");
        if (NodeConstants.CREATE_TRANSFER_ORDER_NODE.equals(humanReviewNextStep)) {
            return humanReviewNextStep;
        }
        return StateGraph.END;
    }

}
