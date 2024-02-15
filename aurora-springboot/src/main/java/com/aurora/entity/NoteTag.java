package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_note_tag")
public class NoteTag {

    /**
     * 笔记标签id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 笔记id
     */
    private Integer noteId;

    /**
     * 标签id
     */
    private Integer tagId;

}
