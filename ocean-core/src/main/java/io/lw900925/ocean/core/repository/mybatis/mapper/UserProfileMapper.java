package io.lw900925.ocean.core.repository.mybatis.mapper;

import io.lw900925.ocean.core.model.entity.UserProfile;

public interface UserProfileMapper {

    UserProfile selectByUsername(String username);
}
