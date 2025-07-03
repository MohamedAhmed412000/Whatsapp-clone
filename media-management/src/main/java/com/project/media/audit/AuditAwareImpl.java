package com.project.media.audit;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("auditAwareImpl")
public class AuditAwareImpl implements ReactiveAuditorAware<String> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor.
     */
    @Override
    public Mono<String> getCurrentAuditor() {
        return Mono.just("SYSTEM");
    }
}
