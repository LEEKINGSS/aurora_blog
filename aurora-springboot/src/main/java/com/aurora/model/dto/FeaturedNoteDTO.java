package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author liking
 * 推荐笔记dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedNoteDTO {

    /**
     * 推荐笔记列表
     */
    private List<NoteDTO> featurednotes;
}
