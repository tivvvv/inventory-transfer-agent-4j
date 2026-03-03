package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.InventoryRecordMapper;
import com.tiv.inventory.transfer.model.InventoryRecord;
import com.tiv.inventory.transfer.service.InventoryRecordService;
import org.springframework.stereotype.Service;

@Service
public class InventoryRecordServiceImpl extends ServiceImpl<InventoryRecordMapper, InventoryRecord> implements InventoryRecordService {

}