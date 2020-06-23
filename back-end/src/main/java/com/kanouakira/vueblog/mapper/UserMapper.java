package com.kanouakira.vueblog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Comment;
import com.kanouakira.vueblog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanouakira.vueblog.entity.UserVo;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-09
 */
public interface UserMapper extends BaseMapper<User> {
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
