package io.lw900925.ocean.oauth2.config;

import io.lw900925.ocean.support.spring.web.servlet.filter.CorsFilter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

@SpringBootConfiguration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public Filter corsFilter() {
        return new CorsFilter();
    }
}
