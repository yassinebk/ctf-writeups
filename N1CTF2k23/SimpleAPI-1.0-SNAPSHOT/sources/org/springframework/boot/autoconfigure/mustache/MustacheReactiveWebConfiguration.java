package org.springframework.boot.autoconfigure.mustache;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.reactive.result.view.MustacheViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/mustache/MustacheReactiveWebConfiguration.class */
class MustacheReactiveWebConfiguration {
    MustacheReactiveWebConfiguration() {
    }

    @ConditionalOnMissingBean
    @Bean
    MustacheViewResolver mustacheViewResolver(Mustache.Compiler mustacheCompiler, MustacheProperties mustache) {
        MustacheViewResolver resolver = new MustacheViewResolver(mustacheCompiler);
        resolver.setPrefix(mustache.getPrefix());
        resolver.setSuffix(mustache.getSuffix());
        resolver.setViewNames(mustache.getViewNames());
        resolver.setRequestContextAttribute(mustache.getRequestContextAttribute());
        resolver.setCharset(mustache.getCharsetName());
        resolver.setOrder(2147483637);
        return resolver;
    }
}
