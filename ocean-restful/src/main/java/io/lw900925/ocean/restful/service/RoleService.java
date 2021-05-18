package io.lw900925.ocean.restful.service;

import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.core.repository.jpa.RoleRepository;
import io.lw900925.ocean.core.repository.mybatis.mapper.RoleMapper;
import io.lw900925.ocean.support.spring.web.exception.AppException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Resource
    private RoleMapper roleMapper;

    @Transactional(readOnly = true)
    public Page<Role> page(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return roleRepository.findAllByAuthorityStartingWith(search, pageable);
        } else {
            return roleRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Role get(String authority) {
        return roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));
    }

    public Role create(Role role) {
        // 角色名称不能重复
        roleRepository.findById(role.getAuthority()).orElseThrow(() -> new AppException(HttpStatus.CONFLICT, "E000001", role.getAuthority()));
        return roleRepository.save(role);
    }

    public Role update(Role role) {
        Role newRole = roleRepository.findById(role.getAuthority()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", role.getAuthority()));
        BeanUtils.copyProperties(role, newRole, "authority");

        return roleRepository.save(newRole);
    }

    public Role delete(String authority) {
        Role role = roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));

        // 判断角色是否被引用
        Long count = roleMapper.countByAuthorityReference(role.getAuthority());
        if (count > 0) {
            throw new AppException("E100001", authority);
        }

        roleRepository.delete(role);
        return role;
    }
}
