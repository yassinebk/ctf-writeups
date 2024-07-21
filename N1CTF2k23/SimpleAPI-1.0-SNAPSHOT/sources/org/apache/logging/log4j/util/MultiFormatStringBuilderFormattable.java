package org.apache.logging.log4j.util;

import org.apache.logging.log4j.message.MultiformatMessage;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/MultiFormatStringBuilderFormattable.class */
public interface MultiFormatStringBuilderFormattable extends MultiformatMessage, StringBuilderFormattable {
    void formatTo(String[] strArr, StringBuilder sb);
}
