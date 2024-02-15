package com.aurora.model.dto;

import com.aurora.entity.Note;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


/**
 * @author liking
 * 后台笔记
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class NoteAdminDTO extends Note {

    /**
     * 笔记访问量
     */
    private Integer viewsCount;

    /**
     * 合集名称
     */
    private String collectionName;

    /**
     * 笔记标签
     */
    private List<TagDTO> tagDTOs;
}
