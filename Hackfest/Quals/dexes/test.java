packag
import java.io.*;

Class Test{
  public static InputStream openNonAsset(String paramString) {
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

	public static void main(String[] args){
    System.out.println(this.open.openNonAsset("classes3.dex"));
}
}
