package com.aurora.mapper;

import com.aurora.entity.Collection;
import com.aurora.model.dto.CollectionAdminDTO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CollectionMapper extends BaseMapper<Collection> {

//    List<CategoryDTO> listCategories();

    List<CollectionAdminDTO> listCollectionsAdmin(@Param("current") Long current, @Param("size") Long size, @Param("conditionVO") ConditionVO conditionVO);

}
