package io.lw900925.ocean.restful.config;

import io.lw900925.ocean.support.spring.data.jpa.auditing.AuditorProvider;
import io.lw900925.ocean.support.spring.data.jpa.auditing.LocalDateTimeProvider;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableJpaRepositories("io.lw900925.ocean.core.repository.jpa")
@EntityScan("io.lw900925.ocean.core.model.entity")
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
