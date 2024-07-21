package org.springframework.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-core-5.2.6.RELEASE.jar:org/springframework/core/io/WritableResource.class */
public interface WritableResource extends Resource {
    OutputStream getOutputStream() throws IOException;

    default boolean isWritable() {
        return true;
    }

    default WritableByteChannel writableChannel() throws IOException {
        return Channels.newChannel(getOutputStream());
    }
}
