package org.springframework.validation;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/validation/MessageCodesResolver.class */
public interface MessageCodesResolver {
    String[] resolveMessageCodes(String str, String str2);

    String[] resolveMessageCodes(String str, String str2, String str3, @Nullable Class<?> cls);
}
