package us.bestapp.henrytaro.player.service;

import android.os.Binder;
import android.util.Log;

import java.util.List;

/**
 * Created by xuhaolin on 15/10/21.
 * 绑定服务并进行一些某些数据处理管理的Binder
 */
public class PlayBinder extends Binder implements us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder {

    //唯一对象
    private static PlayBinder mInstance;
    //绑定的服务
    private us.bestapp.henrytaro.player.service.PlaySerive mService;
    //播放列表处理
    private us.bestapp.henrytaro.player.utils.PlayListUtils mPlayList;
    //历史列表处理
    private us.bestapp.henrytaro.player.utils.HistoryListUtils mHistoryList;
    //播放模式,用于处理下一首歌曲
    private us.bestapp.henrytaro.player.model.PlayModel mPlayMode;
    //播放回调
    private us.bestapp.henrytaro.player.interfaces.IPlayCallback mCallback;

    private PlayBinder(us.bestapp.henrytaro.player.service.PlaySerive service) {
        this.mService = service;
        mHistoryList = new us.bestapp.henrytaro.player.utils.HistoryListUtils();
        mPlayList = new us.bestapp.henrytaro.player.utils.PlayListUtils(null);
        //播放模式与播放列表及历史列表进行绑定
        mPlayMode = new us.bestapp.henrytaro.player.model.PlayModel(mPlayList.getPlayList(false, null), mHistoryList.getHistoryList());
        //历史列表与播放模式进行关联,更新历史列表时进行调整数据处理
        mHistoryList.addHistoryListUpdateListener(mPlayMode);
        //播放列表与播放模式进行关联,更新播放列表时进行调整数据处理
        mPlayList.addPlayListUpdateListener(mPlayMode);
        //设置当前默认开始的歌曲为第一首
        mPlayList.setFirstBeginTrack(us.bestapp.henrytaro.player.utils.PlayListUtils.MODEL_BEGIN_FIRST);
    }

    /**
     * 获取此服务绑定的唯一的数据Binder
     *
     * @param service
     * @return
     */
    public static synchronized PlayBinder getInstance(us.bestapp.henrytaro.player.service.PlaySerive service) {
        if (mInstance == null) {
            mInstance = new PlayBinder(service);
        }
        return mInstance;
    }

    /**
     * 将指定音乐或当前音乐添加到历史列表中
     *
     * @param track 若此参数为null则将当前音乐添加到历史列表中
     * @return
     */
    public boolean putHistoryTrack(us.bestapp.henrytaro.player.model.AbsTrack track) {
        if (track == null) {
            //若参数为null获取当前播放列表
            track = mPlayList.getCurrentTrack();
        }
        return mHistoryList.addLastPlayTrack(track);
    }

    /**
     * 设置当前播放的音乐的索引(此索引只在顺序播放的时候有效,其它模式不需要索引)
     *
     * @param index
     */
    public void setCurrentPlayIndex(int index) {
        if (mPlayMode.getCurrentModel().equals(us.bestapp.henrytaro.player.model.PlayModel.MODEL_ORDER)) {
            mPlayMode.setCurrentPlayIndex(index);
        }
    }

    /**
     * 回收释放资源
     */
    public void recycle() {
        this.mPlayMode.recyle();
        this.mHistoryList.recycle();
        this.mPlayList.recycle();
        this.mHistoryList = null;
        this.mPlayList = null;
        this.mPlayMode = null;
        mInstance = null;
    }

    @Override
    public void destory() {
        this.mService.stopSelf();
    }

    @Override
    public void setPlayModel(String model) {
        mPlayMode.setCurrentModel(model);
    }

    @Override
    public void setPlayListWidthHistoryList(List<? extends us.bestapp.henrytaro.player.model.AbsTrack> playList, List<? extends us.bestapp.henrytaro.player.model.AbsTrack> historyList) {
        if (historyList != null) {
            mHistoryList.setHistoryList(historyList);
            //更新历史列表
            mPlayMode.updateTrackList(mPlayMode.getPlayList(), historyList);
        }

        if (playList != null) {
            mPlayList.setPlayList(playList);
            //更新播放列表
            mPlayMode.updateTrackList(playList, mPlayMode.getHistoryList());
        }
    }

    @Override
    public void setPlayCallback(us.bestapp.henrytaro.player.interfaces.IPlayCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public us.bestapp.henrytaro.player.interfaces.IPlayCallback getPlayCallback() {
        return this.mCallback;
    }

    @Override
    public us.bestapp.henrytaro.player.interfaces.IPlayerOperaHandle getOperaHandle() {
        return mService;
    }

    @Override
    public us.bestapp.henrytaro.player.interfaces.IPlayListHandle getPlayListHandle() {
        return this.mPlayList;
    }

    @Override
    public us.bestapp.henrytaro.player.interfaces.IHistoryListHandle getHistoryListHandle() {
        return this.mHistoryList;
    }

    @Override
    public us.bestapp.henrytaro.player.interfaces.IMediaPlayerCallback getMediaPlayerCallback() {
        return mService;
    }

    /**
     * 设置当前的音乐(此处指即将播放的音乐),此方法主要是用于暂存当前播放音乐的数据及状态
     *
     * @param currentPlay 当前播放的音乐,当此参数为null时返回false
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setCurrentTrack(us.bestapp.henrytaro.player.model.AbsTrack currentPlay) {
        return mPlayList.setCurrentTrack(currentPlay);
    }

    /**
     * 获取当前的音乐(正在播放或者是即将播放的音乐)
     *
     * @return
     */
    public us.bestapp.henrytaro.player.model.AbsTrack getCurrentTrack() {
        return mPlayList.getCurrentTrack();
    }

    /**
     * 获取下一首音乐,音乐的生成规则是由播放模式决定的{@link us.bestapp.henrytaro.player.model.PlayModel},通过设置播放模式从而更改生成音乐的规则
     *
     * @return
     */
    public us.bestapp.henrytaro.player.model.AbsTrack getNextTrack() {
        if (mPlayList.isPlayListEmpty() && mCallback != null) {
            mCallback.onPlayListEmpty();
        }
        us.bestapp.henrytaro.player.model.AbsTrack next = mPlayMode.getNextTrack(mPlayList.getCurrentTrack());
        if (next == null && mCallback != null) {
            mCallback.onFailToGetNextTrack("can not get next track");
        }
        return next;
    }

    /***
     * 获取上一首播放过的音乐;此方法是获取最后一首播放历史的音乐;<font color="#ff9900"><b>当已经在播放历史音乐时使用此方法,会尝试回溯整个历史列表直到历史列表播放完为止(当播放到历史列表的第一首歌曲时,将重复此曲)</b></font>
     *
     * @return
     */
    public us.bestapp.henrytaro.player.model.AbsTrack getPreviousTrack() {
        return mHistoryList.getHistoryPlayTrack(mPlayList.getCurrentTrack());
    }

    public void printPlayList() {
        List<us.bestapp.henrytaro.player.model.AbsTrack> playList = mPlayList.getPlayList();
        if (playList != null) {
            for (int i = 0; i < playList.size(); i++) {
                Log.i("playList", i + "  " + playList.get(i).toString());
            }
        }
    }

    public void printHistoryList() {
        List<us.bestapp.henrytaro.player.model.AbsTrack> historyList = mHistoryList.getHistoryList();
        if (historyList != null) {
            for (int i = 0; i < historyList.size(); i++) {
                Log.i("historyList", i + "  " + historyList.get(i).toString());
            }
        }
    }
}
