package org.springframework.boot.autoconfigure.integration;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
@ConfigurationProperties(prefix = "spring.integration")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationProperties.class */
public class IntegrationProperties {
    private final Jdbc jdbc = new Jdbc();
    private final RSocket rsocket = new RSocket();

    public Jdbc getJdbc() {
        return this.jdbc;
    }

    public RSocket getRsocket() {
        return this.rsocket;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationProperties$Jdbc.class */
    public static class Jdbc {
        private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/integration/jdbc/schema-@@platform@@.sql";
        private String schema = DEFAULT_SCHEMA_LOCATION;
        private DataSourceInitializationMode initializeSchema = DataSourceInitializationMode.EMBEDDED;

        public String getSchema() {
            return this.schema;
        }

        public void setSchema(String schema) {
            this.schema = schema;
        }

        public DataSourceInitializationMode getInitializeSchema() {
            return this.initializeSchema;
        }

        public void setInitializeSchema(DataSourceInitializationMode initializeSchema) {
            this.initializeSchema = initializeSchema;
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationProperties$RSocket.class */
    public static class RSocket {
        private final Client client = new Client();
        private final Server server = new Server();

        public Client getClient() {
            return this.client;
        }

        public Server getServer() {
            return this.server;
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationProperties$RSocket$Client.class */
        public static class Client {
            private String host;
            private Integer port;
            private URI uri;

            public void setHost(String host) {
                this.host = host;
            }

            public String getHost() {
                return this.host;
            }

            public void setPort(Integer port) {
                this.port = port;
            }

            public Integer getPort() {
                return this.port;
            }

            public void setUri(URI uri) {
                this.uri = uri;
            }

            public URI getUri() {
                return this.uri;
            }
        }

        /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/integration/IntegrationProperties$RSocket$Server.class */
        public static class Server {
            boolean messageMappingEnabled;

            public boolean isMessageMappingEnabled() {
                return this.messageMappingEnabled;
            }

            public void setMessageMappingEnabled(boolean messageMappingEnabled) {
                this.messageMappingEnabled = messageMappingEnabled;
            }
        }
    }
}
