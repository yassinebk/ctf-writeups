package org.springframework.boot.web.embedded.tomcat;

import java.io.File;
import java.nio.charset.Charset;
import org.apache.catalina.Valve;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/ConfigurableTomcatWebServerFactory.class */
public interface ConfigurableTomcatWebServerFactory extends ConfigurableWebServerFactory {
    void setBaseDirectory(File baseDirectory);

    void setBackgroundProcessorDelay(int delay);

    void addEngineValves(Valve... engineValves);

    void addConnectorCustomizers(TomcatConnectorCustomizer... tomcatConnectorCustomizers);

    void addContextCustomizers(TomcatContextCustomizer... tomcatContextCustomizers);

    void addProtocolHandlerCustomizers(TomcatProtocolHandlerCustomizer<?>... tomcatProtocolHandlerCustomizers);

    void setUriEncoding(Charset uriEncoding);
}
