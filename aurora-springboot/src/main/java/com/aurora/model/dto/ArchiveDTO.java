package com.aurora.model.dto;

import com.aurora.entity.Archive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDTO {

    private String Time;

    List<Archive> archives;
}
