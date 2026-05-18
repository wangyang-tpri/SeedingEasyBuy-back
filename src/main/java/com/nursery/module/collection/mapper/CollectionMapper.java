package com.nursery.module.collection.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nursery.module.collection.entity.Favorites;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CollectionMapper extends BaseMapper<Favorites> {
}
