package org.springframework.boot.autoconfigure.elasticsearch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/elasticsearch/ElasticsearchRestClientProperties.class */
public class ElasticsearchRestClientProperties {
    private String username;
    private String password;
    private List<String> uris = new ArrayList(Collections.singletonList("http://localhost:9200"));
    private Duration connectionTimeout = Duration.ofSeconds(1);
    private Duration readTimeout = Duration.ofSeconds(30);

    public List<String> getUris() {
        return this.uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
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

    public Duration getReadTimeout() {
        return this.readTimeout;
    }

    public void setReadTimeout(Duration readTimeout) {
        this.readTimeout = readTimeout;
    }
}
