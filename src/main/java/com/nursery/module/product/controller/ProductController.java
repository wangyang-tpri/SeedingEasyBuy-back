package com.nursery.module.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nursery.common.Constants;
import com.nursery.common.PageResult;
import com.nursery.common.Result;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.product.entity.Product;
import com.nursery.module.product.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping("/page")
    public Result<PageResult<Product>> page(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice) {
        Page<Product> page = productService.page(current, size, categoryId, keyword, sortBy, minPrice, maxPrice);
        return Result.ok(new PageResult<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/detail/{id}")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        Map<String, Object> detail = productService.detail(id);
        if (detail == null) {
            return Result.fail("商品不存在或已下架");
        }
        return Result.ok(detail);
    }

    @GetMapping("/recommend")
    public Result<PageResult<Product>> recommend(@RequestParam(defaultValue = "6") int size) {
        Page<Product> page = productService.recommend(size);
        return Result.ok(new PageResult<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/new")
    public Result<PageResult<Product>> newProducts(@RequestParam(defaultValue = "6") int size) {
        Page<Product> page = productService.newProducts(size);
        return Result.ok(new PageResult<>(page.getRecords(), page.getTotal(), page.getSize(), page.getCurrent()));
    }

    @GetMapping("/search")
    public Result<PageResult<Product>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Product> result = productService.search(keyword, page, size);
        return Result.ok(new PageResult<>(result.getRecords(), result.getTotal(), result.getSize(), result.getCurrent()));
    }

    @PostMapping("/add")
    public Result<Map<String, Object>> add(@RequestBody Map<String, Object> body) {
        Product product = new Product();
        product.setName((String) body.get("name"));
        product.setSubtitle((String) body.get("subtitle"));
        product.setCategoryId(body.get("categoryId") != null ? Long.valueOf(body.get("categoryId").toString()) : null);
        product.setDescription((String) body.get("description"));
        product.setPrice(body.get("price") != null ? new BigDecimal(body.get("price").toString()) : null);
        product.setOriginalPrice(body.get("originalPrice") != null ? new BigDecimal(body.get("originalPrice").toString()) : null);
        product.setStock(body.get("stock") != null ? Integer.valueOf(body.get("stock").toString()) : 0);
        product.setUnit((String) body.get("unit"));
        product.setUserId(TokenContext.getUserId());
        product.setContactPhone((String) body.get("phone"));
        product.setAddress((String) body.get("address"));
        product.setVideo((String) body.get("video"));
        product.setImages((String) body.get("images"));
        product.setSpecs((String) body.get("specs"));

        List<String> imageUrls = new ArrayList<>();
        String imagesStr = (String) body.get("images");
        if (imagesStr != null && !imagesStr.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                imageUrls = objectMapper.readValue(imagesStr, new TypeReference<List<String>>() {});
            } catch (Exception e) {
                imageUrls.add(imagesStr);
            }
        }

        Product saved = productService.create(product, imageUrls);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", saved.getId());
        return Result.ok(result);
    }

    @GetMapping("/my")
    public Result<List<Product>> myProducts() {
        Long userId = TokenContext.getUserId();
        return Result.ok(productService.myProducts(userId));
    }

    @PutMapping("/update/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Product product = productService.getById(id);
        if (product == null) return Result.fail("商品不存在");
        if (body.containsKey("name")) product.setName((String) body.get("name"));
        if (body.containsKey("subtitle")) product.setSubtitle((String) body.get("subtitle"));
        if (body.containsKey("categoryId")) product.setCategoryId(Long.valueOf(body.get("categoryId").toString()));
        if (body.containsKey("description")) product.setDescription((String) body.get("description"));
        if (body.containsKey("price")) product.setPrice(new BigDecimal(body.get("price").toString()));
        if (body.containsKey("originalPrice")) product.setOriginalPrice(body.get("originalPrice") != null ? new BigDecimal(body.get("originalPrice").toString()) : null);
        if (body.containsKey("stock")) product.setStock(Integer.valueOf(body.get("stock").toString()));
        if (body.containsKey("unit")) product.setUnit((String) body.get("unit"));
        if (body.containsKey("phone")) product.setContactPhone((String) body.get("phone"));
        if (body.containsKey("address")) product.setAddress((String) body.get("address"));
        if (body.containsKey("video")) product.setVideo((String) body.get("video"));
        if (body.containsKey("images")) product.setImages((String) body.get("images"));
        if (body.containsKey("specs")) product.setSpecs((String) body.get("specs"));
        productService.updateById(product);

        // Update image records
        String imagesStr = (String) body.get("images");
        if (imagesStr != null) {
            productService.updateImages(id, imagesStr);
        }

        return Result.ok();
    }

    @PostMapping("/status/{id}")
    public Result<?> toggleStatus(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) return Result.fail("商品不存在");
        product.setStatus(product.getStatus() == Constants.PRODUCT_ON_SHELF ? Constants.PRODUCT_OFF_SHELF : Constants.PRODUCT_ON_SHELF);
        productService.updateById(product);
        return Result.ok();
    }
}
