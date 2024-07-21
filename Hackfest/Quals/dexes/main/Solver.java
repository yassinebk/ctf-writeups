import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.*;
import java.security.*;
import java.util.zip.*;
import java.lang.reflect.*;

class Solver{

    public static byte[] readBytesFromFile(String filePath, int startByte, int numBytes) throws IOException {
        File file = new File(filePath);
        byte[] byteArray = new byte[numBytes];

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(startByte);
            raf.read(byteArray);
        }

        return byteArray;
    } 
    public static String print(byte[] bytes) {
    // StringBuilder sb = new StringBuilder();
    // sb.append("[ ");
    // System.out.println(bytes.length);
    // for (byte b : bytes) {
    //     sb.append(String.format("0x%02X ", b));
    // }
    // sb.append("]");
     String sb = new String(bytes, StandardCharsets.UTF_8);
    return sb.toString();
}

public String check(String str) {
        byte[] bArr = new byte[6304];
        String str2 = "";
        String str3 = "";
        try {
            bArr= readBytesFromFile("classes3.dex", 0, 6304);
            int read= 6304;
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
            System.out.println(this.print(bArr));
            // Class<?> loadClass = new InMemoryDexClassLoader(allocate, this.context.getClassLoader()).loadClass(getClass().getName());
            // String str4 = (String) loadClass.getMethod("exit", String.class).invoke(loadClass.getDeclaredConstructor(Context.class).newInstance(this.context), str);
            // str2 = str4;
            // str3 = str4;
            // // allocate.clear();
            return "meow";
        } catch (Exception e) {
            
            System.out.println(e.getMessage());

            return "xx";
        } catch (Throwable th) {
            return "wow";
        }
    }

    public static void main(String[] args){
        Solver c = new Solver();
        System.out.println(c.check("aa"));
    }

}