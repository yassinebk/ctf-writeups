package org.springframework.web.servlet.function;

import java.util.Optional;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RouterFunction.class */
public interface RouterFunction<T extends ServerResponse> {
    Optional<HandlerFunction<T>> route(ServerRequest serverRequest);

    default RouterFunction<T> and(RouterFunction<T> other) {
        return new RouterFunctions.SameComposedRouterFunction(this, other);
    }

    default RouterFunction<?> andOther(RouterFunction<?> other) {
        return new RouterFunctions.DifferentComposedRouterFunction(this, other);
    }

    default RouterFunction<T> andRoute(RequestPredicate predicate, HandlerFunction<T> handlerFunction) {
        return and(RouterFunctions.route(predicate, handlerFunction));
    }

    default RouterFunction<T> andNest(RequestPredicate predicate, RouterFunction<T> routerFunction) {
        return and(RouterFunctions.nest(predicate, routerFunction));
    }

    default <S extends ServerResponse> RouterFunction<S> filter(HandlerFilterFunction<T, S> filterFunction) {
        return new RouterFunctions.FilteredRouterFunction(this, filterFunction);
    }

    default void accept(RouterFunctions.Visitor visitor) {
        visitor.unknown(this);
    }
}
