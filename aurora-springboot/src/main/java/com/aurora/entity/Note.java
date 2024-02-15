package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_note")
public class Note {

    /**
     * 笔记id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 合集id
     */
    private Integer collectionId;

    /**
     * 笔记封面
     */
    private String noteCover;

    /**
     * 笔记标题
     */
    private String noteTitle;

    /**
     * 笔记内容
     */
    private String noteContent;

    /**
     * 是否推荐
     */
    private Integer isFeatured;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 笔记状态 1公开 2私密 3草稿
     */
    private Integer status;

    /**
     * 笔记类型 1原创 2转载 3翻译
     */
    private Integer type;

    /**
     * 笔记密码
     */
    private String password;

    /**
     *  原文链接
     */
    private String originalUrl;

    /**
     *  创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     *  更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
