package org.springframework.util;

import java.util.Comparator;
import java.util.Map;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/RouteMatcher.class */
public interface RouteMatcher {

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/RouteMatcher$Route.class */
    public interface Route {
        String value();
    }

    Route parseRoute(String str);

    boolean isPattern(String str);

    String combine(String str, String str2);

    boolean match(String str, Route route);

    @Nullable
    Map<String, String> matchAndExtract(String str, Route route);

    Comparator<String> getPatternComparator(Route route);
}
