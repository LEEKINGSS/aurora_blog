package com.aurora.mapper;

import com.aurora.entity.Note;
import com.aurora.model.dto.ArticleStatisticsDTO;
import com.aurora.model.dto.NoteAdminDTO;
import com.aurora.model.dto.NoteCardDTO;
import com.aurora.model.dto.NoteDTO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NoteMapper extends BaseMapper<Note> {

    /**
     * 获取所有笔记
     */
    List<NoteCardDTO> listNotes(@Param("current") Long current, @Param("size") Long size);

    /**
     * 根据合集id获取笔记
     */
    List<NoteCardDTO> getNotesByCollectionId(@Param("collectionId") Integer collectionId);

    /**
     * 根据id获取笔记
     */
    NoteDTO getNoteById(@Param("noteId") Integer noteId);

    /**
     * 访问上一笔记
     */
    NoteCardDTO getPreNoteById(@Param("noteId") Integer noteId);

    /**
     * 访问下一笔记
     */
    NoteCardDTO getNextNoteById(@Param("noteId") Integer noteId);

    /**
     * 访问首条笔记
     */
    NoteCardDTO getFirstNote();

    /**
     * 访问末尾笔记
     */
    NoteCardDTO getLastNote();

    /**
     * 根据标签id获取文章
     */
    List<NoteCardDTO> listNotesByTagId(@Param("tagId") Integer tagId);

    Integer countNoteAdmins(@Param("conditionVO") ConditionVO conditionVO);

    List<NoteAdminDTO> listNotesAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

    List<ArticleStatisticsDTO> listNoteStatistics();

}

