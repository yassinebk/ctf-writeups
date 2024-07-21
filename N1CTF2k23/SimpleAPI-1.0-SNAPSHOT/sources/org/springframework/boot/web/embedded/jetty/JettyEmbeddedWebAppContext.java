package org.springframework.boot.web.embedded.jetty;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyEmbeddedWebAppContext.class */
class JettyEmbeddedWebAppContext extends WebAppContext {
    protected ServletHandler newServletHandler() {
        return new JettyEmbeddedServletHandler();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferredInitialize() throws Exception {
        ((JettyEmbeddedServletHandler) getServletHandler()).deferredInitialize();
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyEmbeddedWebAppContext$JettyEmbeddedServletHandler.class */
    private static class JettyEmbeddedServletHandler extends ServletHandler {
        private JettyEmbeddedServletHandler() {
        }

        public void initialize() throws Exception {
        }

        void deferredInitialize() throws Exception {
            super.initialize();
        }
    }
}
