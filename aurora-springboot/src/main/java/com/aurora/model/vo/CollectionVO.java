package com.aurora.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "合集")
public class CollectionVO {

    @ApiModelProperty(name = "id", value = "合集id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "合集名不能为空")
    @ApiModelProperty(name = "categoryName", value = "合集名", required = true, dataType = "String")
    private String collectionName;

}
