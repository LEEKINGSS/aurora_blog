package com.aurora.service;

import com.aurora.entity.Collection;
import com.aurora.model.dto.CollectionAdminDTO;
import com.aurora.model.dto.CollectionDTO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.CollectionVO;
import com.aurora.model.vo.ConditionVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CollectionService extends IService<Collection> {
    List<CollectionDTO> listCollections();
    PageResultDTO<CollectionAdminDTO> listCollectionsAdmin(ConditionVO conditionVO);

    List<CollectionDTO> listCollectionsBySearch(ConditionVO conditionVO);

    void deleteCollections(List<Integer> collectionIds);

    void saveOrUpdateCollection(CollectionVO collectionVO);

}
