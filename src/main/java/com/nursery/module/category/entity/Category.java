package com.nursery.module.category.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Long parentId;
    private String icon;
    private Integer sort;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
