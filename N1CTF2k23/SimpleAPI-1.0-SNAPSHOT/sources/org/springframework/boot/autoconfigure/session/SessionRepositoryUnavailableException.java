package org.springframework.boot.autoconfigure.session;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/spring-boot-autoconfigure-2.3.0.RELEASE.jar:org/springframework/boot/autoconfigure/session/SessionRepositoryUnavailableException.class */
public class SessionRepositoryUnavailableException extends RuntimeException {
    private final StoreType storeType;

    public SessionRepositoryUnavailableException(String message, StoreType storeType) {
        super(message);
        this.storeType = storeType;
    }

    public StoreType getStoreType() {
        return this.storeType;
    }
}
