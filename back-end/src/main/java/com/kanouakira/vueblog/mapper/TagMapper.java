package com.kanouakira.vueblog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Post;
import com.kanouakira.vueblog.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kanouakira.vueblog.entity.TagVo;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-14
 */
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 自定义sql分页
     * @param page
     * @return
     */
    IPage<TagVo> selectTagPage(IPage<Tag> page);

}
