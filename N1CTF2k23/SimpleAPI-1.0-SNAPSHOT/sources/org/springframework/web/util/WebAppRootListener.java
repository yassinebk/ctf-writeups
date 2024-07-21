package org.springframework.web.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/util/WebAppRootListener.class */
public class WebAppRootListener implements ServletContextListener {
    @Override // javax.servlet.ServletContextListener
    public void contextInitialized(ServletContextEvent event) {
        WebUtils.setWebAppRootSystemProperty(event.getServletContext());
    }

    @Override // javax.servlet.ServletContextListener
    public void contextDestroyed(ServletContextEvent event) {
        WebUtils.removeWebAppRootSystemProperty(event.getServletContext());
    }
}
