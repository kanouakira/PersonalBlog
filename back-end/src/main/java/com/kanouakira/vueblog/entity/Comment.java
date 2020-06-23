package com.kanouakira.vueblog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论人
     */
    private Long userId;

    /**
     * 被评论文章
     */
    private Long postId;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 评论内容
     */
    private String body;

    /**
     * 评论级别，回复文章是一级其他是二级
     */
    private Integer commentLevel;

    /**
     * 父级评论id
     */
    private Long parentCommentId;

    /**
     * 被回复评论的id
     */
    private Long replyCommentId;

    /**
     * 创建时间
     */
    private LocalDateTime created;

    /**
     * 该评论的用户名
     */
    private String username;

    /**
     * 该评论的用户昵称
     */
    private String name;

    /**
     * 回复的评论发表者用户名
     */
    private String replyUsername;

    /**
     * 回复的评论发表者昵称
     */
    private String replyName;

    /**
     * 子评论
     */
    private List<Comment> childComments;

}
