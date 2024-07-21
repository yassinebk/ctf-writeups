package org.springframework.web.servlet.handler;

import java.util.Map;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/handler/RequestMatchResult.class */
public class RequestMatchResult {
    private final String matchingPattern;
    private final String lookupPath;
    private final PathMatcher pathMatcher;

    public RequestMatchResult(String matchingPattern, String lookupPath, PathMatcher pathMatcher) {
        Assert.hasText(matchingPattern, "'matchingPattern' is required");
        Assert.hasText(lookupPath, "'lookupPath' is required");
        Assert.notNull(pathMatcher, "'pathMatcher' is required");
        this.matchingPattern = matchingPattern;
        this.lookupPath = lookupPath;
        this.pathMatcher = pathMatcher;
    }

    public Map<String, String> extractUriTemplateVariables() {
        return this.pathMatcher.extractUriTemplateVariables(this.matchingPattern, this.lookupPath);
    }
}
