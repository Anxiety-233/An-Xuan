package com.lifehub.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.Photo;

import java.util.List;

public interface PhotoService extends IService<Photo> {
    List<Photo> getPhotosByAlbum(Long albumId);
    Photo uploadPhoto(Photo photo);
    boolean deletePhoto(Long id, Long userId);
}
