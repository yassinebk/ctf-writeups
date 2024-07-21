package org.springframework.boot.autoconfigure.web.client;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RestTemplate.class})
@AutoConfigureAfter({HttpMessageConvertersAutoConfiguration.class})
@Conditional({NotReactiveWebApplicationCondition.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/client/RestTemplateAutoConfiguration.class */
public class RestTemplateAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    @Lazy
    public RestTemplateBuilder restTemplateBuilder(ObjectProvider<HttpMessageConverters> messageConverters, ObjectProvider<RestTemplateCustomizer> restTemplateCustomizers, ObjectProvider<RestTemplateRequestCustomizer<?>> restTemplateRequestCustomizers) {
        RestTemplateBuilder builder = new RestTemplateBuilder(new RestTemplateCustomizer[0]);
        HttpMessageConverters converters = messageConverters.getIfUnique();
        if (converters != null) {
            builder = builder.messageConverters(converters.getConverters());
        }
        return addCustomizers(addCustomizers(builder, restTemplateCustomizers, (v0, v1) -> {
            return v0.customizers(v1);
        }), restTemplateRequestCustomizers, (v0, v1) -> {
            return v0.requestCustomizers(v1);
        });
    }

    private <T> RestTemplateBuilder addCustomizers(RestTemplateBuilder builder, ObjectProvider<T> objectProvider, BiFunction<RestTemplateBuilder, Collection<T>, RestTemplateBuilder> method) {
        List<T> customizers = (List) objectProvider.orderedStream().collect(Collectors.toList());
        if (!customizers.isEmpty()) {
            return method.apply(builder, customizers);
        }
        return builder;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/client/RestTemplateAutoConfiguration$NotReactiveWebApplicationCondition.class */
    static class NotReactiveWebApplicationCondition extends NoneNestedConditions {
        NotReactiveWebApplicationCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/client/RestTemplateAutoConfiguration$NotReactiveWebApplicationCondition$ReactiveWebApplication.class */
        private static class ReactiveWebApplication {
            private ReactiveWebApplication() {
            }
        }
    }
}
