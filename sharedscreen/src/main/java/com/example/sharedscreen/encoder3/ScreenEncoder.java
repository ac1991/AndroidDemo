package com.example.sharedscreen.encoder3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Surface;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by sqwu on 2019/10/16
 * mediaCodec使用流程：Config-->Start
 */
public class ScreenEncoder implements IScreenEncoder {
    private static final String TAG = "ScreenEncoder";
    private MediaFormat mediaFormat;
    private MediaCodec mMediaCodec;
    private int fps = 60;
    private final static int TIMEOUT_US = 10000;

    private MediaMuxer mMuxer;
    private Surface mSurface;

    private boolean stop = false;

    private MediaProjection mediaProjection;
    private Context context;

    public ScreenEncoder(MediaProjection mediaProjection, Context context) {
        this.mediaProjection = mediaProjection;
        this.context = context;
    }

    private void initFormat() {
        mediaFormat = new MediaFormat();

        //使用h264编码
        mediaFormat.setString(MediaFormat.KEY_MIME, MediaFormat.MIMETYPE_VIDEO_AVC);
        //设置视频宽高
        mediaFormat.setInteger(MediaFormat.KEY_WIDTH, 1080);
        mediaFormat.setInteger(MediaFormat.KEY_HEIGHT, 1920);
        //设置比特率
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1080 * 1920 * fps);
        //设置帧率
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, fps);
        //设置视频关键帧间隔，这里设置两秒一个关键帧
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        //设置视频输入颜色格式，这里选择使用Surface作为输入，可以忽略颜色格式的问题，并且不需要直接操作输入缓冲区。
        //搞不好会报错
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);


        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

    private void init() {
        initFormat();
        try {
            mMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);//MediaCodec.createByCodecName("OMX.IMG.TOPAZ.VIDEO.Encoder");

            //初始化媒体合成器，用于生成mp4视频
            mMuxer = new MediaMuxer(createFile(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            mMediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int trackIndex = 0;
    @SuppressLint("NewApi")
    @Override
    public void start() {
        stop = false;


        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mMediaCodec == null) {
                    init();
                }
                //创建一个surface，作为全局变量的原因是因为VirtualPlay要用
                mSurface = mMediaCodec.createInputSurface();

                mMediaCodec.start();

                creatVirtualDisplay(mSurface);
                encoder(mMediaCodec);
            }
        }).start();

    }


    @Override
    public void stop() {
        stop = true;
    }

    private void encoder(MediaCodec mediaCodec) {
        //检索输入缓冲区的集合。start（）返回后调用此函数。调用此方法后，必须不再使用先前由对该方法的较早调用返回的任何ByteBuffer。
        ByteBuffer[] codecInputBuffers = mediaCodec.getInputBuffers();

        //检索输出缓冲区的集合。在start（）返回之后以及每当dequeueOutputBuffer通过return发出信号更改输出缓冲区时，
        // 调用此函数INFO_OUTPUT_BUFFERS_CHANGED。调用此方法后，必须不再使用先前由对该方法的较早调用返回的任何ByteBuffer。
        ByteBuffer[] codecOutputBuffers = mediaCodec.getOutputBuffers();
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        while (!stop) {
            //返回要用有效数据填充的输入缓冲区的索引；如果当前没有可用的缓冲区，则返回-1。
            // 如果timeoutUs == 0，则此方法将立即返回；如果timeoutUs <0，则无限期等待输入缓冲区的可用性；如果timeoutUs> 0，则等待直至“ timeoutUs”微秒。
//            int inputBufferId = mediaCodec.dequeueInputBuffer(TIMEOUT_US);
//            if (inputBufferId >= 0) {
//                queueInputBuffer(mediaCodec, codecInputBuffers, inputBufferId);
//            }

            int outputBufferId = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);

            switch (outputBufferId) {
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    break;
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    trackIndex = mMuxer.addTrack(mMediaCodec.getOutputFormat());
                    mMuxer.start();
                    break;
                case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                    codecOutputBuffers = mediaCodec.getOutputBuffers();
                    break;
                default:
                    muxVideo(outputBufferId, codecOutputBuffers[outputBufferId], bufferInfo);
                    dequeueOutputBuffer(mediaCodec, codecOutputBuffers, outputBufferId, bufferInfo);
                    if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) == 0)
                        break;
            }
        }
//        int outputBufferId = mediaCodec.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
//        muxVideo(outputBufferId, codecOutputBuffers[outputBufferId], bufferInfo);
        mediaCodec.signalEndOfInputStream();
        try {
            mediaCodec.stop();
            mediaCodec.release();

            if (mSurface != null){
                mSurface.release();
                mSurface = null;
            }

            mMuxer.stop();
            mMuxer.release();
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    private int queueInputBuffer(MediaCodec codec, ByteBuffer[] inputBuffers, int index) {
        ByteBuffer buffer = inputBuffers[index];
        buffer.clear();
        int size = buffer.limit();

        byte[] zeroes = new byte[size];
        buffer.put(zeroes);

        codec.queueInputBuffer(index, 0 /* offset */, size, 0 /* timeUs */, 0);

        return size;
    }

    private void dequeueOutputBuffer(MediaCodec codec, ByteBuffer[] outputBuffers, int index, MediaCodec.BufferInfo info) {
        codec.releaseOutputBuffer(index, false /* render */);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void creatVirtualDisplay(Surface surface) {
        VirtualDisplay virtualDisplay = mediaProjection.createVirtualDisplay("test", 1080, 1920, 1, DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, surface, null, null);
    }

    private void muxVideo(int index, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
//        if (!mIsRunning.get()) {
//            Log.w(TAG, "muxVideo: Already stopped!");
//            return;
//        }
//        if (!mMuxerStarted || mVideoTrackIndex == INVALID_INDEX) {
//            mPendingVideoEncoderBufferIndices.add(index);
//            mPendingVideoEncoderBufferInfos.add(buffer);
//            return;
//        }

        writeSampleData(trackIndex, bufferInfo, byteBuffer);
//        mVideoEncoder.releaseOutputBuffer(index);
//        if ((buffer.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
//            if (VERBOSE)
//                Log.d(TAG, "Stop encoder and muxer, since the buffer has been marked with EOS");
        // send release msg
//            mVideoTrackIndex = INVALID_INDEX;
//            signalStop(true);
//        }
    }

    private void writeSampleData(int track, MediaCodec.BufferInfo buffer, ByteBuffer encodedData) {
        if ((buffer.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            // The codec config data was pulled out and fed to the muxer when we got
            // the INFO_OUTPUT_FORMAT_CHANGED status.
            // Ignore it.
//            if (VERBOSE) Log.d(TAG, "Ignoring BUFFER_FLAG_CODEC_CONFIG");
            buffer.size = 0;
        }
        boolean eos = (buffer.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
//        if (buffer.size == 0 && !eos) {
//////            if (VERBOSE) Log.d(TAG, "info.size == 0, drop it.");
////            encodedData = null;
////        } else {
////            if (buffer.presentationTimeUs != 0) { // maybe 0 if eos
//////                if (track == mVideoTrackIndex) {
//////                    resetVideoPts(buffer);
////                }
////
//////                else if (track == mAudioTrackIndex) {
//////                    resetAudioPts(buffer);
//////                }
////            }
////            if (true)
////                Log.d(TAG, "[" + Thread.currentThread().getId() + "] Got buffer, track=" + track
////                        + ", info: size=" + buffer.size
////                        + ", presentationTimeUs=" + buffer.presentationTimeUs);
////            if (!eos && mCallback != null) {
////                mCallback.onRecording(buffer.presentationTimeUs);
////            }
////        }
        if (encodedData != null) {
            encodedData.position(buffer.offset);
            encodedData.limit(buffer.offset + buffer.size);
            mMuxer.writeSampleData(track, encodedData, buffer);
//            if (VERBOSE)
            Log.i(TAG, "Sent " + buffer.size + " bytes to MediaMuxer on track " + track);
        }
    }

    private String createFile() {
        String mDstPath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".mp4";
        ;

        File file = new File(mDstPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mDstPath;
    }

    /**
     * Returns a color format that is supported by the codec and isn't COLOR_FormatSurface.  Throws
     * an exception if none found.
     */
    private static int findNonSurfaceColorFormat(MediaCodecInfo codecInfo, String mimeType) {
        MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType(mimeType);
        for (int i = 0; i < capabilities.colorFormats.length; i++) {
            int colorFormat = capabilities.colorFormats[i];
            if (colorFormat != MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface) {
                return colorFormat;
            }
        }
        return MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface;   // not reached
    }

    /**
     * 471     * Returns the first codec capable of encoding the specified MIME type, or null if no
     * 472     * match was found.
     * 473
     */
    private static MediaCodecInfo selectCodec(String mimeType) {
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
}
