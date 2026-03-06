package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.domain.dto.InventoryTransferData;
import com.tiv.inventory.transfer.mapper.TransferOrderMapper;
import com.tiv.inventory.transfer.model.TransferOrder;
import com.tiv.inventory.transfer.service.TransferOrderService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements TransferOrderService {

    @Resource
    private TransferOrderMapper transferOrderMapper;

    @Override
    public List<InventoryTransferData> collectInventoryTransferDataByProductId(Long productId) {
        return transferOrderMapper.collectInventoryTransferDataByProductId(productId);
    }

}