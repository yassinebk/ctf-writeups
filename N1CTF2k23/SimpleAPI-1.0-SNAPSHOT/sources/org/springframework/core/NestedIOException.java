package org.springframework.core;

import java.io.IOException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/NestedIOException.class */
public class NestedIOException extends IOException {
    static {
        NestedExceptionUtils.class.getName();
    }

    public NestedIOException(String msg) {
        super(msg);
    }

    public NestedIOException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }

    @Override // java.lang.Throwable
    @Nullable
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
    }
}
