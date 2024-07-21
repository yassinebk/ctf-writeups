package org.springframework.boot.autoconfigure.elasticsearch;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/RestClientBuilderCustomizer.class */
public interface RestClientBuilderCustomizer {
    void customize(RestClientBuilder builder);

    default void customize(HttpAsyncClientBuilder builder) {
    }

    default void customize(RequestConfig.Builder builder) {
    }
}
