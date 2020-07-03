package com.example.liveplayer.MediaView;

import android.content.Context;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.logging.Logger;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

public class IjkMediaPlayerView extends LinearLayout {

    private static final String TAG = "IjkMediaPlayerView";

    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private IjkMediaPlayer mMediaPlayer = null;

    /**
     * 视频文件地址
     */
    private String mPath = "";

    private SurfaceView surfaceView;

//    private VideoPlayerListener listener;
    private Context mContext;

    /**
     * 媒体播放器配置器
     */
    private MediaPlayerConfig mConfig = null;

    public IjkMediaPlayerView(@NonNull Context context) {
        this(context, null);
    }

    public IjkMediaPlayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkMediaPlayerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initVideoView(context);
    }

    private void initVideoView(Context context) {
        mContext = context;
        setGravity(Gravity.CENTER);
        //获取焦点，不知道有没有必要~。~
        setFocusable(true);
        createSurfaceView();
        initListener();

        mConfig = new MediaPlayerConfig();

    }

    /**
     * 设置视频地址。
     * 根据是否第一次播放视频，做不同的操作。
     *
     * @param path the path of the video.
     */
    public void setVideoPath(String path) {
        if (TextUtils.equals("", mPath)) {
            //如果是第一次播放视频，那就创建一个新的surfaceView
            mPath = path;
        } else {
            //否则就直接load
            mPath = path;
            load();
        }
    }

    /**
     * 新建一个surfaceview
     */
    private void createSurfaceView() {
        //生成一个新的surface view
        surfaceView = new SurfaceView(mContext);
        surfaceView.getHolder().addCallback(new LmnSurfaceCallback());
        LayoutParams layoutParams = new LayoutParams(1080
                , 1920);
        surfaceView.setLayoutParams(layoutParams);
        this.addView(surfaceView);
    }

    /**
     * surfaceView的监听器
     */
    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG,"surfaceCreated");
            //surfaceview创建成功后，加载视频
            load();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


            Log.d(TAG,"surfaceChanged:width * height:" + width + " * " + height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG,"surfaceDestroyed");
        }
    }

    /**
     * 加载视频
     */
    private void load() {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(surfaceView.getHolder());
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMediaPlayer.prepareAsync();
    }

    /**
     * 创建一个新的player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        mMediaPlayer = ijkMediaPlayer;

        MediaPlayerOption option = new MediaPlayerOption();
        option.setMediaPlayerOptions(mMediaPlayer, mConfig);
        setListener(listener);
    }

    private VideoPlayerListener listener;
    public void setListener(VideoPlayerListener listener) {
        if (listener != null && mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
            mMediaPlayer.setOnCompletionListener(listener);
//            mMediaPlayer.setOnTimedTextListener(listener);
        }
    }

    /**
     *
     */
    private void initListener(){
        listener = new VideoPlayerListener() {
            /**
             * 如果服务端不再推流，客户端将不再回调onBufferingUpdate
             * 推流有内容时调用
             * @param iMediaPlayer
             * @param i
             */
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                Log.d(TAG,"onBufferingUpdate");
            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                //客户端中间断网或者无网会执行
                Log.d(TAG,"onCompletion");
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                //客户端中间断网或者无网会执行
                Log.d(TAG,"onError  i:" + i + "  i1:" + i1);
                return false;
            }

            /**
             * @param iMediaPlayer
             * @param i
             * @param i1
             * @return
             */
            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG,"onInfo  i:" +i + "  i1:" + i1);
                return false;
            }

            /**
             * 如果服务端没有直播数据或者没有推流，客户端将不调本函数
             * @param iMediaPlayer
             */
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"onPrepared   w * h =  " + iMediaPlayer.getVideoWidth() +" * " +  iMediaPlayer.getVideoHeight());
                changeSurfaceViewSize(iMediaPlayer.getVideoWidth(), iMediaPlayer.getVideoHeight());
                start();
//                ijkMediaPlayerView.start();

            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"onSeekComplete");
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                Log.d(TAG,"onVideoSizeChanged  w * h =  " + iMediaPlayer.getVideoWidth() +" * " +  iMediaPlayer.getVideoHeight());
            }
        };
    }

    /**
     * 设置播放器配置信息
     * @param mConfig
     */
    public void setConfig(MediaPlayerConfig mConfig) {
        this.mConfig = mConfig;
        MediaPlayerOption option = new MediaPlayerOption();
        option.setMediaPlayerOptions(mMediaPlayer, mConfig);
    }

    /**
     * 获取配置信息
     * @return
     */
    public MediaPlayerConfig getConfig(){
        return mConfig;
    }

    private void changeSurfaceViewSize(int mediaW, int mediaH){
        int parentViewW = getWidth();
        int parentViewH = getHeight();

        float hScale = (float)mediaH/(float)parentViewH;
        float wScale = (float)mediaW/(float)parentViewW;

        //如果视频的宽度大于父控件的宽度，且视频的高度大于父控件的高度
        if ((mediaH > parentViewH && mediaW > parentViewW) ){
            if( hScale > wScale){
                mediaH = parentViewH;
                mediaW = (int) (mediaW/hScale);
            }else {
                mediaW = parentViewW;
                mediaH = (int) (mediaH/ wScale);
            }
        }else if ((mediaH <= parentViewH && mediaW <= parentViewW)){
            if( hScale > wScale){
                mediaH = parentViewH;
                mediaW = (int) (mediaW/hScale);
            }else {
                mediaW = parentViewW;
                mediaH = (int) (mediaH/ wScale);
            }
        }else if (mediaH > parentViewH && mediaW <= parentViewW){
            mediaH = parentViewH;
            mediaW = (int) (mediaW/hScale);
        }else if (mediaH <= parentViewH && mediaW > parentViewW){
            mediaW = parentViewW;
            mediaH = (int) (mediaH/ wScale);
        }

        Log.d(TAG, "H:" + mediaH + "   W:" + mediaW);

        ViewGroup.LayoutParams layoutParams = surfaceView.getLayoutParams();
        layoutParams.height = mediaH;
        layoutParams.width = mediaW;
        surfaceView.setLayoutParams(layoutParams);

    }

    /**
     * -------======--------- 下面封装了一下控制视频的方法
     */

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }


    public void reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
    }


    public long getDuration() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getDuration();
        } else {
            return 0;
        }
    }


    public long getCurrentPosition() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }


    public void seekTo(long l) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(l);
        }
    }



}
