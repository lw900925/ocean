package org.matrixstudio.ocean.restful.config;

import org.matrixstudio.ocean.support.spring.data.jpa.auditing.AuditorProvider;
import org.matrixstudio.ocean.support.spring.data.jpa.auditing.LocalDateTimeProvider;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories("org.matrixstudio.ocean.core.repository.jpa")
@EntityScan("org.matrixstudio.ocean.core.model.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorProvider", dateTimeProviderRef = "localDateTimeProvider")
public class JpaConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorProvider();
    }

    @Bean
    public LocalDateTimeProvider localDateTimeProvider() {
        return new LocalDateTimeProvider();
    }
}
