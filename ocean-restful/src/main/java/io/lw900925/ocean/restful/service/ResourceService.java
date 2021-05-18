package io.lw900925.ocean.restful.service;

import io.lw900925.ocean.core.model.entity.Resource;
import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.core.repository.jpa.ResourceRepository;
import io.lw900925.ocean.core.repository.jpa.RoleRepository;
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
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private RoleRepository roleRepository;

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

    public Resource save(Resource resource) {
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
            throw new AppException(HttpStatus.BAD_REQUEST, "E000003", "resourceId");
        }
        resourceRepository.deleteById(resourceId);
    }

    public Resource grant(Long resourceId, String authority) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", resourceId));
        Role role = roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));

        List<Role> roles = resource.getRoles();
        long count = roles.stream().filter(r -> r.getAuthority().equals(authority)).count();
        if (count > 0) {
            throw new AppException(HttpStatus.CONFLICT, "E000001", authority);
        }
        roles.add(role);

        return resourceRepository.save(resource);
    }

    public Resource revoke(Long resourceId, String authority) {
        Resource resource = resourceRepository.findById(resourceId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", resourceId));
        Role role = roleRepository.findById(authority).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "E000002", authority));

        List<Role> roles = resource.getRoles().stream().filter(r -> !r.getAuthority().equals(role.getAuthority())).collect(Collectors.toList());
        resource.setRoles(roles);
        return resourceRepository.save(resource);
    }
}
