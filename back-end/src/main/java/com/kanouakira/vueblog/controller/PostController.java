package com.kanouakira.vueblog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanouakira.vueblog.common.annotation.UserLoginToken;
import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.entity.Tag;
import com.kanouakira.vueblog.entity.TagRelationship;
import com.kanouakira.vueblog.mapper.PostMapper;
import com.kanouakira.vueblog.service.PostService;
import com.kanouakira.vueblog.service.TagRelationshipService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客文章表 前端控制器
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostService postService;
    @Autowired
    TagRelationshipService tagRelationshipService;

    /**
     * 查询所有文章
     * @return
     */
    @GetMapping
    public Result getPosts(@RequestParam("page") Integer current_page,
                           @RequestParam("per_page") Integer per_page,
                           @RequestParam(value = "tag", defaultValue = "") String tag){
        Page<Post> page = new Page<>(current_page,per_page);
        IPage<Post> mapIPage;
        if (tag.equals("")){
            mapIPage = postService.selectCustomPage(page);
        }else {
            mapIPage = postService.selectCustomPageByTag(page, tag);
        }
        Map<String , Object> data = new HashMap<>();
        data.put("per_page", per_page);
        data.put("total", mapIPage.getTotal());
        data.put("data", mapIPage.getRecords());
        return Result.succ(200, "操作成功", data);
    }

    /**
     * 根据id查找文章
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getPost(@PathVariable("id") Long id){
        Post post = postService.selectPostById(id);
        return Result.succ(200, "操作成功", post);
    }

    /**
     * 新增一个文章
     * @param post
     * @return
     */
    @UserLoginToken
    @PostMapping
    public Result createPost(@RequestBody Post post){
        //得到servlet中的request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        post.setAuthorId((Long) request.getAttribute("userId"));
        postService.save(post);
        return Result.succ(post.getId());
    }

    /**
     * 根据id修改文章
     * @param id
     * @param post
     * @return
     */
    @UserLoginToken
    @PutMapping("/{id}")
    public Result updatePost(@PathVariable("id") Long id,@RequestBody Post post){
        //得到servlet中的request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        // 仅作者可以修改文章内容
//        if (!request.getAttribute("userId").equals(post.getAuthorId())){
//            return Result.fail("无修改权限");
//        }
        post.setId(id);
        return Result.succ(postService.updatePost(post));
    }

    /**
     * 根据id增加浏览数
     * @param id
     * @return
     */
    @PutMapping("/{id}/addViews")
    public Result addPostView(@PathVariable("id") Long id) {
//        Post post = postService.getOne(new QueryWrapper<Post>().eq("id", id));
        Post post = postService.selectPostById(id);
        post.setViews(post.getViews() + 1);
        return Result.succ(postService.updatePost(post));
    }


    /**
     * 根据id添加标签
     * @param params
     * @return
     */
    @UserLoginToken
    @PostMapping("/{id}/tags")
    public Result addPostTag(@PathVariable("id") Long id, @RequestBody List<Long> params) {
        for(Long tagId : params){
            TagRelationship tagRelationship = new TagRelationship();
            tagRelationship.setPostId(id);
            tagRelationship.setTagId(tagId);
            if (!tagRelationshipService.save(tagRelationship)){
                return Result.fail("添加标签出错");
            }
        }
        return Result.succ("操作成功");
    }

    @UserLoginToken
    @PutMapping("/{id}/tags")
    public Result updatePostTag(@PathVariable("id") Long id, @RequestBody List<Long> params) {
        List<TagRelationship> tagRelationships = tagRelationshipService.list(new QueryWrapper<TagRelationship>().eq("post_id", id));
        for(TagRelationship tagRelationship : tagRelationships){
            tagRelationshipService.removeById(tagRelationship.getId());
        }
        for(Long tagId : params){
            TagRelationship tagRelationship = new TagRelationship();
            tagRelationship.setPostId(id);
            tagRelationship.setTagId(tagId);
            if (!tagRelationshipService.save(tagRelationship)){
                return Result.fail("添加标签出错");
            }
        }
        return Result.succ("操作成功");
    }

    @UserLoginToken
    @GetMapping("/{id}/tags")
    public Result getPostTags(@PathVariable("id") Long id){
        List<TagRelationship> tagRelationships = tagRelationshipService.list(new QueryWrapper<TagRelationship>().eq("post_id", id));
        List<Long> tagIds = new ArrayList<>();
        for(TagRelationship tagRelationship : tagRelationships){
            tagIds.add(tagRelationship.getTagId());
        }
        return Result.succ(tagIds);
    }


    /**
     * 根据id删除文章
     * @param id
     * @return
     */
    @UserLoginToken
    @DeleteMapping("/{id}")
    public Result deletePost(@PathVariable("id") Long id){
        Boolean result = postService.removeById(id);
        return Result.succ(result);
    }
}
