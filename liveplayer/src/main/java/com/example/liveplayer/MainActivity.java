package com.example.liveplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.liveplayer.MediaView.IjkMediaPlayerView;
import com.example.liveplayer.MediaView.IjkMediaPlayerView1;
import com.example.liveplayer.MediaView.MediaPlayerConfig;
import com.example.liveplayer.MediaView.VideoPlayerListener;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "IjkMediaPlayerView";


    IjkMediaPlayerView ijkMediaPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ijkMediaPlayerView = findViewById(R.id.player);
        //http://119.3.36.59/live/test.flv
        //rtmp://192.168.10.199:1935/live/stream1/111111
        ijkMediaPlayerView.setVideoPath("rtmp://192.168.10.199:1935/live/stream1/111111");


//        MediaPlayerConfig mediaPlayerConfig = new MediaPlayerConfig();
//        ijkMediaPlayerView.setListener(new VideoPlayerListener() {
//
//            /**
//             * 推流有内容时调用
//             * @param iMediaPlayer
//             * @param i
//             */
//            @Override
//            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
//                Log.d(TAG,"onBufferingUpdate");
//            }
//
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                Log.d(TAG,"onCompletion");
//            }
//
//            @Override
//            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
//                Log.d(TAG,"onError");
//                return false;
//            }
//
//            /**
//             * 推流停止时调用
//             * @param iMediaPlayer
//             * @param i
//             * @param i1
//             * @return
//             */
//            @Override
//            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
//                Log.d(TAG,"onInfo");
//                return false;
//            }
//
//            @Override
//            public void onPrepared(IMediaPlayer iMediaPlayer) {
//                Log.d(TAG,"onPrepared   w * h =  " + iMediaPlayer.getVideoWidth() +" * " +  iMediaPlayer.getVideoWidth());
////                ViewGroup.LayoutParams layoutParams = ijkMediaPlayerView.getLayoutParams();
////                layoutParams.height = iMediaPlayer.getVideoHeight();
////                layoutParams.width = iMediaPlayer.getVideoWidth();
////                ijkMediaPlayerView.setLayoutParams(layoutParams);
////                ijkMediaPlayerView.start();
//
//            }
//
//            @Override
//            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
//                Log.d(TAG,"onSeekComplete");
//            }
//
//            @Override
//            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
//                Log.d(TAG,"onVideoSizeChanged");
//            }
//        });
//
//        try {
//
//        }catch (Exception e){
//            Toast.makeText(this, "播放失败", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");


    }

//    /**
//     * surfaceView的监听器
//     */
//    private class LmnSurfaceCallback implements SurfaceHolder.Callback {
//        @Override
//        public void surfaceCreated(SurfaceHolder holder) {
//            Log.d(TAG,"surfaceCreated");
//            //surfaceview创建成功后，加载视频
//            ijkMediaPlayerView.prepareAsync();
//        }
//
//        @Override
//        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//
//            Log.d(TAG,"surfaceChanged");
//        }
//
//        @Override
//        public void surfaceDestroyed(SurfaceHolder holder) {
//            Log.d(TAG,"surfaceDestroyed");
//        }
//    }
}
