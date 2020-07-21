package com.kanouakira.vueblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 博客文章表
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String body;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 观看数
     */
    private Integer views;

    /**
     * 文章作者
     */
    private Long authorId;

    /**
     * 是否仅作者可见
     */
    private Boolean onlySelfVisible;

    private User user;

    private List<Tag> tags;

    private List<Comment> comments;

}
