package org.springframework.boot.autoconfigure.rsocket;

import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.server.WebsocketRouteTransport;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.rsocket.server.RSocketServerCustomizer;
import org.springframework.boot.rsocket.server.ServerRSocketFactoryProcessor;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import reactor.netty.http.server.HttpServerRoutes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/rsocket/RSocketWebSocketNettyRouteProvider.class */
class RSocketWebSocketNettyRouteProvider implements NettyRouteProvider {
    private final String mappingPath;
    private final SocketAcceptor socketAcceptor;
    private final List<ServerRSocketFactoryProcessor> processors;
    private final List<RSocketServerCustomizer> customizers;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RSocketWebSocketNettyRouteProvider(String mappingPath, SocketAcceptor socketAcceptor, Stream<ServerRSocketFactoryProcessor> processors, Stream<RSocketServerCustomizer> customizers) {
        this.mappingPath = mappingPath;
        this.socketAcceptor = socketAcceptor;
        this.processors = (List) processors.collect(Collectors.toList());
        this.customizers = (List) customizers.collect(Collectors.toList());
    }

    @Override // java.util.function.Function
    public HttpServerRoutes apply(HttpServerRoutes httpServerRoutes) {
        RSocketServer server = RSocketServer.create(this.socketAcceptor);
        RSocketFactory.ServerRSocketFactory factory = new RSocketFactory.ServerRSocketFactory(server);
        this.processors.forEach(processor -> {
            processor.process(factory);
        });
        this.customizers.forEach(customizer -> {
            customizer.customize(server);
        });
        ServerTransport.ConnectionAcceptor connectionAcceptor = server.asConnectionAcceptor();
        return httpServerRoutes.ws(this.mappingPath, WebsocketRouteTransport.newHandler(connectionAcceptor));
    }
}
