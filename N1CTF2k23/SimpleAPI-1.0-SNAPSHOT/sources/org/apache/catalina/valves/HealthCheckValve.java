package org.apache.catalina.valves;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.tomcat.util.buf.MessageBytes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/valves/HealthCheckValve.class */
public class HealthCheckValve extends ValveBase {
    private static final String UP = "{\n  \"status\": \"UP\",\n  \"checks\": []\n}";
    private String path;

    public HealthCheckValve() {
        super(true);
        this.path = "/health";
    }

    public final String getPath() {
        return this.path;
    }

    public final void setPath(String path) {
        this.path = path;
    }

    @Override // org.apache.catalina.Valve
    public void invoke(Request request, Response response) throws IOException, ServletException {
        MessageBytes requestPathMB = request.getRequestPathMB();
        if (requestPathMB.equals(this.path)) {
            response.setContentType("application/json");
            response.getOutputStream().print(UP);
            return;
        }
        getNext().invoke(request, response);
    }
}
