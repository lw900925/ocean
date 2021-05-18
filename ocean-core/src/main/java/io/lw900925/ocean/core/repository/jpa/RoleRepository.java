package io.lw900925.ocean.core.repository.jpa;

import io.lw900925.ocean.core.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Page<Role> findAllByAuthorityStartingWith(String authority, Pageable pageable);
}
