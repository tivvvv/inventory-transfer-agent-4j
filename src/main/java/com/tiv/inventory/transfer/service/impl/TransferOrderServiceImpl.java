package com.tiv.inventory.transfer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.domain.dto.InventoryTransferData;
import com.tiv.inventory.transfer.domain.dto.TransferSuggest;
import com.tiv.inventory.transfer.domain.enums.TransferOrderStatusEnum;
import com.tiv.inventory.transfer.domain.enums.TransferTypeEnum;
import com.tiv.inventory.transfer.mapper.TransferOrderMapper;
import com.tiv.inventory.transfer.model.TransferOrder;
import com.tiv.inventory.transfer.model.TransferOrderItem;
import com.tiv.inventory.transfer.service.TransferOrderItemService;
import com.tiv.inventory.transfer.service.TransferOrderService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements TransferOrderService {

    @Resource
    private TransferOrderMapper transferOrderMapper;

    @Resource
    private TransferOrderItemService transferOrderItemService;

    @Override
    public List<InventoryTransferData> collectInventoryTransferDataByProductId(Long productId) {
        return transferOrderMapper.collectInventoryTransferDataByProductId(productId);
    }

    @Override
    public void createTransferOrder(TransferSuggest transferSuggest) {
        // 1. 保存调拨单
        TransferOrder transferOrder = new TransferOrder();
        BeanUtil.copyProperties(transferSuggest, transferOrder);
        transferOrder.setOrderCode(IdUtil.simpleUUID())
                .setStatus(TransferOrderStatusEnum.PENDING_AUDIT.getCode())
                .setTransferType(TransferTypeEnum.INTELLIGENT.getCode());
        this.save(transferOrder);

        // 2. 保存调拨单商品明细
        List<TransferOrderItem> transferOrderItems = BeanUtil.copyToList(transferSuggest.getItems(), TransferOrderItem.class);
        transferOrderItems.forEach(item -> item.setTransferOrderId(transferOrder.getId())
                .setActualQuantity(0L));
        transferOrderItemService.saveBatch(transferOrderItems);
        log.info("createTransferOrder--success");
    }

}