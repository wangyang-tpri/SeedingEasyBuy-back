package com.nursery.module.collection.controller;

import com.nursery.common.Result;
import com.nursery.module.collection.service.CollectionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Resource
    private CollectionService collectionService;

    @GetMapping("/list")
    public Result<List<Map<String, Object>>> list() {
        return Result.ok(collectionService.getCollectionList());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Map<String, Object> body) {
        Long productId = Long.valueOf(body.get("productId").toString());
        collectionService.toggle(productId);
        return Result.ok();
    }

    @GetMapping("/check/{productId}")
    public Result<Boolean> check(@PathVariable Long productId) {
        return Result.ok(collectionService.isCollected(productId));
    }
}
