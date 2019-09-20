package com.example.sharedscreen.encoder2;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;

import static android.media.MediaExtractor.MetricsConstants.MIME_TYPE;


/**
 * 参考 1.https://www.jianshu.com/p/9512defaa8c1
 *      2.http://androidxref.com/4.4.2_r2/xref/cts/tests/tests/media/src/android/media/cts/EncodeVirtualDisplayTest.java#approxEquals
 *      3.https://www.cnblogs.com/hrhguanli/p/5043610.html
 *      4.android录屏的三种方案 https://www.jianshu.com/p/8b313692ac85
 */
public class MediaCodecCreater extends HandlerThread {

    private static final int MSG_START = 1;
    private static final int MSG_STOP = 2;

    private static final String TAG = "EncodeVirtualTest";
    private static final boolean VERBOSE = false;           // lots of logging
    private static final boolean DEBUG_SAVE_FILE = false;   // save copy of encoded movie
    private static final String DEBUG_FILE_NAME_BASE = Environment.getExternalStorageState() + "/vedioTest";//"/sdcard/test.";

    private final ByteBuffer mPixelBuf = ByteBuffer.allocateDirect(4);

    // Virtual display characteristics.  Scaled down from full display size because not all
    // devices can encode at the resolution of their own display.
    private static final String NAME = TAG;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int DENSITY = DisplayMetrics.DENSITY_HIGH;
    private static final int UI_TIMEOUT_MS = 2000;
    private static final int UI_RENDER_PAUSE_MS = 200;

    // Encoder parameters.  We use the same width/height as the virtual display.
    private static final String MIME_TYPE = "video/avc";
    private static final int FRAME_RATE = 15;               // 15fps
    private static final int IFRAME_INTERVAL = 10;          // 10 seconds between I-frames
    private static final int BIT_RATE = 6000000;            // 6Mbps

    private DisplayManager mDisplayManager = null;

    // Colors to test (RGB).  These must convert cleanly to and from BT.601 YUV.
    private static final int TEST_COLORS[] = {
            makeColor(10, 100, 200),        // YCbCr 89,186,82
            makeColor(100, 200, 10),        // YCbCr 144,60,98
            makeColor(200, 10, 100),        // YCbCr 203,10,103
            makeColor(10, 200, 100),        // YCbCr 130,113,52
            makeColor(100, 10, 200),        // YCbCr 67,199,154
            makeColor(200, 100, 10),        // YCbCr 119,74,179
    };

    /* TEST_COLORS static initialization; need ARGB for ColorDrawable */
    private static int makeColor(int red, int green, int blue) {
        return 0xff << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
    }

    private MediaFormat mediaFormat;
    private Surface mSurface;

    private int fps = 60;

    private boolean stop = false;

    private MediaProjection mMediaProjection;

    //子线程的handler
    private Handler mediaCodecHandler = new Handler(this.getLooper(), new MediaCodecCallback());

    //UI线程Handler
    private Handler mUIHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_START:
                case MSG_STOP:
                    mediaCodecHandler.sendMessage(msg);
                    break;
            }

            return false;
        }
    });


    public MediaCodecCreater(String name, MediaProjection mediaProjection) {
        super(name);
        this.mMediaProjection = mediaProjection;
        init();

    }

    private MediaMuxer mMediaMuxer;
    private String mp4Path;
    private MediaCodec.BufferInfo mBufferInfo;
    private int muxerIndex;

    private void init() {
        try {


//            initFormat();
//            initMediaCodec();
//
//            mBufferInfo = new MediaCodec.BufferInfo();
//
//            mMediaMuxer = new MediaMuxer(mp4Path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
//            //获取输出的format，有文档说需要直接从mediaCodec中获取，不然有问题，后期可以试试自己创建的
//            muxerIndex = mMediaMuxer.addTrack(mMediaCodec.getOutputFormat());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initFormat() {
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

    private MediaCodec mMediaCodec;

    private void initMediaCodec() {
        try {
            initFormat();
            mMediaCodec = MediaCodec.createByCodecName(MediaFormat.MIMETYPE_VIDEO_AVC);
            //创建一个surface，作为全局变量的原因是因为VirtualPlay要用
            mSurface = mMediaCodec.createInputSurface();
            mMediaCodec.configure(mediaFormat, mSurface, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //子线程中调用
    class MediaCodecCallback implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case MSG_START:

                    break;
                case MSG_STOP:

                    break;
            }
            return false;
        }
    }

    private boolean isEOS = false;

    //开始录制
    public void startRecoder() {
        mMediaCodec.start();
        ByteBuffer[] inputByteBuffers = mMediaCodec.getInputBuffers();
        ByteBuffer[] outputByteBuffers = mMediaCodec.getOutputBuffers();

        while (!stop) {
            if (!isEOS) {
                //返回使用有效输出的Buffer的索引，如果没有相关buffer可用，就返回-1；
                //如果传入的timeoutUs为0，则立马返回；如果输入Buffer可用，将等待返回。
                int inIndex = mMediaCodec.dequeueInputBuffer(10000);

                if (inIndex > 0) {
                    ByteBuffer inByteBuffer = inputByteBuffers[inIndex];
//                    mMediaCodec.queueInputBuffer(inIndex, 0, inByteBuffer., 0, );

                }
            }
        }


    }

    /**
     * 根据MIME_TYPE = "video/avc"选择设备支持的编码器和colorFormat
     *
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
        for (int i : capabilities.colorFormats) colorFormats.add(i);
        if (colorFormats.contains(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar))
            return MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar;
        if (colorFormats.contains(MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar))
            return MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar;
        return 0;
    }

    volatile boolean mInputDone;

    /**
      * Prepares the encoder, decoder, and virtual display.
     */
    private void encodeVirtualDisplayTest() {
        MediaCodec encoder = null;
        MediaCodec decoder = null;
        OutputSurface outputSurface = null;
        VirtualDisplay virtualDisplay = null;

        try {
            // Encoded video resolution matches virtual display.
            MediaFormat encoderFormat = MediaFormat.createVideoFormat(MIME_TYPE, WIDTH, HEIGHT);
            encoderFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                    MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            encoderFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            encoderFormat.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
            encoderFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

            encoder = MediaCodec.createEncoderByType(MIME_TYPE);
            encoder.configure(encoderFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            //创建输入端的surface
            Surface inputSurface = encoder.createInputSurface();
            //开始编码
            encoder.start();

            // Create a virtual display that will output to our encoder.
            //创建一个虚拟显示，将输出到我们的编码器。
//            virtualDisplay = mDisplayManager.createVirtualDisplay(NAME,
//                    WIDTH, HEIGHT, DENSITY, inputSurface, 0);
            virtualDisplay = mMediaProjection.createVirtualDisplay(NAME, WIDTH, HEIGHT, DENSITY, 0, inputSurface, null, null);

            // We also need a decoder to check the output of the encoder.
            //我们还需要一个解码器来检查编码器的输出。
            decoder = MediaCodec.createDecoderByType(MIME_TYPE);
            MediaFormat decoderFormat = MediaFormat.createVideoFormat(MIME_TYPE, WIDTH, HEIGHT);
            outputSurface = new OutputSurface(WIDTH, HEIGHT);
            decoder.configure(decoderFormat, outputSurface.getSurface(), null, 0);
            decoder.start();

            // Run the color slide show on a separate thread.
            mInputDone = false;
//            new ColorSlideShow(virtualDisplay.getDisplay()).start();

            // Record everything we can and check the results.
            //记录下我们能做的一切并检查结果。
            doTestEncodeVirtual(encoder, decoder, outputSurface);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } finally {
            if (VERBOSE) Log.d(TAG, "releasing codecs, surfaces, and virtual display");
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            if (outputSurface != null) {
                outputSurface.release();
            }
            if (encoder != null) {
                encoder.stop();
                encoder.release();
            }
            if (decoder != null) {
                decoder.stop();
                decoder.release();
            }
        }
    }

    /**
     * Drives the encoder and decoder.
     */
    private void doTestEncodeVirtual(MediaCodec encoder, MediaCodec decoder,
                                     OutputSurface outputSurface) {
        final int TIMEOUT_USEC = 10000;
        ByteBuffer[] encoderOutputBuffers = encoder.getOutputBuffers();
        ByteBuffer[] decoderInputBuffers = decoder.getInputBuffers();
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        boolean inputEosSignaled = false;
        int lastIndex = -1;
        int goodFrames = 0;
        int debugFrameCount = 0;

        // Save a copy to disk.  Useful for debugging the test.  Note this is a raw elementary
        // stream, not a .mp4 file, so not all players will know what to do with it.
        FileOutputStream outputStream = null;
        if (DEBUG_SAVE_FILE) {
            String fileName = DEBUG_FILE_NAME_BASE + System.currentTimeMillis() + WIDTH + "x" + HEIGHT + ".mp4";
            try {
                outputStream = new FileOutputStream(fileName);
                Log.d(TAG, "encoded output will be saved as " + fileName);
            } catch (IOException ioe) {
                Log.w(TAG, "Unable to create debug output file " + fileName);
                throw new RuntimeException(ioe);
            }
        }

        // Loop until the output side is done.
        boolean encoderDone = false;
        boolean outputDone = false;
        while (!outputDone) {
            if (VERBOSE) Log.d(TAG, "loop");

            if (!inputEosSignaled && mInputDone) {
                if (VERBOSE) Log.d(TAG, "signaling input EOS");
                encoder.signalEndOfInputStream();
                inputEosSignaled = true;
            }

            boolean decoderOutputAvailable = true;
            boolean encoderOutputAvailable = !encoderDone;
            while (decoderOutputAvailable || encoderOutputAvailable) {
                // Start by draining any pending output from the decoder.  It's important to
                // do this before we try to stuff any more data in.
                int decoderStatus = decoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                if (decoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // no output available yet
                    if (VERBOSE) Log.d(TAG, "no output from decoder available");
                    decoderOutputAvailable = false;
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                    if (VERBOSE) Log.d(TAG, "decoder output buffers changed (but we don't care)");
                } else if (decoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                    // this happens before the first frame is returned
                    MediaFormat decoderOutputFormat = decoder.getOutputFormat();
                    if (VERBOSE) Log.d(TAG, "decoder output format changed: " +
                            decoderOutputFormat);
                } else if (decoderStatus < 0) {
                    Log.e(TAG, "unexpected result from deocder.dequeueOutputBuffer: " + decoderStatus);
                } else {  // decoderStatus >= 0
                    if (VERBOSE) Log.d(TAG, "surface decoder given buffer " + decoderStatus +
                            " (size=" + info.size + ")");
                    if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                        if (VERBOSE) Log.d(TAG, "output EOS");
                        outputDone = true;
                    }

                    // The ByteBuffers are null references, but we still get a nonzero size for
                    // the decoded data.
                    boolean doRender = (info.size != 0);

                    // As soon as we call releaseOutputBuffer, the buffer will be forwarded
                    // to SurfaceTexture to convert to a texture.  The API doesn't guarantee
                    // that the texture will be available before the call returns, so we
                    // need to wait for the onFrameAvailable callback to fire.  If we don't
                    // wait, we risk dropping frames.
                    outputSurface.makeCurrent();
                    decoder.releaseOutputBuffer(decoderStatus, doRender);
                    if (doRender) {
                        if (VERBOSE) Log.d(TAG, "awaiting frame " + (lastIndex + 1));
                        outputSurface.awaitNewImage();
                        outputSurface.drawImage();
                        int foundIndex = checkSurfaceFrame();
                        if (foundIndex == lastIndex + 1) {
                            // found the next one in the series
                            lastIndex = foundIndex;
                            goodFrames++;
                        } else if (foundIndex == lastIndex) {
                            // Sometimes we see the same color two frames in a row.
                            if (VERBOSE) Log.d(TAG, "Got another " + lastIndex);
                        } else if (foundIndex > 0) {
                            // Looks like we missed a color frame.  It's possible something
                            // stalled and we dropped a frame.  Skip forward to see if we
                            // can catch the rest.
                            if (foundIndex < lastIndex) {
                                Log.w(TAG, "Ignoring backward skip from " +
                                        lastIndex + " to " + foundIndex);
                            } else {
                                Log.w(TAG, "Frame skipped, advancing lastIndex from " +
                                        lastIndex + " to " + foundIndex);
                                goodFrames++;
                                lastIndex = foundIndex;
                            }
                        }
                    }
                }
                if (decoderStatus != MediaCodec.INFO_TRY_AGAIN_LATER) {
                    // Continue attempts to drain output.
                    continue;
                }

                // Decoder is drained, check to see if we've got a new buffer of output from
                // the encoder.
                if (!encoderDone) {
                    int encoderStatus = encoder.dequeueOutputBuffer(info, TIMEOUT_USEC);
                    if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
                        // no output available yet
                        if (VERBOSE) Log.d(TAG, "no output from encoder available");
                        encoderOutputAvailable = false;
                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                        // not expected for an encoder
                        encoderOutputBuffers = encoder.getOutputBuffers();
                        if (VERBOSE) Log.d(TAG, "encoder output buffers changed");
                    } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                        // received before first buffer
                        MediaFormat newFormat = encoder.getOutputFormat();
                        if (VERBOSE) Log.d(TAG, "encoder output format changed: " + newFormat);
                    } else if (encoderStatus < 0) {
                        Log.e(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
                    } else { // encoderStatus >= 0
                        ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
                        if (encodedData == null) {
                            Log.e(TAG, "encoderOutputBuffer " + encoderStatus + " was null");
                        }

                        // It's usually necessary to adjust the ByteBuffer values to match BufferInfo.
                        encodedData.position(info.offset);
                        encodedData.limit(info.offset + info.size);

                        if (outputStream != null) {
                            byte[] data = new byte[info.size];
                            encodedData.get(data);
                            encodedData.position(info.offset);
                            try {
                                outputStream.write(data);
                            } catch (IOException ioe) {
                                Log.w(TAG, "failed writing debug data to file");
                                throw new RuntimeException(ioe);
                            }
                            debugFrameCount++;
                        }

                        // Get a decoder input buffer, blocking until it's available.  We just
                        // drained the decoder output, so we expect there to be a free input
                        // buffer now or in the near future (i.e. this should never deadlock
                        // if the codec is meeting requirements).
                        //
                        // The first buffer of data we get will have the BUFFER_FLAG_CODEC_CONFIG
                        // flag set; the decoder will see this and finish configuring itself.
                        int inputBufIndex = decoder.dequeueInputBuffer(-1);
                        ByteBuffer inputBuf = decoderInputBuffers[inputBufIndex];
                        inputBuf.clear();
                        inputBuf.put(encodedData);
                        decoder.queueInputBuffer(inputBufIndex, 0, info.size,
                                info.presentationTimeUs, info.flags);

                        // If everything from the encoder has been passed to the decoder, we
                        // can stop polling the encoder output.  (This just an optimization.)
                        if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                            encoderDone = true;
                            encoderOutputAvailable = false;
                        }
                        if (VERBOSE) Log.d(TAG, "passed " + info.size + " bytes to decoder"
                                + (encoderDone ? " (EOS)" : ""));

                        encoder.releaseOutputBuffer(encoderStatus, false);
                    }
                }
            }
        }

        if (outputStream != null) {
            try {
                outputStream.close();
                if (VERBOSE) Log.d(TAG, "Wrote " + debugFrameCount + " frames");
            } catch (IOException ioe) {
                Log.w(TAG, "failed closing debug file");
                throw new RuntimeException(ioe);
            }
        }

        if (goodFrames != TEST_COLORS.length) {
            Log.e(TAG, "Found " + goodFrames + " of " + TEST_COLORS.length + " expected frames");
        }
    }

    /**
     * 410     * Checks the contents of the current EGL surface to see if it matches expectations.
     * 411     * <p>
     * 412     * The surface may be black or one of the colors we've drawn.  We have sufficiently little
     * 413     * control over the rendering process that we don't know how many (if any) black frames
     * 414     * will appear between each color frame.
     * 415     * <p>
     * 416     * @return the color index, or -2 for black
     * 417     * @throw RuntimeException if the color isn't recognized (probably because the RGB<->YUV
     * 418     *     conversion introduced too much variance)
     * 419
     */
    private int checkSurfaceFrame() {
        boolean frameFailed = false;

        // Read a pixel from the center of the surface.  Might want to read from multiple points
        // and average them together.
        int x = WIDTH / 2;
        int y = HEIGHT / 2;
        GLES20.glReadPixels(x, y, 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, mPixelBuf);
        int r = mPixelBuf.get(0) & 0xff;
        int g = mPixelBuf.get(1) & 0xff;
        int b = mPixelBuf.get(2) & 0xff;
        if (VERBOSE) Log.d(TAG, "GOT: r=" + r + " g=" + g + " b=" + b);

        if (approxEquals(0, r) && approxEquals(0, g) && approxEquals(0, b)) {
            return -2;
        }

        // Walk through the color list and try to find a match.  These may have gone through
        // RGB<->YCbCr conversions, so don't expect exact matches.
        for (int i = 0; i < TEST_COLORS.length; i++) {
            int testRed = (TEST_COLORS[i] >> 16) & 0xff;
            int testGreen = (TEST_COLORS[i] >> 8) & 0xff;
            int testBlue = TEST_COLORS[i] & 0xff;
            if (approxEquals(testRed, r) && approxEquals(testGreen, g) &&
                    approxEquals(testBlue, b)) {
                if (VERBOSE) Log.d(TAG, "Matched color " + i + ": r=" + r + " g=" + g + " b=" + b);
                return i;
            }
        }

        throw new RuntimeException("No match for color r=" + r + " g=" + g + " b=" + b);
    }

    /**
     * 454     * Determines if two color values are approximately equal.
     * 455
     */
    private static boolean approxEquals(int expected, int actual) {
        final int MAX_DELTA = 4;
        return Math.abs(expected - actual) <= MAX_DELTA;
    }

}
