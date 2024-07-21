package org.springframework.boot.autoconfigure.web.reactive;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/TomcatReactiveWebServerFactoryCustomizer.class */
public class TomcatReactiveWebServerFactoryCustomizer implements WebServerFactoryCustomizer<TomcatReactiveWebServerFactory> {
    private final ServerProperties serverProperties;

    public TomcatReactiveWebServerFactoryCustomizer(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override // org.springframework.boot.web.server.WebServerFactoryCustomizer
    public void customize(TomcatReactiveWebServerFactory factory) {
        factory.setDisableMBeanRegistry(!this.serverProperties.getTomcat().getMbeanregistry().isEnabled());
    }
}
