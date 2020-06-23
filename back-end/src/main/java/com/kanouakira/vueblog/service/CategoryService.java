package com.kanouakira.vueblog.service;

import com.kanouakira.vueblog.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanouakira.vueblog.entity.CategoryVo;

import java.util.List;

/**
 * <p>
 * 标签的分类表 服务类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-21
 */
public interface CategoryService extends IService<Category> {
    /**
     * 获取所有分类以及其包含的标签
     * @return
     */
    List<CategoryVo> getCategoriesAndTag();
}
