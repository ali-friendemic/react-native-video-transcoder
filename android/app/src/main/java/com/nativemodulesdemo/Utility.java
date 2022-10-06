package com.nativemodulesdemo;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import org.json.JSONObject;
import java.io.File;

public class Utility {
    public Bitmap getBitmap(String path, Long position, ReactApplicationContext context) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(context, Uri.parse(path));
            bitmap = retriever.getFrameAtTime(position, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (IllegalArgumentException ex) {
//            result.error(channelName, "Assume this is a corrupt video file", null)
            Log.d("Hello", ex.toString());
            return null;
        } catch (RuntimeException ex) {
            Log.d("Hi", ex.toString());
//            result.error(channelName, "Assume this is a corrupt video file", null)
            return null;
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                Log.d("By", ex.toString());
//                result.error(channelName, "Ignore failures while cleaning up", null)
                return null;
            }
        }

        if (bitmap == null) return null;

        Integer width = bitmap.getWidth();
        Integer height = bitmap.getHeight();
        Integer max = Math.max(width, height);
        if (max > 512) {
            float scale = 512f / max;
            Integer w = Math.round(scale * width);
            Integer h = Math.round(scale * height);
            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
        }

        return bitmap;
    }

    public JSONObject getMediaInfoJson(ReactApplicationContext context, String path) {
        File file = new File(path);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        retriever.setDataSource(context, Uri.fromFile(file));

        String durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        String widthStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String heightStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        Long duration = java.lang.Long.parseLong(durationStr);
        Long width = java.lang.Long.parseLong(widthStr);
        Long height = java.lang.Long.parseLong(heightStr);
        Long fileSize = file.length();

        retriever.release();

        JSONObject json = new JSONObject();
        try {
            json.put("path", path);
            json.put("title", title);
            json.put("author", author);
            json.put("width", width);
            json.put("height", height);
            json.put("duration", duration);
            json.put("fileSize", fileSize);

            return json;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
