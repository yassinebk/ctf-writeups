package org.springframework.web.servlet.view.tiles3;

import org.apache.tiles.TilesException;
import org.apache.tiles.preparer.ViewPreparer;
import org.springframework.web.context.WebApplicationContext;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-webmvc-5.2.6.RELEASE.jar:org/springframework/web/servlet/view/tiles3/SpringBeanPreparerFactory.class */
public class SpringBeanPreparerFactory extends AbstractSpringPreparerFactory {
    @Override // org.springframework.web.servlet.view.tiles3.AbstractSpringPreparerFactory
    protected ViewPreparer getPreparer(String name, WebApplicationContext context) throws TilesException {
        return (ViewPreparer) context.getBean(name, ViewPreparer.class);
    }
}
