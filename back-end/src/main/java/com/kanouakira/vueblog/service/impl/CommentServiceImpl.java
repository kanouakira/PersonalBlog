package com.kanouakira.vueblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Comment;
import com.kanouakira.vueblog.mapper.CommentMapper;
import com.kanouakira.vueblog.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-13
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public IPage<Comment> selectCommentPage(IPage<Comment> page) {
        return this.baseMapper.selectCommentPage(page);
    }

    @Override
    public Integer updateComment(Comment comment) {
        return this.baseMapper.updateComment(comment);
    }
}
