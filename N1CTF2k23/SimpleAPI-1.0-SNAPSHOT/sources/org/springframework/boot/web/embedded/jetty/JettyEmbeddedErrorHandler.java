package org.springframework.boot.web.embedded.jetty;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyEmbeddedErrorHandler.class */
class JettyEmbeddedErrorHandler extends ErrorPageErrorHandler {
    public boolean errorPageForMethod(String method) {
        return true;
    }

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
        baseRequest.setMethod("GET");
        super.doError(target, baseRequest, request, response);
    }
}
