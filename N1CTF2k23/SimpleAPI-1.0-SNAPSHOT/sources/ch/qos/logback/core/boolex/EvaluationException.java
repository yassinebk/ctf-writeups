package ch.qos.logback.core.boolex;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-core-1.2.3.jar:ch/qos/logback/core/boolex/EvaluationException.class */
public class EvaluationException extends Exception {
    private static final long serialVersionUID = 1;

    public EvaluationException(String msg) {
        super(msg);
    }

    public EvaluationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EvaluationException(Throwable cause) {
        super(cause);
    }
}
