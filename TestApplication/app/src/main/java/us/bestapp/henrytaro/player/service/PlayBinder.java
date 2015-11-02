package us.bestapp.henrytaro.player.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.service.media.MediaBrowserService;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import us.bestapp.henrytaro.player.interfaces.IPlayCallback;
import us.bestapp.henrytaro.player.interfaces.IPlayListHandle;
import us.bestapp.henrytaro.player.interfaces.IPlayerOperaHandle;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.model.AbsTrack;
import us.bestapp.henrytaro.player.utils.PlayListUtils;
import us.bestapp.henrytaro.player.interfaces.IMediaPlayerCallback;

/**
 * Created by xuhaolin on 15/10/21.
 * 绑定服务并进行一些某些数据处理管理的Binder
 */
public class PlayBinder extends Binder implements ITrackHandleBinder {

    //唯一对象
    private static PlayBinder mInstance;
    //绑定的服务
    private PlaySerive mService;
    //播放列表处理
    private PlayListUtils mPlayList;
    //播放回调
    private IPlayCallback mCallback;


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
     * 创建对象,初始化
     *
     * @param serive
     */
    private PlayBinder(PlaySerive serive) {
        this.mService = serive;
        mPlayList = new PlayListUtils();
    }

    /**
     * 回收资源
     */
    public void recycle() {
        mPlayList.recycle();
        mPlayList = null;
        mService = null;
    }

    /**
     * 获取播放列表操作工具类
     *
     * @return
     */
    public PlayListUtils getPlayList() {
        return this.mPlayList;
    }

    /**
     * 设置当前歌曲
     *
     * @param track
     * @return
     */
    public boolean setCurrentTrack(AbsTrack track) {
        return mPlayList.setCurrentPlayTrack(track, IPlayListHandle.INDEX_NO_USAGE);
    }

    /**
     * 设置当前歌曲的索引
     *
     * @param currentIndex
     */
    public void setCurrentPlayIndex(int currentIndex) {
        mPlayList.setCurrentPlayIndex(currentIndex);
    }

    /**
     * 获取当前的歌曲
     *
     * @return
     */
    public AbsTrack getCurrentTrack() {
        return mPlayList.getCurrentTrack();
    }

    /**
     * 获取下一首歌曲
     *
     * @return
     */
    public AbsTrack getNextTrack() {
        if (mPlayList.isPlayListEmpty() && mCallback != null) {
            mCallback.onPlayListEmpty();
            return null;
        } else {
            AbsTrack next = mPlayList.getNextTrack();
            if (next == null && mCallback != null) {
                mCallback.onFailToGetNextTrack();
            }
            return next;
        }
    }

    /**
     * 获取上一首歌曲
     */
    public AbsTrack getPreviousTrack() {
        return mPlayList.getPreviousTrack();
    }

    @Override
    public void destroy() {
        Intent stopService = new Intent(mService, PlaySerive.class);
        mService.stopService(stopService);
    }

    @Override
    public void setPlayListWithTrackPlayNow(List<? extends AbsTrack> newPlayList, AbsTrack playNow, int nowIndex) {
        mPlayList.setPlayListWithTrackPlayNow(newPlayList, playNow, nowIndex);
        getOperaHandle().play();
    }

    @Override
    public void setPlayCallback(IPlayCallback callback) {
        mCallback = callback;
    }

    @Override
    public IPlayCallback getPlayCallback() {
        return mCallback;
    }

    @Override
    public IPlayerOperaHandle getOperaHandle() {
        return mService;
    }

    @Override
    public IPlayListHandle getPlayListHandle() {
        return mPlayList;
    }

    @Override
    public IMediaPlayerCallback getMediaPlayerCallback() {
        return mService;
    }

    @Override
    public void printPlayList() {
        List<AbsTrack> playList = mPlayList.getPlayList();
        if (playList != null) {
            for (int i = 0; i < playList.size(); i++) {
                Log.i("playList", i + "  " + playList.get(i).toString());
            }
        }
    }

}
