package com.aurora.service;

import com.aurora.entity.Collection;
import com.aurora.model.dto.*;
import com.aurora.model.vo.CollectionVO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CollectionService extends IService<Collection> {
//
//    List<CategoryDTO> listCategories();
    PageResultDTO<CollectionAdminDTO> listCollectionsAdmin(ConditionVO conditionVO);

    List<CollectionDTO> listCollectionsBySearch(ConditionVO conditionVO);

    void deleteCollections(List<Integer> collectionIds);

    void saveOrUpdateCollection(CollectionVO collectionVO);

}