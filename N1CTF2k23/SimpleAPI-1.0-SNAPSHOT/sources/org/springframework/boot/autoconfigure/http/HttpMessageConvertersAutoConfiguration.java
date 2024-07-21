package org.springframework.boot.autoconfigure.http;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.NoneNestedConditions;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jsonb.JsonbAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HttpMessageConverter.class})
@AutoConfigureAfter({GsonAutoConfiguration.class, JacksonAutoConfiguration.class, JsonbAutoConfiguration.class})
@Conditional({NotReactiveWebApplicationCondition.class})
@Import({JacksonHttpMessageConvertersConfiguration.class, GsonHttpMessageConvertersConfiguration.class, JsonbHttpMessageConvertersConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/http/HttpMessageConvertersAutoConfiguration.class */
public class HttpMessageConvertersAutoConfiguration {
    static final String PREFERRED_MAPPER_PROPERTY = "spring.mvc.converters.preferred-json-mapper";

    @ConditionalOnMissingBean
    @Bean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters((Collection) converters.orderedStream().collect(Collectors.toList()));
    }

    @EnableConfigurationProperties({ServerProperties.class})
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({StringHttpMessageConverter.class})
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/http/HttpMessageConvertersAutoConfiguration$StringHttpMessageConverterConfiguration.class */
    protected static class StringHttpMessageConverterConfiguration {
        protected StringHttpMessageConverterConfiguration() {
        }

        @ConditionalOnMissingBean
        @Bean
        public StringHttpMessageConverter stringHttpMessageConverter(ServerProperties serverProperties) {
            StringHttpMessageConverter converter = new StringHttpMessageConverter(serverProperties.getServlet().getEncoding().getCharset());
            converter.setWriteAcceptCharset(false);
            return converter;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/http/HttpMessageConvertersAutoConfiguration$NotReactiveWebApplicationCondition.class */
    static class NotReactiveWebApplicationCondition extends NoneNestedConditions {
        NotReactiveWebApplicationCondition() {
            super(ConfigurationCondition.ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/http/HttpMessageConvertersAutoConfiguration$NotReactiveWebApplicationCondition$ReactiveWebApplication.class */
        private static class ReactiveWebApplication {
            private ReactiveWebApplication() {
            }
        }
    }
}
