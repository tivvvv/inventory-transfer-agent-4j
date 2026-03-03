package com.tiv.inventory.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiv.inventory.transfer.model.Inventory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

}