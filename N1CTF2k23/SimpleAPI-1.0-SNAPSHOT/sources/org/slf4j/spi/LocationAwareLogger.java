package org.slf4j.spi;

import org.slf4j.Logger;
import org.slf4j.Marker;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/slf4j-api-1.7.25.jar:org/slf4j/spi/LocationAwareLogger.class */
public interface LocationAwareLogger extends Logger {
    public static final int TRACE_INT = 0;
    public static final int DEBUG_INT = 10;
    public static final int INFO_INT = 20;
    public static final int WARN_INT = 30;
    public static final int ERROR_INT = 40;

    void log(Marker marker, String str, int i, String str2, Object[] objArr, Throwable th);
}
