package com.nursery.module.banner.controller;

import com.nursery.common.Result;
import com.nursery.interceptor.TokenContext;
import com.nursery.module.banner.entity.Banner;
import com.nursery.module.banner.service.BannerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/banner")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @GetMapping("/list")
    public Result<List<Banner>> list() {
        return Result.ok(bannerService.getActiveBanners());
    }

    @GetMapping("/my")
    public Result<List<Banner>> my() {
        return Result.ok(bannerService.list());
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Map<String, Object> body) {
        Banner banner = new Banner();
        banner.setTitle((String) body.get("title"));
        banner.setImageUrl((String) body.get("imageUrl"));
        banner.setLinkType(body.get("linkType") != null ? Integer.valueOf(body.get("linkType").toString()) : 0);
        banner.setLinkValue((String) body.get("linkValue"));
        banner.setSort(0);
        banner.setStatus(1);
        bannerService.save(banner);
        return Result.ok();
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Map<String, Object> body) {
        Long id = Long.valueOf(body.get("id").toString());
        Banner banner = bannerService.getById(id);
        if (banner != null) {
            if (body.containsKey("title")) banner.setTitle((String) body.get("title"));
            if (body.containsKey("imageUrl")) banner.setImageUrl((String) body.get("imageUrl"));
            if (body.containsKey("linkType")) banner.setLinkType(Integer.valueOf(body.get("linkType").toString()));
            if (body.containsKey("linkValue")) banner.setLinkValue((String) body.get("linkValue"));
            bannerService.updateById(banner);
        }
        return Result.ok();
    }

    @PostMapping("/status/{id}")
    public Result<?> toggleStatus(@PathVariable Long id) {
        Banner banner = bannerService.getById(id);
        if (banner != null) {
            banner.setStatus(banner.getStatus() == 1 ? 0 : 1);
            bannerService.updateById(banner);
        }
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        bannerService.removeById(id);
        return Result.ok();
    }
}
