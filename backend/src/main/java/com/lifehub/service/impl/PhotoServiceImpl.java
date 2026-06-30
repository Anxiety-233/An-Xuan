package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.Photo;
import com.lifehub.mapper.PhotoMapper;
import com.lifehub.service.PhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoServiceImpl extends ServiceImpl<PhotoMapper, Photo> implements PhotoService {

    @Override
    public List<Photo> getPhotosByAlbum(Long albumId) {
        return this.list(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getAlbumId, albumId)
                .orderByDesc(Photo::getCreatedAt));
    }

    @Override
    public Photo uploadPhoto(Photo photo) {
        this.save(photo);
        return photo;
    }

    @Override
    public boolean deletePhoto(Long id, Long userId) {
        return this.remove(new LambdaQueryWrapper<Photo>()
                .eq(Photo::getId, id)
                .eq(Photo::getUploadedBy, userId));
    }
}
