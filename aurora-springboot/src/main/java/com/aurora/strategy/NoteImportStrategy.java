package com.aurora.strategy;

import org.springframework.web.multipart.MultipartFile;

public interface NoteImportStrategy {

    void importNotes(MultipartFile file);

}
