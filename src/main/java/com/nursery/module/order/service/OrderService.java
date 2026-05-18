package com.nursery.module.order.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.common.BusinessException;
import com.nursery.common.Constants;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.cart.entity.Cart;
import com.nursery.module.cart.mapper.CartMapper;
import com.nursery.module.order.entity.Order;
import com.nursery.module.order.entity.OrderItem;
import com.nursery.module.order.mapper.OrderMapper;
import com.nursery.module.order.mapper.OrderItemMapper;
import com.nursery.module.product.entity.Product;
import com.nursery.module.product.entity.ProductSku;
import com.nursery.module.product.mapper.ProductMapper;
import com.nursery.module.product.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    @Resource private OrderItemMapper orderItemMapper;
    @Resource private CartMapper cartMapper;
    @Resource private ProductMapper productMapper;
    @Resource private ProductSkuMapper skuMapper;

    private Long getUserId() { return TokenContext.getUserId(); }

    @Transactional
    public Order create(Map<String, Object> body) {
        Long userId = getUserId();
        Long addressId = Long.valueOf(body.get("addressId").toString());
        String message = (String) body.get("message");

        // Get selected cart items
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId).eq(Cart::getSelected, 1);
        List<Cart> cartItems = cartMapper.selectList(cartWrapper);

        if (cartItems.isEmpty()) {
            throw new BusinessException("请选择要购买的商品");
        }

        // Group by shop (simplified: use first product's shop)
        Long shopId = null;
        for (Cart ci : cartItems) {
            Product p = productMapper.selectById(ci.getProductId());
            if (p != null) { shopId = p.getShopId(); break; }
        }
        if (shopId == null) throw new BusinessException("商品不存在");

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (Cart ci : cartItems) {
            Product product = productMapper.selectById(ci.getProductId());
            if (product == null || product.getStatus() != Constants.PRODUCT_ON_SHELF) {
                throw new BusinessException("商品【" + (product != null ? product.getName() : "") + "】已下架");
            }

            BigDecimal price = product.getPrice();
            String skuName = null;
            if (ci.getSkuId() != null) {
                ProductSku sku = skuMapper.selectById(ci.getSkuId());
                if (sku == null) throw new BusinessException("商品规格不存在");
                if (sku.getStock() < ci.getQuantity()) throw new BusinessException("商品【" + product.getName() + "】库存不足");
                price = sku.getPrice();
                skuName = sku.getSkuName();
                sku.setStock(sku.getStock() - ci.getQuantity());
                skuMapper.updateById(sku);
            } else {
                if (product.getStock() < ci.getQuantity()) throw new BusinessException("商品【" + product.getName() + "】库存不足");
                product.setStock(product.getStock() - ci.getQuantity());
            }
            product.setSales(product.getSales() + ci.getQuantity());
            productMapper.updateById(product);

            BigDecimal subtotal = price.multiply(new BigDecimal(ci.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setSkuId(ci.getSkuId());
            item.setProductName(product.getName());
            item.setSkuName(skuName);
            item.setProductImage(getFirstImage(product.getImages()));
            item.setQuantity(ci.getQuantity());
            item.setPrice(price);
            item.setSubtotal(subtotal);
            orderItems.add(item);
        }

        Order order = new Order();
        order.setOrderNo(DateUtil.format(LocalDateTime.now(), "yyyyMMdd") + RandomUtil.randomNumbers(8));
        order.setUserId(userId);
        order.setShopId(shopId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setFreight(BigDecimal.ZERO);
        order.setDiscount(BigDecimal.ZERO);
        order.setStatus(Constants.ORDER_PENDING_PAY);
        order.setReceiverName("");
        order.setReceiverPhone("");
        order.setReceiverAddress("");
        order.setUserMessage(message);
        save(order);

        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        // Clear selected cart items
        for (Cart ci : cartItems) {
            cartMapper.deleteById(ci.getId());
        }

        return order;
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

    public Page<Order> list(int status, int page, int size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, getUserId());
        if (status >= 0) wrapper.eq(Order::getStatus, status);
        wrapper.orderByDesc(Order::getCreateTime);
        return page(new Page<>(page, size), wrapper);
    }

    public Map<String, Object> detail(Long id) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(getUserId())) {
            throw new BusinessException("订单不存在");
        }
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, id);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);

        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("items", items);
        return result;
    }

    public void cancel(Long id) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(getUserId())) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != Constants.ORDER_PENDING_PAY) {
            throw new BusinessException("只能取消待付款订单");
        }
        order.setStatus(Constants.ORDER_CANCELLED);
        order.setCloseTime(LocalDateTime.now());
        order.setCloseReason("用户取消");
        updateById(order);
    }

    public void pay(Long id) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(getUserId())) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != Constants.ORDER_PENDING_PAY) {
            throw new BusinessException("订单状态不正确");
        }
        order.setStatus(Constants.ORDER_PENDING_DELIVERY);
        order.setPayTime(LocalDateTime.now());
        updateById(order);
    }

    public void confirmReceive(Long id) {
        Order order = getById(id);
        if (order == null || !order.getUserId().equals(getUserId())) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != Constants.ORDER_PENDING_RECEIVE) {
            throw new BusinessException("订单状态不正确");
        }
        order.setStatus(Constants.ORDER_PENDING_REVIEW);
        order.setReceiveTime(LocalDateTime.now());
        updateById(order);
    }
}
