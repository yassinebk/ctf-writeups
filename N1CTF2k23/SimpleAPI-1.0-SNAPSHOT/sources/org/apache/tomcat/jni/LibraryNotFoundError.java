package org.apache.tomcat.jni;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/jni/LibraryNotFoundError.class */
public class LibraryNotFoundError extends UnsatisfiedLinkError {
    private static final long serialVersionUID = 1;
    private final String libraryNames;

    public LibraryNotFoundError(String libraryNames, String errors) {
        super(errors);
        this.libraryNames = libraryNames;
    }

    public String getLibraryNames() {
        return this.libraryNames;
    }
}
