package org.springframework.web.servlet.function;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.springframework.web.servlet.function.RouterFunctions;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctionBuilder.class */
public class RouterFunctionBuilder implements RouterFunctions.Builder {
    private List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList();
    private List<HandlerFilterFunction<ServerResponse, ServerResponse>> filterFunctions = new ArrayList();

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder add(RouterFunction<ServerResponse> routerFunction) {
        Assert.notNull(routerFunction, "RouterFunction must not be null");
        this.routerFunctions.add(routerFunction);
        return this;
    }

    private RouterFunctions.Builder add(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        this.routerFunctions.add(RouterFunctions.route(predicate, handlerFunction));
        return this;
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder GET(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.GET(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder GET(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.GET(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder HEAD(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.HEAD(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder HEAD(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.HEAD(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder POST(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.POST(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder POST(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.POST(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder PUT(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.PUT(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder PUT(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.PUT(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder PATCH(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.PATCH(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder PATCH(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.PATCH(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder DELETE(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.DELETE(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder DELETE(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.DELETE(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder OPTIONS(String pattern, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.OPTIONS(pattern), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder route(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RouterFunctions.route(predicate, handlerFunction));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder OPTIONS(String pattern, RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        return add(RequestPredicates.OPTIONS(pattern).and(predicate), handlerFunction);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder resources(String pattern, Resource location) {
        return add(RouterFunctions.resources(pattern, location));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder resources(Function<ServerRequest, Optional<Resource>> lookupFunction) {
        return add(RouterFunctions.resources(lookupFunction));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder nest(RequestPredicate predicate, Consumer<RouterFunctions.Builder> builderConsumer) {
        Assert.notNull(builderConsumer, "Consumer must not be null");
        RouterFunctionBuilder nestedBuilder = new RouterFunctionBuilder();
        builderConsumer.accept(nestedBuilder);
        RouterFunction<ServerResponse> nestedRoute = nestedBuilder.build();
        this.routerFunctions.add(RouterFunctions.nest(predicate, nestedRoute));
        return this;
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder nest(RequestPredicate predicate, Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier) {
        Assert.notNull(routerFunctionSupplier, "RouterFunction Supplier must not be null");
        RouterFunction<ServerResponse> nestedRoute = routerFunctionSupplier.get();
        this.routerFunctions.add(RouterFunctions.nest(predicate, nestedRoute));
        return this;
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder path(String pattern, Consumer<RouterFunctions.Builder> builderConsumer) {
        return nest(RequestPredicates.path(pattern), builderConsumer);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder path(String pattern, Supplier<RouterFunction<ServerResponse>> routerFunctionSupplier) {
        return nest(RequestPredicates.path(pattern), routerFunctionSupplier);
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder filter(HandlerFilterFunction<ServerResponse, ServerResponse> filterFunction) {
        Assert.notNull(filterFunction, "HandlerFilterFunction must not be null");
        this.filterFunctions.add(filterFunction);
        return this;
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder before(Function<ServerRequest, ServerRequest> requestProcessor) {
        Assert.notNull(requestProcessor, "RequestProcessor must not be null");
        return filter(HandlerFilterFunction.ofRequestProcessor(requestProcessor));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder after(BiFunction<ServerRequest, ServerResponse, ServerResponse> responseProcessor) {
        Assert.notNull(responseProcessor, "ResponseProcessor must not be null");
        return filter(HandlerFilterFunction.ofResponseProcessor(responseProcessor));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder onError(Predicate<Throwable> predicate, BiFunction<Throwable, ServerRequest, ServerResponse> responseProvider) {
        Assert.notNull(predicate, "Predicate must not be null");
        Assert.notNull(responseProvider, "ResponseProvider must not be null");
        return filter(HandlerFilterFunction.ofErrorHandler(predicate, responseProvider));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunctions.Builder onError(Class<? extends Throwable> exceptionType, BiFunction<Throwable, ServerRequest, ServerResponse> responseProvider) {
        Assert.notNull(exceptionType, "ExceptionType must not be null");
        Assert.notNull(responseProvider, "ResponseProvider must not be null");
        exceptionType.getClass();
        return filter(HandlerFilterFunction.ofErrorHandler((v1) -> {
            return r1.isInstance(v1);
        }, responseProvider));
    }

    @Override // org.springframework.web.servlet.function.RouterFunctions.Builder
    public RouterFunction<ServerResponse> build() {
        if (this.routerFunctions.isEmpty()) {
            throw new IllegalStateException("No routes registered. Register a route with GET(), POST(), etc.");
        }
        RouterFunction<ServerResponse> result = new BuiltRouterFunction(this.routerFunctions);
        if (this.filterFunctions.isEmpty()) {
            return result;
        }
        HandlerFilterFunction<ServerResponse, ServerResponse> filter = this.filterFunctions.stream().reduce((v0, v1) -> {
            return v0.andThen(v1);
        }).orElseThrow(IllegalStateException::new);
        return result.filter(filter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctionBuilder$BuiltRouterFunction.class */
    public static class BuiltRouterFunction extends RouterFunctions.AbstractRouterFunction<ServerResponse> {
        private List<RouterFunction<ServerResponse>> routerFunctions;

        public BuiltRouterFunction(List<RouterFunction<ServerResponse>> routerFunctions) {
            Assert.notEmpty(routerFunctions, "RouterFunctions must not be empty");
            this.routerFunctions = routerFunctions;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<ServerResponse>> route(ServerRequest request) {
            for (RouterFunction<ServerResponse> routerFunction : this.routerFunctions) {
                Optional<HandlerFunction<ServerResponse>> result = routerFunction.route(request);
                if (result.isPresent()) {
                    return result;
                }
            }
            return Optional.empty();
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(RouterFunctions.Visitor visitor) {
            this.routerFunctions.forEach(routerFunction -> {
                routerFunction.accept(visitor);
            });
        }
    }
}
