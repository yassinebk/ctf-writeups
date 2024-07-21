package org.springframework.boot.origin;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/origin/Origin.class */
public interface Origin {
    static Origin from(Object source) {
        if (source instanceof Origin) {
            return (Origin) source;
        }
        Origin origin = null;
        if (source instanceof OriginProvider) {
            origin = ((OriginProvider) source).getOrigin();
        }
        if (origin == null && (source instanceof Throwable)) {
            return from(((Throwable) source).getCause());
        }
        return origin;
    }
}
