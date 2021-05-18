package io.lw900925.ocean.restful.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UrlAccessDecisionManager implements AccessDecisionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlAccessDecisionManager.class);

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (CollectionUtils.isEmpty(configAttributes)) {
            throw new AccessDeniedException("Full authentication is required to access this resource.");
        }

        // 获取当前请求主体（用户）的角色（GrantAuthority），与当前请求资源中绑定的角色匹配
        List<String> authorities =  authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        for (ConfigAttribute configAttribute : configAttributes) {
            if(authorities.contains(configAttribute.getAttribute())) {
                return;
            }
        }

        // 没有匹配到有效的角色，返回access denied
        throw new AccessDeniedException("No grant authority to access this resource");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
