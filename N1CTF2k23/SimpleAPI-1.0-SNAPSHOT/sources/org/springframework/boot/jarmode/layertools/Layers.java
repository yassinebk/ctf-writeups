package org.springframework.boot.jarmode.layertools;

import java.util.Iterator;
import java.util.zip.ZipEntry;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-jarmode-layertools-2.6.1.jar:org/springframework/boot/jarmode/layertools/Layers.class */
interface Layers extends Iterable<String> {
    @Override // java.lang.Iterable
    Iterator<String> iterator();

    String getLayer(ZipEntry entry);

    static Layers get(Context context) {
        IndexedLayers indexedLayers = IndexedLayers.get(context);
        if (indexedLayers == null) {
            throw new IllegalStateException("Failed to load layers.idx which is required by layertools");
        }
        return indexedLayers;
    }
}
