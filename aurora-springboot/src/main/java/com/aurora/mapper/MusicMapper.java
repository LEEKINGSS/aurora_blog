package com.aurora.mapper;

import com.aurora.entity.Category;
import com.aurora.entity.Music;
import com.aurora.model.dto.CategoryAdminDTO;
import com.aurora.model.dto.CategoryDTO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author LIKING
 */
@Repository
public interface MusicMapper extends BaseMapper<Music> {

    /**
     * 查询所有音乐
     * @return
     */
    List<Music> listMusics();

}
