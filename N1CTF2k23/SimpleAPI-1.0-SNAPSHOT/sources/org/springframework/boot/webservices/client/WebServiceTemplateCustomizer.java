package org.springframework.boot.webservices.client;

import org.springframework.ws.client.core.WebServiceTemplate;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/webservices/client/WebServiceTemplateCustomizer.class */
public interface WebServiceTemplateCustomizer {
    void customize(WebServiceTemplate webServiceTemplate);
}
