package com.aurora.service;

import com.aurora.entity.UniqueView;
import com.aurora.model.dto.UniqueViewDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface UniqueViewService extends IService<UniqueView> {

    List<UniqueViewDTO> listUniqueViews();

}
