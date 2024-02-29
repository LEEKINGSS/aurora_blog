package com.aurora.model.dto;

import com.aurora.entity.Note;
import com.aurora.entity.Tag;
import com.aurora.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author liking
 * 笔记传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO extends Note {

    /**
     * 作者
     */
    private UserInfo author;

    /**
     * 合集名称
     */
    private String collectionName;

    /**
     * 笔记标签
     */
    private List<Tag> tags;

    /**
     * 笔记浏览量
     */
    private Integer viewCount;

    /**
     * 原文链接
     */
    private String originalUrl;

    /**
     * 上一篇笔记
     */
    private NoteCardDTO preNoteCard;

    /**
     * 下一篇笔记
     */
    private NoteCardDTO nextNoteCard;
}
