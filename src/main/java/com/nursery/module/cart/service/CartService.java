package com.nursery.module.cart.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.common.BusinessException;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.cart.entity.Cart;
import com.nursery.module.cart.mapper.CartMapper;
import com.nursery.module.product.entity.Product;
import com.nursery.module.product.entity.ProductSku;
import com.nursery.module.product.mapper.ProductMapper;
import com.nursery.module.product.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class CartService extends ServiceImpl<CartMapper, Cart> {

    @Resource
    private ProductMapper productMapper;
    @Resource
    private ProductSkuMapper skuMapper;

    private Long getUserId() {
        return TokenContext.getUserId();
    }

    public List<Map<String, Object>> getCartGroups() {
        Long userId = getUserId();
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId).orderByDesc(Cart::getCreateTime);
        List<Cart> items = baseMapper.selectList(wrapper);

        // Group by shop
        Map<Long, List<Map<String, Object>>> shopMap = new LinkedHashMap<>();
        for (Cart item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product == null || product.getStatus() == 0) {
                baseMapper.deleteById(item.getId());
                continue;
            }
            Map<String, Object> vo = new HashMap<>();
            vo.put("id", item.getId());
            vo.put("productId", product.getId());
            vo.put("productName", product.getName());
            vo.put("productImage", getFirstImage(product.getImages()));
            vo.put("price", product.getPrice());
            vo.put("quantity", item.getQuantity());
            vo.put("selected", item.getSelected());
            vo.put("shopId", product.getShopId());
            vo.put("stock", product.getStock());
            if (item.getSkuId() != null) {
                ProductSku sku = skuMapper.selectById(item.getSkuId());
                if (sku != null) {
                    vo.put("skuId", sku.getId());
                    vo.put("skuName", sku.getSkuName());
                    vo.put("price", sku.getPrice());
                    vo.put("stock", sku.getStock());
                }
            }
            shopMap.computeIfAbsent(product.getShopId(), k -> new ArrayList<>()).add(vo);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, List<Map<String, Object>>> entry : shopMap.entrySet()) {
            Map<String, Object> shopGroup = new HashMap<>();
            shopGroup.put("shopId", entry.getKey());
            shopGroup.put("shopName", "店铺" + entry.getKey());
            shopGroup.put("items", entry.getValue());
            result.add(shopGroup);
        }
        return result;
    }

    private String getFirstImage(String images) {
        if (images == null || images.isEmpty()) return "";
        try {
            if (images.startsWith("[")) {
                String[] arr = images.replaceAll("[\\[\\]\"]", "").split(",");
                return arr.length > 0 ? arr[0].trim() : "";
            }
        } catch (Exception e) {}
        return images;
    }

    @Transactional
    public void add(Long productId, Long skuId, Integer quantity) {
        Long userId = getUserId();
        Product product = productMapper.selectById(productId);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException("商品不存在或已下架");
        }
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
               .eq(Cart::getProductId, productId)
               .eq(skuId != null, Cart::getSkuId, skuId);
        Cart existing = getOne(wrapper);
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + (quantity != null ? quantity : 1));
            updateById(existing);
        } else {
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setSkuId(skuId);
            cart.setQuantity(quantity != null ? quantity : 1);
            cart.setSelected(1);
            save(cart);
        }
    }

    public void update(Long id, Integer quantity, Integer selected) {
        Cart cart = getById(id);
        if (cart == null || !cart.getUserId().equals(getUserId())) {
            throw new BusinessException("购物车项不存在");
        }
        if (quantity != null) cart.setQuantity(quantity);
        if (selected != null) cart.setSelected(selected);
        updateById(cart);
    }

    public void delete(Long id) {
        Cart cart = getById(id);
        if (cart != null && cart.getUserId().equals(getUserId())) {
            removeById(id);
        }
    }
}
