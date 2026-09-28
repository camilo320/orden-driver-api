package com.ait.pase.orden_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class BeansConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new ApplicationAuditAware();
    }
}

