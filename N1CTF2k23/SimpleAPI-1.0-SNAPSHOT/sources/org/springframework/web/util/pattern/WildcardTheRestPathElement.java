package org.springframework.web.util.pattern;

import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.web.util.pattern.PathPattern;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/pattern/WildcardTheRestPathElement.class */
public class WildcardTheRestPathElement extends PathElement {
    /* JADX INFO: Access modifiers changed from: package-private */
    public WildcardTheRestPathElement(int pos, char separator) {
        super(pos, separator);
    }

    @Override // org.springframework.web.util.pattern.PathElement
    public boolean matches(int pathIndex, PathPattern.MatchingContext matchingContext) {
        if (pathIndex < matchingContext.pathLength && !matchingContext.isSeparator(pathIndex)) {
            return false;
        }
        if (matchingContext.determineRemainingPath) {
            matchingContext.remainingPathIndex = matchingContext.pathLength;
            return true;
        }
        return true;
    }

    @Override // org.springframework.web.util.pattern.PathElement
    public int getNormalizedLength() {
        return 1;
    }

    @Override // org.springframework.web.util.pattern.PathElement
    public int getWildcardCount() {
        return 1;
    }

    public String toString() {
        return "WildcardTheRest(" + this.separator + "**)";
    }

    @Override // org.springframework.web.util.pattern.PathElement
    public char[] getChars() {
        return (this.separator + SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS).toCharArray();
    }
}
