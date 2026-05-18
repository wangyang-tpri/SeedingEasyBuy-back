package com.nursery.module.category.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.module.category.entity.Category;
import com.nursery.module.category.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    public List<Map<String, Object>> getTree() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1).orderByAsc(Category::getSort);
        List<Category> all = list(wrapper);

        Map<Long, List<Category>> parentMap = all.stream()
                .filter(c -> c.getParentId() > 0)
                .collect(Collectors.groupingBy(Category::getParentId));

        List<Map<String, Object>> tree = new ArrayList<>();
        for (Category c : all) {
            if (c.getParentId() == 0) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", c.getId());
                node.put("name", c.getName());
                node.put("icon", c.getIcon());
                node.put("sort", c.getSort());
                node.put("children", parentMap.getOrDefault(c.getId(), new ArrayList<>()));
                tree.add(node);
            }
        }
        return tree;
    }
}
