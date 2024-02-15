package com.aurora.service.impl;

import com.aurora.entity.ArticleTag;
import com.aurora.entity.NoteTag;
import com.aurora.mapper.ArticleTagMapper;
import com.aurora.mapper.NoteTagMapper;
import com.aurora.service.ArticleTagService;
import com.aurora.service.NoteTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class NoteTagServiceImpl extends ServiceImpl<NoteTagMapper, NoteTag> implements NoteTagService {

}
