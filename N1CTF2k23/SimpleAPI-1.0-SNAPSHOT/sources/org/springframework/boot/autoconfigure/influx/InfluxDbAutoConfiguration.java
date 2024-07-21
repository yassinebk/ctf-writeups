package org.springframework.boot.autoconfigure.influx;

import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.impl.InfluxDBImpl;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@EnableConfigurationProperties({InfluxDbProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({InfluxDB.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/influx/InfluxDbAutoConfiguration.class */
public class InfluxDbAutoConfiguration {
    @ConditionalOnMissingBean
    @ConditionalOnProperty({"spring.influx.url"})
    @Bean
    public InfluxDB influxDb(InfluxDbProperties properties, ObjectProvider<InfluxDbOkHttpClientBuilderProvider> builder) {
        return new InfluxDBImpl(properties.getUrl(), properties.getUser(), properties.getPassword(), determineBuilder(builder.getIfAvailable()));
    }

    private static OkHttpClient.Builder determineBuilder(InfluxDbOkHttpClientBuilderProvider builder) {
        if (builder != null) {
            return builder.get();
        }
        return new OkHttpClient.Builder();
    }
}
