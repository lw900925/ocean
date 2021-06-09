package io.lw900925.ocean.restful.service;

import io.lw900925.ocean.core.model.entity.Resource;
import io.lw900925.ocean.core.repository.jpa.ResourceRepository;
import io.lw900925.ocean.core.repository.jpa.RoleRepository;
import io.lw900925.ocean.core.repository.mybatis.mapper.ResourceMapper;
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

@Service
@Transactional
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleRepository roleRepository;

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @Transactional(readOnly = true)
    public Page<Resource> page(String search, Pageable pageable) {
        if (StringUtils.hasText(search)) {
            return resourceRepository.findAllByResourceNameContaining(search, pageable);
        } else {
            return resourceRepository.findAll(pageable);
        }
    }

    @Transactional(readOnly = true)
    public Resource get(Long resourceId) {
        return resourceRepository.findById(resourceId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", resourceId));
    }

    public Resource create(Resource resource) {
        Resource dbResource = resourceRepository.findByResourceName(resource.getResourceName());
        if (dbResource != null) {
            throw new AppException(HttpStatus.CONFLICT, "E000001", resource.getResourceName());
        }

        dbResource = resourceRepository.findByUri(resource.getUri());
        if (dbResource != null) {
            throw new AppException(HttpStatus.CONFLICT, "E000001", resource.getUri());
        }
        return resourceRepository.save(resource);
    }

    public Resource update(Resource resource) {
        if (resource.getOid() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "E000003", "oid");
        }

        Resource dbResource = resourceRepository.findById(resource.getOid()).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", resource.getOid()));
        BeanUtils.copyProperties(resource, dbResource, "roles");
        return resourceRepository.save(dbResource);
    }

    public void delete(Long resourceId) {
        if (resourceId == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "E000003", resourceId);
        }

        // 判断资源是否被引用
        List<String> authorities = resourceMapper.selectReferenceAuthorities(resourceId);
        if (authorities.size() > 0) {
            throw new AppException("E200001", resourceId);
        }

        resourceRepository.deleteById(resourceId);
    }
}
