package com.nursery.module.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.common.Constants;
import com.nursery.module.product.entity.Product;
import com.nursery.module.product.entity.ProductSku;
import com.nursery.module.product.entity.ProductImage;
import com.nursery.module.product.mapper.ProductMapper;
import com.nursery.module.product.mapper.ProductSkuMapper;
import com.nursery.module.product.mapper.ProductImageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Resource
    private ProductSkuMapper skuMapper;
    @Resource
    private ProductImageMapper imageMapper;

    public Page<Product> page(int current, int size, Long categoryId, String keyword,
                               String sortBy, Long minPrice, Long maxPrice) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, Constants.PRODUCT_ON_SHELF);
        if (categoryId != null && categoryId > 0) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        if (sortBy != null) {
            switch (sortBy) {
                case "sales": wrapper.orderByDesc(Product::getSales); break;
                case "price_asc": wrapper.orderByAsc(Product::getPrice); break;
                case "price_desc": wrapper.orderByDesc(Product::getPrice); break;
                case "newest": wrapper.orderByDesc(Product::getCreateTime); break;
                default: wrapper.orderByDesc(Product::getSort).orderByDesc(Product::getCreateTime);
            }
        } else {
            wrapper.orderByDesc(Product::getSort).orderByDesc(Product::getCreateTime);
        }
        return page(new Page<>(current, size), wrapper);
    }

    public Map<String, Object> detail(Long id) {
        Product product = getById(id);
        if (product == null || product.getStatus() == Constants.PRODUCT_OFF_SHELF) {
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        result.put("product", product);

        LambdaQueryWrapper<ProductSku> skuWrapper = new LambdaQueryWrapper<>();
        skuWrapper.eq(ProductSku::getProductId, id).orderByAsc(ProductSku::getSort);
        result.put("skus", skuMapper.selectList(skuWrapper));

        LambdaQueryWrapper<ProductImage> imgWrapper = new LambdaQueryWrapper<>();
        imgWrapper.eq(ProductImage::getProductId, id).orderByAsc(ProductImage::getSortOrder);
        result.put("images", imageMapper.selectList(imgWrapper));

        return result;
    }

    public Page<Product> recommend(int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, Constants.PRODUCT_ON_SHELF)
               .orderByDesc(Product::getSales).orderByDesc(Product::getCreateTime);
        return page(new Page<>(1, size), wrapper);
    }

    public Page<Product> newProducts(int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, Constants.PRODUCT_ON_SHELF)
               .orderByDesc(Product::getCreateTime);
        return page(new Page<>(1, size), wrapper);
    }

    public Page<Product> search(String keyword, int page, int size) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, Constants.PRODUCT_ON_SHELF)
               .like(Product::getName, keyword);
        return page(new Page<>(page, size), wrapper);
    }
}
