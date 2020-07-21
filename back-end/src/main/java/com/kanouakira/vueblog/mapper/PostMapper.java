package com.kanouakira.vueblog.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.kanouakira.vueblog.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import javafx.geometry.Pos;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 博客文章表 Mapper 接口
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-12
 */
@Repository
@Mapper
public interface PostMapper extends BaseMapper<Post> {

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
