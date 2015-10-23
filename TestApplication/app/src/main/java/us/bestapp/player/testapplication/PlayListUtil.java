package us.bestapp.player.testapplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import us.bestapp.player.testapplication.player.model.PlayModel;

/**
 * Created by xuhaolin on 15/10/18.
 */
public class PlayListUtil<K, M> {
    public static final int SORT_BY_KEY = Integer.MIN_VALUE;
    public static final int SORT_BY_MUSIC = Integer.MAX_VALUE;

    public static final int MODEL_BEGIN_FIRST = Integer.MIN_VALUE;
    public static final int MODEL_BEGIN_LAST = Integer.MAX_VALUE;

    private Map<K, M> mPlayMap;
    private List<M> mMusicList;
    private Comparator mComparator;
    private PlayModel mPlayModel;

    private int mSortType = -1;
    private M mCurrentPlay;

    public PlayListUtil(int sortType, Comparator<K> cpKey, Comparator<M> cpMusic) {
        if (sortType == SORT_BY_KEY) {
            mPlayMap = new TreeMap<>(cpKey);
        } else if (sortType == SORT_BY_MUSIC) {
            if (cpMusic == null) {
                throw new RuntimeException("music comparator can not be null");
            }
            mPlayMap = new HashMap<>();
            mMusicList = new LinkedList<>();
            mComparator = cpMusic;
        } else {
            throw new RuntimeException("can not handle the sort type");
        }

        mSortType = sortType;
    }

    public void setPlayModel(PlayModel playModel){
        mPlayModel=playModel;
    }

    public boolean addNewMusic(K key, M music) {
        if (mPlayMap.containsKey(key)) {
            return false;
        } else {
            mPlayMap.put(key, music);
            if (mSortType == SORT_BY_MUSIC) {
                mMusicList.add(music);
            }
            return true;
        }
    }

    public boolean removeMusicByKey(K key) {
        if (!mPlayMap.containsKey(key)) {
            return false;
        } else {
            if (mSortType == SORT_BY_MUSIC) {
                mMusicList.remove(mPlayMap.get(key));
            }
            mPlayMap.remove(key);
            return true;
        }
    }

    public boolean removeMusicByMusic(M music) {
        for (Map.Entry<K, M> entry : mPlayMap.entrySet()) {
            if (entry.getValue().equals(music)) {
                mPlayMap.remove(entry.getKey());
                if (mSortType == SORT_BY_MUSIC) {
                    mMusicList.remove(music);
                }
                return true;
            }
        }
        return false;
    }

    public boolean setFirstBeginMusic(int beginModel) {
        if (isPlayListEmpty()) {
            return false;
        }
        boolean isSetSucceed = false;
        switch (beginModel) {
            case MODEL_BEGIN_FIRST:
                if (mSortType == SORT_BY_MUSIC) {
                    mCurrentPlay = mMusicList.get(0);
                } else {
                    TreeMap<K, M> map = (TreeMap) mPlayMap;
                    mCurrentPlay = map.get(map.firstKey());
                }
                isSetSucceed = true;
                break;
            case MODEL_BEGIN_LAST:
                if (mSortType == SORT_BY_MUSIC) {
                    mCurrentPlay = mMusicList.get(mMusicList.size() - 1);
                } else {
                    TreeMap<K, M> map = (TreeMap) mPlayMap;
                    mCurrentPlay = map.get(map.lastKey());
                }
                isSetSucceed = true;
                break;
        }
        return isSetSucceed;
    }

    public List<M> getPlayList() {
        List<M> playList = new ArrayList<>(mPlayMap.size());
        if (mSortType == SORT_BY_KEY) {
            Iterator<K> keyIt = mPlayMap.keySet().iterator();
            while (keyIt.hasNext()) {
                playList.add(mPlayMap.get(keyIt.next()));
            }
        } else {
            Collections.sort(mMusicList, mComparator);
            playList.addAll(mMusicList);
        }
        return playList;
    }

    public M getCurrentMusic() {
        return this.mCurrentPlay;
    }

    public void getNextMusic() {
    }

    public void getPreMusic() {
    }

    public boolean isPlayListEmpty() {
        if (mPlayMap.size() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPlayListInNormalState() {
        if (mSortType == SORT_BY_MUSIC) {
            return mPlayMap.size() == mMusicList.size();
        } else {
            return true;
        }
    }
}
