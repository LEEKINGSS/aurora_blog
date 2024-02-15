package com.aurora.service;

import com.aurora.entity.Note;
import com.aurora.model.dto.*;
import com.aurora.model.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface NoteService extends IService<Note> {

    TopAndFeaturedArticlesDTO listTopAndFeaturedArticles();

    /**
     * 获取所有笔记
     */
    PageResultDTO<NoteCardDTO> listNotes();

    PageResultDTO<ArticleCardDTO> listArticlesByCategoryId(Integer categoryId);

    NoteDTO getNoteById(Integer noteId);

    void accessArticle(ArticlePasswordVO articlePasswordVO);

    PageResultDTO<ArticleCardDTO> listArticlesByTagId(Integer tagId);

    PageResultDTO<ArchiveDTO> listArchives();

    PageResultDTO<NoteAdminDTO> listNotesAdmin(ConditionVO conditionVO);

    void saveOrUpdateNote(NoteVO noteVO);

    void updateNoteFeatured(NoteFeaturedVO noteFeaturedVO);

    void updateNoteDelete(DeleteVO deleteVO);

    void deleteNotes(List<Integer> noteIds);

    NoteAdminViewDTO getNoteByIdAdmin(Integer noteId);

    List<String> exportNotes(List<Integer> noteIdList);

    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);

}
