package com.aurora.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionAdminDTO {

    /**
     * 合集id
     */
    private Integer id;

    /**
     * 合集名
     */
    private String collectionName;

    /**
     * 笔记数量
     */
    private Integer noteCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
