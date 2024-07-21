package org.apache.tomcat.util.descriptor.web;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.descriptor.JspConfigDescriptor;
import javax.servlet.descriptor.JspPropertyGroupDescriptor;
import javax.servlet.descriptor.TaglibDescriptor;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/descriptor/web/JspConfigDescriptorImpl.class */
public class JspConfigDescriptorImpl implements JspConfigDescriptor {
    private final Collection<JspPropertyGroupDescriptor> jspPropertyGroups;
    private final Collection<TaglibDescriptor> taglibs;

    public JspConfigDescriptorImpl(Collection<JspPropertyGroupDescriptor> jspPropertyGroups, Collection<TaglibDescriptor> taglibs) {
        this.jspPropertyGroups = jspPropertyGroups;
        this.taglibs = taglibs;
    }

    @Override // javax.servlet.descriptor.JspConfigDescriptor
    public Collection<JspPropertyGroupDescriptor> getJspPropertyGroups() {
        return new ArrayList(this.jspPropertyGroups);
    }

    @Override // javax.servlet.descriptor.JspConfigDescriptor
    public Collection<TaglibDescriptor> getTaglibs() {
        return new ArrayList(this.taglibs);
    }
}
