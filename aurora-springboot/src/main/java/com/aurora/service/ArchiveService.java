package com.aurora.service;

import com.aurora.entity.Archive;
import com.aurora.entity.Article;
import com.aurora.entity.Note;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author liking
 * 归档信息业务逻辑接口
 */
public interface ArchiveService extends IService<Archive> {

    /**
     * 保存文章归档信息
     */
    void saveArticleArchive(Article article);

    /**
     * 保存笔记归档信息
     */
    void saveNoteArchive(Note note);


    /**
     * 逻辑删除归档信息
     */
    void updateArchiveDelete(List<String> archiveIds,int isDelete);

    /**
     * 物理删除归档信息
     */
    void deleteArchives(List<String> archiveIds);

}
