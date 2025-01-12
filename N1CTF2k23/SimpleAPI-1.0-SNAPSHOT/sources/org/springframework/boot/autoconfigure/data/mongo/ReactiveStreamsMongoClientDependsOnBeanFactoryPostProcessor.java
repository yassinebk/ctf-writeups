package org.springframework.boot.autoconfigure.data.mongo;

import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;
@Order(Integer.MAX_VALUE)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/data/mongo/ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor.class */
public class ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public ReactiveStreamsMongoClientDependsOnBeanFactoryPostProcessor(Class<?>... dependsOn) {
        super(MongoClient.class, ReactiveMongoClientFactoryBean.class, dependsOn);
    }
}
