package us.bestapp.player.testapplication.player.service;

import android.os.Binder;

import java.util.List;

import us.bestapp.player.testapplication.player.interfaces.IHistoryListHandle;
import us.bestapp.player.testapplication.player.interfaces.IMediaPlayerCallback;
import us.bestapp.player.testapplication.player.interfaces.IMusicHandleBinder;
import us.bestapp.player.testapplication.player.interfaces.IPlayCallback;
import us.bestapp.player.testapplication.player.interfaces.IPlayListHandle;
import us.bestapp.player.testapplication.player.interfaces.IPlayerOperaHandle;
import us.bestapp.player.testapplication.player.model.AbsMusic;
import us.bestapp.player.testapplication.player.model.PlayModel;
import us.bestapp.player.testapplication.player.utils.HistoryListUtils;
import us.bestapp.player.testapplication.player.utils.PlayListUtils;

/**
 * Created by xuhaolin on 15/10/21.
 * 绑定服务并进行一些某些数据处理管理的Binder
 */
public class PlayBinder extends Binder implements IMusicHandleBinder {

    //唯一对象
    private static PlayBinder mInstance;
    //绑定的服务
    private PlaySerive mService;
    //播放列表处理
    private PlayListUtils mPlayList;
    //历史列表处理
    private HistoryListUtils mHistoryList;
    //播放模式,用于处理下一首歌曲
    private PlayModel mPlayMode;
    //播放回调
    private IPlayCallback mCallback;

    private PlayBinder(PlaySerive service) {
        this.mService = service;
        mHistoryList = new HistoryListUtils();
        mPlayList = new PlayListUtils(null);
        //播放模式与播放列表及历史列表进行绑定
        mPlayMode = new PlayModel(mPlayList.getPlayList(false, null), mHistoryList.getHistoryList());
        //历史列表与播放模式进行关联,更新历史列表时进行调整数据处理
        mHistoryList.addHistoryListUpdateListener(mPlayMode);
        //播放列表与播放模式进行关联,更新播放列表时进行调整数据处理
        mPlayList.addPlayListUpdateListener(mPlayMode);
        //设置当前默认开始的歌曲为第一首
        mPlayList.setFirstBeginMusic(PlayListUtils.MODEL_BEGIN_FIRST);
    }

    /**
     * 获取此服务绑定的唯一的数据Binder
     *
     * @param service
     * @return
     */
    public static synchronized PlayBinder getInstance(PlaySerive service) {
        if (mInstance == null) {
            mInstance = new PlayBinder(service);
        }
        return mInstance;
    }

    /**
     * 将指定音乐或当前音乐添加到历史列表中
     *
     * @param music 若此参数为null则将当前音乐添加到历史列表中
     * @return
     */
    public boolean putHistoryMusic(AbsMusic music) {
        if (music == null) {
            //若参数为null获取当前播放列表
            music = mPlayList.getCurrentMusic();
        }
        return mHistoryList.addLastPlayMusic(music);
    }

    /**
     * 设置当前播放的音乐的索引(此索引只在顺序播放的时候有效,其它模式不需要索引)
     *
     * @param index
     */
    public void setCurrentPlayIndex(int index) {
        if (mPlayMode.getCurrentModel().equals(PlayModel.MODEL_ORDER)) {
            mPlayMode.setCurrentPlayIndex(index);
        }
    }

    @Override
    public void destory() {
        this.mHistoryList = null;
        this.mPlayList = null;
        this.mPlayMode = null;
        this.mService = null;
        mInstance = null;
    }

    @Override
    public void setPlayModel(String model) {
        mPlayMode.setCurrentModel(model);
    }

    @Override
    public void setPlayListWidthHistoryList(List<? extends AbsMusic> playList, List<? extends AbsMusic> historyList) {
        if (historyList != null) {
            mHistoryList.setHistoryList(historyList);
            //更新历史列表
            mPlayMode.updateMusicList(mPlayMode.getPlayList(), historyList);
        }

        if (playList != null) {
            mPlayList.setPlayList(playList);
            //更新播放列表
            mPlayMode.updateMusicList(playList, mPlayMode.getHistoryList());
        }
    }

    @Override
    public void setPlayCallback(IPlayCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public IPlayCallback getPlayCallback() {
        return this.mCallback;
    }

    @Override
    public IPlayerOperaHandle getOperaHandle() {
        return mService;
    }

    @Override
    public IPlayListHandle getPlayListHandle() {
        return this.mPlayList;
    }

    @Override
    public IHistoryListHandle getHistoryListHandle() {
        return this.mHistoryList;
    }

    @Override
    public IMediaPlayerCallback getMediaPlayerCallback() {
        return mService;
    }

    /**
     * 设置当前的音乐(此处指即将播放的音乐),此方法主要是用于暂存当前播放音乐的数据及状态
     *
     * @param currentPlay 当前播放的音乐,当此参数为null时返回false
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setCurrentMusic(AbsMusic currentPlay) {
        return mPlayList.setCurrentMusic(currentPlay);
    }

    /**
     * 获取当前的音乐(正在播放或者是即将播放的音乐)
     *
     * @return
     */
    public AbsMusic getCurrentMusic() {
        return mPlayList.getCurrentMusic();
    }

    /**
     * 获取下一首音乐,音乐的生成规则是由播放模式决定的{@link us.bestapp.player.testapplication.player.model.PlayModel},通过设置播放模式从而更改生成音乐的规则
     *
     * @return
     */
    public AbsMusic getNextMusic() {
        if (mPlayList.isPlayListEmpty() && mCallback != null) {
            mCallback.onPlayListEmpty();
        }
        AbsMusic next = mPlayMode.getNextMusic(mPlayList.getCurrentMusic());
        if (next == null && mCallback != null) {
            mCallback.onFailToGetNextMusic("can not get next music");
        }
        return next;
    }

    /***
     * 获取上一首播放过的音乐;此方法是获取最后一首播放历史的音乐;<font color="#ff9900"><b>当已经在播放历史音乐时使用此方法,会尝试回溯整个历史列表直到历史列表播放完为止(当播放到历史列表的第一首歌曲时,将重复此曲)</b></font>
     *
     * @return
     */
    public AbsMusic getPreviousMusic() {
        return mHistoryList.getHistoryPlayMusic(mPlayList.getCurrentMusic());
    }
}
