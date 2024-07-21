package org.springframework.boot.autoconfigure.web.embedded;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.util.unit.DataSize;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/web/embedded/NettyWebServerFactoryCustomizer.class */
public class NettyWebServerFactoryCustomizer implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory>, Ordered {
    private final Environment environment;
    private final ServerProperties serverProperties;

    public NettyWebServerFactoryCustomizer(Environment environment, ServerProperties serverProperties) {
        this.environment = environment;
        this.serverProperties = serverProperties;
    }

    @Override // org.springframework.core.Ordered
    public int getOrder() {
        return 0;
    }

    @Override // org.springframework.boot.web.server.WebServerFactoryCustomizer
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.setUseForwardHeaders(getOrDeduceUseForwardHeaders());
        PropertyMapper propertyMapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        ServerProperties serverProperties = this.serverProperties;
        serverProperties.getClass();
        propertyMapper.from(this::getMaxHttpHeaderSize).to(maxHttpRequestHeaderSize -> {
            customizeMaxHttpHeaderSize(factory, maxHttpRequestHeaderSize);
        });
        ServerProperties.Netty nettyProperties = this.serverProperties.getNetty();
        nettyProperties.getClass();
        propertyMapper.from(this::getConnectionTimeout).whenNonNull().to(connectionTimeout -> {
            customizeConnectionTimeout(factory, connectionTimeout);
        });
    }

    private boolean getOrDeduceUseForwardHeaders() {
        if (this.serverProperties.getForwardHeadersStrategy() == null) {
            CloudPlatform platform = CloudPlatform.getActive(this.environment);
            return platform != null && platform.isUsingForwardHeaders();
        }
        return this.serverProperties.getForwardHeadersStrategy().equals(ServerProperties.ForwardHeadersStrategy.NATIVE);
    }

    private void customizeMaxHttpHeaderSize(NettyReactiveWebServerFactory factory, DataSize maxHttpHeaderSize) {
        factory.addServerCustomizers(httpServer -> {
            return httpServer.httpRequestDecoder(httpRequestDecoderSpec -> {
                return httpRequestDecoderSpec.maxHeaderSize((int) maxHttpHeaderSize.toBytes());
            });
        });
    }

    private void customizeConnectionTimeout(NettyReactiveWebServerFactory factory, Duration connectionTimeout) {
        factory.addServerCustomizers(httpServer -> {
            return httpServer.tcpConfiguration(tcpServer -> {
                return tcpServer.selectorOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf((int) connectionTimeout.toMillis()));
            });
        });
    }
}
