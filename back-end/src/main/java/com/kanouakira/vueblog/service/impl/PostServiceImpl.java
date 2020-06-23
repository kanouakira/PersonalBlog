package com.kanouakira.vueblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.mapper.PostMapper;
import com.kanouakira.vueblog.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-12
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Override
    public Integer updatePost(Post post) {
        return this.baseMapper.updatePost(post);
    }

    @Override
    public Post selectPostById(Long id) {
        return this.baseMapper.selectPostById(id);
    }

    @Override
    public IPage<Post> selectCustomPageByTag(IPage<Post> page, String tag) {
        return this.baseMapper.selectCustomPageByTag(page, tag);
    }

    @Override
    public IPage<Post> selectCustomPage(IPage<Post> page) {
        return this.baseMapper.selectCustomPage(page);
    }
}
