package io.lw900925.ocean.core.repository.mybatis.mapper;

import io.lw900925.ocean.core.model.entity.User;

import java.util.List;

public interface UserMapper {
    List<User> select();
    User selectOne(String username);
}
