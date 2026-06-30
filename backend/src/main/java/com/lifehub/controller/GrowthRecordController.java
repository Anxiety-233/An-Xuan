package com.lifehub.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lifehub.common.Result;
import com.lifehub.entity.GrowthRecord;
import com.lifehub.service.GrowthRecordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/growth")
public class GrowthRecordController {

    @Resource
    private GrowthRecordService growthRecordService;

    @GetMapping
    public Result<Page<GrowthRecord>> getRecords(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<GrowthRecord> records = growthRecordService.getRecordsByUser(userId, pageNum, pageSize);
        return Result.success(records);
    }

    @PostMapping
    public Result<GrowthRecord> createRecord(@RequestBody GrowthRecord record) {
        GrowthRecord created = growthRecordService.createRecord(record);
        return Result.success(created);
    }

    @PutMapping("/{id}")
    public Result<String> updateRecord(
            @PathVariable Long id,
            @RequestBody GrowthRecord record) {
        boolean success = growthRecordService.updateRecord(id, record);
        return success ? Result.success("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteRecord(
            @PathVariable Long id,
            @RequestParam Long userId) {
        boolean success = growthRecordService.deleteRecord(id, userId);
        return success ? Result.success("删除成功") : Result.error("删除失败");
    }
}
