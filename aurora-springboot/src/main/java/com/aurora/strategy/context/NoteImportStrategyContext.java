package com.aurora.strategy.context;

import com.aurora.enums.MarkdownTypeEnum;
import com.aurora.strategy.NoteImportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class NoteImportStrategyContext {

    @Autowired
    private Map<String, NoteImportStrategy> noteImportStrategyMap;
    /**
     *  导入笔记
     */
    public void importNotes(MultipartFile file, String type) {
        noteImportStrategyMap.get(MarkdownTypeEnum.getMarkdownType(type)).importNotes(file);
    }
}
