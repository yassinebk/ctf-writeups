package org.springframework.boot.autoconfigure.security.reactive;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.autoconfigure.security.StaticResourceLocation;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/reactive/StaticResourceRequest.class */
public final class StaticResourceRequest {
    static final StaticResourceRequest INSTANCE = new StaticResourceRequest();

    private StaticResourceRequest() {
    }

    public StaticResourceServerWebExchange atCommonLocations() {
        return at(EnumSet.allOf(StaticResourceLocation.class));
    }

    public StaticResourceServerWebExchange at(StaticResourceLocation first, StaticResourceLocation... rest) {
        return at(EnumSet.of(first, rest));
    }

    public StaticResourceServerWebExchange at(Set<StaticResourceLocation> locations) {
        Assert.notNull(locations, "Locations must not be null");
        return new StaticResourceServerWebExchange(new LinkedHashSet(locations));
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/security/reactive/StaticResourceRequest$StaticResourceServerWebExchange.class */
    public static final class StaticResourceServerWebExchange implements ServerWebExchangeMatcher {
        private final Set<StaticResourceLocation> locations;

        private StaticResourceServerWebExchange(Set<StaticResourceLocation> locations) {
            this.locations = locations;
        }

        public StaticResourceServerWebExchange excluding(StaticResourceLocation first, StaticResourceLocation... rest) {
            return excluding(EnumSet.of(first, rest));
        }

        public StaticResourceServerWebExchange excluding(Set<StaticResourceLocation> locations) {
            Assert.notNull(locations, "Locations must not be null");
            Set<StaticResourceLocation> subset = new LinkedHashSet<>(this.locations);
            subset.removeAll(locations);
            return new StaticResourceServerWebExchange(subset);
        }

        private List<ServerWebExchangeMatcher> getDelegateMatchers() {
            return (List) getPatterns().map(PathPatternParserServerWebExchangeMatcher::new).collect(Collectors.toList());
        }

        private Stream<String> getPatterns() {
            return this.locations.stream().flatMap((v0) -> {
                return v0.getPatterns();
            });
        }

        public Mono<ServerWebExchangeMatcher.MatchResult> matches(ServerWebExchange exchange) {
            OrServerWebExchangeMatcher matcher = new OrServerWebExchangeMatcher(getDelegateMatchers());
            return matcher.matches(exchange);
        }
    }
}
