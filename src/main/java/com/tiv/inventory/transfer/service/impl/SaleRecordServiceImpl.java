package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.domain.dto.SaleRecordData;
import com.tiv.inventory.transfer.mapper.SaleRecordMapper;
import com.tiv.inventory.transfer.model.SaleRecord;
import com.tiv.inventory.transfer.service.SaleRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleRecordServiceImpl extends ServiceImpl<SaleRecordMapper, SaleRecord> implements SaleRecordService {

    @Resource
    private SaleRecordMapper saleRecordMapper;

    @Override
    public List<SaleRecordData> collectSaleRecordDataByProductId(Long productId) {
        return saleRecordMapper.collectSaleRecordDataByProductId(productId);
    }

}