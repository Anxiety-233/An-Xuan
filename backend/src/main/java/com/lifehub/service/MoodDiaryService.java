package com.lifehub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.MoodDiary;

public interface MoodDiaryService extends IService<MoodDiary> {
    Page<MoodDiary> getDiariesByUser(Long userId, int pageNum, int pageSize);
    MoodDiary createDiary(MoodDiary diary);
    MoodDiary getPartnerMood(Long userId);
}
