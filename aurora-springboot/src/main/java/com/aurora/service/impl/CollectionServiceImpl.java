package com.aurora.service.impl;

import com.aurora.entity.Collection;
import com.aurora.entity.Note;
import com.aurora.exception.BizException;
import com.aurora.mapper.ArticleMapper;
import com.aurora.mapper.CollectionMapper;
import com.aurora.mapper.NoteMapper;
import com.aurora.model.dto.CollectionAdminDTO;
import com.aurora.model.dto.CollectionDTO;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.vo.CollectionVO;
import com.aurora.model.vo.ConditionVO;
import com.aurora.service.CollectionService;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private NoteMapper noteMapper;
    /**
     * 笔记合集mapper
     */
    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public List<CollectionDTO> listCollections() {
        return collectionMapper.listCollections();
    }
//    @SneakyThrows
//    @Override
//    public PageResultDTO<CategoryAdminDTO> listCategoriesAdmin(ConditionVO conditionVO) {
//        Integer count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
//                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Category::getCategoryName, conditionVO.getKeywords()));
//        if (count == 0) {
//            return new PageResultDTO<>();
//        }
//        List<CategoryAdminDTO> categoryList = categoryMapper.listCategoriesAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
//        return new PageResultDTO<>(categoryList, count);
//    }

    @SneakyThrows
    @Override
    public PageResultDTO<CollectionAdminDTO> listCollectionsAdmin(ConditionVO conditionVO) {
        Integer count = collectionMapper.selectCount(new LambdaQueryWrapper<Collection>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Collection::getCollectionName, conditionVO.getKeywords()));
        if (count == 0) {
            return new PageResultDTO<>();
        }
        List<CollectionAdminDTO> collectionList = collectionMapper.listCollectionsAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        return new PageResultDTO<>(collectionList, count);
    }


    @SneakyThrows
    @Override
    public List<CollectionDTO> listCollectionsBySearch(ConditionVO conditionVO) {
        List<Collection> collectionList = collectionMapper.selectList(new LambdaQueryWrapper<Collection>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Collection::getCollectionName, conditionVO.getKeywords())
                .orderByDesc(Collection::getId));
        return BeanCopyUtil.copyList(collectionList, CollectionDTO.class);
    }

    @Override
    public void deleteCollections(List<Integer> collectionIds) {
        Integer count = noteMapper.selectCount(new LambdaQueryWrapper<Note>()
                .in(Note::getCollectionId, collectionIds));
        if (count > 0) {
            throw new BizException("删除失败，该合集下存在文章");
        }
        collectionMapper.deleteBatchIds(collectionIds);
    }

    @Override
    public void saveOrUpdateCollection(CollectionVO collectionVO) {
        Collection existCollection = collectionMapper.selectOne(new LambdaQueryWrapper<Collection>()
                .select(Collection::getId)
                .eq(Collection::getCollectionName, collectionVO.getCollectionName()));
        if (Objects.nonNull(existCollection) && !existCollection.getId().equals(collectionVO.getId())) {
            throw new BizException("合集名已存在");
        }
        Collection collection = Collection.builder()
                .id(collectionVO.getId())
                .collectionName(collectionVO.getCollectionName())
                .build();
        this.saveOrUpdate(collection);
    }
}
