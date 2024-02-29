package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_archive")
public class Archive {

    private String id;

    private String title;

    private String content;

    private String time;

    private LocalDateTime createTime;

    private Integer isDelete;

    private Integer articleId;

    private Integer type;

}
