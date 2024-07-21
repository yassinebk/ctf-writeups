package org.springframework.boot.web.embedded.jetty;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.server.handler.gzip.GzipHandler;
import org.springframework.boot.web.server.Compression;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyHandlerWrappers.class */
public final class JettyHandlerWrappers {
    private JettyHandlerWrappers() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HandlerWrapper createGzipHandlerWrapper(Compression compression) {
        HttpMethod[] values;
        GzipHandler handler = new GzipHandler();
        handler.setMinGzipSize((int) compression.getMinResponseSize().toBytes());
        handler.setIncludedMimeTypes(compression.getMimeTypes());
        for (HttpMethod httpMethod : HttpMethod.values()) {
            handler.addIncludedMethods(new String[]{httpMethod.name()});
        }
        if (compression.getExcludedUserAgents() != null) {
            handler.setExcludedAgentPatterns(compression.getExcludedUserAgents());
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static HandlerWrapper createServerHeaderHandlerWrapper(String header) {
        return new ServerHeaderHandler(header);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/jetty/JettyHandlerWrappers$ServerHeaderHandler.class */
    public static class ServerHeaderHandler extends HandlerWrapper {
        private static final String SERVER_HEADER = "server";
        private final String value;

        ServerHeaderHandler(String value) {
            this.value = value;
        }

        public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
            if (!response.getHeaderNames().contains(SERVER_HEADER)) {
                response.setHeader(SERVER_HEADER, this.value);
            }
            super.handle(target, baseRequest, request, response);
        }
    }
}
