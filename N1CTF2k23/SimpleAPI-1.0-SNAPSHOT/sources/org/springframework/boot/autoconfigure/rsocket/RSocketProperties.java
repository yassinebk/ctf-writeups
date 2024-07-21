package org.springframework.boot.autoconfigure.rsocket;

import java.net.InetAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.rsocket.server.RSocketServer;
@ConfigurationProperties("spring.rsocket")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketProperties.class */
public class RSocketProperties {
    private final Server server = new Server();

    public Server getServer() {
        return this.server;
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketProperties$Server.class */
    public static class Server {
        private Integer port;
        private InetAddress address;
        private RSocketServer.Transport transport = RSocketServer.Transport.TCP;
        private String mappingPath;

        public Integer getPort() {
            return this.port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public InetAddress getAddress() {
            return this.address;
        }

        public void setAddress(InetAddress address) {
            this.address = address;
        }

        public RSocketServer.Transport getTransport() {
            return this.transport;
        }

        public void setTransport(RSocketServer.Transport transport) {
            this.transport = transport;
        }

        public String getMappingPath() {
            return this.mappingPath;
        }

        public void setMappingPath(String mappingPath) {
            this.mappingPath = mappingPath;
        }
    }
}
