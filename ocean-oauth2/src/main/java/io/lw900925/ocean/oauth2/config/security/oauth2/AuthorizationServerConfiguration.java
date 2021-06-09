package io.lw900925.ocean.oauth2.config.security.oauth2;

import io.lw900925.ocean.core.repository.jpa.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@SpringBootConfiguration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    public AuthorizationServerConfiguration(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 注：这里需要设置AuthenticationManager对象，否则没有password授权模式
        endpoints.authenticationManager(authenticationManager);
        endpoints.accessTokenConverter(tokenConverter());

        // 授权类型refresh_token需要额外配置UserDetailsService
        endpoints.userDetailsService(userDetailsService);
    }

    @Bean
    public AccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "123456".toCharArray()).getKeyPair("spring-oauth2");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public ClientDetailsService clientDetailsService() {
        return new JpaClientDetailsService(clientRepository);
    }
}
