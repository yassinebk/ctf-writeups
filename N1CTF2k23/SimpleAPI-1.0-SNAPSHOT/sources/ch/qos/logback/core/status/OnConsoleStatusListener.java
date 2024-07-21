package ch.qos.logback.core.status;

import java.io.PrintStream;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/status/OnConsoleStatusListener.class */
public class OnConsoleStatusListener extends OnPrintStreamStatusListenerBase {
    @Override // ch.qos.logback.core.status.OnPrintStreamStatusListenerBase
    protected PrintStream getPrintStream() {
        return System.out;
    }
}
