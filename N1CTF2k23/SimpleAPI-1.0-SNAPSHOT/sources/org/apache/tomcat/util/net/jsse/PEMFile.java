package org.apache.tomcat.util.net.jsse;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.tomcat.util.buf.Asn1Parser;
import org.apache.tomcat.util.buf.Asn1Writer;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.apache.tomcat.util.res.StringManager;
import org.springframework.asm.Opcodes;
/* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/jsse/PEMFile.class */
public class PEMFile {
    private static final StringManager sm = StringManager.getManager(PEMFile.class);
    private static final byte[] OID_EC_PUBLIC_KEY = {6, 7, 42, -122, 72, -50, 61, 2, 1};
    private String filename;
    private List<X509Certificate> certificates;
    private PrivateKey privateKey;

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/jsse/PEMFile$Format.class */
    private enum Format {
        PKCS1,
        PKCS8,
        RFC5915
    }

    public List<X509Certificate> getCertificates() {
        return this.certificates;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PEMFile(String filename) throws IOException, GeneralSecurityException {
        this(filename, null);
    }

    public PEMFile(String filename, String password) throws IOException, GeneralSecurityException {
        this(filename, password, null);
    }

    public PEMFile(String filename, String password, String keyAlgorithm) throws IOException, GeneralSecurityException {
        this.certificates = new ArrayList();
        this.filename = filename;
        List<Part> parts = new ArrayList<>();
        InputStream inputStream = ConfigFileLoader.getSource().getResource(filename).getInputStream();
        Throwable th = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.US_ASCII));
            Part part = null;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else if (line.startsWith(Part.BEGIN_BOUNDARY)) {
                    part = new Part();
                    part.type = line.substring(Part.BEGIN_BOUNDARY.length(), line.length() - 5).trim();
                } else if (line.startsWith(Part.END_BOUNDARY)) {
                    parts.add(part);
                    part = null;
                } else if (part != null && !line.contains(":") && !line.startsWith(" ")) {
                    Part part2 = part;
                    part2.content += line;
                }
            }
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
            for (Part part3 : parts) {
                String str = part3.type;
                boolean z = true;
                switch (str.hashCode()) {
                    case -2076506627:
                        if (str.equals("X509 CERTIFICATE")) {
                            z = true;
                            break;
                        }
                        break;
                    case -283732602:
                        if (str.equals("ENCRYPTED PRIVATE KEY")) {
                            z = true;
                            break;
                        }
                        break;
                    case -189606537:
                        if (str.equals("CERTIFICATE")) {
                            z = true;
                            break;
                        }
                        break;
                    case -170985982:
                        if (str.equals("PRIVATE KEY")) {
                            z = false;
                            break;
                        }
                        break;
                    case 957721984:
                        if (str.equals("EC PRIVATE KEY")) {
                            z = true;
                            break;
                        }
                        break;
                    case 2121838594:
                        if (str.equals("RSA PRIVATE KEY")) {
                            z = true;
                            break;
                        }
                        break;
                }
                switch (z) {
                    case false:
                        this.privateKey = part3.toPrivateKey(null, keyAlgorithm, Format.PKCS8);
                        break;
                    case true:
                        this.privateKey = part3.toPrivateKey(null, "EC", Format.RFC5915);
                        break;
                    case true:
                        this.privateKey = part3.toPrivateKey(password, keyAlgorithm, Format.PKCS8);
                        break;
                    case true:
                        this.privateKey = part3.toPrivateKey(null, keyAlgorithm, Format.PKCS1);
                        break;
                    case true:
                    case true:
                        this.certificates.add(part3.toCertificate());
                        break;
                }
            }
        } finally {
        }
    }

    /* loaded from: SimpleAPI-1.0-SNAPSHOT.jar:BOOT-INF/lib/tomcat-embed-core-9.0.35.jar:org/apache/tomcat/util/net/jsse/PEMFile$Part.class */
    private class Part {
        public static final String BEGIN_BOUNDARY = "-----BEGIN ";
        public static final String END_BOUNDARY = "-----END ";
        public String type;
        public String content;

        private Part() {
            this.content = "";
        }

        private byte[] decode() {
            return Base64.decodeBase64(this.content);
        }

        public X509Certificate toCertificate() throws CertificateException {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(decode()));
        }

        public PrivateKey toPrivateKey(String password, String keyAlgorithm, Format format) throws GeneralSecurityException, IOException {
            String[] strArr;
            KeySpec keySpec = null;
            if (password == null) {
                switch (format) {
                    case PKCS1:
                        keySpec = parsePKCS1(decode());
                        break;
                    case PKCS8:
                        keySpec = new PKCS8EncodedKeySpec(decode());
                        break;
                    case RFC5915:
                        keySpec = new PKCS8EncodedKeySpec(rfc5915ToPkcs8(decode()));
                        break;
                }
            } else {
                EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(decode());
                SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
                SecretKey secretKey = secretKeyFactory.generateSecret(new PBEKeySpec(password.toCharArray()));
                Cipher cipher = Cipher.getInstance(privateKeyInfo.getAlgName());
                cipher.init(2, secretKey, privateKeyInfo.getAlgParameters());
                keySpec = privateKeyInfo.getKeySpec(cipher);
            }
            InvalidKeyException exception = new InvalidKeyException(PEMFile.sm.getString("jsse.pemParseError", PEMFile.this.filename));
            if (keyAlgorithm == null) {
                for (String algorithm : new String[]{"RSA", "DSA", "EC"}) {
                    try {
                        return KeyFactory.getInstance(algorithm).generatePrivate(keySpec);
                    } catch (InvalidKeySpecException e) {
                        exception.addSuppressed(e);
                    }
                }
            } else {
                try {
                    return KeyFactory.getInstance(keyAlgorithm).generatePrivate(keySpec);
                } catch (InvalidKeySpecException e2) {
                    exception.addSuppressed(e2);
                }
            }
            throw exception;
        }

        /* JADX WARN: Type inference failed for: r0v30, types: [byte[], byte[][]] */
        /* JADX WARN: Type inference failed for: r3v3, types: [byte[], byte[][]] */
        /* JADX WARN: Type inference failed for: r3v6, types: [byte[], byte[][]] */
        private byte[] rfc5915ToPkcs8(byte[] source) {
            Asn1Parser p = new Asn1Parser(source);
            p.parseTag(48);
            p.parseFullLength();
            BigInteger version = p.parseInt();
            if (version.intValue() != 1) {
                throw new IllegalArgumentException(PEMFile.sm.getString("pemFile.notValidRFC5915"));
            }
            p.parseTag(4);
            int privateKeyLen = p.parseLength();
            byte[] privateKey = new byte[privateKeyLen];
            p.parseBytes(privateKey);
            p.parseTag(160);
            int oidLen = p.parseLength();
            byte[] oid = new byte[oidLen];
            p.parseBytes(oid);
            if (oid[0] != 6) {
                throw new IllegalArgumentException(PEMFile.sm.getString("pemFile.notValidRFC5915"));
            }
            p.parseTag(Opcodes.IF_ICMPLT);
            int publicKeyLen = p.parseLength();
            byte[] publicKey = new byte[publicKeyLen];
            p.parseBytes(publicKey);
            if (publicKey[0] != 3) {
                throw new IllegalArgumentException(PEMFile.sm.getString("pemFile.notValidRFC5915"));
            }
            return Asn1Writer.writeSequence(new byte[]{Asn1Writer.writeInteger(0), Asn1Writer.writeSequence(new byte[]{PEMFile.OID_EC_PUBLIC_KEY, oid}), Asn1Writer.writeOctetString(Asn1Writer.writeSequence(new byte[]{Asn1Writer.writeInteger(1), Asn1Writer.writeOctetString(privateKey), Asn1Writer.writeTag((byte) -95, publicKey)}))});
        }

        private RSAPrivateCrtKeySpec parsePKCS1(byte[] source) {
            Asn1Parser p = new Asn1Parser(source);
            p.parseTag(48);
            p.parseFullLength();
            BigInteger version = p.parseInt();
            if (version.intValue() == 1) {
                throw new IllegalArgumentException(PEMFile.sm.getString("pemFile.noMultiPrimes"));
            }
            return new RSAPrivateCrtKeySpec(p.parseInt(), p.parseInt(), p.parseInt(), p.parseInt(), p.parseInt(), p.parseInt(), p.parseInt(), p.parseInt());
        }
    }
}
