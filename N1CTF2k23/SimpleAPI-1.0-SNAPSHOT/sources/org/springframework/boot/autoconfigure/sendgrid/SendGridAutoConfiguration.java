package org.springframework.boot.autoconfigure.sendgrid;

import com.sendgrid.Client;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridAPI;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@EnableConfigurationProperties({SendGridProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SendGrid.class})
@ConditionalOnProperty(prefix = "spring.sendgrid", value = {"api-key"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/sendgrid/SendGridAutoConfiguration.class */
public class SendGridAutoConfiguration {
    @ConditionalOnMissingBean({SendGridAPI.class})
    @Bean
    public SendGrid sendGrid(SendGridProperties properties) {
        if (properties.isProxyConfigured()) {
            HttpHost proxy = new HttpHost(properties.getProxy().getHost(), properties.getProxy().getPort().intValue());
            return new SendGrid(properties.getApiKey(), new Client(HttpClientBuilder.create().setProxy(proxy).build()));
        }
        return new SendGrid(properties.getApiKey());
    }
}
