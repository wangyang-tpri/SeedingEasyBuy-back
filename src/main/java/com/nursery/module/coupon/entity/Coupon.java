package com.nursery.module.coupon.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@TableName("coupon")
public class Coupon {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer type;
    private BigDecimal value;
    private BigDecimal minAmount;
    private Integer totalCount;
    private Integer remainCount;
    private Integer validDays;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
