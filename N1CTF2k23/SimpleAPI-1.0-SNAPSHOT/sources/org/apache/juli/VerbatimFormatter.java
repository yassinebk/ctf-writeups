package org.apache.juli;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/juli/VerbatimFormatter.class */
public class VerbatimFormatter extends Formatter {
    @Override // java.util.logging.Formatter
    public String format(LogRecord record) {
        return record.getMessage() + System.lineSeparator();
    }
}
