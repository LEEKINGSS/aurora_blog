package com.aurora.model.dto;


import com.aurora.entity.Note;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;


/**
 * @author liking
 * 笔记编辑页码DTO
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "笔记")
public class NoteAdminViewDTO extends Note {
    /**
     * 笔记标签
     */
    private List<String> tagNames;

    /**
     * 合集名称
     */
    private String collectionName;
}
