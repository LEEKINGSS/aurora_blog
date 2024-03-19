package com.aurora.service;

import com.aurora.entity.Category;
import com.aurora.entity.Music;
import com.aurora.model.dto.CategoryAdminDTO;
import com.aurora.model.dto.CategoryDTO;
import com.aurora.model.dto.CategoryOptionDTO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.CategoryVO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author LIKING
 */
public interface MusicService extends IService<Music> {

    List<Music> listMusics();

}
