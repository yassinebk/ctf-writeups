package org.springframework.boot.autoconfigure.availability;

import org.springframework.boot.availability.ApplicationAvailabilityBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/availability/ApplicationAvailabilityAutoConfiguration.class */
public class ApplicationAvailabilityAutoConfiguration {
    @Bean
    public ApplicationAvailabilityBean applicationAvailability() {
        return new ApplicationAvailabilityBean();
    }
}
