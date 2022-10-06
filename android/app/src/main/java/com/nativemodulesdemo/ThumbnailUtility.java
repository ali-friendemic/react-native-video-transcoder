package com.nativemodulesdemo;

import android.graphics.Bitmap;
import com.facebook.react.bridge.ReactApplicationContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ThumbnailUtility {
    Utility utility = new Utility();

    public byte[] getByteThumbnail(String path, Integer quality, ReactApplicationContext context){
        Bitmap bmp = utility.getBitmap(path, -1L, context);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, quality, bos);
        byte[] byteArray = bos.toByteArray();
        bmp.recycle();
        return byteArray;
    }

    public String getFileThumbnail(String path, Integer quality, ReactApplicationContext context) {
        try {
            Bitmap bmp = utility.getBitmap(path, -1L, context);
            if (bmp != null) {
                File dir = context.getExternalFilesDir("nativemodulesdemo");
                if (dir != null && !dir.exists()) dir.mkdirs();
                File file = new File(dir, "thumbnail.png");

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, quality, bos);
                byte[] byteArray = bos.toByteArray();

                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray);
                fos.close();

                bmp.recycle();

                String newPath = file.getPath();

                return newPath;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
