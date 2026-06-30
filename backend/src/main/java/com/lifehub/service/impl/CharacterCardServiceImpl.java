package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.CharacterCard;
import com.lifehub.entity.CharacterInteraction;
import com.lifehub.mapper.CharacterCardMapper;
import com.lifehub.mapper.CharacterInteractionMapper;
import com.lifehub.service.CharacterCardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CharacterCardServiceImpl extends ServiceImpl<CharacterCardMapper, CharacterCard> implements CharacterCardService {

    @Resource
    private CharacterInteractionMapper interactionMapper;

    @Override
    public Page<CharacterCard> getPublicCharacters(int pageNum, int pageSize) {
        Page<CharacterCard> page = new Page<>(pageNum, pageSize);
        return this.page(page, new LambdaQueryWrapper<CharacterCard>()
                .eq(CharacterCard::getIsPublic, true)
                .orderByDesc(CharacterCard::getLikeCount));
    }

    @Override
    public CharacterCard getCharacterDetail(Long id) {
        CharacterCard character = this.getById(id);
        if (character != null) {
            // 增加浏览量
            character.setViewCount(character.getViewCount() + 1);
            this.updateById(character);
        }
        return character;
    }

    @Override
    public CharacterCard createCharacter(CharacterCard character) {
        if (character.getViewCount() == null) {
            character.setViewCount(0);
        }
        if (character.getLikeCount() == null) {
            character.setLikeCount(0);
        }
        this.save(character);
        return character;
    }

    @Override
    public boolean likeCharacter(Long id, Long userId) {
        CharacterCard character = this.getById(id);
        if (character == null) {
            return false;
        }

        // 检查是否已经点赞
        long count = interactionMapper.selectCount(new LambdaQueryWrapper<CharacterInteraction>()
                .eq(CharacterInteraction::getCharacterId, id)
                .eq(CharacterInteraction::getUserId, userId)
                .eq(CharacterInteraction::getInteractionType, "like"));

        if (count > 0) {
            return false; // 已经点赞过
        }

        // 记录互动
        CharacterInteraction interaction = new CharacterInteraction();
        interaction.setCharacterId(id);
        interaction.setUserId(userId);
        interaction.setInteractionType("like");
        interactionMapper.insert(interaction);

        // 增加点赞数
        character.setLikeCount(character.getLikeCount() + 1);
        this.updateById(character);

        return true;
    }
}
