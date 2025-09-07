package com.project.user.audit;

import com.project.commons.filters.dto.CustomAuthentication;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        CustomAuthentication authentication = (CustomAuthentication)
            SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
            !"anonymousUser".equalsIgnoreCase(authentication.getName())) {
            return Optional.of(authentication.getName());
        }
        return Optional.of("SYSTEM");
    }
}
