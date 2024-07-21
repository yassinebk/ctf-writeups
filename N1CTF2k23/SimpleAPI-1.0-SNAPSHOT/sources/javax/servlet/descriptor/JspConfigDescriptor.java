package javax.servlet.descriptor;

import java.util.Collection;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/servlet/descriptor/JspConfigDescriptor.class */
public interface JspConfigDescriptor {
    Collection<TaglibDescriptor> getTaglibs();

    Collection<JspPropertyGroupDescriptor> getJspPropertyGroups();
}
