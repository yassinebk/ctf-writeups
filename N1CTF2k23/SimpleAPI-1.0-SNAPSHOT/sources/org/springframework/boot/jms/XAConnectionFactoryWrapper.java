package org.springframework.boot.jms;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;
@FunctionalInterface
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jms/XAConnectionFactoryWrapper.class */
public interface XAConnectionFactoryWrapper {
    ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory) throws Exception;
}
