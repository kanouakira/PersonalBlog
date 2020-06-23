package com.kanouakira.vueblog.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanouakira.vueblog.common.annotation.UserLoginToken;
import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.entity.Comment;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.entity.Tag;
import com.kanouakira.vueblog.entity.TagVo;
import com.kanouakira.vueblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-14
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    TagService tagService;

    /**
     * 查询所有标签
     * @return
     */
    @GetMapping
    public Result getTags(@RequestParam(value = "page",defaultValue = "") Integer current_page,
                          @RequestParam(value = "per_page",defaultValue = "") Integer per_page){
        if (current_page == null){
            return Result.succ(tagService.list());
        }
        Page<Tag> page = new Page<>(current_page,per_page);
        IPage<TagVo> mapIPage = tagService.selectTagPage(page);
        Map<String,Object> data = new HashMap<>();
        data.put("per_page", per_page);
        data.put("total", mapIPage.getTotal());
        data.put("data", mapIPage.getRecords());
        return Result.succ(200, "操作成功", data);
    }

    /**
     * 新增标签
     * @param tag
     * @return
     */
    @PostMapping()
    public Result createTag(@RequestBody Tag tag){
        if (tag.getName() == ""){
            return Result.fail("标签名不能为空");
        }
        if (tagService.getOne(new QueryWrapper<Tag>().eq("name", tag.getName())) != null){
            return Result.fail("标签已存在");
        }
        return Result.succ(tagService.save(tag));
    }

    /**
     * 根据id更新标签
     * @param id
     * @param categoryId
     * @return
     */
    @UserLoginToken
    @PutMapping("/{id}")
    public Result updateTag(@PathVariable("id") Long id,
                            @RequestParam("categoryId") Long categoryId){
        Tag tag = new Tag();
        tag.setId(id);
        tag.setCategoryId(categoryId);
        return Result.succ(tagService.saveOrUpdate(tag));
    }

    /**
     * 根据id删除标签
     * @param id
     * @return
     */
    @UserLoginToken
    @DeleteMapping("/{id}")
    public Result deleteTag(@PathVariable("id") Long id){
        return Result.succ(tagService.removeById(id));
    }
}
