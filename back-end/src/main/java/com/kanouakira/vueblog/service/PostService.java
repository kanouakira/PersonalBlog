package com.kanouakira.vueblog.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kanouakira.vueblog.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-12
 */
public interface PostService extends IService<Post> {
    /**
     * 自定义sql分页
     * @param page
     * @return
     */
    IPage<Post> selectCustomPage(IPage<Post> page,
                                 @Param("id") Long author_id,
                                 @Param("tag") String tag,
                                 @Param("manage")String manage,
                                 @Param("search")String search);

    /**
     * 根据id返回一篇文章
     * @param id
     * @return
     */
    Post selectPostById(Long id);

    /**
     * 根据id更新一篇文章
     * @param post
     * @return
     */
    Integer updatePost(Post post);


}
