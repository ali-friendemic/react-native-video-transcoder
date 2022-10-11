package com.nativemodulesdemo;

import android.graphics.Bitmap;
import com.facebook.react.bridge.ReactApplicationContext;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

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
                String dir = context.getExternalFilesDir("nativemodulesdemo").getAbsolutePath();
                String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
                String destPath = dir + File.separator + "IMG_" + out + path.hashCode() + ".png";
                File file = new File(destPath);

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
