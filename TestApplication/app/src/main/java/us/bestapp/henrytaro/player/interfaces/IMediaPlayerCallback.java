package us.bestapp.henrytaro.player.interfaces;

import android.media.MediaPlayer;

/**
 * Created by xuhaolin on 15/10/21.
 * 与播放器关联的监听事件
 */
public interface IMediaPlayerCallback {
    /**
     * 设置播放完毕监听事件
     *
     * @param listener         自定义监听事件
     * @param isRemoveOrigianl 是否移除默认的监听事件(建议非必要情况下不移除,若移除可能出现后台播放音乐完毕时无法自动切换到下一首)
     */
    void setOnCompletitonListener(MediaPlayer.OnCompletionListener listener, boolean isRemoveOrigianl);

    /**
     * 设置缓冲数据更新事件
     */
    void setOnBufferUpdateListener(MediaPlayer.OnBufferingUpdateListener listener);

    /**
     * 设置出错监听事件
     */
    void setOnErrorListener(MediaPlayer.OnErrorListener listener);

    /**
     * 设置预加载完成监听事件
     */
    void setOnPreparedListener(MediaPlayer.OnPreparedListener listener);

    /**
     * 设置音乐信息相关监听事件
     */
    void setOnInfoListener(MediaPlayer.OnInfoListener listener);

    /**
     * 设置拖动进度条完成时的监听事件
     */
    void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener);

    /**
     * 设置视频显示大小更改的监听事件
     */
    void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener);

    /**
     * 设置播放器状态切换监听事件
     */
    void setOnPlayerStatusChangedListener(us.bestapp.henrytaro.player.interfaces.IPlayCallback.onPlayerStatusChangedListener listener);

    /**
     * 添加进度监听事件
     */
    boolean addOnProgressUpdateListener(us.bestapp.henrytaro.player.interfaces.IPlayCallback.onProgressUpdateListener listener);

    /**
     * 移除进度监听事件
     */
    boolean removeOnProgressUpdateListener(us.bestapp.henrytaro.player.interfaces.IPlayCallback.onProgressUpdateListener listener);

}
