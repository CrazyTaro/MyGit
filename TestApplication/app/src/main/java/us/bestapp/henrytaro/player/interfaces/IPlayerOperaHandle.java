package us.bestapp.henrytaro.player.interfaces;

import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/18.
 */
public interface IPlayerOperaHandle {
    /**
     * 播放当前音乐(若当前音乐不存在尝试按默认方式默认一首音乐)
     */
    void play();

    /**
     * 暂停播放,在暂停状态调用则再次进行播放
     */
    void pause();

    /**
     * 上一首(可播放历史记录)
     */
    void previous();

    /**
     * 下一首
     */
    void next();

    /**
     * 播放指定音乐
     *
     * @param source 音乐来源
     * @param index  此音乐在列表中的位置
     */
    void play(AbsTrack source, int index);

    /**
     * 更新播放进度
     */
    void seekTo(int position);

    /**
     * 是否正在播放
     */
    boolean isPlaying();

    /**
     * 获取当前播放的时间点
     */
    int getCurrentPosition();

    /**
     * 获取当前音乐播放的时长,当时长不可用时,返回-1
     */
    int getDuration();

    /**
     * 销毁更新线程
     */
    void destoryUpdateThread();

    /**
     * 创建并开始更新线程
     */
    void createUpdateThread();

    /**
     * 暂停更新线程(不销毁,实际上只是暂停了更新通知,但线程并没有暂停)
     */
    boolean pauseUpdateNotify();


    /**
     * 继续更新线程(只有线程未被销毁且停止过的情况才有效)
     */
    boolean continueUpdateNotify();
}
