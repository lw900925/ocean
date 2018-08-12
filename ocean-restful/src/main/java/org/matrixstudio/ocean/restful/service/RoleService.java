package org.matrixstudio.ocean.restful.service;

import org.matrixstudio.ocean.core.model.entity.Role;
import org.matrixstudio.ocean.core.repository.jpa.RoleRepository;
import org.matrixstudio.ocean.restful.controller.RestException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<Role> list(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return roleRepository.findAllByAuthorityStartingWith(search, pageable);
        } else {
            return roleRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Role get(String authority) {
        return roleRepository.findById(authority).orElseThrow(EntityNotFoundException::new);
    }

    public Role create(Role role) {
        // 角色名称不能重复
        Role newRole = roleRepository.findById(role.getAuthority()).orElse(null);
        if (newRole != null) {
            throw new EntityExistsException(String.format("Role name %s has already exists.", role.getAuthority()));
        }

        newRole = new Role();
        BeanUtils.copyProperties(role, newRole);
        return roleRepository.save(newRole);
    }

    public Role update(Role role) {
        Role newRole = roleRepository.findById(role.getAuthority()).orElseThrow(EntityNotFoundException::new);
        BeanUtils.copyProperties(role, newRole, "authority");

        return roleRepository.save(newRole);
    }

    public Role delete(String authority) {
        Role role = roleRepository.findById(authority).orElseThrow(EntityNotFoundException::new);

        // 判断角色是否被引用
        Long count = roleRepository.countByAuthorityReference(role.getAuthority());
        if (count > 0) {
            throw new RestException(HttpStatus.BAD_REQUEST, "ROLE_HAS_BEEN_USED", "Role has already used");
        }

        roleRepository.delete(role);
        return role;
    }
}
