package com.tiv.inventory.transfer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tiv.inventory.transfer.model.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}