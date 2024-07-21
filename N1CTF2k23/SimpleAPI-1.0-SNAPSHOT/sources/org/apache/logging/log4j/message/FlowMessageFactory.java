package org.apache.logging.log4j.message;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/message/FlowMessageFactory.class */
public interface FlowMessageFactory {
    EntryMessage newEntryMessage(Message message);

    ExitMessage newExitMessage(Object obj, Message message);

    ExitMessage newExitMessage(EntryMessage entryMessage);

    ExitMessage newExitMessage(Object obj, EntryMessage entryMessage);
}
