package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.WarehouseMapper;
import com.tiv.inventory.transfer.model.Warehouse;
import com.tiv.inventory.transfer.service.WarehouseService;
import org.springframework.stereotype.Service;

@Service
public class WarehouseServiceImpl extends ServiceImpl<WarehouseMapper, Warehouse> implements WarehouseService {

}