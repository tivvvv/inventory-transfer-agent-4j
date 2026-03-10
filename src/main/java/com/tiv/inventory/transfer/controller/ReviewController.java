package com.tiv.inventory.transfer.controller;

import com.tiv.inventory.transfer.common.BusinessResponse;
import com.tiv.inventory.transfer.service.ReviewService;
import com.tiv.inventory.transfer.util.ResultUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 审核控制器
 */
@Slf4j
@RestController
@RequestMapping("/review")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @GetMapping()
    public BusinessResponse<?> review(@RequestParam(value = "approval") Boolean approval,
                                      @RequestParam(value = "threadId") String threadId) {
        reviewService.review(approval, threadId);
        return ResultUtils.success();
    }

}