package io.lw900925.ocean.core.repository.mybatis.mapper;

import io.lw900925.ocean.core.model.entity.Role;

import java.util.List;

public interface RoleMapper {
    Role selectOne(String authority);
    List<Role> selectByUserReference(String username);
    List<Role> selectByResourceReference(Long resourceId);
    List<String> selectReferenceUsernames(String authority);
}
