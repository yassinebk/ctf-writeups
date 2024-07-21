package org.springframework.boot.autoconfigure.couchbase;

import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.core.env.SecurityConfig;
import com.couchbase.client.core.env.TimeoutConfig;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import java.net.URL;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
@EnableConfigurationProperties({CouchbaseProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({Cluster.class})
@ConditionalOnProperty({"spring.couchbase.connection-string"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/couchbase/CouchbaseAutoConfiguration.class */
public class CouchbaseAutoConfiguration {
    @ConditionalOnMissingBean
    @Bean
    public ClusterEnvironment couchbaseClusterEnvironment(CouchbaseProperties properties, ObjectProvider<ClusterEnvironmentBuilderCustomizer> customizers) {
        ClusterEnvironment.Builder builder = initializeEnvironmentBuilder(properties);
        customizers.orderedStream().forEach(customizer -> {
            customizer.customize(builder);
        });
        return builder.build();
    }

    @ConditionalOnMissingBean
    @Bean(destroyMethod = "disconnect")
    public Cluster couchbaseCluster(CouchbaseProperties properties, ClusterEnvironment couchbaseClusterEnvironment) {
        ClusterOptions options = ClusterOptions.clusterOptions(properties.getUsername(), properties.getPassword()).environment(couchbaseClusterEnvironment);
        return Cluster.connect(properties.getConnectionString(), options);
    }

    private ClusterEnvironment.Builder initializeEnvironmentBuilder(CouchbaseProperties properties) {
        ClusterEnvironment.Builder builder = ClusterEnvironment.builder();
        CouchbaseProperties.Timeouts timeouts = properties.getEnv().getTimeouts();
        builder.timeoutConfig(TimeoutConfig.kvTimeout(timeouts.getKeyValue()).analyticsTimeout(timeouts.getAnalytics()).kvDurableTimeout(timeouts.getKeyValueDurable()).queryTimeout(timeouts.getQuery()).viewTimeout(timeouts.getView()).searchTimeout(timeouts.getSearch()).managementTimeout(timeouts.getManagement()).connectTimeout(timeouts.getConnect()).disconnectTimeout(timeouts.getDisconnect()));
        CouchbaseProperties.Io io = properties.getEnv().getIo();
        builder.ioConfig(IoConfig.maxHttpConnections(io.getMaxEndpoints()).numKvConnections(io.getMinEndpoints()).idleHttpConnectionTimeout(io.getIdleHttpConnectionTimeout()));
        if (properties.getEnv().getSsl().getEnabled().booleanValue()) {
            builder.securityConfig(SecurityConfig.enableTls(true).trustManagerFactory(getTrustManagerFactory(properties.getEnv().getSsl())));
        }
        return builder;
    }

    private TrustManagerFactory getTrustManagerFactory(CouchbaseProperties.Ssl ssl) {
        String resource = ssl.getKeyStore();
        try {
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            KeyStore keyStore = loadKeyStore(resource, ssl.getKeyStorePassword());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory;
        } catch (Exception ex) {
            throw new IllegalStateException("Could not load Couchbase key store '" + resource + "'", ex);
        }
    }

    private KeyStore loadKeyStore(String resource, String keyStorePassword) throws Exception {
        KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
        URL url = ResourceUtils.getURL(resource);
        store.load(url.openStream(), keyStorePassword != null ? keyStorePassword.toCharArray() : null);
        return store;
    }
}
