package io.lw900925.ocean.oauth2.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories("io.lw900925.ocean.core.repository")
@EntityScan("io.lw900925.ocean.core.model.entity")
public class JpaConfiguration {
}
