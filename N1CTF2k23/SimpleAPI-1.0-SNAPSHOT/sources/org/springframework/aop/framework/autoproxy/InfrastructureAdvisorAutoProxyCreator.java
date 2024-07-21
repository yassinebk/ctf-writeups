package org.springframework.aop.framework.autoproxy;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/framework/autoproxy/InfrastructureAdvisorAutoProxyCreator.class */
public class InfrastructureAdvisorAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
    public void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        super.initBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override // org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator
    protected boolean isEligibleAdvisorBean(String beanName) {
        return this.beanFactory != null && this.beanFactory.containsBeanDefinition(beanName) && this.beanFactory.getBeanDefinition(beanName).getRole() == 2;
    }
}
