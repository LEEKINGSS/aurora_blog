package com.aurora.model.dto;

import com.aurora.entity.Collection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author liking
 * 合集DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO extends Collection {

    /**
     * 笔记数据量
     */
    private Integer noteCount;

}
