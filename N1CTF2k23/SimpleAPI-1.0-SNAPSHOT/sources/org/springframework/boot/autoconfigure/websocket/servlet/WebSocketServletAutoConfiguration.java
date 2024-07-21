package org.springframework.boot.autoconfigure.websocket.servlet;

import io.undertow.websockets.jsr.Bootstrap;
import javax.servlet.Servlet;
import javax.websocket.server.ServerContainer;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.websocket.server.WsSci;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@AutoConfigureBefore({ServletWebServerFactoryAutoConfiguration.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Servlet.class, ServerContainer.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/servlet/WebSocketServletAutoConfiguration.class */
public class WebSocketServletAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Tomcat.class, WsSci.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/servlet/WebSocketServletAutoConfiguration$TomcatWebSocketConfiguration.class */
    static class TomcatWebSocketConfiguration {
        TomcatWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketServletWebServerCustomizer"})
        @Bean
        TomcatWebSocketServletWebServerCustomizer websocketServletWebServerCustomizer() {
            return new TomcatWebSocketServletWebServerCustomizer();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({WebSocketServerContainerInitializer.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/servlet/WebSocketServletAutoConfiguration$JettyWebSocketConfiguration.class */
    static class JettyWebSocketConfiguration {
        JettyWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketServletWebServerCustomizer"})
        @Bean
        JettyWebSocketServletWebServerCustomizer websocketServletWebServerCustomizer() {
            return new JettyWebSocketServletWebServerCustomizer();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Bootstrap.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/servlet/WebSocketServletAutoConfiguration$UndertowWebSocketConfiguration.class */
    static class UndertowWebSocketConfiguration {
        UndertowWebSocketConfiguration() {
        }

        @ConditionalOnMissingBean(name = {"websocketServletWebServerCustomizer"})
        @Bean
        UndertowWebSocketServletWebServerCustomizer websocketServletWebServerCustomizer() {
            return new UndertowWebSocketServletWebServerCustomizer();
        }
    }
}
