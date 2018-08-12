package org.matrixstudio.ocean.core.repository.jpa;

import org.matrixstudio.ocean.core.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientRepository extends JpaRepository<Client, String>, JpaSpecificationExecutor<Client> {
}
