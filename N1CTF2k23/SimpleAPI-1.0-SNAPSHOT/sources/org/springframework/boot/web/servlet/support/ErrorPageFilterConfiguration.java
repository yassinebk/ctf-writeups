package org.springframework.boot.web.servlet.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/servlet/support/ErrorPageFilterConfiguration.class */
class ErrorPageFilterConfiguration {
    ErrorPageFilterConfiguration() {
    }

    @Bean
    ErrorPageFilter errorPageFilter() {
        return new ErrorPageFilter();
    }
}
