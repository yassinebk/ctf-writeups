package org.springframework.cache.interceptor;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CacheOperationInvoker.class */
public interface CacheOperationInvoker {
    Object invoke() throws ThrowableWrapper;

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/cache/interceptor/CacheOperationInvoker$ThrowableWrapper.class */
    public static class ThrowableWrapper extends RuntimeException {
        private final Throwable original;

        public ThrowableWrapper(Throwable original) {
            super(original.getMessage(), original);
            this.original = original;
        }

        public Throwable getOriginal() {
            return this.original;
        }
    }
}
