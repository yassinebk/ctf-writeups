package org.springframework.http.codec.xml;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.springframework.core.codec.CodecException;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/http/codec/xml/JaxbContextContainer.class */
final class JaxbContextContainer {
    private final ConcurrentMap<Class<?>, JAXBContext> jaxbContexts = new ConcurrentHashMap(64);

    public Marshaller createMarshaller(Class<?> clazz) throws CodecException, JAXBException {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        return jaxbContext.createMarshaller();
    }

    public Unmarshaller createUnmarshaller(Class<?> clazz) throws CodecException, JAXBException {
        JAXBContext jaxbContext = getJaxbContext(clazz);
        return jaxbContext.createUnmarshaller();
    }

    private JAXBContext getJaxbContext(Class<?> clazz) throws CodecException {
        return this.jaxbContexts.computeIfAbsent(clazz, key -> {
            try {
                return JAXBContext.newInstance(new Class[]{clazz});
            } catch (JAXBException ex) {
                throw new CodecException("Could not create JAXBContext for class [" + clazz + "]: " + ex.getMessage(), ex);
            }
        });
    }
}
