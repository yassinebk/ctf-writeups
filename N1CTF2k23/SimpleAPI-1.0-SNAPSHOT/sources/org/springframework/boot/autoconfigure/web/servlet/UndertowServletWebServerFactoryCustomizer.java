package org.springframework.boot.autoconfigure.web.servlet;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/servlet/UndertowServletWebServerFactoryCustomizer.class */
public class UndertowServletWebServerFactoryCustomizer implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {
    private final ServerProperties serverProperties;

    public UndertowServletWebServerFactoryCustomizer(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override // org.springframework.boot.web.server.WebServerFactoryCustomizer
    public void customize(UndertowServletWebServerFactory factory) {
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            deploymentInfo.setEagerFilterInit(this.serverProperties.getUndertow().isEagerFilterInit());
        });
    }
}
