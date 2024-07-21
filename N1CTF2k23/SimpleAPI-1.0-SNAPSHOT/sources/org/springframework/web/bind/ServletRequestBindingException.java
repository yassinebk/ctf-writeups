package org.springframework.web.bind;

import org.springframework.web.util.NestedServletException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/bind/ServletRequestBindingException.class */
public class ServletRequestBindingException extends NestedServletException {
    public ServletRequestBindingException(String msg) {
        super(msg);
    }

    public ServletRequestBindingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
