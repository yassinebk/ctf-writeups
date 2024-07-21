package org.apache.catalina.core;

import java.util.concurrent.atomic.AtomicInteger;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.tomcat.util.res.StringManager;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/core/StandardWrapperValve.class */
public final class StandardWrapperValve extends ValveBase {
    private volatile long processingTime;
    private volatile long maxTime;
    private volatile long minTime;
    private final AtomicInteger requestCount;
    private final AtomicInteger errorCount;
    private static final StringManager sm = StringManager.getManager(Constants.Package);

    public StandardWrapperValve() {
        super(true);
        this.minTime = Long.MAX_VALUE;
        this.requestCount = new AtomicInteger(0);
        this.errorCount = new AtomicInteger(0);
    }

    /* JADX WARN: Removed duplicated region for block: B:126:0x04a1  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x04b1  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x05b7  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x05c7  */
    /* JADX WARN: Removed duplicated region for block: B:192:0x0729  */
    /* JADX WARN: Removed duplicated region for block: B:195:0x0739  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x0856  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0866  */
    /* JADX WARN: Removed duplicated region for block: B:282:0x0a4f  */
    /* JADX WARN: Removed duplicated region for block: B:285:0x0a5f  */
    /* JADX WARN: Removed duplicated region for block: B:289:0x07eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:293:0x054c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0436 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:309:0x06be A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:315:0x09e4 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:321:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:323:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:325:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:327:? A[RETURN, SYNTHETIC] */
    @Override // org.apache.catalina.Valve
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void invoke(org.apache.catalina.connector.Request r10, org.apache.catalina.connector.Response r11) throws java.io.IOException, javax.servlet.ServletException {
        /*
            Method dump skipped, instructions count: 2665
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.catalina.core.StandardWrapperValve.invoke(org.apache.catalina.connector.Request, org.apache.catalina.connector.Response):void");
    }

    private void exception(Request request, Response response, Throwable exception) {
        request.setAttribute("javax.servlet.error.exception", exception);
        response.setStatus(500);
        response.setError();
    }

    public long getProcessingTime() {
        return this.processingTime;
    }

    public long getMaxTime() {
        return this.maxTime;
    }

    public long getMinTime() {
        return this.minTime;
    }

    public int getRequestCount() {
        return this.requestCount.get();
    }

    public int getErrorCount() {
        return this.errorCount.get();
    }

    public void incrementErrorCount() {
        this.errorCount.incrementAndGet();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.catalina.valves.ValveBase, org.apache.catalina.util.LifecycleMBeanBase, org.apache.catalina.util.LifecycleBase
    public void initInternal() throws LifecycleException {
    }
}
