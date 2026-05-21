package com.nursery.module.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@TableName("`user`")
public class User {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String openid;
    private String unionid;
    private String nickname;
    private String avatar;
    private String phone;
    private Integer gender;
    private Integer userType;
    private Integer status;
    private Integer points;
    private LocalDateTime lastLogin;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
