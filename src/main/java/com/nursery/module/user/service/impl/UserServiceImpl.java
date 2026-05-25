package com.nursery.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.common.BusinessException;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.user.entity.User;
import com.nursery.module.user.mapper.UserMapper;
import com.nursery.module.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User loginByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = getOne(wrapper);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setNickname("微信用户");
            user.setStatus(0);
            user.setUserType(0);
            save(user);
        }
        user.setLastLogin(LocalDateTime.now());
        updateById(user);
        return user;
    }

    @Override
    public User loginByPhone(String phone) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = getOne(wrapper);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setOpenid(phone);
            user.setNickname("手机用户");
            user.setStatus(0);
            user.setUserType(0);
            save(user);
        }
        user.setLastLogin(LocalDateTime.now());
        updateById(user);
        return user;
    }

    @Override
    public User getByToken() {
        Long userId = TokenContext.getUserId();
        if (userId == null) {
            throw new BusinessException(401, "未登录");
        }
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        return user;
    }

    @Override
    public void updateProfile(User user) {
        Long userId = TokenContext.getUserId();
        user.setId(userId);
        updateById(user);
    }
}
