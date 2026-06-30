package com.lifehub.controller;

import com.lifehub.common.Result;
import com.lifehub.entity.Photo;
import com.lifehub.service.PhotoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Resource
    private PhotoService photoService;

    @GetMapping
    public Result<List<Photo>> getPhotosByAlbum(@RequestParam Long albumId) {
        List<Photo> photos = photoService.getPhotosByAlbum(albumId);
        return Result.success(photos);
    }

    @PostMapping
    public Result<Photo> uploadPhoto(@RequestBody Photo photo) {
        Photo uploaded = photoService.uploadPhoto(photo);
        return Result.success(uploaded);
    }

    @DeleteMapping("/{id}")
    public Result<String> deletePhoto(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = photoService.deletePhoto(id, userId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
