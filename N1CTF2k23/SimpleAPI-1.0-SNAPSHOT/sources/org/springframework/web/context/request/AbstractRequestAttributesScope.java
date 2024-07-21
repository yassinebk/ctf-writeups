package org.springframework.web.context.request;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.lang.Nullable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-web-5.2.6.RELEASE.jar:org/springframework/web/context/request/AbstractRequestAttributesScope.class */
public abstract class AbstractRequestAttributesScope implements Scope {
    protected abstract int getScope();

    @Override // org.springframework.beans.factory.config.Scope
    public Object get(String name, ObjectFactory<?> objectFactory) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        Object scopedObject = attributes.getAttribute(name, getScope());
        if (scopedObject == null) {
            scopedObject = objectFactory.getObject();
            attributes.setAttribute(name, scopedObject, getScope());
            Object retrievedObject = attributes.getAttribute(name, getScope());
            if (retrievedObject != null) {
                scopedObject = retrievedObject;
            }
        }
        return scopedObject;
    }

    @Override // org.springframework.beans.factory.config.Scope
    @Nullable
    public Object remove(String name) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        Object scopedObject = attributes.getAttribute(name, getScope());
        if (scopedObject != null) {
            attributes.removeAttribute(name, getScope());
            return scopedObject;
        }
        return null;
    }

    @Override // org.springframework.beans.factory.config.Scope
    public void registerDestructionCallback(String name, Runnable callback) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        attributes.registerDestructionCallback(name, callback, getScope());
    }

    @Override // org.springframework.beans.factory.config.Scope
    @Nullable
    public Object resolveContextualObject(String key) {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        return attributes.resolveReference(key);
    }
}
