package com.nursery.module.product.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long shopId;
    private Long categoryId;
    private String name;
    private String subtitle;
    private String description;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer stock;
    private Integer sales;
    private String images;
    private String video;
    private String specs;
    private String plantingGuide;
    private Integer hasSku;
    private String unit;
    private String contactPhone;
    private String address;
    private Integer minQuantity;
    private Integer status;
    private Integer sort;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
