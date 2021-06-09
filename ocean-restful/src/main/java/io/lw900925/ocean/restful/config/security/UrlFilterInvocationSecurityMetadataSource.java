package io.lw900925.ocean.restful.config.security;

import io.lw900925.ocean.core.model.entity.Resource;
import io.lw900925.ocean.core.model.entity.Role;
import io.lw900925.ocean.core.repository.jpa.ResourceRepository;
import io.lw900925.ocean.core.repository.mybatis.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

public class UrlFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UrlFilterInvocationSecurityMetadataSource.class);

    private String[] ignoreUrls = new String[] {};

    private ResourceRepository resourceRepository;
    private RoleMapper roleMapper;

    private final AntPathMatcher MATCHER = new AntPathMatcher();

    /**
     * 返回URL所需的用户权限信息
     * @param object URL
     * @return null
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestUri = ((FilterInvocation) object).getHttpRequest().getRequestURI();

        // 对列表中的请求放行
        for (String ignoreUrl : ignoreUrls) {
            if (MATCHER.match(ignoreUrl, requestUri)) {
                return null;
            }
        }

        // 获取数据库中的资源集合，与当前请求URL匹配
        List<Resource> resources = resourceRepository.findAll();
        Resource requestResource = resources.stream().filter(resource -> MATCHER.match(resource.getUri(), requestUri)).findFirst().orElse(null);

        // 获取资源中绑定的角色，创建ConfigAttribute对象
        if (requestResource != null) {
            List<Role> roles = roleMapper.selectByResourceReference(requestResource.getOid());
            if (roles.size() > 0) {
                return SecurityConfig.createList(roles.stream().map(Role::getAuthority).toArray(String[]::new));
            }
        }

        return SecurityConfig.createList("permission_denied");
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    // ---------- Setter ----------
    public void setIgnoreUrls(String[] ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }

    public void setResourceRepository(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
