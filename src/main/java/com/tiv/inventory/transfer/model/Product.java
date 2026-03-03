package com.tiv.inventory.transfer.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品信息表
 *
 * @TableName product
 */
@Data
@TableName(value = "product")
public class Product {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 商品编码
     */
    private String productCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品规格
     */
    private String spec;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 商品分类
     */
    private String category;

    /**
     * 商品状态:1-启用 0-停用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

}