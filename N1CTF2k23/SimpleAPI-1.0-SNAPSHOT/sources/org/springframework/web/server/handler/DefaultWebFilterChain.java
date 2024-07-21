package org.springframework.web.server.handler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/server/handler/DefaultWebFilterChain.class */
public class DefaultWebFilterChain implements WebFilterChain {
    private final List<WebFilter> allFilters;
    private final WebHandler handler;
    @Nullable
    private final WebFilter currentFilter;
    @Nullable
    private final DefaultWebFilterChain chain;

    public DefaultWebFilterChain(WebHandler handler, List<WebFilter> filters) {
        Assert.notNull(handler, "WebHandler is required");
        this.allFilters = Collections.unmodifiableList(filters);
        this.handler = handler;
        DefaultWebFilterChain chain = initChain(filters, handler);
        this.currentFilter = chain.currentFilter;
        this.chain = chain.chain;
    }

    private static DefaultWebFilterChain initChain(List<WebFilter> filters, WebHandler handler) {
        DefaultWebFilterChain chain = new DefaultWebFilterChain(filters, handler, null, null);
        ListIterator<? extends WebFilter> iterator = filters.listIterator(filters.size());
        while (iterator.hasPrevious()) {
            chain = new DefaultWebFilterChain(filters, handler, iterator.previous(), chain);
        }
        return chain;
    }

    private DefaultWebFilterChain(List<WebFilter> allFilters, WebHandler handler, @Nullable WebFilter currentFilter, @Nullable DefaultWebFilterChain chain) {
        this.allFilters = allFilters;
        this.currentFilter = currentFilter;
        this.handler = handler;
        this.chain = chain;
    }

    @Deprecated
    public DefaultWebFilterChain(WebHandler handler, WebFilter... filters) {
        this(handler, Arrays.asList(filters));
    }

    public List<WebFilter> getFilters() {
        return this.allFilters;
    }

    public WebHandler getHandler() {
        return this.handler;
    }

    @Override // org.springframework.web.server.WebFilterChain
    public Mono<Void> filter(ServerWebExchange exchange) {
        return Mono.defer(() -> {
            if (this.currentFilter != null && this.chain != null) {
                return invokeFilter(this.currentFilter, this.chain, exchange);
            }
            return this.handler.handle(exchange);
        });
    }

    private Mono<Void> invokeFilter(WebFilter current, DefaultWebFilterChain chain, ServerWebExchange exchange) {
        String currentName = current.getClass().getName();
        return current.filter(exchange, chain).checkpoint(currentName + " [DefaultWebFilterChain]");
    }
}
