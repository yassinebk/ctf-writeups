package org.springframework.boot.autoconfigure.data.jpa;

import javax.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/jpa/EntityManagerFactoryDependsOnPostProcessor.class */
public class EntityManagerFactoryDependsOnPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public EntityManagerFactoryDependsOnPostProcessor(String... dependsOn) {
        super(EntityManagerFactory.class, AbstractEntityManagerFactoryBean.class, dependsOn);
    }

    public EntityManagerFactoryDependsOnPostProcessor(Class<?>... dependsOn) {
        super(EntityManagerFactory.class, AbstractEntityManagerFactoryBean.class, dependsOn);
    }
}
