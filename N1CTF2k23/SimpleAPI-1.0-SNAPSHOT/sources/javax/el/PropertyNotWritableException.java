package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/PropertyNotWritableException.class */
public class PropertyNotWritableException extends ELException {
    private static final long serialVersionUID = 4511862414551151572L;

    public PropertyNotWritableException() {
    }

    public PropertyNotWritableException(String pMessage) {
        super(pMessage);
    }

    public PropertyNotWritableException(Throwable exception) {
        super(exception);
    }

    public PropertyNotWritableException(String pMessage, Throwable pRootCause) {
        super(pMessage, pRootCause);
    }
}
