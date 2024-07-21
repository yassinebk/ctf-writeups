package org.springframework.boot.autoconfigure.websocket.reactive;

import javax.servlet.Servlet;
import javax.websocket.server.ServerContainer;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@AutoConfigureBefore({ReactiveWebServerFactoryAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, ServerContainer.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/reactive/WebSocketReactiveAutoConfiguration.class */
public class WebSocketReactiveAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Tomcat.class, WsSci.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/reactive/WebSocketReactiveAutoConfiguration$TomcatWebSocketConfiguration.class */
    static class TomcatWebSocketConfiguration {
        TomcatWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketReactiveWebServerCustomizer"})
        @Bean
        TomcatWebSocketReactiveWebServerCustomizer websocketReactiveWebServerCustomizer() {
            return new TomcatWebSocketReactiveWebServerCustomizer();
        }
    }
}
