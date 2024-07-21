package org.apache.coyote;

import javax.servlet.http.HttpUpgradeHandler;
import org.apache.tomcat.ContextBind;
import org.apache.tomcat.InstanceManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/coyote/UpgradeToken.class */
public final class UpgradeToken {
    private final ContextBind contextBind;
    private final HttpUpgradeHandler httpUpgradeHandler;
    private final InstanceManager instanceManager;

    public UpgradeToken(HttpUpgradeHandler httpUpgradeHandler, ContextBind contextBind, InstanceManager instanceManager) {
        this.contextBind = contextBind;
        this.httpUpgradeHandler = httpUpgradeHandler;
        this.instanceManager = instanceManager;
    }

    public final ContextBind getContextBind() {
        return this.contextBind;
    }

    public final HttpUpgradeHandler getHttpUpgradeHandler() {
        return this.httpUpgradeHandler;
    }

    public final InstanceManager getInstanceManager() {
        return this.instanceManager;
    }
}
