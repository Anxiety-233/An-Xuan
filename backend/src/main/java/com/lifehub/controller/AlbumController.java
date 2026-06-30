package com.lifehub.controller;

import com.lifehub.common.Result;
import com.lifehub.entity.Album;
import com.lifehub.service.AlbumService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    @Resource
    private AlbumService albumService;

    @GetMapping
    public Result<List<Album>> getAlbums(@RequestParam Long userId) {
        List<Album> albums = albumService.getAlbumsByUser(userId);
        return Result.success(albums);
    }

    @GetMapping("/{id}")
    public Result<Album> getAlbumById(@PathVariable Long id) {
        Album album = albumService.getById(id);
        return album != null ? Result.success(album) : Result.error("相册不存在");
    }

    @PostMapping
    public Result<Album> createAlbum(@RequestBody Album album) {
        Album created = albumService.createAlbum(album);
        return Result.success(created);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteAlbum(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = albumService.deleteAlbum(id, userId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
