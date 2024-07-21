package org.apache.commons.logging.impl;
@Deprecated
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-jcl-5.2.6.RELEASE.jar:org/apache/commons/logging/impl/SimpleLog.class */
public class SimpleLog extends NoOpLog {
    public SimpleLog(String name) {
        super(name);
        System.out.println(SimpleLog.class.getName() + " is deprecated and equivalent to NoOpLog in spring-jcl. Use a standard LogFactory.getLog(Class/String) call instead.");
    }
}
