package javax.websocket;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-websocket-9.0.35.jar:javax/websocket/SendResult.class */
public final class SendResult {
    private final Throwable exception;
    private final boolean ok;

    public SendResult(Throwable exception) {
        this.exception = exception;
        this.ok = exception == null;
    }

    public SendResult() {
        this(null);
    }

    public Throwable getException() {
        return this.exception;
    }

    public boolean isOK() {
        return this.ok;
    }
}
