package org.apache.logging.log4j.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/log4j-api-2.13.2.jar:org/apache/logging/log4j/util/ProcessIdUtil.class */
public class ProcessIdUtil {
    public static final String DEFAULT_PROCESSID = "-";

    public static String getProcessId() {
        try {
            Class<?> managementFactoryClass = Class.forName("java.lang.management.ManagementFactory");
            Method getRuntimeMXBean = managementFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]);
            Class<?> runtimeMXBeanClass = Class.forName("java.lang.management.RuntimeMXBean");
            Method getName = runtimeMXBeanClass.getDeclaredMethod("getName", new Class[0]);
            Object runtimeMXBean = getRuntimeMXBean.invoke(null, new Object[0]);
            String name = (String) getName.invoke(runtimeMXBean, new Object[0]);
            return name.split("@")[0];
        } catch (Exception e) {
            try {
                return new File("/proc/self").getCanonicalFile().getName();
            } catch (IOException e2) {
                return "-";
            }
        }
    }
}
