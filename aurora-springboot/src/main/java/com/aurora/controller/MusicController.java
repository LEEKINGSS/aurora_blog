package com.aurora.controller;

import com.aurora.entity.Music;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Liking
 * @description 音乐处理模块
 * @date: 2024/3/18 22:51
 */

@Api(tags = "音乐处理模块")
@RestController
public class MusicController {

    @Autowired
    private MusicService musicService;

    /**
     * 获取所有音乐
     */
    @ApiOperation("获取所有音乐")
    @GetMapping("/musics/all")
    public ResultVO<List<Music>> listMusics() {
        return ResultVO.ok(musicService.listMusics());
    }
}
