package org.springframework.boot.jta.bitronix;

import javax.jms.ConnectionFactory;
import javax.jms.XAConnectionFactory;
import org.springframework.boot.jms.XAConnectionFactoryWrapper;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/jta/bitronix/BitronixXAConnectionFactoryWrapper.class */
public class BitronixXAConnectionFactoryWrapper implements XAConnectionFactoryWrapper {
    @Override // org.springframework.boot.jms.XAConnectionFactoryWrapper
    public ConnectionFactory wrapConnectionFactory(XAConnectionFactory connectionFactory) {
        PoolingConnectionFactoryBean pool = new PoolingConnectionFactoryBean();
        pool.setConnectionFactory(connectionFactory);
        return pool;
    }
}
