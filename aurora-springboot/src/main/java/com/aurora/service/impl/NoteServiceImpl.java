package com.aurora.service.impl;

import com.alibaba.fastjson.JSON;
import com.aurora.entity.Collection;
import com.aurora.entity.Note;
import com.aurora.entity.NoteTag;
import com.aurora.entity.Tag;
import com.aurora.enums.FileExtEnum;
import com.aurora.enums.FilePathEnum;
import com.aurora.exception.BizException;
import com.aurora.mapper.*;
import com.aurora.model.dto.*;
import com.aurora.model.vo.*;
import com.aurora.service.*;
import com.aurora.strategy.BaiduTranslateStrategy;
import com.aurora.strategy.context.SearchStrategyContext;
import com.aurora.strategy.context.UploadStrategyContext;
import com.aurora.util.BeanCopyUtil;
import com.aurora.util.PageUtil;
import com.aurora.util.UserUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.aurora.constant.ArticleConstant.*;
import static com.aurora.constant.RabbitMQConstant.SUBSCRIBE_EXCHANGE;
import static com.aurora.constant.RedisConstant.*;
import static com.aurora.enums.ArticleStatusEnum.DRAFT;
import static com.aurora.enums.StatusCodeEnum.ARTICLE_ACCESS_FAIL;


/**
 * @author liking
 * 笔记服务实现类
 */
@Service
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements NoteService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired
    private SearchStrategyContext searchStrategyContext;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private NoteTagMapper noteTagMapper;

    @Autowired
    private BaiduTranslateStrategy baiduTranslateStrategy;

    /**
     * 笔记合集mapper
     */
    @Autowired
    private CollectionMapper collectionMapper;

    /**
     * 笔记标签service
     */
    @Autowired
    private NoteTagService noteTagService;

    @Autowired
    private ArchiveService archiveService;

    @SneakyThrows
    @Override
    public TopAndFeaturedArticlesDTO listTopAndFeaturedArticles() {
        List<ArticleCardDTO> articleCardDTOs = articleMapper.listTopAndFeaturedArticles();
        if (articleCardDTOs.size() == 0) {
            return new TopAndFeaturedArticlesDTO();
        } else if (articleCardDTOs.size() > 3) {
            articleCardDTOs = articleCardDTOs.subList(0, 3);
        }
        TopAndFeaturedArticlesDTO topAndFeaturedArticlesDTO = new TopAndFeaturedArticlesDTO();
        topAndFeaturedArticlesDTO.setTopArticle(articleCardDTOs.get(0));
        articleCardDTOs.remove(0);
        topAndFeaturedArticlesDTO.setFeaturedArticles(articleCardDTOs);
        return topAndFeaturedArticlesDTO;
    }

    @SneakyThrows
    public PageResultDTO<NoteCardDTO> listNotes() {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>()
                .eq(Note::getIsDelete, 0)
                .eq(Note::getStatus, 1);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> noteMapper.selectCount(queryWrapper));
        List<NoteCardDTO> notes = noteMapper.listNotes(PageUtil.getLimitCurrent(), PageUtil.getSize());
        return new PageResultDTO<>(notes, asyncCount.get());
    }

    @SneakyThrows
    @Override
    public PageResultDTO<NoteCardDTO> listNotesByCollectionId(Integer collectionId) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<Note>().eq(Note::getCollectionId, collectionId);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> noteMapper.selectCount(queryWrapper));
        List<NoteCardDTO> notes = noteMapper.getNotesByCollectionId(collectionId);
        return new PageResultDTO<>(notes, asyncCount.get());
    }

    /**
     * 根据id获取笔记
     */
    @SneakyThrows
    @Override
    public NoteDTO getNoteById(Integer noteId) {
        Note noteForCheck = noteMapper.selectOne(new LambdaQueryWrapper<Note>().eq(Note::getId, noteId));
        if (Objects.isNull(noteForCheck)) {
            return null;
        }
        if (noteForCheck.getStatus().equals(ARTICLE_STATUS_PRIVATE)) {
            Boolean isAccess;
            try {
                isAccess = redisService.sIsMember(ARTICLE_ACCESS + UserUtil.getUserDetailsDTO().getId(), noteId);
            } catch (Exception exception) {
                throw new BizException(ARTICLE_ACCESS_FAIL);
            }
            if (isAccess.equals(false)) {
                throw new BizException(ARTICLE_ACCESS_FAIL);
            }
        }
        updateNoteViewsCount(noteId);
        CompletableFuture<NoteDTO> asyncNote = CompletableFuture.supplyAsync(() -> noteMapper.getNoteById(noteId));
        CompletableFuture<NoteCardDTO> asyncPreNote = CompletableFuture.supplyAsync(() -> {
            NoteCardDTO preNote = noteMapper.getPreNoteById(noteId);
            if (Objects.isNull(preNote)) {
                preNote = noteMapper.getLastNote();
            }
            return preNote;
        });
        CompletableFuture<NoteCardDTO> asyncNextNote = CompletableFuture.supplyAsync(() -> {
            NoteCardDTO nextNote = noteMapper.getNextNoteById(noteId);
            if (Objects.isNull(nextNote)) {
                nextNote = noteMapper.getFirstNote();
            }
            return nextNote;
        });
        NoteDTO note = asyncNote.get();
        if (Objects.isNull(note)) {
            return null;
        }
        Double score = redisService.zScore(NOTE_VIEWS_COUNT, noteId);
        if (Objects.nonNull(score)) {
            note.setViewCount(score.intValue());
        }
        note.setPreNoteCard(asyncPreNote.get());
        note.setNextNoteCard(asyncNextNote.get());
        return note;
    }

    @Override
    public void accessNote(NotePasswordVO notePasswordVO) {
        Note note = noteMapper.selectOne(new LambdaQueryWrapper<Note>().eq(Note::getId, notePasswordVO.getNoteId()));
        if (Objects.isNull(note)) {
            throw new BizException("笔记不存在");
        }
        if (note.getPassword().equals(notePasswordVO.getNotePassword())) {
            redisService.sAdd(NOTE_ACCESS + UserUtil.getUserDetailsDTO().getId(), notePasswordVO.getNoteId());
        } else {
            throw new BizException("密码错误");
        }
    }

    @SneakyThrows
    @Override
    public PageResultDTO<NoteCardDTO> listNotesByTagId(Integer tagId) {
        LambdaQueryWrapper<NoteTag> queryWrapper = new LambdaQueryWrapper<NoteTag>().eq(NoteTag::getTagId, tagId);
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> noteTagMapper.selectCount(queryWrapper));
        List<NoteCardDTO> notes = noteMapper.listNotesByTagId(tagId);
        return new PageResultDTO<>(notes, asyncCount.get());
    }

    @SneakyThrows
    @Override
    public PageResultDTO<NoteAdminDTO> listNotesAdmin(ConditionVO conditionVO) {
        // 异步查询笔记总数
        CompletableFuture<Integer> asyncCount = CompletableFuture.supplyAsync(() -> noteMapper.countNoteAdmins(conditionVO));
        List<NoteAdminDTO> noteAdminDTOs = noteMapper.listNotesAdmin(PageUtil.getLimitCurrent(), PageUtil.getSize(), conditionVO);
        Map<Object, Double> viewsCountMap = redisService.zAllScore(NOTE_VIEWS_COUNT);
        noteAdminDTOs.forEach(item -> {
            Double viewsCount = viewsCountMap.get(item.getId());
            if (Objects.nonNull(viewsCount)) {
                item.setViewsCount(viewsCount.intValue());
            }
        });
        return new PageResultDTO<>(noteAdminDTOs, asyncCount.get());
    }

    /**
     * 保存并更新笔记
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateNote(NoteVO noteVO) {
        Collection collection = saveNoteCollection(noteVO);
        Note note = BeanCopyUtil.copyObject(noteVO, Note.class);
        if (Objects.nonNull(collection)) {
            note.setCollectionId(collection.getId());
        }
        note.setUserId(UserUtil.getUserDetailsDTO().getUserInfoId());
        String noteContent = note.getNoteContent();
        noteContent = noteContent.replaceAll("[*_:`~#\\[\\]<>]", "");
        int count = 0;
        for (int i = 0; i < noteContent.length(); i++) {
            if (isChineseCharacter(noteContent.charAt(i))) {
                count++;
            }
        }
        note.setNoteTime(getReadTime(count));
        note.setNoteCount(count);
        if (!note.getNoteQuotes().contains("|")){
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode;
            if (!Objects.equals(note.getNoteQuotes(), "") && note.getNoteQuotes() != null) {
                try {
                    jsonNode = objectMapper.readTree(baiduTranslateStrategy.getTransResult(note.getNoteQuotes(), "auto", "en"));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                String translation = jsonNode.get("trans_result").get(0).get("dst").asText();
                if (Objects.nonNull(note.getNoteQuotes())) {
                    note.setNoteQuotes(note.getNoteQuotes() + "|" + translation);
                }
            }
        }
        if(note.getId() != null){
            Note tmp = noteMapper.selectById(note.getId());
            // 查看是否有归档id
            if (Objects.isNull(tmp.getArchiveId())) {
                note.setArchiveId(UUID.randomUUID().toString());
            }else{
                note.setArchiveId(tmp.getArchiveId());
            }
        }else{
            note.setArchiveId(UUID.randomUUID().toString());
        }
        this.saveOrUpdate(note);
        saveNoteTag(noteVO, note.getId());
        if (note.getStatus().equals(ARTICLE_STATUS_PUBLIC)) {
            rabbitTemplate.convertAndSend(SUBSCRIBE_EXCHANGE, "*", new Message(JSON.toJSONBytes(note.getId()), new MessageProperties()));
            archiveService.saveNoteArchive(note);
        }
    }

    /**
     * 判断字符是否为汉字
     */
    public boolean isChineseCharacter(char c) {
        return c >= '\u4e00' && c <= '\u9fa5';
    }

    /**
     * 判断阅读汉字需要的时间
     */
    public String getReadTime(int count) {
        int minutes = count / WORDS_PER_MINUTE;
        int seconds = (count - minutes * WORDS_PER_MINUTE) / (WORDS_PER_MINUTE / 60);
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }

    /**
     * 保存笔记标签
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveNoteTag(NoteVO noteVO, Integer noteId) {
        if (Objects.nonNull(noteVO.getId())) {
            noteTagMapper.delete(new LambdaQueryWrapper<NoteTag>()
                    .eq(NoteTag::getNoteId, noteVO.getId()));
        }
        List<String> tagNames = noteVO.getTagNames();
        if (CollectionUtils.isNotEmpty(tagNames)) {
            List<Tag> existTags = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNames));
            List<String> existTagNames = existTags.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIds = existTags.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            tagNames.removeAll(existTagNames);
            if (CollectionUtils.isNotEmpty(tagNames)) {
                List<Tag> tags = tagNames.stream().map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                tagService.saveBatch(tags);
                List<Integer> tagIds = tags.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                existTagIds.addAll(tagIds);
            }
            List<NoteTag> noteTags = existTagIds.stream().map(item -> NoteTag.builder()
                            .noteId(noteId)
                            .tagId(item)
                            .build())
                    .collect(Collectors.toList());
            noteTagService.saveBatch(noteTags);
        }
    }

    /**
     * 保存笔记合集
     */
    private Collection saveNoteCollection(NoteVO noteVO) {
        Collection collection = collectionMapper.selectOne(new LambdaQueryWrapper<Collection>()
                .eq(Collection::getCollectionName, noteVO.getCollectionName()));
        if (Objects.isNull(collection) && !noteVO.getStatus().equals(DRAFT.getStatus())) {
            collection = Collection.builder()
                    .collectionName(noteVO.getCollectionName())
                    .build();
            collectionMapper.insert(collection);
        }
        return collection;
    }

    /**
     * 更新笔记的推荐状态
     */
    @Override
    public void updateNoteFeatured(NoteFeaturedVO noteFeaturedVO) {
        Note note = Note.builder()
                .id(noteFeaturedVO.getId())
                .isFeatured(noteFeaturedVO.getIsFeatured())
                .build();
        this.updateById(note);
    }

    /**
     * 逻辑删除笔记
     */
    @Override
    public void updateNoteDelete(DeleteVO deleteVO) {
        List<Note> notes = deleteVO.getIds().stream()
                .map(id -> Note.builder()
                        .id(id)
                        .isDelete(deleteVO.getIsDelete())
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(notes);
        // 更新笔记归档信息
        notes = noteMapper.selectBatchIds(deleteVO.getIds());
        List<String> archiveIds = notes.stream()
                .map(Note::getArchiveId)
                .collect(Collectors.toList());
        archiveService.updateArchiveDelete(archiveIds, deleteVO.getIsDelete());
    }

    /**
     * 物理删除笔记
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNotes(List<Integer> noteIds) {
        List<Note> notes = noteMapper.selectBatchIds(noteIds);
        List<String> archiveIds = notes.stream()
                .map(Note::getArchiveId)
                .collect(Collectors.toList());
        archiveService.deleteArchives(archiveIds);
        noteTagMapper.delete(new LambdaQueryWrapper<NoteTag>()
                .in(NoteTag::getNoteId, noteIds));
        noteMapper.deleteBatchIds(noteIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NoteAdminViewDTO getNoteByIdAdmin(Integer noteId) {
        Note note = noteMapper.selectById(noteId);
        Collection collection = collectionMapper.selectById(note.getCollectionId());
        String collectionName = null;
        if (Objects.nonNull(collection)) {
            collectionName = collection.getCollectionName();
        }
        List<String> tagNames = tagMapper.listTagNamesByNoteId(noteId);
        NoteAdminViewDTO noteAdminViewDTO = BeanCopyUtil.copyObject(note, NoteAdminViewDTO.class);
        noteAdminViewDTO.setCollectionName(collectionName);
        noteAdminViewDTO.setTagNames(tagNames);
        return noteAdminViewDTO;
    }

    @Override
    public List<String> exportNotes(List<Integer> noteIds) {
        List<Note> notes = noteMapper.selectList(new LambdaQueryWrapper<Note>()
                .select(Note::getNoteTitle, Note::getNoteContent)
                .in(Note::getId, noteIds));
        List<String> urls = new ArrayList<>();
        for (Note note : notes) {
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(note.getNoteContent().getBytes())) {
                String url = uploadStrategyContext.executeUploadStrategy(note.getNoteTitle() + FileExtEnum.MD.getExtName(), inputStream, FilePathEnum.MD.getPath());
                urls.add(url);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BizException("导出笔记失败");
            }
        }
        return urls;
    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    public void updateNoteViewsCount(Integer noteId) {
        redisService.zIncr(NOTE_VIEWS_COUNT, noteId, 1D);
    }

}
