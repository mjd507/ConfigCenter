package com.configcenter.admin.controller;

import com.configcenter.admin.entity.UserAppRole;
import com.configcenter.admin.service.UserService;
import com.configcenter.admin.util.UserUtils;
import com.configcenter.admin.vo.request.LoginReq;
import com.configcenter.common.util.ApiResponse;
import com.configcenter.common.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by mjd on 2020/4/12 22:49
 */
@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ApiResponse login(@RequestBody LoginReq loginReq) {
        String name = loginReq.getName();
        String pwd = loginReq.getPwd();
        String token = userService.login(name, pwd);
        return ApiResponse.ok(MapUtils.newMap().put("access-token", token));
    }

    @GetMapping("info")
    public ApiResponse info() {
        return ApiResponse.ok(MapUtils.newMap().put("name", UserUtils.getUserName()));
    }

    @GetMapping("apps_with_role")
    public ApiResponse listApps() {
        List<UserAppRole> apps = userService.listUserAppsWithRole(UserUtils.getUid());
        return ApiResponse.ok(apps);
    }

}
