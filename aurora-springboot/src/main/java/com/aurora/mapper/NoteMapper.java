package com.aurora.mapper;

import com.aurora.entity.Article;
import com.aurora.entity.Note;
import com.aurora.model.dto.*;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteMapper extends BaseMapper<Note> {

//    List<ArticleCardDTO> listTopAndFeaturedArticles();
//
    List<NoteCardDTO> listNotes(@Param("current") Long current, @Param("size") Long size);
    List<NoteCardDTO> getNotesByCollectionId(@Param("current") Long current, @Param("size") Long size, @Param("collectionId") Integer collectionId);

    NoteDTO getNoteById(@Param("noteId") Integer noteId);

    NoteCardDTO getPreNoteById(@Param("noteId") Integer noteId);

    NoteCardDTO getNextNoteById(@Param("noteId") Integer noteId);

    NoteCardDTO getFirstNote();

    NoteCardDTO getLastNote();
    List<NoteCardDTO> listNotesByTagId(@Param("tagId") Integer tagId);
//
//    List<ArticleCardDTO> listArchives(@Param("current") Long current, @Param("size") Long size);
//
    Integer countNoteAdmins(@Param("conditionVO") ConditionVO conditionVO);

    List<NoteAdminDTO> listNotesAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);
//
//    List<ArticleStatisticsDTO> listArticleStatistics();

}

