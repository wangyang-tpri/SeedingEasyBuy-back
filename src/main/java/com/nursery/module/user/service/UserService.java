package com.nursery.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nursery.module.user.entity.User;

public interface UserService extends IService<User> {
    User loginByOpenid(String openid);
    User loginByPhone(String phone);
    User getByToken();
    void updateProfile(User user);
}
