package org.springframework.aop.target;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/target/SimpleBeanTargetSource.class */
public class SimpleBeanTargetSource extends AbstractBeanFactoryBasedTargetSource {
    @Override // org.springframework.aop.TargetSource
    public Object getTarget() throws Exception {
        return getBeanFactory().getBean(getTargetBeanName());
    }
}
