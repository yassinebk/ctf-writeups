package org.springframework.web.servlet.tags;

import javax.servlet.jsp.JspTagException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/tags/ArgumentAware.class */
public interface ArgumentAware {
    void addArgument(@Nullable Object obj) throws JspTagException;
}
