package com.kanouakira.vueblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.mapper.PostMapper;
import com.kanouakira.vueblog.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;

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

//    @Cacheable(key = "#page.getCurrent()+'-'+#page.getSize()+#tag", value = "customPageList")
    @Override
    public IPage<Post> selectCustomPage(IPage<Post> page, Long author_id, String tag, String manage, String search) {
        return this.baseMapper.selectCustomPage(page, author_id, tag, manage, search);
    }
}
