package org.springframework.web.context.request.async;

import java.util.concurrent.Callable;
import org.springframework.web.context.request.NativeWebRequest;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/request/async/TimeoutCallableProcessingInterceptor.class */
public class TimeoutCallableProcessingInterceptor implements CallableProcessingInterceptor {
    @Override // org.springframework.web.context.request.async.CallableProcessingInterceptor
    public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
        return new AsyncRequestTimeoutException();
    }
}
