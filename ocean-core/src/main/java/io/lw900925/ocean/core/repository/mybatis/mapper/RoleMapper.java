package io.lw900925.ocean.core.repository.mybatis.mapper;

import io.lw900925.ocean.core.model.entity.Role;

public interface RoleMapper {
    Role selectOne(String authority);
    Long countByAuthorityReference(String authority);
}
