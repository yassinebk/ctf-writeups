package org.springframework.aop.target;

import org.springframework.beans.BeansException;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/target/LazyInitTargetSource.class */
public class LazyInitTargetSource extends AbstractBeanFactoryBasedTargetSource {
    @Nullable
    private Object target;

    @Override // org.springframework.aop.TargetSource
    @Nullable
    public synchronized Object getTarget() throws BeansException {
        if (this.target == null) {
            this.target = getBeanFactory().getBean(getTargetBeanName());
            postProcessTargetObject(this.target);
        }
        return this.target;
    }

    protected void postProcessTargetObject(Object targetObject) {
    }
}
