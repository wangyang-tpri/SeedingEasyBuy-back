package com.nursery.module.user.controller;

import cn.hutool.core.util.StrUtil;
import com.nursery.common.JwtUtil;
import com.nursery.common.Result;
import com.nursery.module.user.dto.LoginResponse;
import com.nursery.module.user.entity.User;
import com.nursery.module.user.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        if (StrUtil.isBlank(code)) {
            return Result.fail("code不能为空");
        }
        // MVP: use code as openid directly (real WeChat login requires calling WeChat API)
        User user = userService.loginByOpenid(code);
        String token = jwtUtil.createToken(user.getId());
        return Result.ok(new LoginResponse(token, user));
    }

    @PostMapping("/login_by_phone")
    public Result<LoginResponse> loginByPhone(@RequestBody Map<String, String> body) {
        System.out.println("phone" + body.get("phone"));
        String phone = body.get("phone");
        if (StrUtil.isBlank(phone)) {
            return Result.fail("手机号不能为空");
        }
        User user = userService.loginByPhone(phone);
        String token = jwtUtil.createToken(user.getId());
        return Result.ok(new LoginResponse(token, user));
    }

    @GetMapping("/token_info")
    public Result<User> tokenInfo() {
        User user = userService.getByToken();
        return Result.ok(user);
    }
}

@RestController
@RequestMapping("/user")
class UserInfoController {

    @Resource
    private UserService userService;

    @GetMapping("/info")
    public Result<User> info() {
        return Result.ok(userService.getByToken());
    }

    @PutMapping("/info")
    public Result<?> updateInfo(@RequestBody User user) {
        userService.updateProfile(user);
        return Result.ok();
    }
}
