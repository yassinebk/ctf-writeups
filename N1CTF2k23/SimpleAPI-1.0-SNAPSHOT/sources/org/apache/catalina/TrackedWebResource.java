package org.apache.catalina;

import java.io.Closeable;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/catalina/TrackedWebResource.class */
public interface TrackedWebResource extends Closeable {
    Exception getCreatedBy();

    String getName();
}
