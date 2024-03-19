package com.aurora.service.impl;

import com.aurora.entity.Article;
import com.aurora.entity.Category;
import com.aurora.entity.Music;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.CategoryMapper;
import com.aurora.mapper.MusicMapper;
import com.aurora.model.dto.CategoryAdminDTO;
import com.aurora.model.dto.CategoryDTO;
import com.aurora.model.dto.CategoryOptionDTO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.CategoryVO;
import com.aurora.model.vo.ConditionVO;
import com.aurora.service.CategoryService;
import com.aurora.service.MusicService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper, Music> implements MusicService {
    @Autowired
    private MusicMapper musicMapper;

    @Override
    public List<Music> listMusics() {
        return musicMapper.listMusics();
    }
}
