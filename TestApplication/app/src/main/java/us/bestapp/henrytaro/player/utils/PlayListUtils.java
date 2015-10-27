package us.bestapp.henrytaro.player.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放列表管理
 */
public class PlayListUtils implements us.bestapp.henrytaro.player.interfaces.IPlayListHandle {

    /**
     * 从第一首开始
     */
    public static final int MODEL_BEGIN_FIRST = Integer.MIN_VALUE;
    /**
     * 从最后一首开始
     */
    public static final int MODEL_BEGIN_LAST = Integer.MAX_VALUE;

    //播放列表
    private List<us.bestapp.henrytaro.player.model.AbsTrack> mAbsTrackList;
    //排序标准
    private Comparator<? super us.bestapp.henrytaro.player.model.AbsTrack> mComparator;
    //列表更新监听事件
    private List<IPlayListUpdateListener> mPlayListUpdateCallbackList;

    //是否通知回调
    private boolean mIsNotify = true;
    //是否已排序
    private boolean mIsSorted = false;
    //当前播放的音乐
    private us.bestapp.henrytaro.player.model.AbsTrack mCurrentPlay;
    private int mCurrentIndex;

    //排序锁
    private Lock mComparatorLock = new ReentrantLock();
    //列表更新锁
    private Lock mPlayListLock = new ReentrantLock();

    /**
     * 构造函数,播放列表管理,此类不处理下一首的功能(由播放模式{@link us.bestapp.henrytaro.player.model.PlayModel}处理),也不处理上一首的功能(由历史列表处理{@link us.bestapp.henrytaro.player.utils.HistoryListUtils}).此类主要是对当前播放列表的数据进行管理和操作
     *
     * @param cp 列表排序标准,可为null,若为null则不排序,按添加的音乐顺序处理
     */
    public PlayListUtils(Comparator<? super us.bestapp.henrytaro.player.model.AbsTrack> cp) {
        mAbsTrackList = new LinkedList<>();
        mPlayListUpdateCallbackList = new LinkedList<>();
        this.mComparator = cp;
    }

    /**
     * 添加新音乐到列表中
     *
     * @param absTrack 添加的音乐
     */
    private void addTrack(us.bestapp.henrytaro.player.model.AbsTrack absTrack) {
        mPlayListLock.lock();
        mAbsTrackList.add(absTrack);
        mPlayListLock.unlock();
        mIsSorted = false;
        //添加完音乐回调事件
        if (mPlayListUpdateCallbackList.size() > 0 && mIsNotify) {
            for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList)
                listener.onAddNewTrack(absTrack);
        }
    }

    /**
     * 移除音乐
     *
     * @param absTrack 需要被移除的音乐
     * @return
     */
    private boolean removeTrack(us.bestapp.henrytaro.player.model.AbsTrack absTrack) {
        //是否成功移除
        boolean isSucceed = false;
        mPlayListLock.lock();
        //尝试移除
        isSucceed = mAbsTrackList.remove(absTrack);
        mPlayListLock.unlock();
        //移除回调事件
        if (mPlayListUpdateCallbackList.size() > 0 && mIsNotify) {
            for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList)
                listener.onRemoveOldTrack(absTrack);
        }
        return isSucceed;
    }

    /**
     * 设置当前的音乐
     */
    public boolean setCurrentTrack(us.bestapp.henrytaro.player.model.AbsTrack track) {
        if (track == null) {
            return false;
        } else {
            //尝试将当前音乐添加到播放列表中
            addNewTrack(track);
            this.mCurrentPlay = track;
            return true;
        }
    }

    /**
     * 设置第一首音乐
     *
     * @param beginModel 设置模式<br/>
     *                   {@link #MODEL_BEGIN_FIRST}<br/>
     *                   {@link #MODEL_BEGIN_LAST}<br/>
     * @return
     */
    public boolean setFirstBeginTrack(int beginModel) {
        //列表为空,不处理
        if (isPlayListEmpty()) {
            return false;
        }
        boolean isSetSucceed = false;
        switch (beginModel) {
            case MODEL_BEGIN_FIRST:
                mCurrentPlay = mAbsTrackList.get(0);
                mCurrentIndex = 0;
                mCurrentPlay.index = mCurrentIndex;
                isSetSucceed = true;
                break;
            case MODEL_BEGIN_LAST:
                mCurrentPlay = mAbsTrackList.get(mAbsTrackList.size() - 1);
                mCurrentIndex = mAbsTrackList.size() - 1;
                mCurrentPlay.index = mCurrentIndex;
                isSetSucceed = true;
                break;
        }
        return isSetSucceed;
    }

    /**
     * 排序
     *
     * @param playList 排序指定的播放列表
     * @param cp       排序规则
     */
    public void sortPlayList(List<us.bestapp.henrytaro.player.model.AbsTrack> playList, Comparator<? super us.bestapp.henrytaro.player.model.AbsTrack> cp) {
        if (cp == null) {
            cp = mComparator;
        }

        if (cp != null) {
            mComparatorLock.lock();
            Collections.sort(playList, cp);
            mComparatorLock.unlock();
        } else {
            throw new RuntimeException("can not use a null comparator to sort");
        }
    }


    /**
     * 回收释放所有资源
     */
    public void recycle() {
        if (mComparator != null) {
            mComparator = null;
        }
        mCurrentPlay = null;
        mAbsTrackList.clear();
        mAbsTrackList = null;
    }

    @Override
    public void setComparator(Comparator<? super us.bestapp.henrytaro.player.model.AbsTrack> cp) {
        mComparatorLock.lock();
        this.mComparator = cp;
        mComparatorLock.unlock();
    }

    @Override
    public boolean addNewTrack(us.bestapp.henrytaro.player.model.AbsTrack absTrack) {
        if (absTrack == null) {
            return false;
        }
        //当前列表中是否包含此音乐
        //是则不添加
        if (mAbsTrackList.contains(absTrack)) {
            return false;
        } else {
            //否则添加音乐
            addTrack(absTrack);
            return true;
        }
    }

    @Override
    public boolean addNewTrack(us.bestapp.henrytaro.player.model.AbsTrack absTrack, boolean isMandatory) {
        if (absTrack == null) {
            return false;
        }
        //强制性添加音乐,不管当前音乐是否在列表中已存在
        if (isMandatory) {
            addTrack(absTrack);
            return true;
        } else {
            return addNewTrack(absTrack);
        }
    }

    @Override
    public void setPlayList(List<? extends us.bestapp.henrytaro.player.model.AbsTrack> playList) {
        mPlayListLock.lock();
        mAbsTrackList.clear();
        mAbsTrackList.addAll(playList);
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onResetPlayList(playList);
        }
    }

    @Override
    public void clearPlayList() {
        mPlayListLock.lock();
        mAbsTrackList.clear();
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onPlayListClear();
        }
    }

    @Override
    public void appendPlayList(List<? extends us.bestapp.henrytaro.player.model.AbsTrack> appendList, boolean isMandatory) {
        if (appendList == null || appendList.size() <= 0) {
            return;
        }
        mPlayListLock.lock();
        if (isMandatory) {
            for (us.bestapp.henrytaro.player.model.AbsTrack m : appendList) {
                mAbsTrackList.add(m);
            }
        } else {
            for (us.bestapp.henrytaro.player.model.AbsTrack m : appendList) {
                mAbsTrackList.add(m);
            }
        }
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onAppendTrackList(appendList);
        }
    }

    @Override
    public boolean removeOldTrack(us.bestapp.henrytaro.player.model.AbsTrack absTrack) {
        return removeTrack(absTrack);
    }

    @Override
    public void removeOldTrackList(List<? extends us.bestapp.henrytaro.player.model.AbsTrack> removeList) {
        if (removeList == null || removeList.size() <= 0) {
            return;
        }
        mPlayListLock.lock();
        for (us.bestapp.henrytaro.player.model.AbsTrack m : removeList) {
            mAbsTrackList.remove(m);
        }
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onRemoveTrackList(removeList);
        }
    }

    @Override
    public List<us.bestapp.henrytaro.player.model.AbsTrack> getPlayList(boolean isNeedSort, Comparator<? super us.bestapp.henrytaro.player.model.AbsTrack> cp) {
        //尝试排序
        if (isNeedSort && !mIsSorted) {
            mPlayListLock.lock();
            sortPlayList(mAbsTrackList, cp);
            mPlayListLock.unlock();
            //排序完成
            mIsSorted = true;
        }
        return mAbsTrackList;
    }

    @Override
    public List<us.bestapp.henrytaro.player.model.AbsTrack> getPlayList() {
        return mAbsTrackList;
    }

    @Override
    public us.bestapp.henrytaro.player.model.AbsTrack getCurrentTrack() {
        return this.mCurrentPlay;
    }

    @Override
    public List<us.bestapp.henrytaro.player.model.AbsTrack> searchTrack(List<us.bestapp.henrytaro.player.model.AbsTrack> searchResult, String keyStr) {
        if (searchResult == null) {
            searchResult = new ArrayList<>(mAbsTrackList.size() / 2);
        }
        //搜索操作
        for (int i = 0; i < mAbsTrackList.size(); i++) {
            us.bestapp.henrytaro.player.model.AbsTrack track = mAbsTrackList.get(i);
            if (track.isMatchSearch(keyStr)) {
                searchResult.add(track);
            }
        }
        return searchResult;
    }

    @Override
    public boolean isPlayListEmpty() {
        if (mAbsTrackList.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addPlayListUpdateListener(IPlayListUpdateListener listener) {
        mPlayListUpdateCallbackList.add(listener);
    }

    @Override
    public void removePlayListUpdateListener(IPlayListUpdateListener listener) {
        mPlayListUpdateCallbackList.remove(listener);
    }
}
