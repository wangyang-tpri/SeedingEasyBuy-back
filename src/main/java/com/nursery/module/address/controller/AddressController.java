package com.nursery.module.address.controller;

import com.nursery.common.Result;
import com.nursery.module.address.entity.Address;
import com.nursery.module.address.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    @GetMapping("/list")
    public Result<List<Address>> list() {
        return Result.ok(addressService.list());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Address address) {
        try {
            addressService.add(address);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Address address) {
        addressService.update(address);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        addressService.delete(id);
        return Result.ok();
    }

    @PutMapping("/default/{id}")
    public Result<?> setDefault(@PathVariable Long id) {
        addressService.setDefault(id);
        return Result.ok();
    }
}
