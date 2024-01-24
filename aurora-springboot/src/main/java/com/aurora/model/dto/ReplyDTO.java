package com.aurora.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "回复者")
public class ReplyDTO {

    @ApiModelProperty(name = "id", value = "回复id", dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "commentId", value = "评论id", dataType = "Integer")
    private Integer parentId;

    @ApiModelProperty(name = "userId", value = "用户id", dataType = "Integer")
    private Integer userId;

    @ApiModelProperty(name = "nickname", value = "用户昵称", dataType = "String")
    private String nickname;

    @ApiModelProperty(name = "avatar", value = "用户头像", dataType = "String")
    private String avatar;

    @ApiModelProperty(name = "webSite", value = "个人网站", dataType = "String")
    private String webSite;

    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Integer")
    private Integer replyUserId;

    @ApiModelProperty(name = "replyNickname", value = "回复用户昵称", dataType = "String")
    private String replyNickname;

    @ApiModelProperty(name = "replyWebsite", value = "回复用户网站", dataType = "String")
    private String replyWebsite;

    @ApiModelProperty(name = "replyAvatar", value = "回复内容", dataType = "String")
    private String commentContent;

    @ApiModelProperty(name = "createTime", value = "回复创建时间", dataType = "LocalDateTime")
    private LocalDateTime createTime;

}
