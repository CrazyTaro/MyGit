package us.bestapp.player.testapplication.player.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import us.bestapp.player.testapplication.player.interfaces.IPlayListHandle;
import us.bestapp.player.testapplication.player.model.AbsMusic;
import us.bestapp.player.testapplication.player.model.PlayModel;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放列表管理
 */
public class PlayListUtils implements IPlayListHandle {

    /**
     * 从第一首开始
     */
    public static final int MODEL_BEGIN_FIRST = Integer.MIN_VALUE;
    /**
     * 从最后一首开始
     */
    public static final int MODEL_BEGIN_LAST = Integer.MAX_VALUE;

    //播放列表
    private List<AbsMusic> mAbsMusicList;
    //排序标准
    private Comparator<? super AbsMusic> mComparator;
    //列表更新监听事件
    private List<IPlayListHandle.IPlayListUpdateListener> mPlayListUpdateCallbackList;

    //是否通知回调
    private boolean mIsNotify = true;
    //是否已排序
    private boolean mIsSorted = false;
    //当前播放的音乐
    private AbsMusic mCurrentPlay;
    private int mCurrentIndex;

    //排序锁
    private Lock mComparatorLock = new ReentrantLock();
    //列表更新锁
    private Lock mPlayListLock = new ReentrantLock();

    /**
     * 构造函数,播放列表管理,此类不处理下一首的功能(由播放模式{@link PlayModel}处理),也不处理上一首的功能(由历史列表处理{@link HistoryListUtils}).此类主要是对当前播放列表的数据进行管理和操作
     *
     * @param cp 列表排序标准,可为null,若为null则不排序,按添加的音乐顺序处理
     */
    public PlayListUtils(Comparator<? super AbsMusic> cp) {
        mAbsMusicList = new LinkedList<>();
        mPlayListUpdateCallbackList = new LinkedList<>();
        this.mComparator = cp;
    }

    /**
     * 添加新音乐到列表中
     *
     * @param absMusic 添加的音乐
     */
    private void addMusic(AbsMusic absMusic) {
        mPlayListLock.lock();
        mAbsMusicList.add(absMusic);
        mPlayListLock.unlock();
        mIsSorted = false;
        //添加完音乐回调事件
        if (mPlayListUpdateCallbackList.size() > 0 && mIsNotify) {
            for (IPlayListHandle.IPlayListUpdateListener listener : mPlayListUpdateCallbackList)
                listener.onAddNewMusic(absMusic);
        }
    }

    /**
     * 移除音乐
     *
     * @param absMusic 需要被移除的音乐
     * @return
     */
    private boolean removeMusic(AbsMusic absMusic) {
        //是否成功移除
        boolean isSucceed = false;
        mPlayListLock.lock();
        //尝试移除
        isSucceed = mAbsMusicList.remove(absMusic);
        mPlayListLock.unlock();
        //移除回调事件
        if (mPlayListUpdateCallbackList.size() > 0 && mIsNotify) {
            for (IPlayListHandle.IPlayListUpdateListener listener : mPlayListUpdateCallbackList)
                listener.onRemoveOldMusic(absMusic);
        }
        return isSucceed;
    }

    /**
     * 设置当前的音乐
     */
    public boolean setCurrentMusic(AbsMusic music) {
        if (music == null) {
            return false;
        } else {
            //尝试将当前音乐添加到播放列表中
            addNewMusic(music);
            this.mCurrentPlay = music;
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
    public boolean setFirstBeginMusic(int beginModel) {
        //列表为空,不处理
        if (isPlayListEmpty()) {
            return false;
        }
        boolean isSetSucceed = false;
        switch (beginModel) {
            case MODEL_BEGIN_FIRST:
                mCurrentPlay = mAbsMusicList.get(0);
                mCurrentIndex = 0;
                mCurrentPlay.index = mCurrentIndex;
                isSetSucceed = true;
                break;
            case MODEL_BEGIN_LAST:
                mCurrentPlay = mAbsMusicList.get(mAbsMusicList.size() - 1);
                mCurrentIndex = mAbsMusicList.size() - 1;
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
    public void sortPlayList(List<AbsMusic> playList, Comparator<? super AbsMusic> cp) {
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

    @Override
    public void setComparator(Comparator<? super AbsMusic> cp) {
        mComparatorLock.lock();
        this.mComparator = cp;
        mComparatorLock.unlock();
    }

    @Override
    public boolean addNewMusic(AbsMusic absMusic) {
        if (absMusic == null) {
            return false;
        }
        //当前列表中是否包含此音乐
        //是则不添加
        if (mAbsMusicList.contains(absMusic)) {
            return false;
        } else {
            //否则添加音乐
            addMusic(absMusic);
            return true;
        }
    }

    @Override
    public boolean addNewMusic(AbsMusic absMusic, boolean isMandatory) {
        if (absMusic == null) {
            return false;
        }
        //强制性添加音乐,不管当前音乐是否在列表中已存在
        if (isMandatory) {
            addMusic(absMusic);
            return true;
        } else {
            return addNewMusic(absMusic);
        }
    }

    @Override
    public void setPlayList(List<? extends AbsMusic> playList) {
        mPlayListLock.lock();
        mAbsMusicList.clear();
        mAbsMusicList.addAll(playList);
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onResetPlayList(playList);
        }
    }

    @Override
    public void clearPlayList() {
        mPlayListLock.lock();
        mAbsMusicList.clear();
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onPlayListClear();
        }
    }

    @Override
    public void appendPlayList(List<? extends AbsMusic> appendList, boolean isMandatory) {
        if (appendList == null || appendList.size() <= 0) {
            return;
        }
        mPlayListLock.lock();
        if (isMandatory) {
            for (AbsMusic m : appendList) {
                mAbsMusicList.add(m);
            }
        } else {
            for (AbsMusic m : appendList) {
                mAbsMusicList.add(m);
            }
        }
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onAppendMusicList(appendList);
        }
    }

    @Override
    public boolean removeOldMusic(AbsMusic absMusic) {
        return removeMusic(absMusic);
    }

    @Override
    public void removeOldMusicList(List<? extends AbsMusic> removeList) {
        if (removeList == null || removeList.size() <= 0) {
            return;
        }
        mPlayListLock.lock();
        for (AbsMusic m : removeList) {
            mAbsMusicList.remove(m);
        }
        mPlayListLock.unlock();
        for (IPlayListUpdateListener listener : mPlayListUpdateCallbackList) {
            listener.onRemoveMusicList(removeList);
        }
    }

    @Override
    public List<AbsMusic> getPlayList(boolean isNeedSort, Comparator<? super AbsMusic> cp) {
        //尝试排序
        if (isNeedSort && !mIsSorted) {
            mPlayListLock.lock();
            sortPlayList(mAbsMusicList, cp);
            mPlayListLock.unlock();
            //排序完成
            mIsSorted = true;
        }
        return mAbsMusicList;
    }

    @Override
    public AbsMusic getCurrentMusic() {
        return this.mCurrentPlay;
    }

    @Override
    public List<AbsMusic> searchMusic(List<AbsMusic> searchResult, String keyStr) {
        if (searchResult == null) {
            searchResult = new ArrayList<>(mAbsMusicList.size() / 2);
        }
        //搜索操作
        for (int i = 0; i < mAbsMusicList.size(); i++) {
            AbsMusic music = mAbsMusicList.get(i);
            if (music.isMatchSearch(keyStr)) {
                searchResult.add(music);
            }
        }
        return searchResult;
    }

    @Override
    public boolean isPlayListEmpty() {
        if (mAbsMusicList.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addPlayListUpdateListener(IPlayListHandle.IPlayListUpdateListener listener) {
        mPlayListUpdateCallbackList.add(listener);
    }

    @Override
    public void removePlayListUpdateListener(IPlayListHandle.IPlayListUpdateListener listener) {
        mPlayListUpdateCallbackList.remove(listener);
    }
}
