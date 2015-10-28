package us.bestapp.henrytaro.player.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import us.bestapp.henrytaro.player.interfaces.IPlayListHandle;
import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放列表管理
 */
public class PlayListUtils implements IPlayListHandle {

    //播放列表
    private List<AbsTrack> mAbsTrackList;
    private List<AbsTrack> mCacheTrackList;
    private Random mRandom = new Random();
    private String mCurrentModel = MODEL_ORDER;
    //当前播放的音乐
    private AbsTrack mCurrentPlay;
    private int mCurrentIndex;

    //排序标准
    private Comparator<? super AbsTrack> mComparator;
    //列表更新监听事件
    private List<IPlayListUpdateListener> mPlayListUpdateCallbackList;

    //排序锁
    private Lock mComparatorLock = new ReentrantLock();
    //列表更新锁
    private Lock mPlayListLock = new ReentrantLock();

    public PlayListUtils() {
        mAbsTrackList = new ArrayList<>();
        mCacheTrackList = new ArrayList<>();
        mCurrentIndex = -1;
    }

    /**
     * 获取单曲循环的歌曲,当当前歌曲不存在时,尝试获取列表第一首歌曲返回
     *
     * @return
     */
    private AbsTrack getSingleTrack() {
        //单曲循环,返回当前播放的歌曲
        //当歌曲不存在时,尝试返回第一首歌曲
        AbsTrack currentPlay = getCurrentTrack();
        if (currentPlay == null) {
            setCurrentPlayTrack(mAbsTrackList.get(0), 0);
            return getCurrentTrack();
        } else {
            return currentPlay;
        }
    }

    /**
     * 获取顺序播放的歌曲(必须在列表存在数据时)
     *
     * @param isNext true获取下一首,false获取上一首
     * @return
     */
    private AbsTrack getOrderTrack(boolean isNext) {
        if (isNext) {
            //顺序播放
            mCurrentIndex++;
            if (mCurrentIndex >= mAbsTrackList.size()) {
                mCurrentIndex = 0;
            }
        } else {
            mCurrentIndex--;
            if (mCurrentIndex < 0) {
                mCurrentIndex = mAbsTrackList.size() - 1;
            }
        }
        return mAbsTrackList.get(mCurrentIndex);
    }

    /**
     * 获取随机播放的歌曲(必须在列表存在数据时)
     *
     * @param isNext true获取下一首,False获取上一首
     * @return
     */
    private AbsTrack getRandomTrack(boolean isNext) {
        //随机播放,从随机列表中返回歌曲
        if (isNext) {
            mCurrentIndex++;
            if (mCurrentIndex >= mCacheTrackList.size()) {
                mCurrentIndex = 0;
            }
        } else {
            mCurrentIndex--;
            if (mCurrentIndex < 0) {
                mCurrentIndex = mCacheTrackList.size() - 1;
            }
        }
        return mCacheTrackList.get(mCurrentIndex);
    }

    /**
     * 获取下一首歌曲,可能返回null
     *
     * @return
     */
    public AbsTrack getNextTrack() {
        if (mAbsTrackList.size() <= 0) {
            return null;
        }
        switch (mCurrentModel) {
            case MODEL_SINGLE:
                return getSingleTrack();
            case MODEL_ORDER:
                return getOrderTrack(true);
            case MODEL_RANDOM:
                return getRandomTrack(true);
            default:
                return null;
        }
    }

    /**
     * 返回上一首播放过的歌曲(即列表中的上一首),可能返回null
     *
     * @return
     */
    public AbsTrack getPreviousTrack() {
        if (mAbsTrackList.size() <= 0) {
            return null;
        }
        switch (mCurrentModel) {
            case MODEL_SINGLE:
                return getSingleTrack();
            case MODEL_ORDER:
                return getOrderTrack(false);
            case MODEL_RANDOM:
                return getRandomTrack(false);
            default:
                return null;
        }
    }

    /**
     * 设置当前的歌曲及其列表中的索引(该索引实际上只有在顺序播放时有效)
     *
     * @param currentPlay  当前歌曲
     * @param currentIndex 当前歌曲对应的索引
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setCurrentPlayTrack(AbsTrack currentPlay, int currentIndex) {
        if (currentPlay != null) {
            //设置当前歌曲及索引
            mCurrentPlay = currentPlay;
            setCurrentPlayIndex(currentIndex);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取当前歌曲的索引(实际只有顺序播放时有效)
     *
     * @return
     */
    public int getCurrentPlayIndex() {
        return mCurrentIndex;
    }

    /**
     * 设置当前播放歌曲的索引,若索引值不合法,则不作任何操作
     *
     * @param currentIndex
     */
    public void setCurrentPlayIndex(int currentIndex) {
        //判断索引是否合法
        if (currentIndex < 0 || currentIndex >= mAbsTrackList.size()) {
            return;
        } else {
            mCurrentIndex = currentIndex;
        }
    }

    /**
     * 回收资源,只有当不再需要使用列表时才可以使用此方法(建议只在服务关闭时调用)
     */
    public void recycle() {
        mPlayListLock.lock();
        mAbsTrackList.clear();
        mPlayListLock.unlock();
        mCacheTrackList.clear();
        mCurrentPlay = null;
        mAbsTrackList = null;
        mCacheTrackList = null;
    }

    /**
     * 创建当前播放列表对应的随机播放列表
     *
     * @param playNow 需要立即播放的歌曲,此参数可以为null
     */
    private void createRandomPlayCacheList(AbsTrack playNow) {
        mCacheTrackList.clear();
        //设置随机种子
        mRandom.setSeed(System.currentTimeMillis());
        //创建临时存在数据的列表
        List<AbsTrack> tempList = new LinkedList<>();
        tempList.addAll(mAbsTrackList);
        //若需要立即播放的歌曲不为null
        if (playNow != null) {
            //临时列表移除该歌曲
            tempList.remove(playNow);
            //将该歌曲添加到当前的随机列表中第一项
            mCacheTrackList.add(playNow);
            //设置当前的播放歌曲及索引为该歌曲,第一项索引为0
            setCurrentPlayTrack(playNow, 0);
        }
        //其余的歌曲随机生成播放位置
        for (; tempList.size() > 0; ) {
            int randomIndex = mRandom.nextInt(tempList.size());
            mCacheTrackList.add(tempList.get(randomIndex));
            tempList.remove(randomIndex);
        }
    }

    /**
     * 对指定的列表进行排序
     *
     * @param sortList 需要进行排序的列表
     * @param cp       排序规则
     */
    private void sortPlayList(List<AbsTrack> sortList, Comparator<? super AbsTrack> cp) {
        if (sortList == null || sortList.size() <= 0) {
            return;
        }
        if (cp != null) {
            Collections.sort(sortList, cp);
        }
    }

    @Override
    public void setComparator(Comparator<? super AbsTrack> cp) {
        if (cp != null) {
            mComparatorLock.lock();
            mComparator = cp;
            mComparatorLock.unlock();
        }
    }

    @Override
    public void setPlayList(List<? extends AbsTrack> playList) {
        setPlayListWithTrackPlayNow(playList, null, 0);
    }

    @Override
    public void setPlayListWithTrackPlayNow(List<? extends AbsTrack> playList, AbsTrack playNow, int nowIndex) {
        if (playList == null || playList.size() <= 0) {
            return;
        } else if (mAbsTrackList != playList) {
            mPlayListLock.lock();
            mAbsTrackList.clear();
            mAbsTrackList.addAll(playList);
            mPlayListLock.unlock();
            //创建播放列表对应的随机列表
            createRandomPlayCacheList(playNow);
        }

        //当前播放模式不为随机,则将当前歌曲的索引记录下来
        if (!MODEL_RANDOM.equals(mCurrentModel) && playNow != null) {
            setCurrentPlayTrack(playNow, nowIndex);
        }
    }

    @Override
    public void setPlayModel(String model) {
        //判断模式是否合法
        if (!MODEL_SINGLE.equals(model) && !MODEL_ORDER.equals(model) && !MODEL_RANDOM.equals(model)) {
            return;
        } else {
            if (!mCurrentModel.equals(model)) {
                mCurrentModel = model;
            }
        }
    }

    @Override
    public String getPlayModel() {
        return mCurrentModel;
    }

    @Override
    public void clearPlayList() {
        mPlayListLock.lock();
        mAbsTrackList.clear();
        mPlayListLock.unlock();
    }

    @Override
    public List<AbsTrack> getPlayList(boolean isNeedSort, Comparator<? super AbsTrack> cp) {
        //创建镜像列表
        List<AbsTrack> newList = new ArrayList<>(mAbsTrackList.size());
        newList.addAll(mAbsTrackList);
        if (isNeedSort) {
            if (cp == null) {
                cp = mComparator;
            }
            sortPlayList(newList, cp);
        }
        return newList;
    }

    @Override
    public List<AbsTrack> getPlayList() {
        return getPlayList(false, null);
    }

    @Override
    public AbsTrack getCurrentTrack() {
        return mCurrentPlay;
    }

    @Override
    public List<AbsTrack> searchTrack(List<AbsTrack> searchResult, String keyStr) {
        if (keyStr == null) {
            return searchResult;
        }
        if (searchResult == null) {
            searchResult = new ArrayList<>();
        }
        //搜索列表
        for (AbsTrack track : mAbsTrackList) {
            if (track != null) {
                if (track.isMatchSearch(keyStr)) {
                    searchResult.add(track);
                }
            }
        }
        return searchResult;
    }

    @Override
    public boolean isPlayListEmpty() {
        return mAbsTrackList.isEmpty();
    }

    @Override
    public void addPlayListUpdateListener(IPlayListUpdateListener listener) {
        if (listener != null) {
            mPlayListUpdateCallbackList.add(listener);
        }
    }

    @Override
    public void removePlayListUpdateListener(IPlayListUpdateListener listener) {
        if (listener != null) {
            mPlayListUpdateCallbackList.remove(listener);
        }
    }

}
