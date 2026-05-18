package com.nursery.module.collection.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.collection.entity.Favorites;
import com.nursery.module.collection.mapper.CollectionMapper;
import com.nursery.module.product.entity.Product;
import com.nursery.module.product.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class CollectionService extends ServiceImpl<CollectionMapper, Favorites> {

    @Resource
    private ProductMapper productMapper;

    private Long getUserId() { return TokenContext.getUserId(); }

    public List<Map<String, Object>> getCollectionList() {
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorites::getUserId, getUserId()).orderByDesc(Favorites::getCreateTime);
        List<Favorites> collections = baseMapper.selectList(wrapper);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Favorites c : collections) {
            Product p = productMapper.selectById(c.getProductId());
            if (p != null) {
                Map<String, Object> vo = new HashMap<>();
                vo.put("id", c.getId());
                vo.put("productId", p.getId());
                vo.put("productName", p.getName());
                vo.put("price", p.getPrice());
                vo.put("image", getFirstImage(p.getImages()));
                vo.put("sales", p.getSales());
                vo.put("createTime", c.getCreateTime());
                result.add(vo);
            }
        }
        return result;
    }

    public void toggle(Long productId) {
        Long userId = getUserId();
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorites::getUserId, userId).eq(Favorites::getProductId, productId);
        Favorites existing = getOne(wrapper);
        if (existing != null) {
            removeById(existing.getId());
        } else {
            Favorites c = new Favorites();
            c.setUserId(userId);
            c.setProductId(productId);
            save(c);
        }
    }

    public boolean isCollected(Long productId) {
        LambdaQueryWrapper<Favorites> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Favorites::getUserId, getUserId()).eq(Favorites::getProductId, productId);
        return count(wrapper) > 0;
    }

    private String getFirstImage(String images) {
        if (images == null || images.isEmpty()) return "";
        try {
            if (images.startsWith("[")) {
                return images.replaceAll("[\\[\\]\"]", "").split(",")[0].trim();
            }
        } catch (Exception e) {}
        return images;
    }
}
