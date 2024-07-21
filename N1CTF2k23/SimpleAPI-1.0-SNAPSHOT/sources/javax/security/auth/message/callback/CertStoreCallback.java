package javax.security.auth.message.callback;

import java.security.cert.CertStore;
import javax.security.auth.callback.Callback;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:javax/security/auth/message/callback/CertStoreCallback.class */
public class CertStoreCallback implements Callback {
    private CertStore certStore;

    public void setCertStore(CertStore certStore) {
        this.certStore = certStore;
    }

    public CertStore getCertStore() {
        return this.certStore;
    }
}
