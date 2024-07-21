package org.springframework.boot.autoconfigure.web.reactive.error;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
@AutoConfigureBefore({WebFluxAutoConfiguration.class})
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({WebFluxConfigurer.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/reactive/error/ErrorWebFluxAutoConfiguration.class */
public class ErrorWebFluxAutoConfiguration {
    private final ServerProperties serverProperties;

    public ErrorWebFluxAutoConfiguration(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @ConditionalOnMissingBean(value = {ErrorWebExceptionHandler.class}, search = SearchStrategy.CURRENT)
    @Bean
    @Order(-1)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties, ObjectProvider<ViewResolver> viewResolvers, ServerCodecConfigurer serverCodecConfigurer, ApplicationContext applicationContext) {
        DefaultErrorWebExceptionHandler exceptionHandler = new DefaultErrorWebExceptionHandler(errorAttributes, resourceProperties, this.serverProperties.getError(), applicationContext);
        exceptionHandler.setViewResolvers((List) viewResolvers.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    @ConditionalOnMissingBean(value = {ErrorAttributes.class}, search = SearchStrategy.CURRENT)
    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }
}
