package com.tiv.inventory.transfer.node;

import cn.hutool.core.date.DateUtil;
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

    private final static String SYSTEM_PROMPT = """
            # 角色:
               你是一名专业的供应链分析师和库存管理专家, 擅长基于销售数据和季节趋势进行精准的库存预测和调拨规划.
            
            # 任务:
               严格基于提供的商品历史销售数据, 商品剩余库存数据, 库存历史调拨数据和当前日期, 分析销售模式, 预测未来需求, 并生成一份科学合理的库存调拨单.
            
            # 分析要求:
            ## 第一步: 销售模式分析
             1. 识别销售的季节性规律和趋势.
             2. 分析促销活动对销售的影响程度.
             3. 计算季度环比增长率和同比增长率.
            
            ## 第二步: 需求预测
             1. 基于历史数据和当前时间, 预测未来周期的需求量.
             2. 考虑季节性因素和增长趋势.
             3. 评估预测的不确定性范围.
            
            ## 第三步: 库存优化计算
             1. 计算各仓库的安全库存水平, 减少库存积压与缺货风险.
             2. 识别库存过剩或不足的仓库.
             3. 确定最优的库存分配方案.
            
            # 输出要求:
             生成一份格式化的“库存调拨单”.
             不可凭空虚构数据, 若数据不足, 请说明假设.
             输出结果必须以JSON格式, 便于系统解析, 输出数据中禁止出现运算.
             只返回两个仓库之间的调拨方案即可.
             将详细的解释说明放在comment字段中.
             输出示例结构:
             {
               "sourceWarehouseId": 1,
               "targetWarehouseId": 2,
               "status": 0,
               "createdBy": "AI智能助手",
               "transferType": 1,
               "transferDate": "2026-01-01",
               "comment": "具体的解释说明"
               "items": [
                 {
                   "productId": 11,
                   "transferQuantity": 500,
                   "actualQuantity": 0,
                   "remark": "备注"
                 },
                 ...
               ]
            }
            """;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        // 1. 从状态机获取商品历史销售数据,商品剩余库存数据和库存历史调拨数据
        String productId = state.value(Constants.PRODUCT_ID, "");
        String saleRecordData = state.value(Constants.SALE_RECORD_DATA, "");
        String productInventoryData = state.value(Constants.PRODUCT_INVENTORY_DATA, "");
        String inventoryTransferData = state.value(Constants.INVENTORY_TRANSFER_DATA, "");

        // 2. 调用大模型生成调拨建议
        Flux<String> flux = client.prompt()
                .system(SYSTEM_PROMPT)
                .user(t -> t.text("""
                        商品id: {productId}
                        商品历史销售数据: {saleRecordData}
                        商品剩余库存数据: {productInventoryData}
                        库存历史调拨数据: {inventoryTransferData}
                        当前日期: {curDate}
                        帮我生成一份库存调拨单, 输出结果必须为JSON格式
                        """).params(Map.of(Constants.PRODUCT_ID, productId,
                        Constants.SALE_RECORD_DATA, saleRecordData,
                        Constants.PRODUCT_INVENTORY_DATA, productInventoryData,
                        Constants.INVENTORY_TRANSFER_DATA, inventoryTransferData,
                        Constants.CUR_DATE, DateUtil.today())))
                .stream()
                .content();
        StringBuilder sb = new StringBuilder();
        flux.doOnNext(sb::append).blockLast();
        log.info("ai调拨建议: {}", sb);
        return Map.of();
    }

}
