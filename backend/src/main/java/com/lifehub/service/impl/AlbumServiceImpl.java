package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.Album;
import com.lifehub.mapper.AlbumMapper;
import com.lifehub.service.AlbumService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumServiceImpl extends ServiceImpl<AlbumMapper, Album> implements AlbumService {

    @Override
    public List<Album> getAlbumsByUser(Long userId) {
        return this.list(new LambdaQueryWrapper<Album>()
                .eq(Album::getCreatedBy, userId)
                .or()
                .eq(Album::getIsCoupleShared, true)
                .orderByDesc(Album::getCreatedAt));
    }

    @Override
    public Album createAlbum(Album album) {
        this.save(album);
        return album;
    }

    @Override
    public boolean deleteAlbum(Long id, Long userId) {
        return this.remove(new LambdaQueryWrapper<Album>()
                .eq(Album::getId, id)
                .eq(Album::getCreatedBy, userId));
    }
}
