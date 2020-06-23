package com.kanouakira.vueblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.User;
import com.kanouakira.vueblog.entity.UserVo;
import com.kanouakira.vueblog.mapper.UserMapper;
import com.kanouakira.vueblog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public IPage<UserVo> selectUserPage(IPage<UserVo> page) {
        return this.baseMapper.selectUserPage(page);
    }

    @Override
    public UserVo selectUserById(Long id) {
        return this.baseMapper.selectUserById(id);
    }
}
