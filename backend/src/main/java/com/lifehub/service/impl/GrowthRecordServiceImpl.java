package com.lifehub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lifehub.entity.GrowthRecord;
import com.lifehub.mapper.GrowthRecordMapper;
import com.lifehub.service.GrowthRecordService;
import org.springframework.stereotype.Service;

@Service
public class GrowthRecordServiceImpl extends ServiceImpl<GrowthRecordMapper, GrowthRecord> implements GrowthRecordService {

    @Override
    public Page<GrowthRecord> getRecordsByUser(Long userId, int pageNum, int pageSize) {
        Page<GrowthRecord> page = new Page<>(pageNum, pageSize);
        return this.page(page, new LambdaQueryWrapper<GrowthRecord>()
                .eq(GrowthRecord::getUserId, userId)
                .orderByDesc(GrowthRecord::getRecordDate));
    }

    @Override
    public GrowthRecord createRecord(GrowthRecord record) {
        this.save(record);
        return record;
    }

    @Override
    public boolean updateRecord(Long id, GrowthRecord record) {
        record.setId(id);
        return this.updateById(record);
    }

    @Override
    public boolean deleteRecord(Long id, Long userId) {
        return this.remove(new LambdaQueryWrapper<GrowthRecord>()
                .eq(GrowthRecord::getId, id)
                .eq(GrowthRecord::getUserId, userId));
    }
}
