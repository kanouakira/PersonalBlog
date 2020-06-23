package com.kanouakira.vueblog.service.impl;

import com.kanouakira.vueblog.entity.Role;
import com.kanouakira.vueblog.mapper.RoleMapper;
import com.kanouakira.vueblog.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
