package com.lifehub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.Album;

import java.util.List;

public interface AlbumService extends IService<Album> {
    List<Album> getAlbumsByUser(Long userId);
    Album createAlbum(Album album);
    boolean deleteAlbum(Long id, Long userId);
}
