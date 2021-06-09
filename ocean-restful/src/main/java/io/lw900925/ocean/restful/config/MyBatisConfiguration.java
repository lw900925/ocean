package io.lw900925.ocean.restful.config;

import io.lw900925.ocean.core.keygen.SnowFlakeKeyGenerator;
import io.lw900925.ocean.support.mybatis.plugins.AuditingInterceptor;
import io.lw900925.ocean.support.mybatis.plugins.KeyGenInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;

@SpringBootConfiguration
@MapperScan("io.lw900925.ocean.core.repository.mybatis.mapper")
public class MyBatisConfiguration {

    @Bean
    public Interceptor keyGenerationInterceptor() {
        return new KeyGenInterceptor(new SnowFlakeKeyGenerator());
    }

    @Bean
    public Interceptor auditingInterceptor(AuditorAware<String> auditorProvider, DateTimeProvider dateTimeProvider) {
        return new AuditingInterceptor(auditorProvider, dateTimeProvider);
    }

    static class MyBatisConfigurationCustomizer implements ConfigurationCustomizer {
        @Override
        public void customize(Configuration configuration) {

        }
    }
}
