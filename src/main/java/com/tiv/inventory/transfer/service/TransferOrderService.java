package com.tiv.inventory.transfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiv.inventory.transfer.domain.dto.InventoryTransferData;
import com.tiv.inventory.transfer.model.TransferOrder;

import java.util.List;

public interface TransferOrderService extends IService<TransferOrder> {

    List<InventoryTransferData> collectInventoryTransferDataByProductId(Long productId);

}