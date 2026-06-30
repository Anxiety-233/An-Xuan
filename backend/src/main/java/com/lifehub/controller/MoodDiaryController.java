package com.lifehub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifehub.common.Result;
import com.lifehub.entity.MoodDiary;
import com.lifehub.service.MoodDiaryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/moods")
public class MoodDiaryController {

    @Resource
    private MoodDiaryService moodDiaryService;

    @GetMapping
    public Result<Page<MoodDiary>> getMoods(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<MoodDiary> diaries = moodDiaryService.getDiariesByUser(userId, pageNum, pageSize);
        return Result.success(diaries);
    }

    @PostMapping
    public Result<MoodDiary> createMood(@RequestBody MoodDiary diary) {
        MoodDiary created = moodDiaryService.createDiary(diary);
        return Result.success(created);
    }

    @GetMapping("/partner")
    public Result<MoodDiary> getPartnerMood(@RequestParam Long userId) {
        MoodDiary partnerMood = moodDiaryService.getPartnerMood(userId);
        return Result.success(partnerMood);
    }
}
