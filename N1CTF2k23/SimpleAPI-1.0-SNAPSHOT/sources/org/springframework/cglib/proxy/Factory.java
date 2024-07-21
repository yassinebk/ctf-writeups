package org.springframework.cglib.proxy;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/cglib/proxy/Factory.class */
public interface Factory {
    Object newInstance(Callback callback);

    Object newInstance(Callback[] callbackArr);

    Object newInstance(Class[] clsArr, Object[] objArr, Callback[] callbackArr);

    Callback getCallback(int i);

    void setCallback(int i, Callback callback);

    void setCallbacks(Callback[] callbackArr);

    Callback[] getCallbacks();
}
