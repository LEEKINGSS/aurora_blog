package com.aurora.service;

import com.aurora.entity.Photo;
import com.aurora.model.dto.PageResultDTO;
import com.aurora.model.dto.PhotoAdminDTO;
import com.aurora.model.dto.PhotoDTO;
import com.aurora.model.vo.ConditionVO;
import com.aurora.model.vo.DeleteVO;
import com.aurora.model.vo.PhotoInfoVO;
import com.aurora.model.vo.PhotoVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface PhotoService extends IService<Photo> {

    PageResultDTO<PhotoAdminDTO> listPhotos(ConditionVO conditionVO);

    void updatePhoto(PhotoInfoVO photoInfoVO);

    void savePhotos(PhotoVO photoVO);

    void updatePhotosAlbum(PhotoVO photoVO);

    void updatePhotoDelete(DeleteVO deleteVO);

    void deletePhotos(List<Integer> photoIds);

    PhotoDTO listPhotosByAlbumId(Integer albumId);

}
