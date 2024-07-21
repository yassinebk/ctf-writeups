package org.slf4j.helpers;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/slf4j-api-1.7.25.jar:org/slf4j/helpers/NOPLoggerFactory.class */
public class NOPLoggerFactory implements ILoggerFactory {
    @Override // org.slf4j.ILoggerFactory
    public Logger getLogger(String name) {
        return NOPLogger.NOP_LOGGER;
    }
}
