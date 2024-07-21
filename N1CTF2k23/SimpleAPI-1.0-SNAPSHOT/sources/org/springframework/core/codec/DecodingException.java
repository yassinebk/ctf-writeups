package org.springframework.core.codec;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/codec/DecodingException.class */
public class DecodingException extends CodecException {
    public DecodingException(String msg) {
        super(msg);
    }

    public DecodingException(String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
