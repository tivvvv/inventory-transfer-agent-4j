package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.TransferOrderItemMapper;
import com.tiv.inventory.transfer.model.TransferOrderItem;
import com.tiv.inventory.transfer.service.TransferOrderItemService;
import org.springframework.stereotype.Service;

@Service
public class TransferOrderItemServiceImpl extends ServiceImpl<TransferOrderItemMapper, TransferOrderItem> implements TransferOrderItemService {

}