package org.apache.naming.factory;

import java.util.HashSet;
import java.util.Set;
import javax.naming.spi.ObjectFactory;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.naming.StringManager;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/naming/factory/LookupFactory.class */
public class LookupFactory implements ObjectFactory {
    private static final Log log = LogFactory.getLog(LookupFactory.class);
    private static final StringManager sm = StringManager.getManager(LookupFactory.class);
    private static final ThreadLocal<Set<String>> names = new ThreadLocal<Set<String>>() { // from class: org.apache.naming.factory.LookupFactory.1
        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        public Set<String> initialValue() {
            return new HashSet();
        }
    };

    /* JADX WARN: Removed duplicated region for block: B:27:0x00f2 A[Catch: Throwable -> 0x010a, all -> 0x01fa, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x01fa, blocks: (B:9:0x0033, B:11:0x0046, B:12:0x0072, B:13:0x0073, B:15:0x0081, B:17:0x009b, B:27:0x00f2, B:21:0x00c5, B:19:0x00a9, B:20:0x00c4, B:23:0x00d1, B:24:0x00ec, B:37:0x013b, B:43:0x016f, B:45:0x017e, B:47:0x018b, B:49:0x01d4, B:52:0x01e5, B:53:0x01e6, B:40:0x0151, B:41:0x0160, B:42:0x0161), top: B:65:0x0033, inners: #1, #2 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object getObjectInstance(java.lang.Object r8, javax.naming.Name r9, javax.naming.Context r10, java.util.Hashtable<?, ?> r11) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 531
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.naming.factory.LookupFactory.getObjectInstance(java.lang.Object, javax.naming.Name, javax.naming.Context, java.util.Hashtable):java.lang.Object");
    }
}
