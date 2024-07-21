package org.springframework.boot.autoconfigure.cassandra;

import ch.qos.logback.classic.Level;
import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.DeprecatedConfigurationProperty;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.boot.logging.LoggingSystem;
@ConfigurationProperties(prefix = "spring.data.cassandra")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties.class */
public class CassandraProperties {
    private String keyspaceName;
    private String sessionName;
    private String localDatacenter;
    private String username;
    private String password;
    private final List<String> contactPoints = new ArrayList(Collections.singleton("127.0.0.1:9042"));
    private int port = 9042;
    private Compression compression = Compression.NONE;
    private String schemaAction = LoggingSystem.NONE;
    private boolean ssl = false;
    private final Connection connection = new Connection();
    private final Pool pool = new Pool();
    private final Request request = new Request();

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$Compression.class */
    public enum Compression {
        LZ4,
        SNAPPY,
        NONE
    }

    public String getKeyspaceName() {
        return this.keyspaceName;
    }

    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.session-name")
    @Deprecated
    public String getClusterName() {
        return getSessionName();
    }

    @Deprecated
    public void setClusterName(String clusterName) {
        setSessionName(clusterName);
    }

    public List<String> getContactPoints() {
        return this.contactPoints;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getLocalDatacenter() {
        return this.localDatacenter;
    }

    public void setLocalDatacenter(String localDatacenter) {
        this.localDatacenter = localDatacenter;
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

    public Compression getCompression() {
        return this.compression;
    }

    public void setCompression(Compression compression) {
        this.compression = compression;
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.request.consistency")
    @Deprecated
    public DefaultConsistencyLevel getConsistencyLevel() {
        return getRequest().getConsistency();
    }

    @Deprecated
    public void setConsistencyLevel(DefaultConsistencyLevel consistency) {
        getRequest().setConsistency(consistency);
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.request.serial-consistency")
    @Deprecated
    public DefaultConsistencyLevel getSerialConsistencyLevel() {
        return getRequest().getSerialConsistency();
    }

    @Deprecated
    public void setSerialConsistencyLevel(DefaultConsistencyLevel serialConsistency) {
        getRequest().setSerialConsistency(serialConsistency);
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.request.page-size")
    @Deprecated
    public int getFetchSize() {
        return getRequest().getPageSize();
    }

    @Deprecated
    public void setFetchSize(int fetchSize) {
        getRequest().setPageSize(fetchSize);
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.connection.init-query-timeout")
    @Deprecated
    public Duration getConnectTimeout() {
        return getConnection().getInitQueryTimeout();
    }

    @Deprecated
    public void setConnectTimeout(Duration connectTimeout) {
        getConnection().setInitQueryTimeout(connectTimeout);
    }

    @DeprecatedConfigurationProperty(replacement = "spring.data.cassandra.request.timeout")
    @Deprecated
    public Duration getReadTimeout() {
        return getRequest().getTimeout();
    }

    @Deprecated
    public void setReadTimeout(Duration readTimeout) {
        getRequest().setTimeout(readTimeout);
    }

    public boolean isSsl() {
        return this.ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getSchemaAction() {
        return this.schemaAction;
    }

    public void setSchemaAction(String schemaAction) {
        this.schemaAction = schemaAction;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public Pool getPool() {
        return this.pool;
    }

    public Request getRequest() {
        return this.request;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$Connection.class */
    public static class Connection {
        private Duration connectTimeout = Duration.ofSeconds(5);
        private Duration initQueryTimeout = Duration.ofMillis(500);

        public Duration getConnectTimeout() {
            return this.connectTimeout;
        }

        public void setConnectTimeout(Duration connectTimeout) {
            this.connectTimeout = connectTimeout;
        }

        public Duration getInitQueryTimeout() {
            return this.initQueryTimeout;
        }

        public void setInitQueryTimeout(Duration initQueryTimeout) {
            this.initQueryTimeout = initQueryTimeout;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$Request.class */
    public static class Request {
        private DefaultConsistencyLevel consistency;
        private DefaultConsistencyLevel serialConsistency;
        private Duration timeout = Duration.ofSeconds(2);
        private int pageSize = Level.TRACE_INT;
        private final Throttler throttler = new Throttler();

        public Duration getTimeout() {
            return this.timeout;
        }

        public void setTimeout(Duration timeout) {
            this.timeout = timeout;
        }

        public DefaultConsistencyLevel getConsistency() {
            return this.consistency;
        }

        public void setConsistency(DefaultConsistencyLevel consistency) {
            this.consistency = consistency;
        }

        public DefaultConsistencyLevel getSerialConsistency() {
            return this.serialConsistency;
        }

        public void setSerialConsistency(DefaultConsistencyLevel serialConsistency) {
            this.serialConsistency = serialConsistency;
        }

        public int getPageSize() {
            return this.pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public Throttler getThrottler() {
            return this.throttler;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$Pool.class */
    public static class Pool {
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration idleTimeout = Duration.ofSeconds(120);
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration heartbeatInterval = Duration.ofSeconds(30);

        public Duration getIdleTimeout() {
            return this.idleTimeout;
        }

        public void setIdleTimeout(Duration idleTimeout) {
            this.idleTimeout = idleTimeout;
        }

        public Duration getHeartbeatInterval() {
            return this.heartbeatInterval;
        }

        public void setHeartbeatInterval(Duration heartbeatInterval) {
            this.heartbeatInterval = heartbeatInterval;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$Throttler.class */
    public static class Throttler {
        private ThrottlerType type = ThrottlerType.NONE;
        private int maxQueueSize = 10000;
        private int maxConcurrentRequests = 10000;
        private int maxRequestsPerSecond = 10000;
        private Duration drainInterval = Duration.ofMillis(10);

        public ThrottlerType getType() {
            return this.type;
        }

        public void setType(ThrottlerType type) {
            this.type = type;
        }

        public int getMaxQueueSize() {
            return this.maxQueueSize;
        }

        public void setMaxQueueSize(int maxQueueSize) {
            this.maxQueueSize = maxQueueSize;
        }

        public int getMaxConcurrentRequests() {
            return this.maxConcurrentRequests;
        }

        public void setMaxConcurrentRequests(int maxConcurrentRequests) {
            this.maxConcurrentRequests = maxConcurrentRequests;
        }

        public int getMaxRequestsPerSecond() {
            return this.maxRequestsPerSecond;
        }

        public void setMaxRequestsPerSecond(int maxRequestsPerSecond) {
            this.maxRequestsPerSecond = maxRequestsPerSecond;
        }

        public Duration getDrainInterval() {
            return this.drainInterval;
        }

        public void setDrainInterval(Duration drainInterval) {
            this.drainInterval = drainInterval;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/cassandra/CassandraProperties$ThrottlerType.class */
    public enum ThrottlerType {
        CONCURRENCY_LIMITING("ConcurrencyLimitingRequestThrottler"),
        RATE_LIMITING("RateLimitingRequestThrottler"),
        NONE("PassThroughRequestThrottler");
        
        private final String type;

        ThrottlerType(String type) {
            this.type = type;
        }

        public String type() {
            return this.type;
        }
    }
}
