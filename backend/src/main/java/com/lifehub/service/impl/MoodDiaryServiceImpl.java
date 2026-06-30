package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.MoodDiary;
import com.lifehub.mapper.MoodDiaryMapper;
import com.lifehub.service.MoodDiaryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MoodDiaryServiceImpl extends ServiceImpl<MoodDiaryMapper, MoodDiary> implements MoodDiaryService {

    @Override
    public Page<MoodDiary> getDiariesByUser(Long userId, int pageNum, int pageSize) {
        Page<MoodDiary> page = new Page<>(pageNum, pageSize);
        return this.page(page, new LambdaQueryWrapper<MoodDiary>()
                .eq(MoodDiary::getUserId, userId)
                .orderByDesc(MoodDiary::getDiaryDate));
    }

    @Override
    public MoodDiary createDiary(MoodDiary diary) {
        this.save(diary);
        return diary;
    }

    @Override
    public MoodDiary getPartnerMood(Long userId) {
        // 简化实现：假设情侣ID为userId+1或userId-1
        Long partnerId = (userId % 2 == 0) ? userId - 1 : userId + 1;

        return this.getOne(new LambdaQueryWrapper<MoodDiary>()
                .eq(MoodDiary::getUserId, partnerId)
                .eq(MoodDiary::getDiaryDate, LocalDate.now())
                .eq(MoodDiary::getIsVisibleToPartner, true)
                .orderByDesc(MoodDiary::getCreatedAt)
                .last("LIMIT 1"));
    }
}
