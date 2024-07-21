package org.slf4j.impl;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.slf4j.spi.MDCAdapter;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/logback-classic-1.2.3.jar:org/slf4j/impl/StaticMDCBinder.class */
public class StaticMDCBinder {
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    public MDCAdapter getMDCA() {
        return new LogbackMDCAdapter();
    }

    public String getMDCAdapterClassStr() {
        return LogbackMDCAdapter.class.getName();
    }
}
