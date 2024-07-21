package org.springframework.web.server.handler;

import java.util.List;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/handler/FilteringWebHandler.class */
public class FilteringWebHandler extends WebHandlerDecorator {
    private final DefaultWebFilterChain chain;

    public FilteringWebHandler(WebHandler handler, List<WebFilter> filters) {
        super(handler);
        this.chain = new DefaultWebFilterChain(handler, filters);
    }

    public List<WebFilter> getFilters() {
        return this.chain.getFilters();
    }

    @Override // org.springframework.web.server.handler.WebHandlerDecorator, org.springframework.web.server.WebHandler
    public Mono<Void> handle(ServerWebExchange exchange) {
        return this.chain.filter(exchange);
    }
}
