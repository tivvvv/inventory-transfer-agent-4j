package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.SaleRecordMapper;
import com.tiv.inventory.transfer.model.SaleRecord;
import com.tiv.inventory.transfer.service.SaleRecordService;
import org.springframework.stereotype.Service;

@Service
public class SaleRecordServiceImpl extends ServiceImpl<SaleRecordMapper, SaleRecord> implements SaleRecordService {

}