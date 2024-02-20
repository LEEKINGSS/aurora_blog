package com.aurora.model.dto;

import com.aurora.entity.Note;
import com.aurora.entity.Tag;
import com.aurora.entity.UserInfo;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author liking
 * 前端笔记卡片数据传输对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NoteCardDTO extends Note {

    /**
     * 笔记作者
     */
    private UserInfo author;

    /**
     * 笔记合集
     */
    private String collectionName;

    /**
     * 合集id
     */
    private Integer collectionId;

    /**
     * 笔记标签
     */
    private List<Tag> tags;


}
