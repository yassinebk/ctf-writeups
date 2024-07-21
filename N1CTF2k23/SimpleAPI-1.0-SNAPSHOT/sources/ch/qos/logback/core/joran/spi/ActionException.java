package ch.qos.logback.core.joran.spi;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/joran/spi/ActionException.class */
public class ActionException extends Exception {
    private static final long serialVersionUID = 2743349809995319806L;

    public ActionException() {
    }

    public ActionException(Throwable rootCause) {
        super(rootCause);
    }
}
