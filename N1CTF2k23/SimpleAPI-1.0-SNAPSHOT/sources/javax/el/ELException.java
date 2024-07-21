package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/ELException.class */
public class ELException extends RuntimeException {
    private static final long serialVersionUID = -2161386187282690885L;

    public ELException() {
    }

    public ELException(String pMessage) {
        super(pMessage);
    }

    public ELException(Throwable pRootCause) {
        super(pRootCause);
    }

    public ELException(String pMessage, Throwable pRootCause) {
        super(pMessage, pRootCause);
    }
}
