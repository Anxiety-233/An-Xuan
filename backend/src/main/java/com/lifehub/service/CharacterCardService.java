package com.lifehub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.CharacterCard;

public interface CharacterCardService extends IService<CharacterCard> {
    Page<CharacterCard> getPublicCharacters(int pageNum, int pageSize);
    CharacterCard getCharacterDetail(Long id);
    CharacterCard createCharacter(CharacterCard character);
    boolean likeCharacter(Long id, Long userId);
}
