package us.bestapp.henrytaro.player.interfaces;

import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.List;

import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/21.
 * 与音乐有关的操作事件(非播放器操作)
 */
public interface ITrackHandleBinder extends IBinder {

    /**
     * 销毁binder,将所有引用置null
     */
    public void destroy();

    /**
     * 设置新的播放列表同时立即播放指定歌曲(使用此方法可以立即播放该歌曲)
     *
     * @param newPlayList 新的播放列表
     * @param playNow     需要立即播放的歌曲
     * @param nowIndex    当前立即播放歌曲的索引
     */
    public void setPlayListWithTrackPlayNow(List<? extends AbsTrack> newPlayList, AbsTrack playNow, int nowIndex);

    /**
     * 设置播放回调事件(处理某些错误或信息)
     *
     * @param callback
     */
    public void setPlayCallback(IPlayCallback callback);

    /**
     * 获取播放回调事件
     *
     * @return
     */
    public IPlayCallback getPlayCallback();

    /**
     * 获取服务绑定的操作接口
     *
     * @return
     */
    public IPlayerOperaHandle getOperaHandle();

    /**
     * 获取播放列表操作接口
     */
    public IPlayListHandle getPlayListHandle();

    /**
     * 获取服务绑定的与播放器关系的监听事件接口
     *
     * @return
     */
    public IMediaPlayerCallback getMediaPlayerCallback();

    public void printPlayList();
}
