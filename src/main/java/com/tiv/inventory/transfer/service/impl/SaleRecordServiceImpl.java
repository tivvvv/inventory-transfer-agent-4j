package com.tiv.inventory.transfer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.constant.TopicConstants;
import com.tiv.inventory.transfer.domain.dto.SaleRecordData;
import com.tiv.inventory.transfer.domain.request.ProductSaleRequest;
import com.tiv.inventory.transfer.mapper.SaleRecordMapper;
import com.tiv.inventory.transfer.model.Inventory;
import com.tiv.inventory.transfer.model.SaleRecord;
import com.tiv.inventory.transfer.service.InventoryService;
import com.tiv.inventory.transfer.service.SaleRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RefreshScope
public class SaleRecordServiceImpl extends ServiceImpl<SaleRecordMapper, SaleRecord> implements SaleRecordService {

    @Resource
    private SaleRecordMapper saleRecordMapper;

    @Resource
    private InventoryService inventoryService;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${threshold.productQuantity}")
    private Long threshold;

    @Override
    public List<SaleRecordData> collectSaleRecordDataByProductId(Long productId) {
        return saleRecordMapper.collectSaleRecordDataByProductId(productId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sale(ProductSaleRequest productSaleRequest) {
        // 1. 保存销售记录
        SaleRecord saleRecord = new SaleRecord();
        BeanUtil.copyProperties(productSaleRequest, saleRecord);
        this.save(saleRecord);

        // 2. 更新库存剩余数量
        inventoryService.update(Wrappers.lambdaUpdate(Inventory.class)
                .eq(Inventory::getWarehouseId, productSaleRequest.getWarehouseId())
                .eq(Inventory::getProductId, productSaleRequest.getProductId())
                .setSql("quantity = quantity - " + productSaleRequest.getQuantity()));

        // 3. 查询剩余库存
        Inventory inventory = inventoryService.getOne(Wrappers.lambdaQuery(Inventory.class)
                .eq(Inventory::getWarehouseId, productSaleRequest.getWarehouseId())
                .eq(Inventory::getProductId, productSaleRequest.getProductId()));

        // 4. 判断剩余库存是否小于阈值
        if (inventory.getQuantity() > threshold) {
            return;
        }
        log.info("剩余库存小于阈值,发送消息");
        kafkaTemplate.send(TopicConstants.PRODUCT_SALE_TOPIC, JSONUtil.toJsonStr(productSaleRequest));
    }

}