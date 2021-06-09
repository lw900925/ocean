package io.lw900925.ocean.restful.service;

import com.google.common.collect.Lists;
import io.lw900925.ocean.core.model.entity.Resource;
import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.core.repository.jpa.ResourceRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;
    @javax.annotation.Resource
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
    public List<Role> listByResourceReference(Long resourceId) {
        return roleMapper.selectByResourceReference(resourceId);
    }

    @Transactional(readOnly = true)
    public Role get(String authority) {
        return roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));
    }

    public Role saveOrUpdate(Role role) {
        List<Resource> resources = role.getResources().stream()
                .map(Resource::getOid)
                .map(resourceId -> resourceRepository.findById(resourceId)
                        .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", resourceId)))
                .collect(Collectors.toList());
        role.setResources(resources);
        return roleRepository.save(role);
    }

    public Role delete(String authority) {
        Role role = roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));

        // 判断角色是否被引用
        List<String> usernames = roleMapper.selectReferenceUsernames(role.getAuthority());
        if (usernames.size() > 0) {
            throw new AppException("E100001", authority);
        }

        // 删除关联的资源
        role.setResources(Lists.newArrayList());

        roleRepository.delete(role);
        return role;
    }
}
