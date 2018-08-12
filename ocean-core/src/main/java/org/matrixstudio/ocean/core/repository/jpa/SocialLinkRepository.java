package org.matrixstudio.ocean.core.repository.jpa;

import org.matrixstudio.ocean.core.model.entity.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SocialLinkRepository extends JpaRepository<SocialLink, Long>, JpaSpecificationExecutor<SocialLink> {
    List<SocialLink> findAllByUsername(String username);
    SocialLink findByUsernameAndUid(String username, String uid);
}
