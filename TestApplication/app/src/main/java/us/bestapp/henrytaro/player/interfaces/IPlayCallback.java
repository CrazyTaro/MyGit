package us.bestapp.henrytaro.player.interfaces;

import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/20.
 * 播放出错或相关信息回调处理
 */
public interface IPlayCallback {
    /**
     * 出错标签,在播放时
     */
    public static final int IN_PLAY = 0x110;
    /**
     * 出错标签,在暂停时
     */
    public static final int IN_PAUSE = 0x120;

    /**
     * 出错标签,在停止音乐时
     */
    public static final int IN_STOP = 0x119;
    /**
     * 出错标签,在下一首切换时
     */
    public static final int IN_NEXT = 0x911;
    /**
     * 出错标签,在上一首切换时
     */
    public static final int IN_PREVIOUS = 0x10086;
    /**
     * 出错标签,在拖动进度条时
     */
    public static final int IN_SEEK_TO = 0x10000;

    /**
     * 播放出错
     *
     * @param errorMessage 出错信息(可能来自Exception,与可能是处理时出现的问题)
     * @param inWhichLink  在哪个环节出错,即出错标签<br/>
     *                     {@link #IN_PLAY}<br/>
     *                     {@link #IN_PAUSE}<br/>
     *                     {@link #IN_STOP}<br/>
     *                     {@link #IN_NEXT}<br/>
     *                     {@link #IN_PREVIOUS}<br/>
     *                     {@link #IN_SEEK_TO}<br/>
     * @param failTrack    播放失败的歌曲
     */
    void onFailPlay(String errorMessage, int inWhichLink, AbsTrack failTrack);

    /**
     * 音乐不存在,通常在播放时回调
     */
    void onTrackNotExsit();

    /***
     * 播放列表为空事件
     */
    void onPlayListEmpty();

    /**
     * 获取下一首歌曲失败
     */
    void onFailToGetNextTrack();

    /**
     * 服务销毁
     */
    void onServiceDestory();

    /**
     * 播放器状态切换监听事件
     */
    public interface onPlayerStatusChangedListener {

        /**
         * 播放器状态切换
         *
         * @param afterChanged 切换后的状态
         */
        void onPlayerStatusChanged(int afterChanged);
    }

    /**
     * 播放器当前播放进度监听事件
     */
    public interface onProgressUpdateListener {
        /**
         * 更新进度
         *
         * @param progress 当前进度
         * @param duration 时长
         */
        void onProgressUpdate(int progress, int duration);
    }
}
