package org.matrixstudio.ocean.support.spring.data.jpa.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorProvider implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.ofNullable(username);
    }
}
