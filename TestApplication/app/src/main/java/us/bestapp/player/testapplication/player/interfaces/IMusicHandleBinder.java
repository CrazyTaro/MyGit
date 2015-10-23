package us.bestapp.player.testapplication.player.interfaces;

import android.os.IBinder;

import java.util.List;

import us.bestapp.player.testapplication.player.model.AbsMusic;
import us.bestapp.player.testapplication.player.model.PlayModel;

/**
 * Created by xuhaolin on 15/10/21.
 * 与音乐有关的操作事件(非播放器操作)
 */
public interface IMusicHandleBinder extends IBinder {
    /**
     * 销毁binder,将所有引用置null
     */
    public void destory();

    /**
     * 设置播放模式,<font color="#ff9900"><b>此方法可能会抛出异常,当参数不合法时</b></font><br/>
     *
     * @param model {@link PlayModel#MODEL_SINGLE}单曲循环<br/>
     *              {@link PlayModel#MODEL_ORDER}列表循环<br/>
     *              {@link PlayModel#MODEL_RANDOM}随机播放<br/>
     */
    public void setPlayModel(String model);

    /**
     * 绑定与播放模式关联的播放列表及历史列表,当参数为null时,不处理对应的参数数据;<font color="#ff9900"><b>当同时需要更新两个列表时,请一起更新,在 #循环模式# 中数据播放列表依赖于历史列表</b></font>
     *
     * @param playList    播放列表
     * @param historyList 历史列表
     */
    public void setPlayListWidthHistoryList(List<? extends AbsMusic> playList, List<? extends AbsMusic> historyList);

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
     * 获取历史列表操作接口
     *
     * @return
     */
    public IHistoryListHandle getHistoryListHandle();

    /**
     * 获取服务绑定的与播放器关系的监听事件接口
     *
     * @return
     */
    public IMediaPlayerCallback getMediaPlayerCallback();
}
