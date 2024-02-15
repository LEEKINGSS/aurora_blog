package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.model.dto.*;
import com.aurora.model.vo.CategoryVO;
import com.aurora.model.vo.CollectionVO;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.ResultVO;
import com.aurora.service.CategoryService;
import com.aurora.service.CollectionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.aurora.constant.OptTypeConstant.DELETE;
import static com.aurora.constant.OptTypeConstant.SAVE_OR_UPDATE;

/**
 * @author liking
 * 合集模块
 */
@Api(tags = "合集模块")
@RestController
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

//    @ApiOperation("获取所有分类")
//    @GetMapping("/categories/all")
//    public ResultVO<List<CategoryDTO>> listCategories() {
//        return ResultVO.ok(categoryService.listCategories());
//    }

    @ApiOperation(value = "查看后台合集列表")
    @GetMapping("/admin/collections")
    public ResultVO<PageResultDTO<CollectionAdminDTO>> listCollectionsAdmin(ConditionVO conditionVO) {
        return ResultVO.ok(collectionService.listCollectionsAdmin(conditionVO));
    }

    @ApiOperation(value = "搜索笔记合集")
    @GetMapping("/admin/collections/search")
    public ResultVO<List<CollectionDTO>> listCollectionDTOsBySearch(ConditionVO conditionVO) {
        return ResultVO.ok(collectionService.listCollectionsBySearch(conditionVO));
    }

    @OptLog(optType = DELETE)
    @ApiOperation(value = "删除合集")
    @DeleteMapping("/admin/collections")
    public ResultVO<?> deleteCollections(@RequestBody List<Integer> collectionIds) {
        collectionService.deleteCollections(collectionIds);
        return ResultVO.ok();
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "添加或修改合集")
    @PostMapping("/admin/collections")
    public ResultVO<?> saveOrUpdateCollection(@Valid @RequestBody CollectionVO collectionVO) {
        collectionService.saveOrUpdateCollection(collectionVO);
        return ResultVO.ok();
    }
//    @OptLog(optType = SAVE_OR_UPDATE)
//    @ApiOperation(value = "添加或修改分类")
//    @PostMapping("/admin/categories")
//    public ResultVO<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
//        categoryService.saveOrUpdateCategory(categoryVO);
//        return ResultVO.ok();
//    }


}
