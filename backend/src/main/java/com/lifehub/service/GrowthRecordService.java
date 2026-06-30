package com.lifehub.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lifehub.entity.GrowthRecord;

public interface GrowthRecordService extends IService<GrowthRecord> {
    Page<GrowthRecord> getRecordsByUser(Long userId, int pageNum, int pageSize);
    GrowthRecord createRecord(GrowthRecord record);
    boolean updateRecord(Long id, GrowthRecord record);
    boolean deleteRecord(Long id, Long userId);
}
