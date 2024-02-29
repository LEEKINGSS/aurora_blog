package com.aurora.service.impl;

import com.aurora.entity.Archive;
import com.aurora.entity.Article;
import com.aurora.entity.Note;
import com.aurora.mapper.ArchiveMapper;
import com.aurora.service.ArchiveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.aurora.constant.ArticleConstant.ARCHIVE_TYPE_ARTICLE;
import static com.aurora.constant.ArticleConstant.ARCHIVE_TYPE_NOTE;

@Service
public class ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements ArchiveService {

    private ArchiveMapper archiveMapper;

    /**
     * 保存文章归档信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticleArchive(Article article) {
        Archive archive = new Archive();
        LocalDateTime time;
        if (article.getCreateTime() != null) {
            time = article.getCreateTime();
        } else if (article.getUpdateTime() != null) {
            time = article.getUpdateTime();
        } else {
            throw new RuntimeException("文章归档时出错！");
        }
        int month = time.getMonth().getValue();
        int year = time.getYear();
        String key = year + "-" + month;
        archive.setTime(key);
        archive.setTitle(article.getArticleTitle());
        archive.setContent(article.getArticleContent());
        archive.setCreateTime(time);
        archive.setId(article.getArchiveId());
        archive.setType(ARCHIVE_TYPE_ARTICLE);
        archive.setArticleId(article.getId());
        this.saveOrUpdate(archive);
    }

    /**
     * 保存笔记归档信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveNoteArchive(Note note) {
        Archive archive = new Archive();
        LocalDateTime time;
        if (note.getCreateTime() != null) {
            time = note.getCreateTime();
        } else if (note.getUpdateTime() != null) {
            time = note.getUpdateTime();
        } else {
            throw new RuntimeException("笔记归档时出错！");
        }
        int month = time.getMonth().getValue();
        int year = time.getYear();
        String key = year + "-" + month;
        archive.setTime(key);
        archive.setTitle(note.getNoteTitle());
        archive.setContent(note.getNoteContent());
        archive.setCreateTime(time);
        archive.setId(note.getArchiveId());
        archive.setType(ARCHIVE_TYPE_NOTE);
        archive.setArticleId(note.getId());
        this.saveOrUpdate(archive);
    }

    /**
     * 逻辑删除归档信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArchiveDelete(List<String> archiveIds,int isDelete) {
        List<Archive> archives = archiveIds.stream()
                .map(archiveId -> Archive.builder()
                        .id(archiveId)
                        .isDelete(isDelete)
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(archives);
    }

    /**
     * 物理删除归档信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArchives(List<String> archiveIds) {
        archiveMapper.deleteBatchIds(archiveIds);
    }


}
