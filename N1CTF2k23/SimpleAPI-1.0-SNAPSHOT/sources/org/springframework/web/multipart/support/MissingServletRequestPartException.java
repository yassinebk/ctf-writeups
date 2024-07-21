package org.springframework.web.multipart.support;

import javax.servlet.ServletException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/multipart/support/MissingServletRequestPartException.class */
public class MissingServletRequestPartException extends ServletException {
    private final String partName;

    public MissingServletRequestPartException(String partName) {
        super("Required request part '" + partName + "' is not present");
        this.partName = partName;
    }

    public String getRequestPartName() {
        return this.partName;
    }
}
