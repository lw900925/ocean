package org.matrixstudio.ocean.core.repository.jpa;

import org.matrixstudio.ocean.core.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Page<Role> findAllByAuthorityStartingWith(String authority, Pageable pageable);

    @Query(value = "SELECT COUNT(b.authority) FROM mk_role a, mk_user_role_ref b WHERE a.authority = b.authority AND a.authority = :authority", nativeQuery = true)
    Long countByAuthorityReference(@Param("authority") String authority);
}
