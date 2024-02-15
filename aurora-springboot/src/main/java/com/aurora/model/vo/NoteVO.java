package com.aurora.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "笔记")
public class NoteVO {

    @ApiModelProperty(name = "id", value = "笔记id", dataType = "Integer")
    private Integer id;

    @NotBlank(message = "笔记标题不能为空")
    @ApiModelProperty(name = "noteTitle", value = "笔记标题", required = true, dataType = "String")
    private String noteTitle;

    @NotBlank(message = "笔记内容不能为空")
    @ApiModelProperty(name = "noteContent", value = "笔记内容", required = true, dataType = "String")
    private String noteContent;

    @ApiModelProperty(name = "noteCover", value = "笔记缩略图", dataType = "String")
    private String noteCover;

    @ApiModelProperty(name = "collection", value = "笔记合集", dataType = "Integer")
    private String collectionName;

    @ApiModelProperty(name = "tagNameList", value = "笔记标签", dataType = "List<Integer>")
    private List<String> tagNames;

    @ApiModelProperty(name = "isFeatured", value = "是否推荐", dataType = "Integer")
    private Integer isFeatured;

    @ApiModelProperty(name = "status", value = "笔记状态", dataType = "String")
    private Integer status;

    @ApiModelProperty(name = "type", value = "笔记类型", dataType = "Integer")
    private Integer type;

    @ApiModelProperty(name = "originalUrl", value = "原文链接", dataType = "String")
    private String originalUrl;

    @ApiModelProperty(name = "password", value = "笔记访问密码", dataType = "String")
    private String password;
}
