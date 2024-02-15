package com.aurora.strategy.impl;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurora.exception.BizException;
import com.aurora.model.vo.ArticleVO;
import com.aurora.model.vo.NoteVO;
import com.aurora.service.ArticleService;
import com.aurora.service.NoteService;
import com.aurora.strategy.ArticleImportStrategy;
import com.aurora.strategy.NoteImportStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

import static com.aurora.enums.ArticleStatusEnum.DRAFT;

@Slf4j
@Service("normalNoteImportStrategyImpl")
public class NormalNoteImportStrategyImpl implements NoteImportStrategy {

    @Autowired
    private NoteService noteService;

    @Override
    public void importNotes(MultipartFile file) {
        String noteTitle = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];
        StringBuilder noteContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            while (reader.ready()) {
                noteContent.append((char) reader.read());
            }
        } catch (IOException e) {
            log.error(StrUtil.format("导入笔记失败, 堆栈:{}", ExceptionUtil.stacktraceToString(e)));
            throw new BizException("导入笔记失败");
        }
        NoteVO noteVO = NoteVO.builder()
                .noteTitle(noteTitle)
                .noteContent(noteContent.toString())
                .status(DRAFT.getStatus())
                .build();
        noteService.saveOrUpdateNote(noteVO);
    }
}
