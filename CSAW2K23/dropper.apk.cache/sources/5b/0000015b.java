package com.example.dropper;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import d.l;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class MainActivity extends l {
    @Override // androidx.fragment.app.v, androidx.activity.h, t.d, android.app.Activity
    public final void onCreate(Bundle bundle) {
        Class<?> cls;
        Object obj;
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        try {
            byte[] decode = Base64.decode("ZGV4CjAzNQAWORryq3+hLJ+yXt9y3L5lCBAqyp3c8Q6UBwAAcAAAAHhWNBIAAAAAAAAAANAGAAAoAAAAcAAAABMAAAAQAQAACwAAAFwBAAABAAAA4AEAABAAAADoAQAAAQAAAGgCAAAMBQAAiAIAAPYDAAD4AwAAAAQAAA4EAAARBAAAFAQAABoEAAAeBAAAPQQAAFkEAABzBAAAigQAAKEEAAC+BAAA0AQAAOcEAAD7BAAADwUAAC0FAAA9BQAAVwUAAHMFAACHBQAAigUAAI4FAACSBQAAlgUAAJ8FAACnBQAAswUAAL8FAADIBQAA2AUAAPIFAAD+BQAAAwYAABMGAAAkBgAALgYAADUGAAADAAAABwAAAAgAAAAJAAAACgAAAAsAAAAMAAAADQAAAA4AAAAPAAAAEAAAABEAAAASAAAAEwAAABQAAAAVAAAAFgAAABgAAAAZAAAABAAAAAUAAAAAAAAABAAAAAoAAAAAAAAABQAAAAoAAADMAwAABAAAAA0AAAAAAAAABAAAAA4AAAAAAAAAFgAAABAAAAAAAAAAFwAAABAAAADYAwAAFwAAABAAAADgAwAAFwAAABAAAADoAwAAFwAAABAAAADwAwAABgAAABEAAADoAwAAAQARACEAAAABAAUAAQAAAAEAAQAeAAAAAQACACIAAAADAAcAAQAAAAMAAQAlAAAABgAGAAEAAAAIAAUAJAAAAAkABQABAAAACgAJAAEAAAALAAUAGgAAAAsABQAcAAAACwAAAB8AAAAMAAgAAQAAAAwAAwAjAAAADgAKABsAAAAPAAQAHQAAAAEAAAABAAAACQAAAAAAAAACAAAAuAYAAJYGAAAAAAAABAAAAAMAAgCoAwAASwAAAAAAIgAMABoBIABwIAwAEABuEA0AAAAMAB8ACwBuEAkAAAAiAQMAIgIGAG4QCwAAAAwDcCAFADIAcCADACEAbhAEAAEADAFuEAoAAAAoDA0BKB8NAW4QBgABAG4QCgAAABoBAABxAA8AAAAMAG4gDgAQAAwAaQAAABMAEwETATIBEwIqAHEwAgAQAgwAEQBuEAoAAAAnAQAADgAAABUAAQAqAAAAAwAFAAJ/CCknACcABwADAAIAAAC/AwAAGQAAALFFI1ASABIBNVEPAGICAACQAwQBSAICA7dijiJQAgAB2AEBASjyIgQKAHAgCAAEABEEAAABAAEAAQAAAKQDAAAEAAAAcBAHAAAADgAKAA4AEQAOHnhqPOFOPBwpHj08LqamAnkdPAAnAwAAAA48PKM+AAAAAwAAAAAAAAAAAAAAAQAAAAUAAAABAAAABwAAAAEAAAAKAAAAAQAAABIAAAAGPGluaXQ+AAxEcm9wcGVkLmphdmEAAUkAAUwABExJSUkAAkxMAB1MY29tL2V4YW1wbGUvZHJvcHBlZC9Ecm9wcGVkOwAaTGRhbHZpay9hbm5vdGF0aW9uL1Rocm93czsAGExqYXZhL2lvL0J1ZmZlcmVkUmVhZGVyOwAVTGphdmEvaW8vSU9FeGNlcHRpb247ABVMamF2YS9pby9JbnB1dFN0cmVhbTsAG0xqYXZhL2lvL0lucHV0U3RyZWFtUmVhZGVyOwAQTGphdmEvaW8vUmVhZGVyOwAVTGphdmEvbGFuZy9FeGNlcHRpb247ABJMamF2YS9sYW5nL09iamVjdDsAEkxqYXZhL2xhbmcvU3RyaW5nOwAcTGphdmEvbmV0L0h0dHBVUkxDb25uZWN0aW9uOwAOTGphdmEvbmV0L1VSTDsAGExqYXZhL25ldC9VUkxDb25uZWN0aW9uOwAaTGphdmEvdXRpbC9CYXNlNjQkRGVjb2RlcjsAEkxqYXZhL3V0aWwvQmFzZTY0OwABVgACVkwAAltCAAJbQwAHY29ubmVjdAAGZGVjb2RlAApkaXNjb25uZWN0AApnZXREZWNvZGVyAAdnZXRGbGFnAA5nZXRJbnB1dFN0cmVhbQAYaHR0cDovL21pc2MuY3Nhdy5pbzozMDAzAApub3RUaGVGbGFnAANvYmYADm9wZW5Db25uZWN0aW9uAA9wcmludFN0YWNrVHJhY2UACHJlYWRMaW5lAAV2YWx1ZQBXfn5EOHsiY29tcGlsYXRpb24tbW9kZSI6ImRlYnVnIiwiaGFzLWNoZWNrc3VtcyI6ZmFsc2UsIm1pbi1hcGkiOjEsInZlcnNpb24iOiIyLjEuNy1yMSJ9AAICASYcARgEAQADAAAIAIGABIwHAQmIBQEJyAYAAAAAAAABAAAAjgYAAKwGAAAAAAAAAQAAAAAAAAABAAAAsAYAABAAAAAAAAAAAQAAAAAAAAABAAAAKAAAAHAAAAACAAAAEwAAABABAAADAAAACwAAAFwBAAAEAAAAAQAAAOABAAAFAAAAEAAAAOgBAAAGAAAAAQAAAGgCAAABIAAAAwAAAIgCAAADIAAAAwAAAKQDAAABEAAABQAAAMwDAAACIAAAKAAAAPYDAAAEIAAAAQAAAI4GAAAAIAAAAQAAAJYGAAADEAAAAgAAAKwGAAAGIAAAAQAAALgGAAAAEAAAAQAAANAGAAA=", 0);
            FileOutputStream openFileOutput = openFileOutput("dropped.dex", 0);
            openFileOutput.write(decode);
            openFileOutput.flush();
            openFileOutput.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        File file = new File(getFilesDir(), "dropped.dex");
        Method method = null;
        try {
            cls = new DexClassLoader(file.getAbsolutePath(), getCacheDir().getAbsolutePath(), null, getClassLoader()).loadClass("com.example.dropped.Dropped");
        } catch (ClassNotFoundException e3) {
            e3.printStackTrace();
            cls = null;
        }
        try {
            obj = cls.newInstance();
        } catch (IllegalAccessException | InstantiationException e4) {
            e4.printStackTrace();
            obj = null;
        }
        try {
            method = cls.getMethod("getFlag", null);
        } catch (NoSuchMethodException e5) {
            e5.printStackTrace();
        }
        try {
            method.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e6) {
            e6.printStackTrace();
        }
        file.delete();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("test");
        builder.show();
        finish();
        System.exit(0);
    }
}