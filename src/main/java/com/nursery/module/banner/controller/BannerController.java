package com.nursery.module.banner.controller;

import com.nursery.common.Result;
import com.nursery.module.banner.entity.Banner;
import com.nursery.module.banner.service.BannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<List<Banner>> list() {
        return Result.ok(bannerService.getActiveBanners());
    }
}
