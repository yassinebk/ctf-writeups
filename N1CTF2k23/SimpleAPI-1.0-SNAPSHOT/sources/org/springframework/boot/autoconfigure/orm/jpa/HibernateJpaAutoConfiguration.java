package org.springframework.boot.autoconfigure.orm.jpa;

import javax.persistence.EntityManager;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
@EnableConfigurationProperties({JpaProperties.class})
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, EntityManager.class, SessionImplementor.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class})
@Import({HibernateJpaConfiguration.class})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaAutoConfiguration.class */
public class HibernateJpaAutoConfiguration {
}
