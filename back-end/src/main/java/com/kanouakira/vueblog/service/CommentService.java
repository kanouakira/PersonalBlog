package com.kanouakira.vueblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanouakira.vueblog.entity.Post;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-13
 */
public interface CommentService extends IService<Comment> {
    /**
     * 自定义sql分页
     * @param page
     * @return
     */
    IPage<Comment> selectCommentPage(IPage<Comment> page);

    /**
     * 根据id更新评论
     * @param comment
     * @return
     */
    Integer updateComment(Comment comment);
}
