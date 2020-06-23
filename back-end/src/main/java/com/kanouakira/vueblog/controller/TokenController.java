package com.kanouakira.vueblog.controller;

import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.common.utils.HashUtil;
import com.kanouakira.vueblog.entity.User;
import com.kanouakira.vueblog.entity.UserVo;
import com.kanouakira.vueblog.service.TokenService;
import com.kanouakira.vueblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取token的表现层
 */
@RestController
@RequestMapping("/tokens")
public class TokenController {

    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @PostMapping()
    public Result login(@RequestBody User user){
        User queryUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (queryUser == null){
            return Result.fail("用户名或密码错误！");
        }
        if (!HashUtil.hash(user.getPassword(), "SHA-256").equals(queryUser.getPassword())){
            return Result.fail("用户名或密码错误！");
        }
        String token = tokenService.getToken(queryUser);

        queryUser.setLastLogin(LocalDateTime.now());
        userService.saveOrUpdate(queryUser);

        Map<String, Object> data = new HashMap<>();
        data.put("user", queryUser);
        data.put("token", token);
        return Result.succ(data);
    }

    @GetMapping()
    public Result getUser(@RequestParam String token){
        HashMap<String, Object> data = new HashMap<>();
        String userId = JWT.decode(token).getAudience().get(0);
        UserVo user = userService.selectUserById(Long.valueOf(userId));
        if (user.getStatus() != 5){
            return Result.fail("权限不足");
        }
        data.put("role",user.getRoleName());
        data.put("name",user.getName()!=null ? user.getName() : user.getUsername());
        data.put("avatar",user.getAvatar());
        data.put("status",user.getStatus());
        return Result.succ(data);
    }

    @PostMapping("/logout")
    public Result logout(){
        return Result.succ(null);
    }

}
