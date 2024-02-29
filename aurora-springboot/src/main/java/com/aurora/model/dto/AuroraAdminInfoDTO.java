package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuroraAdminInfoDTO {

    private Integer viewsCount;

    private Integer messageCount;

    private Integer userCount;

    private Integer articleCount;

    private Integer noteCount;

    private List<CategoryDTO> categoryDTOs;

    private List<CollectionDTO> collectionDTOs;

    private List<TagDTO> tagDTOs;

    private List<ArticleStatisticsDTO> articleStatisticsDTOs;

    private List<UniqueViewDTO> uniqueViewDTOs;

    private List<ArticleRankDTO> articleRankDTOs;

    private List<NoteRankDTO> noteRankDTOs;
}
