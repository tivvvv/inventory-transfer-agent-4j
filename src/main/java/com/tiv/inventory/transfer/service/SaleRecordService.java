package com.tiv.inventory.transfer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tiv.inventory.transfer.domain.dto.SaleRecordData;
import com.tiv.inventory.transfer.model.SaleRecord;

import java.util.List;

public interface SaleRecordService extends IService<SaleRecord> {

    List<SaleRecordData> collectSaleRecordDataByProductId(Long productId);

}