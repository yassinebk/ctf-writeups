package org.springframework.boot.web.embedded.tomcat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.servlet.ServletException;
import org.apache.catalina.Container;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Manager;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.session.ManagerBase;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.util.ClassUtils;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-2.3.0.RELEASE.jar:org/springframework/boot/web/embedded/tomcat/TomcatEmbeddedContext.class */
public class TomcatEmbeddedContext extends StandardContext {
    private TomcatStarter starter;

    @Override // org.apache.catalina.core.StandardContext
    public boolean loadOnStartup(Container[] children) {
        return true;
    }

    @Override // org.apache.catalina.core.StandardContext, org.apache.catalina.Context
    public void setManager(Manager manager) {
        if (manager instanceof ManagerBase) {
            manager.setSessionIdGenerator(new LazySessionIdGenerator());
        }
        super.setManager(manager);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deferredLoadOnStartup() throws LifecycleException {
        doWithThreadContextClassLoader(getLoader().getClassLoader(), () -> {
            getLoadOnStartupWrappers(findChildren()).forEach(this::load);
        });
    }

    private Stream<Wrapper> getLoadOnStartupWrappers(Container[] children) {
        Map<Integer, List<Wrapper>> grouped = new TreeMap<>();
        for (Container child : children) {
            Wrapper wrapper = (Wrapper) child;
            int order = wrapper.getLoadOnStartup();
            if (order >= 0) {
                grouped.computeIfAbsent(Integer.valueOf(order), o -> {
                    return new ArrayList();
                }).add(wrapper);
            }
        }
        return grouped.values().stream().flatMap((v0) -> {
            return v0.stream();
        });
    }

    private void load(Wrapper wrapper) {
        try {
            wrapper.load();
        } catch (ServletException ex) {
            String message = sm.getString("standardContext.loadOnStartup.loadException", getName(), wrapper.getName());
            if (getComputedFailCtxIfServletStartFails()) {
                throw new WebServerException(message, ex);
            }
            getLogger().error(message, StandardWrapper.getRootCause(ex));
        }
    }

    private void doWithThreadContextClassLoader(ClassLoader classLoader, Runnable code) {
        ClassLoader existingLoader = classLoader != null ? ClassUtils.overrideThreadContextClassLoader(classLoader) : null;
        try {
            code.run();
            if (existingLoader != null) {
                ClassUtils.overrideThreadContextClassLoader(existingLoader);
            }
        } catch (Throwable th) {
            if (existingLoader != null) {
                ClassUtils.overrideThreadContextClassLoader(existingLoader);
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setStarter(TomcatStarter starter) {
        this.starter = starter;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TomcatStarter getStarter() {
        return this.starter;
    }
}
