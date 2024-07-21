package org.springframework.remoting.jaxws;

import javax.xml.ws.Service;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/remoting/jaxws/LocalJaxWsServiceFactoryBean.class */
public class LocalJaxWsServiceFactoryBean extends LocalJaxWsServiceFactory implements FactoryBean<Service>, InitializingBean {
    @Nullable
    private Service service;

    @Override // org.springframework.beans.factory.InitializingBean
    public void afterPropertiesSet() {
        this.service = createJaxWsService();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.springframework.beans.factory.FactoryBean
    @Nullable
    public Service getObject() {
        return this.service;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public Class<? extends Service> getObjectType() {
        return this.service != null ? this.service.getClass() : Service.class;
    }

    @Override // org.springframework.beans.factory.FactoryBean
    public boolean isSingleton() {
        return true;
    }
}
