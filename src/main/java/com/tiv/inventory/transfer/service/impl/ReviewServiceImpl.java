package com.tiv.inventory.transfer.service.impl;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.state.StateSnapshot;
import com.tiv.inventory.transfer.constant.Constants;
import com.tiv.inventory.transfer.service.ReviewService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private CompiledGraph compiledGraph;

    @Override
    public void review(Boolean approval, String threadId) {
        log.info("ReviewServiceImpl--review--threadId: {}, approval: {}", threadId, approval);
        RunnableConfig runnableConfig = RunnableConfig.builder()
                .threadId(threadId)
                .build();

        // 获取当前工作流状态快照
        StateSnapshot stateSnapshot = compiledGraph.getState(runnableConfig);
        OverAllState curState = stateSnapshot.state();
        log.info("ReviewServiceImpl--review--curState: {}", curState);

        // 设置工作流为恢复执行状态
        curState.withResume();
        // 注入人工审核结果
        curState.withHumanFeedback(new OverAllState.HumanFeedback(Map.of(Constants.APPROVAL, approval), ""));
        // 调用工作流继续执行
        compiledGraph.call(curState, runnableConfig).get();
    }

}
