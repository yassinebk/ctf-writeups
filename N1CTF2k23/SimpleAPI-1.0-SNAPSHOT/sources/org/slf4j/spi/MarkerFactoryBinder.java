package org.slf4j.spi;

import org.slf4j.IMarkerFactory;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/slf4j-api-1.7.25.jar:org/slf4j/spi/MarkerFactoryBinder.class */
public interface MarkerFactoryBinder {
    IMarkerFactory getMarkerFactory();

    String getMarkerFactoryClassStr();
}
