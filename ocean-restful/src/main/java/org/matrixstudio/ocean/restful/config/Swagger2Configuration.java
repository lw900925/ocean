package org.matrixstudio.ocean.restful.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootConfiguration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class Swagger2Configuration {

    @Bean
    public Swagger2Properties swagger2Properties() {
        return new Swagger2Properties();
    }

    @Bean
    public Docket apiDocket(Swagger2Properties swagger2Properties) {
        Swagger2Properties.Docket docket = swagger2Properties.getDocket();
        Swagger2Properties.ApiInfo apiInfo = swagger2Properties.getApiInfo();
        Swagger2Properties.Oauth oauth = swagger2Properties.getOauth();


        Docket apiDocket = new Docket(DocumentationType.SWAGGER_2)
                .groupName(docket.getGroupName())
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage(docket.getBasePackage()))
                .build();

        if (apiInfo != null) {
            apiDocket.apiInfo(apiInfo(apiInfo));
        }

        if (oauth != null) {
            apiDocket.securitySchemes(Collections.singletonList(oauth2(oauth)));
            apiDocket.securityContexts(Collections.singletonList(securityContext(oauth)));
        }

        apiDocket.ignoredParameterTypes(Pageable.class);

        return apiDocket;
    }

    @Bean
    @ConditionalOnBean(Swagger2Properties.class)
    @ConditionalOnProperty(prefix = "springfox.oauth", name = {"client-id", "client-secret"})
    public SecurityConfiguration securityInfo(Swagger2Properties swagger2Properties) {
        Swagger2Properties.Oauth oauth = swagger2Properties.getOauth();

        return SecurityConfigurationBuilder
                .builder()
                .clientId(oauth.getClientId())
                .clientSecret(oauth.getClientSecret())
                .scopeSeparator(",")
                .appName(oauth.getAppName())
                .build();
    }

    private ApiInfo apiInfo(Swagger2Properties.ApiInfo apiInfo) {
        Swagger2Properties.ApiInfo.Contact contact = apiInfo.getContact();

        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title(apiInfo.getTitle());
        builder.description(apiInfo.getDescription());
        if (contact != null) {
            builder.contact(new Contact(contact.getName(), contact.getUrl(), contact.getEmail()));
        }
        builder.version(apiInfo.getVersion());
        return builder.build();
    }

    private SecurityScheme oauth2(Swagger2Properties.Oauth oauth) {
        return new OAuthBuilder()
                .name(oauth.getName())
                .scopes(Arrays.asList(scopes(oauth.getScopes())))
                .grantTypes(Arrays.asList(grantTypes(oauth.getTokenUrl())))
                .build();
    }

    private SecurityContext securityContext(Swagger2Properties.Oauth oauth) {
        SecurityReference oauthSecurityReference = SecurityReference.builder()
                .reference(oauth.getName())
                .scopes(scopes(oauth.getScopes()))
                .build();

        return SecurityContext.builder()
                .forPaths(PathSelectors.ant("/**"))
                .securityReferences(Collections.singletonList(oauthSecurityReference))
                .build();
    }

    private AuthorizationScope[] scopes(List<Swagger2Properties.Oauth.Scope> scopes) {
        return scopes.stream()
                .map(scope -> new AuthorizationScope(scope.getName(), scope.getDescription()))
                .toArray(AuthorizationScope[]::new);

    }

    private GrantType[] grantTypes(String tokenUrl) {
        return new GrantType[]{
                new ResourceOwnerPasswordCredentialsGrant(tokenUrl)
        };
    }
}
