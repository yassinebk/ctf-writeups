package org.springframework.boot.loader.jar;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:org/springframework/boot/loader/jar/FileHeader.class */
interface FileHeader {
    boolean hasName(CharSequence name, char suffix);

    long getLocalHeaderOffset();

    long getCompressedSize();

    long getSize();

    int getMethod();
}
