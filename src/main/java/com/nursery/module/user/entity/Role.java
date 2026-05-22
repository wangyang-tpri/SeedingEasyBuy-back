package com.nursery.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("role")
public class Role {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private String permissions;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
