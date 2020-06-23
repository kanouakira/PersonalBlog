package com.kanouakira.vueblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanouakira.vueblog.entity.UserVo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-09
 */
public interface UserService extends IService<User> {
    /**
     * 自定义sql分页
     * @param page
     * @return
     */
    IPage<UserVo> selectUserPage(IPage<UserVo> page);

    /**
     * 根据id返回一个用户
     * @param id
     * @return
     */
    UserVo selectUserById(Long id);
}
