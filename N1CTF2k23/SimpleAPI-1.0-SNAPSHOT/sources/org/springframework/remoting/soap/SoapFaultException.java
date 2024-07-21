package org.springframework.remoting.soap;

import javax.xml.namespace.QName;
import org.springframework.remoting.RemoteInvocationFailureException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-context-5.2.6.RELEASE.jar:org/springframework/remoting/soap/SoapFaultException.class */
public abstract class SoapFaultException extends RemoteInvocationFailureException {
    public abstract String getFaultCode();

    public abstract QName getFaultCodeAsQName();

    public abstract String getFaultString();

    public abstract String getFaultActor();

    /* JADX INFO: Access modifiers changed from: protected */
    public SoapFaultException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
