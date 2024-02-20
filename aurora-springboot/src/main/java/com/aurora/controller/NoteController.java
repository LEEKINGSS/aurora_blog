package com.aurora.controller;

import com.aurora.annotation.OptLog;
import com.aurora.enums.FilePathEnum;
import com.aurora.model.dto.*;
import com.aurora.model.vo.*;
import com.aurora.service.ArticleService;
import com.aurora.service.NoteService;
import com.aurora.strategy.NoteImportStrategy;
import com.aurora.strategy.context.ArticleImportStrategyContext;
import com.aurora.strategy.context.NoteImportStrategyContext;
import com.aurora.strategy.context.UploadStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.util.List;

import static com.aurora.constant.OptTypeConstant.*;

@Api(tags = "笔记模块")
@RestController
public class NoteController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private NoteImportStrategyContext noteImportStrategyContext;

    @Autowired
    private NoteService noteService;

    @ApiOperation("获取推荐笔记")
    @GetMapping("/notes/featured")
    public ResultVO<TopAndFeaturedArticlesDTO> listTopAndFeaturedArticles() {
        return ResultVO.ok(articleService.listTopAndFeaturedArticles());
    }

    @ApiOperation("获取所有笔记")
    @GetMapping("/notes/all")
    public ResultVO<PageResultDTO<NoteCardDTO>> listArticles() {
        return ResultVO.ok(noteService.listNotes());
    }

    @ApiOperation("根据合集id获取文章")
    @GetMapping("/notes/collectionId")
    public ResultVO<PageResultDTO<NoteCardDTO>> listNotesByCollectionId(@RequestParam Integer collectionId) {
        return ResultVO.ok(noteService.listNotesByCollectionId(collectionId));
    }

    @ApiOperation("根据id获取笔记")
    @GetMapping("/notes/{noteId}")
    public ResultVO<NoteDTO> getNoteById(@PathVariable("noteId") Integer noteId) {
        return ResultVO.ok(noteService.getNoteById(noteId));
    }

    @ApiOperation("校验笔记访问密码")
    @PostMapping("/notes/access")
    public ResultVO<?> accessNote(@Valid @RequestBody NotePasswordVO notePasswordVO) {
        noteService.accessNote(notePasswordVO);
        return ResultVO.ok();
    }
    @ApiOperation("根据标签id获取笔记")
    @GetMapping("/notes/tagId")
    public ResultVO<PageResultDTO<NoteCardDTO>> listNotesByTagId(@RequestParam Integer tagId) {
        return ResultVO.ok(noteService.listNotesByTagId(tagId));
    }
//
//    @ApiOperation("获取所有文章归档")
//    @GetMapping("/archives/all")
//    public ResultVO<PageResultDTO<ArchiveDTO>> listArchives() {
//        return ResultVO.ok(articleService.listArchives());
//    }

    @ApiOperation("获取后台笔记")
    @GetMapping("/admin/notes")
    public ResultVO<PageResultDTO<NoteAdminDTO>> listNotesAdmin(ConditionVO conditionVO) {
        return ResultVO.ok(noteService.listNotesAdmin(conditionVO));
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation("保存和修改笔记")
    @PostMapping("/admin/notes")
    public ResultVO<?> saveOrUpdateNote(@Valid @RequestBody NoteVO noteVO) {
        noteService.saveOrUpdateNote(noteVO);
        return ResultVO.ok();
    }

    @OptLog(optType = UPDATE)
    @ApiOperation("修改笔记推荐")
    @PutMapping("/admin/notes/featured")
    public ResultVO<?> updateNoteFeatured(@Valid @RequestBody NoteFeaturedVO noteFeaturedVO) {
        noteService.updateNoteFeatured(noteFeaturedVO);
        return ResultVO.ok();
    }

    @ApiOperation("删除或者恢复笔记")
    @PutMapping("/admin/notes")
    public ResultVO<?> updateNoteDelete(@Valid @RequestBody DeleteVO deleteVO) {
        noteService.updateNoteDelete(deleteVO);
        return ResultVO.ok();
    }

    @OptLog(optType = DELETE)
    @ApiOperation("物理删除笔记")
    @DeleteMapping("/admin/notes/delete")
    public ResultVO<?> deleteNotes(@RequestBody List<Integer> noteIds) {
        noteService.deleteNotes(noteIds);
        return ResultVO.ok();
    }

    @OptLog(optType = UPLOAD)
    @ApiOperation("上传笔记图片")
    @ApiImplicitParam(name = "file", value = "笔记图片", required = true, dataType = "MultipartFile")
    @PostMapping("/admin/notes/images")
    public ResultVO<String> saveNoteImages(@RequestParam("file") MultipartFile file, @RequestParam("title") String title) {
        return ResultVO.ok(uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.NOTE.getPath() + title + "/"));
    }

    @ApiOperation("根据id查看后台笔记")
    @ApiImplicitParam(name = "noteId", value = "笔记id", required = true, dataType = "Integer")
    @GetMapping("/admin/notes/{noteId}")
    public ResultVO<NoteAdminViewDTO> getNoteBackById(@PathVariable("noteId") Integer noteId) {
        return ResultVO.ok(noteService.getNoteByIdAdmin(noteId));
    }


    @OptLog(optType = UPLOAD)
    @ApiOperation(value = "导入笔记")
    @PostMapping("/admin/notes/import")
    public ResultVO<?> importNotes(MultipartFile file, @RequestParam(required = false,name = "type") String type) {
        noteImportStrategyContext.importNotes(file, type);
        return ResultVO.ok();
    }

    @OptLog(optType = EXPORT)
    @ApiOperation(value = "导出笔记")
    @ApiImplicitParam(name = "noteIdList", value = "笔记id", required = true, dataType = "List<Integer>")
    @PostMapping("/admin/notes/export")
    public ResultVO<List<String>> exportNotes(@RequestBody List<Integer> noteIds) {
        return ResultVO.ok(noteService.exportNotes(noteIds));
    }
//
//    @ApiOperation(value = "搜索文章")
//    @GetMapping("/articles/search")
//    public ResultVO<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
//        return ResultVO.ok(articleService.listArticlesBySearch(condition));
//    }

}
