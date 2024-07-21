package javax.servlet;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/ServletContextAttributeEvent.class */
public class ServletContextAttributeEvent extends ServletContextEvent {
    private static final long serialVersionUID = 1;
    private final String name;
    private final Object value;

    public ServletContextAttributeEvent(ServletContext source, String name, Object value) {
        super(source);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
