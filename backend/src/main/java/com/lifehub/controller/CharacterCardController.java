package com.lifehub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifehub.common.Result;
import com.lifehub.entity.CharacterCard;
import com.lifehub.service.CharacterCardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/characters")
public class CharacterCardController {

    @Resource
    private CharacterCardService characterCardService;

    @GetMapping
    public Result<Page<CharacterCard>> getCharacters(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<CharacterCard> characters = characterCardService.getPublicCharacters(pageNum, pageSize);
        return Result.success(characters);
    }

    @GetMapping("/{id}")
    public Result<CharacterCard> getCharacterDetail(@PathVariable Long id) {
        CharacterCard character = characterCardService.getCharacterDetail(id);
        return character != null ? Result.success(character) : Result.error("角色不存在");
    }

    @PostMapping
    public Result<CharacterCard> createCharacter(@RequestBody CharacterCard character) {
        CharacterCard created = characterCardService.createCharacter(character);
        return Result.success(created);
    }

    @PostMapping("/{id}/like")
    public Result<String> likeCharacter(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = characterCardService.likeCharacter(id, userId);
        return success ? Result.success("点赞成功") : Result.error("已经点赞过或角色不存在");
    }
}
