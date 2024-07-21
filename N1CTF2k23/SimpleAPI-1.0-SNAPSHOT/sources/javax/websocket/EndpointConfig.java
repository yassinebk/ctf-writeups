package javax.websocket;

import java.util.List;
import java.util.Map;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/EndpointConfig.class */
public interface EndpointConfig {
    List<Class<? extends Encoder>> getEncoders();

    List<Class<? extends Decoder>> getDecoders();

    Map<String, Object> getUserProperties();
}
