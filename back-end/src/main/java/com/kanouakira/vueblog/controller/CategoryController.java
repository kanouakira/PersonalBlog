package com.kanouakira.vueblog.controller;


import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 标签的分类表 前端控制器
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping()
    public Result getCategories(){
        return Result.succ(categoryService.getCategoriesAndTag());
    }
}
