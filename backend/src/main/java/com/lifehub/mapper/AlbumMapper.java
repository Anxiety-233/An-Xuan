package com.lifehub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lifehub.entity.Album;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlbumMapper extends BaseMapper<Album> {
}
