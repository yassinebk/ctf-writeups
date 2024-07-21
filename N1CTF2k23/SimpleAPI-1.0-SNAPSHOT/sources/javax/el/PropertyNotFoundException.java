package javax.el;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/jakarta.el-3.0.3.jar:javax/el/PropertyNotFoundException.class */
public class PropertyNotFoundException extends ELException {
    private static final long serialVersionUID = 7876728153282609955L;

    public PropertyNotFoundException() {
    }

    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(Throwable exception) {
        super(exception);
    }

    public PropertyNotFoundException(String pMessage, Throwable pRootCause) {
        super(pMessage, pRootCause);
    }
}
