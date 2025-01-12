package org.apache.catalina.valves;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/valves/RemoteAddrValve.class */
public final class RemoteAddrValve extends RequestFilterValve {
    private static final Log log = LogFactory.getLog(RemoteAddrValve.class);

    @Override // org.apache.catalina.valves.RequestFilterValve, org.apache.catalina.Valve
    public void invoke(Request request, Response response) throws IOException, ServletException {
        String property;
        if (getAddConnectorPort()) {
            property = request.getRequest().getRemoteAddr() + ";" + request.getConnector().getPortWithOffset();
        } else {
            property = request.getRequest().getRemoteAddr();
        }
        process(property, request, response);
    }

    @Override // org.apache.catalina.valves.RequestFilterValve
    protected Log getLog() {
        return log;
    }
}
