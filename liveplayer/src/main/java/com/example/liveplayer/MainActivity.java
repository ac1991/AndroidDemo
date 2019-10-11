package com.example.liveplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tv.danmaku.ijk.media.exo.IjkExoMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class MainActivity extends AppCompatActivity {

    IjkExoMediaPlayer exoMediaPlayer;

    IMediaPlayer iMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
