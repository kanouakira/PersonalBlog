package com.kanouakira.vueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanouakira.vueblog.common.annotation.UserLoginToken;
import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.common.utils.HashUtil;
import com.kanouakira.vueblog.entity.Comment;
import com.kanouakira.vueblog.entity.User;
import com.kanouakira.vueblog.entity.UserVo;
import com.kanouakira.vueblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-09
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 查询所有用户
     * @return
     */
    @GetMapping
    public Result getUsers(@RequestParam("page") Integer current_page,
                           @RequestParam("per_page") Integer per_page){
        Page<UserVo> page = new Page<>(current_page,per_page);
        IPage<UserVo> mapIPage = userService.selectUserPage(page);
        Map<String , Object> data = new HashMap<>();
        data.put("per_page", per_page);
        data.put("total", mapIPage.getTotal());
        data.put("data", mapIPage.getRecords());
        return Result.succ(data);
    }

    /**
     * 根据id查找用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getUser(@PathVariable("id") Long id){
        User user = userService.getOne(new QueryWrapper<User>().eq("id", id));
        user.setPassword(null);
        return Result.succ(200, "操作成功", user);
    }

    /**
     * 新增一个用户
     * @param user
     * @return
     */
    @PostMapping
    public Result createUser(@RequestBody User user){
        User queryUser = userService.getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if(queryUser != null){
            return Result.fail("用户名已存在");
        }
        user.setPassword(HashUtil.hash(user.getPassword(),"SHA-256"));
        return Result.succ(userService.save(user));
    }

    /**
     * 根据id修改用户
     * @param id
     * @param user
     * @return
     */
    @UserLoginToken
    @PutMapping("/{id}")
    public Result updateUser(@PathVariable("id") Long id,@RequestBody User user){
        //得到servlet中的request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Integer tokenStatus = userService.getById((Serializable) request.getAttribute("userId")).getStatus();

        // 最高等级的管理员无视此条规则
        if (tokenStatus != 5){
            // 仅可修改自己的资料
            if (!request.getAttribute("userId").equals(id)){
                return Result.fail("操作失败");
            }
        }
        user.setId(id);
        if (!StringUtils.isEmpty(user.getPassword())){
            user.setPassword(HashUtil.hash(user.getPassword(),"SHA-256"));
        }
        userService.saveOrUpdate(user);
        return Result.succ(userService.getOne(new QueryWrapper<User>().eq("id", id)));
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @UserLoginToken
    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable("id") Long id){
        return Result.succ(userService.removeById(id));
    }
}
