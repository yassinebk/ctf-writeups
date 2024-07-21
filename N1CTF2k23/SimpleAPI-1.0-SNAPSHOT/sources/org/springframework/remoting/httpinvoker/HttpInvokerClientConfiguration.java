package org.springframework.remoting.httpinvoker;

import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/remoting/httpinvoker/HttpInvokerClientConfiguration.class */
public interface HttpInvokerClientConfiguration {
    String getServiceUrl();

    @Nullable
    String getCodebaseUrl();
}
