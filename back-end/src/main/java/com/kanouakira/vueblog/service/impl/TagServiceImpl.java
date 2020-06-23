package com.kanouakira.vueblog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kanouakira.vueblog.entity.Tag;
import com.kanouakira.vueblog.entity.TagVo;
import com.kanouakira.vueblog.mapper.TagMapper;
import com.kanouakira.vueblog.service.TagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author kanouakira
 * @since 2020-06-14
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public IPage<TagVo> selectTagPage(IPage<Tag> page) {
        return this.baseMapper.selectTagPage(page);
    }

}
