package io.lw900925.ocean.restful.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.lw900925.ocean.core.repository.jpa.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
@EnableResourceServer
@EnableConfigurationProperties(value = { ResourceServerConfiguration.ResourceServerExtensionProperties.class })
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    public static final Logger LOGGER = LoggerFactory.getLogger(ResourceServerConfiguration.class);

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private ResourceServerExtensionProperties resourceServerExtensionProperties;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        // 添加未登录请求处理和无权限访问处理
        http.exceptionHandling().authenticationEntryPoint(new DefaultAuthenticationEntryPoint(objectMapper));
        http.exceptionHandling().accessDeniedHandler(new DefaultAccessDeniedHandler(objectMapper));

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();
        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                object.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
                object.setAccessDecisionManager(urlAccessDecisionManager());
                return object;
            }
        });

        // 不需要授权的请求
        String[] ignoreUrls = resourceServerExtensionProperties.getIgnoreUrls().toArray(new String[0]);
        LOGGER.debug("以下URL将不会被Spring Security拦截：\n\r{}", String.join("\n\r", ignoreUrls));
        registry.antMatchers(ignoreUrls).permitAll();
        registry.anyRequest().access("#oauth2.hasScope('app')");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
        UrlFilterInvocationSecurityMetadataSource securityMetadataSource = new UrlFilterInvocationSecurityMetadataSource();
        securityMetadataSource.setResourceRepository(resourceRepository);
        securityMetadataSource.setIgnoreUrls(resourceServerExtensionProperties.getIgnoreUrls().toArray(new String[0]));
        return securityMetadataSource;
    }

    @Bean
    public AccessDecisionManager urlAccessDecisionManager() {
        return new UrlAccessDecisionManager();
    }


    // ---------- Properties configuration ----------
    @ConfigurationProperties(prefix = "security.oauth2.resource")
    public class ResourceServerExtensionProperties {

        private List<String> ignoreUrls = new ArrayList<>();

        public List<String> getIgnoreUrls() {
            return ignoreUrls;
        }

        public void setIgnoreUrls(List<String> ignoreUrls) {
            this.ignoreUrls = ignoreUrls;
        }
    }
}
