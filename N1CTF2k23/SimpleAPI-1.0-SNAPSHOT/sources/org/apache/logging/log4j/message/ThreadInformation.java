package org.apache.logging.log4j.message;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/message/ThreadInformation.class */
public interface ThreadInformation {
    void printThreadInfo(StringBuilder sb);

    void printStack(StringBuilder sb, StackTraceElement[] stackTraceElementArr);
}
