package org.apache.logging.log4j.message;

import java.util.Map;
import org.apache.logging.log4j.util.PerformanceSensitive;
@AsynchronouslyFormattable
@PerformanceSensitive({"allocation"})
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/message/StringMapMessage.class */
public class StringMapMessage extends MapMessage<StringMapMessage, String> {
    private static final long serialVersionUID = 1;

    public StringMapMessage() {
    }

    public StringMapMessage(int initialCapacity) {
        super(initialCapacity);
    }

    public StringMapMessage(Map<String, String> map) {
        super(map);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.apache.logging.log4j.message.MapMessage
    public StringMapMessage newInstance(Map<String, String> map) {
        return new StringMapMessage(map);
    }
}
