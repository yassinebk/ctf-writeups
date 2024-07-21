package com.hackfest.carkeypro;

import android.content.Context;
import android.util.Log;
import dalvik.system.InMemoryDexClassLoader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.Adler32;

/* loaded from: classes3-dex2jar.jar:com/hackfest/carkeypro/CarKeyPro.class */
public class CarKeyPro {
    private Context context;

    public CarKeyPro(Context context) {
        this.context = context;
    }

    public static String b2h(byte[] bArr, int i, int i2) {
        char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] cArr2 = new char[i2 * 2];
        while (i < i2) {
            int i3 = bArr[i] & 255;
            cArr2[i * 2] = cArr[i3 >>> 4];
            cArr2[(i * 2) + 1] = cArr[i3 & 15];
            i++;
        }
        return new String(cArr2);
    }

    public String check(String str) {
        byte[] bArr = new byte[6304];
        String str2 = "";
        String str3 = "";
        try {
            int read = openNonAsset("classes3.dex").read(bArr, 0, 6304);
            int i = 6085 + 1;
            bArr[6085] = (byte) 1;
            int i2 = i + 1;
            bArr[i] = (byte) 1;
            int i3 = i2 + 1;
            bArr[i2] = (byte) (-16);
            bArr[i3] = (byte) 20;
            bArr[i3 + 1] = (byte) 1;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(bArr, 32, read - 32);
            messageDigest.digest(bArr, 12, 20);
            Adler32 adler32 = new Adler32();
            adler32.reset();
            adler32.update(bArr, 12, read - 12);
            int value = (int) adler32.getValue();
            bArr[8] = (byte) value;
            bArr[9] = (byte) (value >> 8);
            bArr[10] = (byte) (value >> 16);
            bArr[11] = (byte) (value >> 24);
            ByteBuffer allocate = ByteBuffer.allocate(bArr.length);
            allocate.put(bArr);
            allocate.position(0);
            Class<?> loadClass = new InMemoryDexClassLoader(allocate, this.context.getClassLoader()).loadClass(getClass().getName());
            String str4 = (String) loadClass.getMethod("exit", String.class).invoke(loadClass.getDeclaredConstructor(Context.class).newInstance(this.context), str);
            str2 = str4;
            str3 = str4;
            allocate.clear();
            return str4;
        } catch (Exception e) {
            return str3;
        } catch (Throwable th) {
            return str2;
        }
    }

    public void exit(int i) {
        Log.i("Hackfest", "Exiting");
    }

    public InputStream openNonAsset(String str) {
        try {
            return (InputStream) Class.forName("android.content.res.AssetManager").getMethod("openNonAsset", String.class).invoke(this.context.getAssets(), str);
        } catch (Exception e) {
            while (true) {
                e.printStackTrace();
            }
        }
    }

    public String status() {
        return "hackfest{not_that_easy_to_be_the_flag}";
    }
}