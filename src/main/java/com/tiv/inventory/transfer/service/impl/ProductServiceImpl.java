package com.tiv.inventory.transfer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tiv.inventory.transfer.mapper.ProductMapper;
import com.tiv.inventory.transfer.model.Product;
import com.tiv.inventory.transfer.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}