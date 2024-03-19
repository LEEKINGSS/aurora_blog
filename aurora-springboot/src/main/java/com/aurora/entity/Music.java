package com.aurora.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Liking
 * @description 音乐
 * @date: 2024/3/18 22:55
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_music")
public class Music {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String head;

    private String name;

    private String music;

}
