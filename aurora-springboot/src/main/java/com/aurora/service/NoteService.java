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

    /**
     * 根据合集id获取笔记
     */
    PageResultDTO<NoteCardDTO> listNotesByCollectionId(Integer collectionId);

    /**
     * 根据id获取笔记
     */
    NoteDTO getNoteById(Integer noteId);

    void accessNote(NotePasswordVO notePasswordVO);

    /**
     * 根据标签id获取文章
     */
    PageResultDTO<NoteCardDTO> listNotesByTagId(Integer tagId);

    PageResultDTO<NoteAdminDTO> listNotesAdmin(ConditionVO conditionVO);

    void saveOrUpdateNote(NoteVO noteVO);

    void updateNoteFeatured(NoteFeaturedVO noteFeaturedVO);

    void updateNoteDelete(DeleteVO deleteVO);

    void deleteNotes(List<Integer> noteIds);

    NoteAdminViewDTO getNoteByIdAdmin(Integer noteId);

    List<String> exportNotes(List<Integer> noteIdList);

    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);

}
