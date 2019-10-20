package com.example.liveplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.liveplayer.MediaView.IjkMediaPlayerView;
import com.example.liveplayer.MediaView.VideoPlayerListener;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    IjkMediaPlayerView ijkMediaPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ijkMediaPlayerView = findViewById(R.id.player);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ijkMediaPlayerView.setVideoPath("http://192.168.10.199:8080/live/stream1.flv");
        ijkMediaPlayerView.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {
                Log.d(TAG,"onBufferingUpdate");
            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"onCompletion");
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG,"onError");
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                Log.d(TAG,"onInfo");
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"onPrepared");
                ijkMediaPlayerView.start();
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {
                Log.d(TAG,"onSeekComplete");
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                Log.d(TAG,"onVideoSizeChanged");
            }
        });

        try {

        }catch (Exception e){
            Toast.makeText(this, "播放失败", Toast.LENGTH_LONG).show();
        }
    }
}
