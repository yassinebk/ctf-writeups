package org.springframework.web.util.pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.http.server.PathContainer;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/pattern/PathPatternParser.class */
public class PathPatternParser {
    private static final Log logger = LogFactory.getLog(PathPatternParser.class);
    private boolean matchOptionalTrailingSeparator = true;
    private boolean caseSensitive = true;
    private PathContainer.Options pathOptions = PathContainer.Options.HTTP_PATH;

    public void setMatchOptionalTrailingSeparator(boolean matchOptionalTrailingSeparator) {
        this.matchOptionalTrailingSeparator = matchOptionalTrailingSeparator;
    }

    public boolean isMatchOptionalTrailingSeparator() {
        return this.matchOptionalTrailingSeparator;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    public void setPathOptions(PathContainer.Options pathOptions) {
        this.pathOptions = pathOptions;
    }

    public PathContainer.Options getPathOptions() {
        return this.pathOptions;
    }

    public PathPattern parse(String pathPattern) throws PatternParseException {
        int wildcardIndex = pathPattern.indexOf(SecurityConstraint.ROLE_ALL_AUTHENTICATED_USERS + this.pathOptions.separator());
        if (wildcardIndex != -1 && wildcardIndex != pathPattern.length() - 3) {
            logger.warn("'**' patterns are not supported in the middle of patterns and will be rejected in the future. Consider using '*' instead for matching a single path segment.");
        }
        return new InternalPathPatternParser(this).parse(pathPattern);
    }
}
