package com.nursery.module.banner.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("banner")
public class Banner {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer linkType;
    private String linkValue;
    private Integer sort;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
