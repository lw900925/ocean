package io.lw900925.ocean.core.repository.mybatis.mapper;

import io.lw900925.ocean.core.model.entity.Resource;

import java.util.List;

public interface ResourceMapper {
    int insert(Resource resource);
    List<String> selectReferenceAuthorities(Long resourceId);
}
