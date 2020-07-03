package com.example.liveplayer.MediaView;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by sqwu on 2019/11/21
 */
public class IjkMediaPlayerView1 extends SurfaceView {

    private static final String TAG = "IjkMediaPlayerView";

    /**
     * 由ijkplayer提供，用于播放视频，需要给他传入一个surfaceView
     */
    private IjkMediaPlayer mMediaPlayer = null;

    /**
     * 视频文件地址
     */
    private String mPath = "";

    /**
     * 媒体播放器配置器
     */
    private MediaPlayerConfig mConfig = null;




    public IjkMediaPlayerView1(Context context) {
        this(context, null);
    }

    public IjkMediaPlayerView1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IjkMediaPlayerView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //添加回调
//        getHolder().addCallback(new LmnSurfaceCallback());
        //创建IMediaPlayer
        createPlayer();

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

        }

//        load();
    }



    /**
     * 加载视频
     */
    public void prepareAsync() {
        if (TextUtils.isEmpty(mPath)){
            return;
        }

        if (mMediaPlayer != null  && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
//        //每次都要重新创建IMediaPlayer
//        createPlayer();
        try {
            mMediaPlayer.setDataSource(mPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //给mediaPlayer设置视图
        mMediaPlayer.setDisplay(getHolder());

        mMediaPlayer.prepareAsync();
    }

    /**
     * 创建一个新的player
     */
    public void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

        //开启硬解码
//        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);

        MediaPlayerOption option = new MediaPlayerOption();
        option.setMediaPlayerOptions(ijkMediaPlayer, mConfig);

        mMediaPlayer = ijkMediaPlayer;

    }



    private VideoPlayerListener listener;
    public void setListener(VideoPlayerListener listener) {
        this.listener = listener;
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnPreparedListener(listener);
            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(listener);
            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(listener);
        }
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

    public void setConfig(MediaPlayerConfig mConfig) {
        this.mConfig = mConfig;
        MediaPlayerOption option = new MediaPlayerOption();
        option.setMediaPlayerOptions(mMediaPlayer, mConfig);
    }
}
