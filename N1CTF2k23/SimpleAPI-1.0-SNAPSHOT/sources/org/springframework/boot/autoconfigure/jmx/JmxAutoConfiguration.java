package org.springframework.boot.autoconfigure.jmx;

import javax.management.MBeanServer;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.MBeanExportConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationJmxAttributeSource;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.export.naming.ObjectNamingStrategy;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.util.StringUtils;
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MBeanExporter.class})
@ConditionalOnProperty(prefix = "spring.jmx", name = {"enabled"}, havingValue = "true")
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/jmx/JmxAutoConfiguration.class */
public class JmxAutoConfiguration {
    private final Environment environment;

    public JmxAutoConfiguration(Environment environment) {
        this.environment = environment;
    }

    @ConditionalOnMissingBean(value = {MBeanExporter.class}, search = SearchStrategy.CURRENT)
    @Bean
    @Primary
    public AnnotationMBeanExporter mbeanExporter(ObjectNamingStrategy namingStrategy, BeanFactory beanFactory) {
        AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
        exporter.setRegistrationPolicy(RegistrationPolicy.FAIL_ON_EXISTING);
        exporter.setNamingStrategy(namingStrategy);
        String serverBean = this.environment.getProperty("spring.jmx.server", "mbeanServer");
        if (StringUtils.hasLength(serverBean)) {
            exporter.setServer((MBeanServer) beanFactory.getBean(serverBean, MBeanServer.class));
        }
        return exporter;
    }

    @ConditionalOnMissingBean(value = {ObjectNamingStrategy.class}, search = SearchStrategy.CURRENT)
    @Bean
    public ParentAwareNamingStrategy objectNamingStrategy() {
        ParentAwareNamingStrategy namingStrategy = new ParentAwareNamingStrategy(new AnnotationJmxAttributeSource());
        String defaultDomain = this.environment.getProperty("spring.jmx.default-domain");
        if (StringUtils.hasLength(defaultDomain)) {
            namingStrategy.setDefaultDomain(defaultDomain);
        }
        boolean uniqueNames = ((Boolean) this.environment.getProperty("spring.jmx.unique-names", Boolean.class, false)).booleanValue();
        namingStrategy.setEnsureUniqueRuntimeObjectNames(uniqueNames);
        return namingStrategy;
    }

    @ConditionalOnMissingBean
    @Bean
    public MBeanServer mbeanServer() {
        MBeanExportConfiguration.SpecificPlatform platform = MBeanExportConfiguration.SpecificPlatform.get();
        if (platform != null) {
            return platform.getMBeanServer();
        }
        MBeanServerFactoryBean factory = new MBeanServerFactoryBean();
        factory.setLocateExistingServerIfPossible(true);
        factory.afterPropertiesSet();
        return factory.getObject();
    }
}
