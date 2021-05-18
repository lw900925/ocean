package io.lw900925.ocean.core.repository.jpa;

import io.lw900925.ocean.core.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {
}
