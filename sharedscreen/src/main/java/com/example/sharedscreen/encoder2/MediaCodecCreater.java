package com.example.sharedscreen.encoder2;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.IOException;

public class MediaCodecCreater extends HandlerThread {
    private MediaFormat mediaFormat;
    private int fps = 60;
    //子线程的handler
    private Handler mediaCodecHandler = new Handler(this.getLooper(), new MediaCodecCallback());

    //UI线程Handler
    private Handler mUIHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    public MediaCodecCreater(String name) {
        super(name);

    }

    private void initFormat(){
        mediaFormat = new MediaFormat();

        //使用h264编码
        mediaFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_VIDEO_AVC);
        //设置视频宽高
        mediaFormat.setInteger(MediaFormat.KEY_WIDTH, 1080);
        mediaFormat.setInteger(MediaFormat.KEY_HEIGHT, 1920);
        //设置比特率
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1080 * 1920 * fps * 3);
        //设置帧率
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        //设置视频关键帧间隔，这里设置两秒一个关键帧
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
        //设置视频输入颜色格式，这里选择使用Surface作为输入，可以忽略颜色格式的问题，并且不需要直接操作输入缓冲区。
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            /**
             * 可选配置，设置码率模式
             * BITRATE_MODE_VBR：恒定质量
             * BITRATE_MODE_VBR：可变码率
             * BITRATE_MODE_CBR：恒定码率
             */
            mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR);
            /**
             * 可选配置，设置H264 Profile
             * 需要做兼容性检查
             */
            mediaFormat.setInteger(MediaFormat.KEY_PROFILE, MediaCodecInfo.CodecProfileLevel.AVCProfileHigh);
            /**
             * 可选配置，设置H264 Level
             * 需要做兼容性检查
             */
            mediaFormat.setInteger(MediaFormat.KEY_LEVEL, MediaCodecInfo.CodecProfileLevel.HEVCHighTierLevel31);

        }


    }

    private void initMediaCodec(){
        try {
            initFormat();
            MediaCodec mediaCodec = MediaCodec.createByCodecName(MediaFormat.MIMETYPE_VIDEO_AVC);
            mediaCodec.configure(mediaFormat, mediaCodec.createInputSurface(), null, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //子线程中调用
    class MediaCodecCallback implements Handler.Callback{

        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    }

}
