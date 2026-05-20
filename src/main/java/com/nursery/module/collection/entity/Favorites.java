package com.nursery.module.collection.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("collection")
public class Favorites {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private Long productId;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
