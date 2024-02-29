package com.aurora.mapper;

import com.aurora.entity.Archive;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ArchiveMapper extends BaseMapper<Archive> {
    List<Archive> listArchives(@Param("current") Long current, @Param("size") Long size);
}

