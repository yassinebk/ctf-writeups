package org.springframework.web.context;

import javax.servlet.ServletConfig;
import org.springframework.beans.factory.Aware;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/ServletConfigAware.class */
public interface ServletConfigAware extends Aware {
    void setServletConfig(ServletConfig servletConfig);
}
