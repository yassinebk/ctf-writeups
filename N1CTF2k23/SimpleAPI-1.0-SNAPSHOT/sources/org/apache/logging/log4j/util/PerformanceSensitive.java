package org.apache.logging.log4j.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.CLASS)
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/PerformanceSensitive.class */
public @interface PerformanceSensitive {
    String[] value() default {""};
}
