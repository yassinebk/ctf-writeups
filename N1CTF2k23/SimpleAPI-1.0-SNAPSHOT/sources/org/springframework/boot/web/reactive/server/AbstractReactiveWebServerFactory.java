package org.springframework.boot.web.reactive.server;

import org.springframework.boot.web.server.AbstractConfigurableWebServerFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/reactive/server/AbstractReactiveWebServerFactory.class */
public abstract class AbstractReactiveWebServerFactory extends AbstractConfigurableWebServerFactory implements ConfigurableReactiveWebServerFactory {
    public AbstractReactiveWebServerFactory() {
    }

    public AbstractReactiveWebServerFactory(int port) {
        super(port);
    }
}
