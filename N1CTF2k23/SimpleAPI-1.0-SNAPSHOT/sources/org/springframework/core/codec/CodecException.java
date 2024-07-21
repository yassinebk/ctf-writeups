package org.springframework.core.codec;

import org.springframework.core.NestedRuntimeException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/CodecException.class */
public class CodecException extends NestedRuntimeException {
    public CodecException(String msg) {
        super(msg);
    }

    public CodecException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
