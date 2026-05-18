package com.nursery.module.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nursery.common.PageResult;
import com.nursery.common.Result;
import com.nursery.module.order.entity.Order;
import com.nursery.module.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Order> create(@RequestBody Map<String, Object> body) {
        Order order = orderService.create(body);
        return Result.ok(order);
    }

    @GetMapping("/list")
    public Result<PageResult<Order>> list(
            @RequestParam(defaultValue = "-1") int status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Order> result = orderService.list(status, page, size);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent()));
    }

    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.ok(orderService.detail(id));
    }

    @PostMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id) {
        orderService.cancel(id);
        return Result.ok();
    }

    @PostMapping("/pay/{id}")
    public Result<?> pay(@PathVariable Long id) {
        orderService.pay(id);
        return Result.ok();
    }

    @PostMapping("/confirm_receive/{id}")
    public Result<?> confirmReceive(@PathVariable Long id) {
        orderService.confirmReceive(id);
        return Result.ok();
    }
}
