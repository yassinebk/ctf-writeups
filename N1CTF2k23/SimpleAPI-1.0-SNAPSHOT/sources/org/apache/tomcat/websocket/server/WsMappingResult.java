package org.apache.tomcat.websocket.server;

import java.util.Map;
import javax.websocket.server.ServerEndpointConfig;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:org/apache/tomcat/websocket/server/WsMappingResult.class */
class WsMappingResult {
    private final ServerEndpointConfig config;
    private final Map<String, String> pathParams;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WsMappingResult(ServerEndpointConfig config, Map<String, String> pathParams) {
        this.config = config;
        this.pathParams = pathParams;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ServerEndpointConfig getConfig() {
        return this.config;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Map<String, String> getPathParams() {
        return this.pathParams;
    }
}
