package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/MethodNotFoundException.class */
public class MethodNotFoundException extends ELException {
    private static final long serialVersionUID = 7727548537051164640L;

    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(Throwable exception) {
        super(exception);
    }

    public MethodNotFoundException(String pMessage, Throwable pRootCause) {
        super(pMessage, pRootCause);
    }
}
