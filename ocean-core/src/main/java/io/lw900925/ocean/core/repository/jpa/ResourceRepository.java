package io.lw900925.ocean.core.repository.jpa;

import io.lw900925.ocean.core.model.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceRepository extends JpaRepository<Resource, Long>, JpaSpecificationExecutor<Resource> {
    Page<Resource> findAllByResourceNameContaining(String resourceName, Pageable pageable);
    Resource findByResourceName(String resourceName);
    Resource findByUri(String uri);
}
