package org.springframework.web.servlet.function;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions.class */
public abstract class RouterFunctions {
    private static final Log logger = LogFactory.getLog(RouterFunctions.class);
    public static final String REQUEST_ATTRIBUTE = RouterFunctions.class.getName() + ".request";
    public static final String URI_TEMPLATE_VARIABLES_ATTRIBUTE = RouterFunctions.class.getName() + ".uriTemplateVariables";
    public static final String MATCHING_PATTERN_ATTRIBUTE = RouterFunctions.class.getName() + ".matchingPattern";

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$Builder.class */
    public interface Builder {
        Builder GET(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder GET(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder HEAD(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder HEAD(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder POST(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder POST(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder PUT(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder PUT(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder PATCH(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder PATCH(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder DELETE(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder DELETE(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder OPTIONS(String str, HandlerFunction<ServerResponse> handlerFunction);

        Builder route(RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder OPTIONS(String str, RequestPredicate requestPredicate, HandlerFunction<ServerResponse> handlerFunction);

        Builder add(RouterFunction<ServerResponse> routerFunction);

        Builder resources(String str, Resource resource);

        Builder resources(Function<ServerRequest, Optional<Resource>> function);

        Builder nest(RequestPredicate requestPredicate, Supplier<RouterFunction<ServerResponse>> supplier);

        Builder nest(RequestPredicate requestPredicate, Consumer<Builder> consumer);

        Builder path(String str, Supplier<RouterFunction<ServerResponse>> supplier);

        Builder path(String str, Consumer<Builder> consumer);

        Builder filter(HandlerFilterFunction<ServerResponse, ServerResponse> handlerFilterFunction);

        Builder before(Function<ServerRequest, ServerRequest> function);

        Builder after(BiFunction<ServerRequest, ServerResponse, ServerResponse> biFunction);

        Builder onError(Predicate<Throwable> predicate, BiFunction<Throwable, ServerRequest, ServerResponse> biFunction);

        Builder onError(Class<? extends Throwable> cls, BiFunction<Throwable, ServerRequest, ServerResponse> biFunction);

        RouterFunction<ServerResponse> build();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$Visitor.class */
    public interface Visitor {
        void startNested(RequestPredicate requestPredicate);

        void endNested(RequestPredicate requestPredicate);

        void route(RequestPredicate requestPredicate, HandlerFunction<?> handlerFunction);

        void resources(Function<ServerRequest, Optional<Resource>> function);

        void unknown(RouterFunction<?> routerFunction);
    }

    public static Builder route() {
        return new RouterFunctionBuilder();
    }

    public static <T extends ServerResponse> RouterFunction<T> route(RequestPredicate predicate, HandlerFunction<T> handlerFunction) {
        return new DefaultRouterFunction(predicate, handlerFunction);
    }

    public static <T extends ServerResponse> RouterFunction<T> nest(RequestPredicate predicate, RouterFunction<T> routerFunction) {
        return new DefaultNestedRouterFunction(predicate, routerFunction);
    }

    public static RouterFunction<ServerResponse> resources(String pattern, Resource location) {
        return resources(resourceLookupFunction(pattern, location));
    }

    public static Function<ServerRequest, Optional<Resource>> resourceLookupFunction(String pattern, Resource location) {
        return new PathResourceLookupFunction(pattern, location);
    }

    public static RouterFunction<ServerResponse> resources(Function<ServerRequest, Optional<Resource>> lookupFunction) {
        return new ResourcesRouterFunction(lookupFunction);
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$AbstractRouterFunction.class */
    static abstract class AbstractRouterFunction<T extends ServerResponse> implements RouterFunction<T> {
        public String toString() {
            ToStringVisitor visitor = new ToStringVisitor();
            accept(visitor);
            return visitor.toString();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$SameComposedRouterFunction.class */
    static final class SameComposedRouterFunction<T extends ServerResponse> extends AbstractRouterFunction<T> {
        private final RouterFunction<T> first;
        private final RouterFunction<T> second;

        public SameComposedRouterFunction(RouterFunction<T> first, RouterFunction<T> second) {
            this.first = first;
            this.second = second;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<T>> route(ServerRequest request) {
            Optional<HandlerFunction<T>> firstRoute = this.first.route(request);
            if (firstRoute.isPresent()) {
                return firstRoute;
            }
            return this.second.route(request);
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            this.first.accept(visitor);
            this.second.accept(visitor);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$DifferentComposedRouterFunction.class */
    static final class DifferentComposedRouterFunction extends AbstractRouterFunction<ServerResponse> {
        private final RouterFunction<?> first;
        private final RouterFunction<?> second;

        public DifferentComposedRouterFunction(RouterFunction<?> first, RouterFunction<?> second) {
            this.first = first;
            this.second = second;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<ServerResponse>> route(ServerRequest request) {
            Optional route = this.first.route(request);
            if (route.isPresent()) {
                return route;
            }
            return this.second.route(request);
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            this.first.accept(visitor);
            this.second.accept(visitor);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$FilteredRouterFunction.class */
    static final class FilteredRouterFunction<T extends ServerResponse, S extends ServerResponse> implements RouterFunction<S> {
        private final RouterFunction<T> routerFunction;
        private final HandlerFilterFunction<T, S> filterFunction;

        public FilteredRouterFunction(RouterFunction<T> routerFunction, HandlerFilterFunction<T, S> filterFunction) {
            this.routerFunction = routerFunction;
            this.filterFunction = filterFunction;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<S>> route(ServerRequest request) {
            Optional<HandlerFunction<T>> route = this.routerFunction.route(request);
            HandlerFilterFunction<T, S> handlerFilterFunction = this.filterFunction;
            handlerFilterFunction.getClass();
            return (Optional<HandlerFunction<S>>) route.map(this::apply);
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            this.routerFunction.accept(visitor);
        }

        public String toString() {
            return this.routerFunction.toString();
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$DefaultRouterFunction.class */
    private static final class DefaultRouterFunction<T extends ServerResponse> extends AbstractRouterFunction<T> {
        private final RequestPredicate predicate;
        private final HandlerFunction<T> handlerFunction;

        public DefaultRouterFunction(RequestPredicate predicate, HandlerFunction<T> handlerFunction) {
            Assert.notNull(predicate, "Predicate must not be null");
            Assert.notNull(handlerFunction, "HandlerFunction must not be null");
            this.predicate = predicate;
            this.handlerFunction = handlerFunction;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<T>> route(ServerRequest request) {
            if (this.predicate.test(request)) {
                if (RouterFunctions.logger.isTraceEnabled()) {
                    RouterFunctions.logger.trace(String.format("Predicate \"%s\" matches against \"%s\"", this.predicate, request));
                }
                return Optional.of(this.handlerFunction);
            }
            return Optional.empty();
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            visitor.route(this.predicate, this.handlerFunction);
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$DefaultNestedRouterFunction.class */
    private static final class DefaultNestedRouterFunction<T extends ServerResponse> extends AbstractRouterFunction<T> {
        private final RequestPredicate predicate;
        private final RouterFunction<T> routerFunction;

        public DefaultNestedRouterFunction(RequestPredicate predicate, RouterFunction<T> routerFunction) {
            Assert.notNull(predicate, "Predicate must not be null");
            Assert.notNull(routerFunction, "RouterFunction must not be null");
            this.predicate = predicate;
            this.routerFunction = routerFunction;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<T>> route(ServerRequest serverRequest) {
            return (Optional) this.predicate.nest(serverRequest).map(nestedRequest -> {
                if (RouterFunctions.logger.isTraceEnabled()) {
                    RouterFunctions.logger.trace(String.format("Nested predicate \"%s\" matches against \"%s\"", this.predicate, serverRequest));
                }
                Optional<HandlerFunction<T>> result = this.routerFunction.route(nestedRequest);
                if (result.isPresent() && nestedRequest != serverRequest) {
                    serverRequest.attributes().clear();
                    serverRequest.attributes().putAll(nestedRequest.attributes());
                }
                return result;
            }).orElseGet(Optional::empty);
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            visitor.startNested(this.predicate);
            this.routerFunction.accept(visitor);
            visitor.endNested(this.predicate);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunctions$ResourcesRouterFunction.class */
    public static class ResourcesRouterFunction extends AbstractRouterFunction<ServerResponse> {
        private final Function<ServerRequest, Optional<Resource>> lookupFunction;

        public ResourcesRouterFunction(Function<ServerRequest, Optional<Resource>> lookupFunction) {
            Assert.notNull(lookupFunction, "Function must not be null");
            this.lookupFunction = lookupFunction;
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public Optional<HandlerFunction<ServerResponse>> route(ServerRequest request) {
            return this.lookupFunction.apply(request).map(ResourceHandlerFunction::new);
        }

        @Override // org.springframework.web.servlet.function.RouterFunction
        public void accept(Visitor visitor) {
            visitor.resources(this.lookupFunction);
        }
    }
}
