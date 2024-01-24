package com.aurora.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "评论者")
public class CommentDTO {

    @ApiModelProperty(name = "id", value = "评论id", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(name = "avatar", value = "用户头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(name = "webSite", value = "个人网站", dataType = "String")
    private String webSite;

    @ApiModelProperty(name = "commentContent", value = "评论内容", dataType = "String")
    private String commentContent;

    @ApiModelProperty(name = "createTime", value = "评论创建时间", dataType = "LocalDateTime")
    private LocalDateTime createTime;

    private List<ReplyDTO> replyDTOs;

}
