package com.nursery.module.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nursery.module.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
