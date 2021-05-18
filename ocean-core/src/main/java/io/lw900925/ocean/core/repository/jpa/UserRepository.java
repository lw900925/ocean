package io.lw900925.ocean.core.repository.jpa;

import io.lw900925.ocean.core.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
    User findByPhoneNumber(String phoneNumber);

    User findByEmail(String email);

    Page<User> findAllByUsernameStartingWith(String username, Pageable pageable);
}
