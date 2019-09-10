package com.example.sharedscreen.encoder2;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;

/**
 * 参考 1.https://www.jianshu.com/p/9512defaa8c1
 */
public class MediaCodecCreater extends HandlerThread {
    private MediaFormat mediaFormat;
    private Surface mSurface;

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

    private MediaCodec mMediaCodec;
    private void initMediaCodec(){
        try {
            initFormat();
            mMediaCodec = MediaCodec.createByCodecName(MediaFormat.MIMETYPE_VIDEO_AVC);
            //创建一个surface，作为全局变量的原因是因为VirtualPlay要用
            mSurface = mMediaCodec.createInputSurface();
            mMediaCodec.configure(mediaFormat, mSurface, null, 0);

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

    //开始录制
    public void startRecoder(){
        mMediaCodec.start();
        ByteBuffer[] inputByteBuffer = mMediaCodec.getInputBuffers();
        ByteBuffer[] outputByteBuffer = mMediaCodec.getOutputBuffers();


    }

    /**
     * 根据MIME_TYPE = "video/avc"选择设备支持的编码器和colorFormat
     * @param mimeType
     * @return
     */
    private MediaCodecInfo selectSupportCodec(String mimeType) {
        int numCodecs = MediaCodecList.getCodecCount();
        for (int i = 0; i < numCodecs; i++) {
            MediaCodecInfo codecInfo = MediaCodecList.getCodecInfoAt(i);
            if (!codecInfo.isEncoder()) {
                continue;
            }
            String[] types = codecInfo.getSupportedTypes();
            for (int j = 0; j < types.length; j++) {
                if (types[j].equalsIgnoreCase(mimeType)) {
                    return codecInfo;
                }
            }
        }
        return null;
    }

    /**
     * 根据mime类型匹配编码器支持的颜色格式
     */
    private int selectSupportColorFormat(MediaCodecInfo mCodecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = mCodecInfo.getCapabilitiesForType(mimeType);
        HashSet<Integer> colorFormats = new HashSet<>();
        for(int i : capabilities.colorFormats) colorFormats.add(i);
        if(colorFormats.contains(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar)) return MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
        if(colorFormats.contains(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar)) return MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar;
        return 0;
    }
}
