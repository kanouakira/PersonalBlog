package com.kanouakira.vueblog.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kanouakira.vueblog.common.annotation.UserLoginToken;
import com.kanouakira.vueblog.common.lang.Result;
import com.kanouakira.vueblog.entity.Comment;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.service.CommentService;
import com.kanouakira.vueblog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论表 前端控制器
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-13
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    /**
     * 查询所有评论
     * @return
     */
    @GetMapping
    public Result getComments(@RequestParam("page") Integer current_page,
                              @RequestParam("per_page") Integer per_page){
        Page<Comment> page = new Page<>(current_page,per_page);
        IPage<Comment> mapIPage = commentService.selectCommentPage(page);
        Map<String , Object> data = new HashMap<>();
        data.put("per_page", per_page);
        data.put("total", mapIPage.getTotal());
        data.put("data", mapIPage.getRecords());
        return Result.succ(data);
    }

    /**
     * 根据id查找评论
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getComment(@PathVariable("id") Long id){
        Comment comment = commentService.getOne(new QueryWrapper<Comment>().eq("id", id));
        return Result.succ(200, "操作成功", comment);
    }

    /**
     * 新增一个评论
     * @param comment
     * @return
     */
    @UserLoginToken
    @PostMapping
    public Result createPost(@RequestBody Comment comment){
        //得到servlet中的request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        comment.setUserId((Long) request.getAttribute("userId"));
        return Result.succ(commentService.save(comment));
    }

    /**
     * 根据id修改评论
     * @param id
     * @param comment
     * @return
     */
    @UserLoginToken
    @PutMapping("/{id}")
    public Result updatePost(@PathVariable("id") Long id, @RequestBody Comment comment){
//        //得到servlet中的request
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        // 仅作者可以修改评论内容
//        if (!request.getAttribute("userId").equals(comment.getUserId())){
//            return Result.fail("无修改权限");
//        }
        comment.setId(id);
        return Result.succ(commentService.updateComment(comment));
    }

    /**
     * 根据id删除评论
     * @param id
     * @return
     */
    @UserLoginToken
    @DeleteMapping("/{id}")
    public Result deletePost(@PathVariable("id") Long id){
        return Result.succ(commentService.removeById(id));
    }
}
