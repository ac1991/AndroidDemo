package com.example.liveplayer.MediaView;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by sqwu on 2019/11/22
 */
public class MediaPlayerOption {
    private IjkMediaPlayer mMediaPlayer;

    //硬件解码
    public static final int OPTION_HARDWARE_DECODE = 1;
    //软件解码
    public static final int OPTION_SOFTWARE_DECODE = 0;

    public void setMediaPlayerOptions(IjkMediaPlayer ijkMediaPlayer, MediaPlayerConfig config){
        if (ijkMediaPlayer == null || config == null){
            return;
        }
        mMediaPlayer = ijkMediaPlayer;
        setMediaCodec(config.decodeType);
        setFrameDrop(config.framedrop);
        setSkipLoopFilter(config.skipLoopFilter);
        setAnalyzeDuration(config.analyzeduration);
        setFlushPackets(config.flushPackets);
        setStartOnPrepared(config.startOnPrepared);
        setFast(config.fast);
        setPacketBuffering(config.packetBuffering);
        setMediacodecAutoRotate(config.mediacodecAutoRotate);
        setMediacodecHandleResolutionChange(config.mediacodecHandleResolutionChange);
        setMaxBufferSize(config.maxBufferSize);
        setMinFrames(config.minFrames);
        setMaxCachedDuration(config.maxCachedDuration);
        setInfbuf(config.infbuf);
        setFflags(config.fflags);
        setProbesize(config.probesize);
        setReconnect(config.reconnect);
        setRtspTransport(config.rtspTransport);
        setAnalyzeMaxDuration(config.analyzemaxduration);
        setMaxFps(config.maxFps);
    }

    private void setMediaCodec(long decodeType){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", decodeType);
    }

    private void setFrameDrop(long framedrop){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", framedrop);
    }

    private void setSkipLoopFilter(long skipLoopFilter){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", skipLoopFilter);
    }

    private void setAnalyzeDuration(long analyzeduration){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzeduration", analyzeduration);
    }

    private void setRtspTransport(String rtspTransport){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "rtsp_transport", rtspTransport);
    }

    private void setAnalyzeMaxDuration(long analyzemaxduration){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "analyzemaxduration", analyzemaxduration);
    }

    private void setFlushPackets(long flushPackets){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "flush_packets", flushPackets);
    }

    private void setStartOnPrepared(long startOnPrepared){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", startOnPrepared);
    }

    private void setFast(long fast){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fast", fast);
    }

    private void setPacketBuffering(long packetBuffering){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "packet-buffering", packetBuffering);
    }

    private void setMediacodecAutoRotate(long mediacodecAutoRotate){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", mediacodecAutoRotate);
    }
    private void setMediacodecHandleResolutionChange(long mediacodecHandleResolutionChange){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", mediacodecHandleResolutionChange);
    }

    private void setMaxBufferSize(long maxBufferSize){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "max-buffer-size", maxBufferSize);
    }

    private void setMinFrames(long minFrames){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "min-frames", minFrames);
    }

    private void setMaxCachedDuration(long maxCachedDuration){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max_cached_duration", maxCachedDuration);
    }

    private void setInfbuf(long infbuf){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "infbuf", infbuf);
    }

    private void setFflags(String fflags){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "fflags", fflags);
    }

    private void setProbesize(long probesize){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "probesize", probesize);
    }

    private void setReconnect(long reconnect){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "reconnect", reconnect);
    }

    private void setMaxFps(long maxFps){
        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-fps", maxFps);
    }


}
