package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.TransferOrderMapper;
import com.tiv.inventory.transfer.model.TransferOrder;
import com.tiv.inventory.transfer.service.TransferOrderService;
import org.springframework.stereotype.Service;

@Service
public class TransferOrderServiceImpl extends ServiceImpl<TransferOrderMapper, TransferOrder> implements TransferOrderService {

}