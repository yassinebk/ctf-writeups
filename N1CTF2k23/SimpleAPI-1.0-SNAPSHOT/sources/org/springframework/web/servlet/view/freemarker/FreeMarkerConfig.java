package org.springframework.web.servlet.view.freemarker;

import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/freemarker/FreeMarkerConfig.class */
public interface FreeMarkerConfig {
    Configuration getConfiguration();

    TaglibFactory getTaglibFactory();
}
