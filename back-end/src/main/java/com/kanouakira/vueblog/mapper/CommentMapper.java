package com.kanouakira.vueblog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-13
 */
public interface CommentMapper extends BaseMapper<Comment> {
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
