package com.kanouakira.vueblog.mapper;

import com.kanouakira.vueblog.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanouakira.vueblog.entity.CategoryVo;

import java.util.List;

/**
 * <p>
 * 标签的分类表 Mapper 接口
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-21
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 获取所有分类以及其包含的标签
     * @return
     */
    List<CategoryVo> getCategoriesAndTag();
}
