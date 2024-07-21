package org.springframework.util;

import java.util.Comparator;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/PathMatcher.class */
public interface PathMatcher {
    boolean isPattern(String str);

    boolean match(String str, String str2);

    boolean matchStart(String str, String str2);

    String extractPathWithinPattern(String str, String str2);

    Map<String, String> extractUriTemplateVariables(String str, String str2);

    Comparator<String> getPatternComparator(String str);

    String combine(String str, String str2);
}
