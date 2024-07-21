package org.springframework.web.servlet.function;

import java.util.Optional;
import org.springframework.web.servlet.function.RequestPredicates;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/function/RequestPredicate.class */
public interface RequestPredicate {
    boolean test(ServerRequest serverRequest);

    default RequestPredicate and(RequestPredicate other) {
        return new RequestPredicates.AndRequestPredicate(this, other);
    }

    default RequestPredicate negate() {
        return new RequestPredicates.NegateRequestPredicate(this);
    }

    default RequestPredicate or(RequestPredicate other) {
        return new RequestPredicates.OrRequestPredicate(this, other);
    }

    default Optional<ServerRequest> nest(ServerRequest request) {
        return test(request) ? Optional.of(request) : Optional.empty();
    }

    default void accept(RequestPredicates.Visitor visitor) {
        visitor.unknown(this);
    }
}
