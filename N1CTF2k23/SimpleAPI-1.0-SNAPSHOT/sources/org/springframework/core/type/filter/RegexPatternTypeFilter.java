package org.springframework.core.type.filter;

import java.util.regex.Pattern;
import org.springframework.core.type.ClassMetadata;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/type/filter/RegexPatternTypeFilter.class */
public class RegexPatternTypeFilter extends AbstractClassTestingTypeFilter {
    private final Pattern pattern;

    public RegexPatternTypeFilter(Pattern pattern) {
        Assert.notNull(pattern, "Pattern must not be null");
        this.pattern = pattern;
    }

    @Override // org.springframework.core.type.filter.AbstractClassTestingTypeFilter
    protected boolean match(ClassMetadata metadata) {
        return this.pattern.matcher(metadata.getClassName()).matches();
    }
}
