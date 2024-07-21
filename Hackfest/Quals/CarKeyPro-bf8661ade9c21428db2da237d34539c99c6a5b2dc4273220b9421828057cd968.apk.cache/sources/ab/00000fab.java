package com.hackfest.carkeypro;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import dalvik.system.InMemoryDexClassLoader;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.zip.Adler32;
import kotlin.UByte;

/* loaded from: classes3.dex */
public class CarKeyPro {
    private Context context;

    public static String b2h(byte[] bytes, int offset, int length) {
        char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[length * 2];
        for (int j = offset; j < length; j++) {
            int v = bytes[j] & UByte.MAX_VALUE;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public CarKeyPro(Context ctx) {
        this.context = ctx;
    }

    public String status() {
        return "hackfest{not_that_easy_to_be_the_flag}";
    }

    public InputStream openNonAsset(String paramString) {
        try {
            Class localClass = Class.forName("android.content.res.AssetManager");
            Class[] arrayOfClass = {String.class};
            Method localMethod = localClass.getMethod("openNonAsset", arrayOfClass);
            AssetManager localAssetManager = this.context.getAssets();
            Object[] arrayOfObject = {paramString};
            InputStream localInputStream = (InputStream) localMethod.invoke(localAssetManager, arrayOfObject);
            return localInputStream;
        } catch (Exception e) {
            while (true) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: check  reason: collision with other method in class */
    public String m44check(String status) {
        Log.i("Hackfest", "Exiting");
        return;
    }

    public void exit(int status) {
        Log.i("Hackfest", "Exiting");
    }

    /* renamed from: check */
    public String m44check(String arg0) {
        String status = "";
        byte[] d = new byte[6304];
        InputStream localInputStream = openNonAsset("classes3.dex");
        try {
            int dl = localInputStream.read(d, 0, 6304);
            int pi = 6085 + 1;
            d[6085] = 1;
            int pi2 = pi + 1;
            d[pi] = 1;
            int pi3 = pi2 + 1;
            d[pi2] = -16;
            int pi4 = pi3 + 1;
            d[pi3] = 20;
            int i = pi4 + 1;
            d[pi4] = 1;
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(d, 32, dl - 32);
            digest.digest(d, 12, 20);
            Adler32 cs = new Adler32();
            cs.reset();
            cs.update(d, 12, dl - 12);
            int sum = (int) cs.getValue();
            d[8] = (byte) sum;
            d[9] = (byte) (sum >> 8);
            d[10] = (byte) (sum >> 16);
            d[11] = (byte) (sum >> 24);
            ByteBuffer bb = ByteBuffer.allocate(d.length);
            bb.put(d);
            bb.position(0);
            ClassLoader loader = new InMemoryDexClassLoader(bb, this.context.getClassLoader());
            Class thisClass = loader.loadClass(getClass().getName());
            Method method = thisClass.getMethod("exit", String.class);
            status = (String) method.invoke(thisClass.getDeclaredConstructor(Context.class).newInstance(this.context), arg0);
            bb.clear();
            return status;
        } catch (Exception e) {
            return status;
        } catch (Throwable th) {
            return status;
        }
    }
}