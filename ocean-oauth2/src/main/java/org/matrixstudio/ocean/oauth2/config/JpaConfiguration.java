package org.matrixstudio.ocean.oauth2.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories("org.matrixstudio.ocean.core.repository")
@EntityScan("org.matrixstudio.ocean.core.model.entity")
public class JpaConfiguration {
}
