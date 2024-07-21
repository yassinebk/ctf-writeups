package org.yaml.snakeyaml.util;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/snakeyaml-1.26.jar:org/yaml/snakeyaml/util/PlatformFeatureDetector.class */
public class PlatformFeatureDetector {
    private Boolean isRunningOnAndroid = null;

    public boolean isRunningOnAndroid() {
        if (this.isRunningOnAndroid == null) {
            String name = System.getProperty("java.runtime.name");
            this.isRunningOnAndroid = Boolean.valueOf(name != null && name.startsWith("Android Runtime"));
        }
        return this.isRunningOnAndroid.booleanValue();
    }
}
