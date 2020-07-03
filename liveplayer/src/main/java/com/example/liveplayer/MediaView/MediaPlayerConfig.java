package com.example.liveplayer.MediaView;

/**
 * Created by sqwu on 2019/11/22
 *
 * 播放器配置变量
 *
 * 参考：1.https://github.com/Bilibili/ijkplayer/blob/master/ijkmedia/ijkplayer/ff_ffplay_options.h
 * 2.https://www.jianshu.com/p/843c86a9e9ad
 */
public class MediaPlayerConfig {
    /**
     * 解码类型：0 软解
     * 1硬件解码
     */
    public long  decodeType = 1;

    /**
     * 自动旋屏
     */
    public long  mediacodecAutoRotate = 1;

    /**
     * 处理分辨率变化
     */
    public long  mediacodecHandleResolutionChange = 1;

    /**
     * 丢帧  是在视频帧处理不过来的时候丢弃一些帧达到同步的效果
     */
    public long  framedrop = 5;

    /**
     *设置是否开启环路过滤: 0开启，画面质量高，解码开销大，48关闭，画面质量差点，解码开销小
     */
    public long  skipLoopFilter = 48;

    /**
     * 设置播放前的探测时间 1,达到首屏秒开效果
     */
    public long  analyzeduration = 1;

    /**
     *设置播放前的最大探测时间
     */
    public long  analyzemaxduration = 100L;

    /**
     * 如果是rtsp协议，可以优先用tcp(默认是用udp)
     */
    public String  rtspTransport = "tcp";

    /**
     *每处理一个packet之后刷新io上下文
     */
    public long  flushPackets = 1;

    /**
     * 需要准备好后自动播放
     */
    public long  startOnPrepared = 0;

    /**
     * 不额外优化 0否  1是
     */
    public long  fast = 0;

    /**
     * 是否开启预缓冲，一般直播项目会开启，达到秒开的效果，不过带来了播放丢帧卡顿的体验
     * 0是  1否
     */
    public long  packetBuffering = 1;



    /**
     * 最大缓冲大小,单位kb
     */
    public long  maxBufferSize = 1024 * 5;

    /**
     * 默认最小帧数
     */
    public long  minFrames = 2;

    /**
     * 最大缓存时长
     */
    public long  maxCachedDuration = 3;

    /**
     * 是否限制输入缓存数
     */
    public long  infbuf = 1;

    /**
     *ijkplayer和ffplay在打开rtmp串流视频时，大多数都会遇到5~10秒的延迟，
     * 在ffplay播放时，如果加上-fflags nobuffer可以缩短播放的rtmp视频延迟在1s内
     */
    public String  fflags = "nobuffer";

    /**
     * 播放前的探测Size，默认是1M, 改小一点会出画面更快
     */
    public long  probesize = 1024;

    /**
     * 播放重连次数
     */
    public long  reconnect = 5;

    /**
     * 最大帧率，大于这个帧率的将降帧处理
     */
    public long maxFps = 30;

}
