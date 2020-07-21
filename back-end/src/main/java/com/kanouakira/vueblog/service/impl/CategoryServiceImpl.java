package com.kanouakira.vueblog.service.impl;

import com.kanouakira.vueblog.entity.Category;
import com.kanouakira.vueblog.entity.CategoryVo;
import com.kanouakira.vueblog.mapper.CategoryMapper;
import com.kanouakira.vueblog.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 标签的分类表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-21
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Cacheable(value = "categories")
    @Override
    public List<CategoryVo> getCategoriesAndTag() {
        return this.baseMapper.getCategoriesAndTag();
    }
}
