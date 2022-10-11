package com.nativemodulesdemo;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.otaliastudios.transcoder.Transcoder;
import com.otaliastudios.transcoder.TranscoderListener;
import com.otaliastudios.transcoder.internal.utils.Logger;
import com.otaliastudios.transcoder.source.DataSource;
import com.otaliastudios.transcoder.source.UriDataSource;
import com.otaliastudios.transcoder.strategy.DefaultAudioStrategy;
import com.otaliastudios.transcoder.strategy.DefaultVideoStrategy;
import com.otaliastudios.transcoder.strategy.RemoveTrackStrategy;
import com.otaliastudios.transcoder.strategy.TrackStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Future;

public class TranscoderModule extends ReactContextBaseJavaModule {
    private Future<Void> transcodeFuture = null;
    private ReactApplicationContext _context;
    private String TAG = "TranscoderModule";
    private Object LOG = new Logger(TAG);
    private ThumbnailUtility thumbnailUtility = new ThumbnailUtility();

    TranscoderModule(ReactApplicationContext context) {
        super(context);
        _context = context;
    }

    @ReactMethod
    public void getByteThumbnail(String name, String path, Integer quality, Callback result) {
        byte[] thumbnailByteArray = thumbnailUtility.getByteThumbnail(path, quality, _context);
        result.invoke(thumbnailByteArray);
    }

    @ReactMethod
    public void getFileThumbnail(String name, String path, Integer quality, Callback result) {
        String thumbnail = thumbnailUtility.getFileThumbnail(path, quality, _context);
        result.invoke(thumbnail);
    }

    @ReactMethod
    public void cancelCompression(String name, Callback result) {
        transcodeFuture.cancel(true);
        result.invoke(false);
    }

    @ReactMethod
    public void compressVideo(
            String name,
            String path,
            Integer quality,
            Boolean deleteOrigin,
            Boolean includeAudio,
            Integer frameRate,
            Callback result
    ) {
        Integer frame = 30;
        if (frameRate != null) frame = frameRate;
        String tempDir = _context.getExternalFilesDir("nativemodulesdemo").getAbsolutePath();
        String out = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
        String destPath = tempDir + File.separator + "VID_" + out + path.hashCode() + ".mp4";

        TrackStrategy videoTrackStrategy = DefaultVideoStrategy.atMost(340).build();
        TrackStrategy audioTrackStrategy = null;

        switch (quality) {
            case 0:
                videoTrackStrategy = DefaultVideoStrategy.atMost(720).build();
                break;
            case 1:
                videoTrackStrategy = DefaultVideoStrategy.atMost(360).build();
                break;
            case 2:
                videoTrackStrategy = DefaultVideoStrategy.atMost(640).build();
                break;
            case 3:
                videoTrackStrategy = new DefaultVideoStrategy.Builder()
                        .keyFrameInterval(3f)
                        .bitRate(1280 * 720 * 4L)
                        .frameRate(frame)
                        .build();
                break;
            case 4:
                videoTrackStrategy = DefaultVideoStrategy.atMost(480, 640).build();
                break;
            case 5:
                videoTrackStrategy = DefaultVideoStrategy.atMost(540, 960).build();
                break;
            case 6:
                videoTrackStrategy = DefaultVideoStrategy.atMost(720, 1280).build();
                break;
            case 7:
                videoTrackStrategy = DefaultVideoStrategy.atMost(1080, 1920).build();
                break;
            default:
                videoTrackStrategy = DefaultVideoStrategy.atMost(720).build();
        }

        if (includeAudio) {
            Integer sampleRate = DefaultAudioStrategy.SAMPLE_RATE_AS_INPUT;
            Integer channels = DefaultAudioStrategy.CHANNELS_AS_INPUT;

            audioTrackStrategy = DefaultAudioStrategy.builder()
                    .channels(channels)
                    .sampleRate(sampleRate)
                    .build();
        } else {
            new RemoveTrackStrategy();
        }

        DataSource dataSource = new UriDataSource(_context, Uri.parse(path));

        transcodeFuture = Transcoder.into(destPath).addDataSource(dataSource)
                .setAudioTrackStrategy(audioTrackStrategy)
                .setVideoTrackStrategy(videoTrackStrategy)
                .setListener(new TranscoderListener() {
                    public void onTranscodeProgress(double progress) {
                        Log.d("Hello", String.valueOf(progress));
//                        progressCall.invoke("updateProgress", progress * 100.00);
                    }

                    public void onTranscodeCompleted(int successCode) {
//                        progressCall.invoke("updateProgress", 100.00);
                        JSONObject json = new Utility().getMediaInfoJson(_context, destPath);
                        try {
                            json.put("isCancel", false);
                        } catch (JSONException e) {
                            result.invoke(null);
                            e.printStackTrace();
                        }
                        result.invoke(json.toString());
                        if (deleteOrigin) {
                            new File(path).delete();
                        }
                    }

                    public void onTranscodeCanceled() {
                        result.invoke(null);
                    }

                    public void onTranscodeFailed(@NonNull Throwable exception) {
                        Log.d("Hellooo", exception.toString());
                        result.invoke(null);
                    }
                }).transcode();
    }

    @Override
    public String getName() {
        return "TranscoderModule";
    }
}

