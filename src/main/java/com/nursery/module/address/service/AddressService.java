package com.nursery.module.address.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nursery.common.BusinessException;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.address.entity.Address;
import com.nursery.module.address.mapper.AddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {

    private Long getUserId() { return TokenContext.getUserId(); }

    public List<Address> list() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, getUserId()).orderByDesc(Address::getIsDefault).orderByDesc(Address::getCreateTime);
        return list(wrapper);
    }

    @Transactional
    public void add(Address address) {
        Long userId = getUserId();
        if (userId == null) {
            throw new BusinessException(401, "请先登录");
        }
        address.setUserId(userId);
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault();
        }
        save(address);
    }

    @Transactional
    public void update(Address address) {
        Address existing = getById(address.getId());
        if (existing == null || !existing.getUserId().equals(getUserId())) {
            throw new BusinessException("地址不存在");
        }
        if (address.getIsDefault() != null && address.getIsDefault() == 1) {
            clearDefault();
        }
        address.setUserId(getUserId());
        updateById(address);
    }

    public void delete(Long id) {
        Address address = getById(id);
        if (address != null && address.getUserId().equals(getUserId())) {
            removeById(id);
        }
    }

    @Transactional
    public void setDefault(Long id) {
        Address address = getById(id);
        if (address == null || !address.getUserId().equals(getUserId())) {
            throw new BusinessException("地址不存在");
        }
        clearDefault();
        address.setIsDefault(1);
        updateById(address);
    }

    private void clearDefault() {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Address::getUserId, getUserId()).eq(Address::getIsDefault, 1);
        List<Address> defaults = list(wrapper);
        for (Address a : defaults) {
            a.setIsDefault(0);
            updateById(a);
        }
    }
}
