package org.springframework.util;

import java.util.Collection;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/util/ExceptionTypeFilter.class */
public class ExceptionTypeFilter extends InstanceFilter<Class<? extends Throwable>> {
    public ExceptionTypeFilter(Collection<? extends Class<? extends Throwable>> includes, Collection<? extends Class<? extends Throwable>> excludes, boolean matchIfEmpty) {
        super(includes, excludes, matchIfEmpty);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.util.InstanceFilter
    public boolean match(Class<? extends Throwable> instance, Class<? extends Throwable> candidate) {
        return candidate.isAssignableFrom(instance);
    }
}
