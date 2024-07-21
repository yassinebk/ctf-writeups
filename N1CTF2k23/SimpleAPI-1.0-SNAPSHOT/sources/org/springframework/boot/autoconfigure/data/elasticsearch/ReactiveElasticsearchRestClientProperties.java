package org.springframework.boot.autoconfigure.data.elasticsearch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;
@ConfigurationProperties(prefix = "spring.data.elasticsearch.client.reactive")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/elasticsearch/ReactiveElasticsearchRestClientProperties.class */
public class ReactiveElasticsearchRestClientProperties {
    private List<String> endpoints = new ArrayList(Collections.singletonList("localhost:9200"));
    private boolean useSsl = false;
    private String username;
    private String password;
    private Duration connectionTimeout;
    private Duration socketTimeout;
    private DataSize maxInMemorySize;

    public List<String> getEndpoints() {
        return this.endpoints;
    }

    public void setEndpoints(List<String> endpoints) {
        this.endpoints = endpoints;
    }

    public boolean isUseSsl() {
        return this.useSsl;
    }

    public void setUseSsl(boolean useSsl) {
        this.useSsl = useSsl;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Duration getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(Duration connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Duration getSocketTimeout() {
        return this.socketTimeout;
    }

    public void setSocketTimeout(Duration socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public DataSize getMaxInMemorySize() {
        return this.maxInMemorySize;
    }

    public void setMaxInMemorySize(DataSize maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }
}
