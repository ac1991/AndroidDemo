package com.example.sharedscreen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sharedscreen.encoder.AudioEncodeConfig;
import com.example.sharedscreen.encoder.ScreenRecorder;
import com.example.sharedscreen.encoder.Utils;
import com.example.sharedscreen.encoder.VideoEncodeConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
     MediaProjectionManager projectManager;

     private static final int SCREEN_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ImageReader imageReader = ImageReader.newInstance(1080, 1920, PixelFormat.RGBA_8888, 3)


        initConfig();

        // java
        projectManager = (MediaProjectionManager) getSystemService(
                Context.MEDIA_PROJECTION_SERVICE);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 代表桌面获取的intent，并使用 startActivityForResult()调用分享功能
                Intent intent = projectManager.createScreenCaptureIntent();
                startActivityForResult(intent, SCREEN_REQUEST_CODE);

//                start();
            }
        });

        findViewById(R.id.stop).setOnClickListener(view -> stop());
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    MediaProjection projection;
    VirtualDisplay display;

    // startActivityForResult()的Activity复写这个接口
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resuleData) {
        if (requestCode == SCREEN_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            projection = projectManager.getMediaProjection(resultCode, resuleData);
        }


//        display = projection.createVirtualDisplay(name, width, height, dpi, flags, surface, callback, handler);
    }

    private void stop(){
        if (mScreenRecorder != null){
            mScreenRecorder.quit();
        }

        if (display != null){
//            display.setSurface(null);
            display.release();
            display = null;
        }

//        if (projection != null){
////            projection.unregisterCallback();
//            projection.stop();
//            projection = null;
//        }
    }

    private ScreenRecorder mScreenRecorder;
    private void start(){
        File dir = Utils.getSavingDir();
        if (!dir.exists() && !dir.mkdirs()) {
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.US);
        final File file = new File(dir, "Screenshots-" + format.format(new Date())
                + "-" + videoEncodeConfig.width + "x" + videoEncodeConfig.height + ".mp4");
        Log.d("@@", "Create recorder with :" + videoEncodeConfig + " \n " + audioEncodeConfig + "\n " + file.getAbsolutePath());
//        mScreenRecorder = newRecorder(file);

//        mScreenRecorder.start();
    }

    private ScreenRecorder newRecorder(File file){
//        VirtualDisplay virtualDisplay = getOrCreateVirtualDiaplay();
//        mScreenRecorder = new ScreenRecorder(videoEncodeConfig, audioEncodeConfig, virtualDisplay, file.getAbsolutePath());

        return mScreenRecorder;
    }

    private VideoEncodeConfig videoEncodeConfig = null;
    private AudioEncodeConfig audioEncodeConfig = null;
    private void initConfig(){
        int width = 1080;
        int height = 1920;
        int framerate = 120;
        int iframe = 30;
        int bitrate = 12000;
        MediaCodecInfo.CodecProfileLevel profileLevel = new MediaCodecInfo.CodecProfileLevel();
        profileLevel.profile = MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline;
        profileLevel.level = MediaCodecInfo.CodecProfileLevel.AVCLevel1;

        videoEncodeConfig = new VideoEncodeConfig(width, height, bitrate, framerate, iframe,"OMX.google.h264.encoder",MediaFormat.MIMETYPE_VIDEO_AVC, profileLevel);


        int audioBitrate = 80;
        int samplerate = 44100;
        int channels = 1;
        String audioCodeName = "OMX.google.aac.encoder";
        int profile = 1;//CodecProfileLevel.AACObjectMain
        audioEncodeConfig = new AudioEncodeConfig(audioCodeName, MediaFormat.MIMETYPE_AUDIO_AAC, audioBitrate, samplerate, channels, profile);
    }


}
