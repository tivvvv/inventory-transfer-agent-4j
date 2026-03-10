package com.tiv.inventory.transfer.node;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 通知节点
 */
@Slf4j
@AllArgsConstructor
public class NotifyNode implements NodeAction {

    private final EmailService emailService;

    private final String receiver;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String threadId = state.value(Constants.THREAD_ID, "");
        String transferSuggestFormattedData = state.value(Constants.TRANSFER_SUGGEST_FORMATTED_DATA, "");
        JSONObject jsonObject = JSONUtil.parseObj(transferSuggestFormattedData);
        String comment = jsonObject.getStr(Constants.COMMENT);
        Map<String, Object> variables = Map.of(
                Constants.COMMENT, comment,
                Constants.APPROVE_LINK, "http://localhost:8113/review?approval=true&threadId=" + threadId,
                Constants.REJECT_LINK, "http://localhost:8113/review?approval=false&threadId=" + threadId);

        log.info("NotifyNode--apply-receiver: {}, variables: {}", receiver, variables);
        emailService.sendTemplateEmail(receiver, variables);

        return Map.of();
    }

}
