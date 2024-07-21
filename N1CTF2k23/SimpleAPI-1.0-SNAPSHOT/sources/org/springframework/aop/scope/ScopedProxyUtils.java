package org.springframework.aop.scope;

import org.springframework.aop.framework.autoproxy.AutoProxyUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-aop-5.2.6.RELEASE.jar:org/springframework/aop/scope/ScopedProxyUtils.class */
public abstract class ScopedProxyUtils {
    private static final String TARGET_NAME_PREFIX = "scopedTarget.";
    private static final int TARGET_NAME_PREFIX_LENGTH = TARGET_NAME_PREFIX.length();

    public static BeanDefinitionHolder createScopedProxy(BeanDefinitionHolder definition, BeanDefinitionRegistry registry, boolean proxyTargetClass) {
        String originalBeanName = definition.getBeanName();
        BeanDefinition targetDefinition = definition.getBeanDefinition();
        String targetBeanName = getTargetBeanName(originalBeanName);
        RootBeanDefinition proxyDefinition = new RootBeanDefinition(ScopedProxyFactoryBean.class);
        proxyDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, targetBeanName));
        proxyDefinition.setOriginatingBeanDefinition(targetDefinition);
        proxyDefinition.setSource(definition.getSource());
        proxyDefinition.setRole(targetDefinition.getRole());
        proxyDefinition.getPropertyValues().add("targetBeanName", targetBeanName);
        if (proxyTargetClass) {
            targetDefinition.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
        } else {
            proxyDefinition.getPropertyValues().add("proxyTargetClass", Boolean.FALSE);
        }
        proxyDefinition.setAutowireCandidate(targetDefinition.isAutowireCandidate());
        proxyDefinition.setPrimary(targetDefinition.isPrimary());
        if (targetDefinition instanceof AbstractBeanDefinition) {
            proxyDefinition.copyQualifiersFrom((AbstractBeanDefinition) targetDefinition);
        }
        targetDefinition.setAutowireCandidate(false);
        targetDefinition.setPrimary(false);
        registry.registerBeanDefinition(targetBeanName, targetDefinition);
        return new BeanDefinitionHolder(proxyDefinition, originalBeanName, definition.getAliases());
    }

    public static String getTargetBeanName(String originalBeanName) {
        return TARGET_NAME_PREFIX + originalBeanName;
    }

    public static String getOriginalBeanName(@Nullable String targetBeanName) {
        Assert.isTrue(isScopedTarget(targetBeanName), () -> {
            return "bean name '" + targetBeanName + "' does not refer to the target of a scoped proxy";
        });
        return targetBeanName.substring(TARGET_NAME_PREFIX_LENGTH);
    }

    public static boolean isScopedTarget(@Nullable String beanName) {
        return beanName != null && beanName.startsWith(TARGET_NAME_PREFIX);
    }
}
