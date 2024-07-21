package org.springframework.boot.autoconfigure.web.reactive;

import java.util.Collections;
import java.util.Map;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.config.ConfigFileApplicationListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ContextPathCompositeHandler;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({DispatcherHandler.class, HttpHandler.class})
@AutoConfigureAfter({WebFluxAutoConfiguration.class})
@ConditionalOnMissingBean({HttpHandler.class})
@AutoConfigureOrder(ConfigFileApplicationListener.DEFAULT_ORDER)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/HttpHandlerAutoConfiguration.class */
public class HttpHandlerAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/HttpHandlerAutoConfiguration$AnnotationConfig.class */
    public static class AnnotationConfig {
        private ApplicationContext applicationContext;

        public AnnotationConfig(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Bean
        public HttpHandler httpHandler(ObjectProvider<WebFluxProperties> propsProvider) {
            HttpHandler httpHandler = WebHttpHandlerBuilder.applicationContext(this.applicationContext).build();
            WebFluxProperties properties = propsProvider.getIfAvailable();
            if (properties != null && StringUtils.hasText(properties.getBasePath())) {
                Map<String, HttpHandler> handlersMap = Collections.singletonMap(properties.getBasePath(), httpHandler);
                return new ContextPathCompositeHandler(handlersMap);
            }
            return httpHandler;
        }
    }
}
