package com.nursery.module.user.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.module.user.entity.Role;
import com.nursery.module.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
}
