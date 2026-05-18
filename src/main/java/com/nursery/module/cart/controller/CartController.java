package com.nursery.module.cart.controller;

import com.nursery.common.Result;
import com.nursery.module.cart.service.CartService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource
    private CartService cartService;

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        return Result.ok(cartService.getCartGroups());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        Long skuId = body.get("skuId") != null ? Long.valueOf(body.get("skuId").toString()) : null;
        Integer quantity = body.get("quantity") != null ? Integer.valueOf(body.get("quantity").toString()) : 1;
        cartService.add(productId, skuId, quantity);
        return Result.ok();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Integer quantity = body.get("quantity") != null ? Integer.valueOf(body.get("quantity").toString()) : null;
        Integer selected = body.get("selected") != null ? Integer.valueOf(body.get("selected").toString()) : null;
        cartService.update(id, quantity, selected);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        cartService.delete(id);
        return Result.ok();
    }
}
