package org.springframework.boot.autoconfigure.data.redis;

import java.net.UnknownHostException;
import java.time.Duration;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({GenericObjectPool.class, JedisConnection.class, Jedis.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/redis/JedisConnectionConfiguration.class */
class JedisConnectionConfiguration extends RedisConnectionConfiguration {
    JedisConnectionConfiguration(RedisProperties properties, ObjectProvider<RedisSentinelConfiguration> sentinelConfiguration, ObjectProvider<RedisClusterConfiguration> clusterConfiguration) {
        super(properties, sentinelConfiguration, clusterConfiguration);
    }

    @ConditionalOnMissingBean({RedisConnectionFactory.class})
    @Bean
    JedisConnectionFactory redisConnectionFactory(ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) throws UnknownHostException {
        return createJedisConnectionFactory(builderCustomizers);
    }

    private JedisConnectionFactory createJedisConnectionFactory(ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        JedisClientConfiguration clientConfiguration = getJedisClientConfiguration(builderCustomizers);
        if (getSentinelConfig() != null) {
            return new JedisConnectionFactory(getSentinelConfig(), clientConfiguration);
        }
        if (getClusterConfiguration() != null) {
            return new JedisConnectionFactory(getClusterConfiguration(), clientConfiguration);
        }
        return new JedisConnectionFactory(getStandaloneConfig(), clientConfiguration);
    }

    private JedisClientConfiguration getJedisClientConfiguration(ObjectProvider<JedisClientConfigurationBuilderCustomizer> builderCustomizers) {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = applyProperties(JedisClientConfiguration.builder());
        RedisProperties.Pool pool = getProperties().getJedis().getPool();
        if (pool != null) {
            applyPooling(pool, builder);
        }
        if (StringUtils.hasText(getProperties().getUrl())) {
            customizeConfigurationFromUrl(builder);
        }
        builderCustomizers.orderedStream().forEach(customizer -> {
            customizer.customize(builder);
        });
        return builder.build();
    }

    private JedisClientConfiguration.JedisClientConfigurationBuilder applyProperties(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        if (getProperties().isSsl()) {
            builder.useSsl();
        }
        if (getProperties().getTimeout() != null) {
            Duration timeout = getProperties().getTimeout();
            builder.readTimeout(timeout).connectTimeout(timeout);
        }
        if (StringUtils.hasText(getProperties().getClientName())) {
            builder.clientName(getProperties().getClientName());
        }
        return builder;
    }

    private void applyPooling(RedisProperties.Pool pool, JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        builder.usePooling().poolConfig(jedisPoolConfig(pool));
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties.Pool pool) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(pool.getMaxActive());
        config.setMaxIdle(pool.getMaxIdle());
        config.setMinIdle(pool.getMinIdle());
        if (pool.getTimeBetweenEvictionRuns() != null) {
            config.setTimeBetweenEvictionRunsMillis(pool.getTimeBetweenEvictionRuns().toMillis());
        }
        if (pool.getMaxWait() != null) {
            config.setMaxWaitMillis(pool.getMaxWait().toMillis());
        }
        return config;
    }

    private void customizeConfigurationFromUrl(JedisClientConfiguration.JedisClientConfigurationBuilder builder) {
        RedisConnectionConfiguration.ConnectionInfo connectionInfo = parseUrl(getProperties().getUrl());
        if (connectionInfo.isUseSsl()) {
            builder.useSsl();
        }
    }
}
