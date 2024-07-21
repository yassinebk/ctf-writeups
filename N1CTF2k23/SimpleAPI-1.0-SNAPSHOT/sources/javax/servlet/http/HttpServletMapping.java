package javax.servlet.http;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/http/HttpServletMapping.class */
public interface HttpServletMapping {
    String getMatchValue();

    String getPattern();

    String getServletName();

    MappingMatch getMappingMatch();
}
