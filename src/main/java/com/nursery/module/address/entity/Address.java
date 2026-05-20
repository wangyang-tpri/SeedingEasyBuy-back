package com.nursery.module.address.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("address")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private String receiverName;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private String fullAddress;
    private Integer isDefault;
    private String label;
    private BigDecimal lng;
    private BigDecimal lat;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
