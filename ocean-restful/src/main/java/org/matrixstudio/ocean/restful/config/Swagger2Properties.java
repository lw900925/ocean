package org.matrixstudio.ocean.restful.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "springfox")
@Data
public class Swagger2Properties {

    private Docket docket = new Docket();
    private ApiInfo apiInfo = new ApiInfo();
    private Oauth oauth = new Oauth();

    @Data
    static class Docket {
        private String groupName;
        private String basePackage;
    }

    @Data
    static class ApiInfo {
        private String title;
        private String description;
        private Contact contact = new Contact();
        private String version;

        @Data
        static class Contact {
            private String name;
            private String url;
            private String email;
        }
    }

    @Data
    static class Oauth {
        private String name;
        private String clientId;
        private String clientSecret;
        private String appName;
        private String tokenUrl;
        private List<Scope> scopes = new ArrayList<>();

        @Data
        static class Scope {
            private String name;
            private String description;
        }
    }
}
