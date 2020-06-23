package com.kanouakira.vueblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kanouakira.vueblog.entity.TagVo;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-14
 */
public interface TagService extends IService<Tag> {

    /**
     * 自定义sql分页
     * @param page
     * @return
     */
    IPage<TagVo> selectTagPage(IPage<Tag> page);
}
