package us.bestapp.player.testapplication.player.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import us.bestapp.player.testapplication.player.interfaces.IHistoryListHandle;
import us.bestapp.player.testapplication.player.model.AbsMusic;

/**
 * Created by xuhaolin on 15/10/18.
 * 历史播放列表管理
 */
public class HistoryListUtils implements IHistoryListHandle {

    private List<AbsMusic> mHistoryList;
    //历史列表上限,小于0代表无上限,大于0代表上限值为该值
    private int mHistoryUpperLimit = -1;
    //更新历史列表时的回调接口
    private List<IHistoryListHandle.IHistoryLisUpdateListener> mHistoryUpdateCallbackList;
    //当前播放历史列表的使用的索引
    private int mCurrentHistoryIndex = 0;
    //同步锁
    private Lock mHistoryListLock = new ReentrantLock();

    public HistoryListUtils() {
        mHistoryList = new LinkedList();
        //监听事件列表
        mHistoryUpdateCallbackList = new LinkedList<>();
    }

    @Override
    public List<AbsMusic> getHistoryList() {
        return mHistoryList;
    }

    @Override
    public void setHistoryList(List<? extends AbsMusic> historyList) {
        mHistoryListLock.lock();
        this.mHistoryList.clear();
        this.mHistoryList.addAll(historyList);
        mHistoryListLock.unlock();
        for (IHistoryLisUpdateListener listener : mHistoryUpdateCallbackList) {
            listener.onResetHistoryList(historyList);
        }
    }

    @Override
    public void clearHistoryList() {
        mHistoryListLock.lock();
        this.mHistoryList.clear();
        mHistoryListLock.unlock();
        for (IHistoryLisUpdateListener listener : mHistoryUpdateCallbackList) {
            listener.onHistoryListClear();
        }
    }

    @Override
    public void removeHistoryUpdateListener(IHistoryListHandle.IHistoryLisUpdateListener listener) {
        mHistoryUpdateCallbackList.remove(listener);
    }

    @Override
    public void addHistoryListUpdateListener(IHistoryListHandle.IHistoryLisUpdateListener listener) {
        mHistoryUpdateCallbackList.add(listener);
    }

    @Override
    public void setHistoryLisUpperLimit(int count) {
        mHistoryUpperLimit = count;
        if (isHistoryListUpperLimit()) {
            updateHistoryListUpperLimit(count);
        }
    }

    @Override
    public boolean isHistoryListUpperLimit() {
        if (mHistoryUpperLimit <= 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isHistoryListFull() {
        if (isHistoryListUpperLimit() && checkHistoryLisExsit()) {
            return mHistoryList.size() >= mHistoryUpperLimit;
        } else {
            return false;
        }
    }

    /**
     * 检查当前历史列表是否存在,存在返回true,其它情况返回false
     */
    public boolean checkHistoryLisExsit() {
        if (mHistoryList == null) {
            return false;
//            throw new RuntimeException("play list can not be null");
        } else {
            return true;
        }
    }

    /**
     * 添加最后一首播放的歌曲,成功添加返回true,其它情况返回false,<b>注意,每一首歌曲只在列表中保存一次,重复保存同一首歌曲将移除之前已保存的该歌曲(并将歌曲保存到当前最后播放的位置)</b>
     *
     * @param music
     * @return
     */
    public boolean addLastPlayMusic(AbsMusic music) {
        //检查列表是否存在
        if (!checkHistoryLisExsit()) {
            return false;
        }
        //判断当前列表中是否存在此歌曲,是则移除
        if (mHistoryList.contains(music)) {
            this.removeMusic(music, STATE_REMOVE_ADD);
        } else if (isHistoryListFull()) {
            this.removeMusic(0, STATE_REMOVE_ADD);
        }
        //添加歌曲并回调事件
        this.addMusic(music);
        return true;
    }

    /**
     * 获取一首历史歌曲,此方法是动态计算的;当当前播放的歌曲是不是最近播放过的歌曲时,返回的是最近播放过的歌曲(即历史列表中第一首);当当前播放的歌曲存在历史列表中时,返回的是历史列表中此歌曲的上一首播放记录,直到历史列表最初始的歌曲为止(达到最初始歌曲时重复该歌曲)
     *
     * @param currentMusic 当前播放歌曲
     * @return
     */
    public AbsMusic getHistoryPlayMusic(AbsMusic currentMusic) {
        //判断当前的历史列表索引是否合法
        if (mCurrentHistoryIndex >= mHistoryList.size()) {
            return null;
        }
        //获取当前历史索引的歌曲
        AbsMusic currentHistory = mHistoryList.get(mCurrentHistoryIndex);
        //进行歌曲的匹配确定歌曲是否与当前历史索引记录歌曲相同(即是否在播放历史列表中的歌曲)
        if (currentHistory.equals(currentMusic)) {
            //若是尝试获取再上一首的历史歌曲
            //若获取到的歌曲已经达到历史列表的最开始的歌曲,则重播此歌曲
            mCurrentHistoryIndex = (mCurrentHistoryIndex + 1) < mHistoryList.size() ? mCurrentHistoryIndex + 1 : mCurrentHistoryIndex;
        } else {
            //若不是,获取最近的历史歌曲
            mCurrentHistoryIndex = 0;
        }
        return mHistoryList.get(mCurrentHistoryIndex);
    }

    /**
     * 判断当前播放的音乐是否是历史列表中最后一首歌曲
     */
    public boolean isCurrentPlayLastMuisc() {
        if (mCurrentHistoryIndex == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取最后一次播放过的歌曲
     */
    public AbsMusic getLastPlayMusic() {
        if (!checkHistoryLisExsit()) {
            return null;
        }
        if (mHistoryList.size() > 0) {
            return mHistoryList.get(0);
        } else {
            return null;
        }
    }

    /**
     * 添加歌曲并回调事件,歌曲永远添加到第一个位置
     */
    private void addMusic(AbsMusic music) {
        mHistoryListLock.lock();
        mHistoryList.add(0, music);
        mHistoryListLock.unlock();
        if (mHistoryUpdateCallbackList.size() > 0) {
            for (IHistoryListHandle.IHistoryLisUpdateListener listener : mHistoryUpdateCallbackList) {
                listener.onAddHistoryMusic(music);
            }
        }
    }

    /**
     * 移除歌曲并回调事件
     *
     * @param music
     * @param removeState 表示当前移除歌曲事件发生的位置,提供更精确的处理判断,{@link #STATE_REMOVE_ADD},{@link #STATE_REMOVE_UPDATE}
     */
    private void removeMusic(AbsMusic music, String removeState) {
        mHistoryListLock.lock();
        boolean isSuccess = mHistoryList.remove(music);
        mHistoryListLock.unlock();
        if (mHistoryUpdateCallbackList.size() > 0) {
            for (IHistoryListHandle.IHistoryLisUpdateListener listener : mHistoryUpdateCallbackList) {
                listener.onRemoveHistoryMusic(music, removeState);
            }
        }
    }

    /**
     * 移除歌曲并回调事件
     *
     * @param index
     * @param removeState 表示当前移除歌曲事件发生的位置,提供更精确的处理判断,{@link #STATE_REMOVE_ADD},{@link #STATE_REMOVE_UPDATE}
     */
    private void removeMusic(int index, String removeState) {
        AbsMusic oldMusic = mHistoryList.get(index);
        removeMusic(oldMusic, removeState);
    }

    /**
     * 根据列表上限更新历史列表(主要用于移除列表中大于上限部分的数据,从首元素开始移除)
     */
    private void updateHistoryListUpperLimit(int limit) {
        if (checkHistoryLisExsit()) {
            if (mHistoryList.size() > limit) {
                int offset = mHistoryList.size() - limit;
                for (int i = 0; i < offset; i++) {
                    removeMusic(i, STATE_REMOVE_UPDATE);
                }
            }
        }
    }
}
