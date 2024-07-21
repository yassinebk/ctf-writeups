package javax.servlet;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletException.class */
public class ServletException extends Exception {
    private static final long serialVersionUID = 1;

    public ServletException() {
    }

    public ServletException(String message) {
        super(message);
    }

    public ServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ServletException(Throwable rootCause) {
        super(rootCause);
    }

    public Throwable getRootCause() {
        return getCause();
    }
}
