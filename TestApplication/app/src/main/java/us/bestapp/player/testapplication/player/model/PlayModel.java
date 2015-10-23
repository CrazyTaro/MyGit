package us.bestapp.player.testapplication.player.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import us.bestapp.player.testapplication.player.utils.HistoryListUtils;
import us.bestapp.player.testapplication.player.utils.PlayListUtils;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放模式,模式的切换与下一首歌曲的生成由此类决定,主要功能为根据指定模式产生对应的下一首歌曲数据
 */
public class PlayModel implements HistoryListUtils.IHistoryLisUpdateListener, PlayListUtils.IPlayListUpdateListener {
    /**
     * 顺序播放模式
     */
    public static final String MODEL_ORDER = "model_order";
    /**
     * 随机播放模式
     */
    public static final String MODEL_RANDOM = "model_random";
    /**
     * 单曲循环播放模式
     */
    public static final String MODEL_SINGLE = "model_single";

    protected String mCurrentModel = null;
    protected int mCurrentPlayIndex = -1;
    /**
     * 保留原始播放列表的引用(不进行任何操作)
     */
    protected List<? extends AbsMusic> mPlayList;
    /**
     * 保留原始历史列表的引用(不进行任何操作)
     */
    protected List<? extends AbsMusic> mHistoryList;
    /**
     * 此map为缓存已播放歌曲,用于随机产生下一首歌曲时可以优先筛选非播放过的歌曲,歌曲唯一标识通过{@link AbsMusic#getKey()}获取
     */
    protected Map mHistoryMap = new HashMap();
    /**
     * 此列表为缓存播放列表中未被播放过的所有歌曲,与之对应的是已播放的历史列表 mHistoryMap
     */
    protected List<AbsMusic> mUnplayList = new ArrayList<>();
    protected Random mRandom = new Random();

    /**
     * 构造函数;<font color="#ff9900"><b>此方法两个参数都可以为null,但若设为null必须在此后此类被使用前调用{@link #updateMusicList(List, List)}进行设置,否则将出错</b></font>
     *
     * @param playList
     * @param historyList
     */
    public PlayModel(List<AbsMusic> playList, List<AbsMusic> historyList) {
        //更新并绑定播放列表及历史列表
        updateMusicList(playList, historyList);
        //默认模式为列表循环
        mCurrentModel = MODEL_ORDER;
    }

    /***
     * 更新并绑定播放列表及历史列表.<font color="#ff9900"><b>此方法中更新播放列表时将依赖于历史列表,请尽可能保证两个参数同时不为null或者先单独更新历史列表再更新播放列表</b></font>,两个参数都可为null,若为null时不进行任何更新操作,初始化数据除外
     *
     * @param playList    播放列表
     * @param historyList 历史列表
     */
    public void updateMusicList(List<? extends AbsMusic> playList, List<? extends AbsMusic> historyList) {
        //更新播放列表
        updateHistoryList(historyList);
        //更新历史列表
        updatePlayList(playList);
    }

    /**
     * 更新历史列表,将历史列表中的数据缓存到map中,以方便进行搜索及获取
     *
     * @param historyList
     */
    private void updateHistoryList(List<? extends AbsMusic> historyList) {
        if (this.mHistoryList == historyList) {
            return;
        }

//        if (mHistoryList != null) {
//            mHistoryList.clear();
//        } else {
//            mHistoryList = new LinkedList<>();
//        }
//        mHistoryList.addAll(historyList);
        //保留原始引用
        this.mHistoryList = historyList;
        if (mHistoryMap != null) {
            mHistoryMap.clear();
        }
        //缓存历史列表中的数据
        if (historyList != null) {
            for (AbsMusic m : historyList) {
                mHistoryMap.put(m.getKey(), m);
            }
        }

    }

    /**
     * 更新播放列表
     *
     * @param playList
     */
    private void updatePlayList(List<? extends AbsMusic> playList) {
        if (this.mPlayList == playList) {
            return;
        }

//        if (mPlayList != null) {
//            mPlayList.clear();
//        } else {
//            mPlayList = new LinkedList<>();
//        }
//        mPlayList.addAll(playList);
        //保留原始引用
        this.mPlayList = playList;
        if (mUnplayList != null) {
            mUnplayList.clear();
        }
        //检测播放列表中的每一项歌曲,若历史列表中存在该歌曲,则先移除
        //只保留播放列表中未被播放过歌曲
        if (playList != null) {
            for (AbsMusic m : playList) {
                if (!mHistoryMap.containsKey(m.getKey())) {
                    mUnplayList.add(m);
                }
            }
            //当历史列表已经存在所有歌曲时(即播放列表中没有歌曲是未被播放过的),歌曲将重新随机选取并播放
            if (mUnplayList.size() <= 0) {
                mUnplayList.addAll(playList);
                //清空历史列表
                mHistoryMap.clear();
            }
        }

    }

    /**
     * 获取播放列表
     */
    public List<? extends AbsMusic> getPlayList() {
        return this.mPlayList;
    }

    /**
     * 获取历史列表
     */
    public List<? extends AbsMusic> getHistoryList() {
        return this.mHistoryList;
    }

    /**
     * 设置当前的播放模式
     *
     * @param model
     */
    public void setCurrentModel(String model) {
        mCurrentModel = model;
    }

    /**
     * 获取当前的播放模式
     *
     * @return
     */
    public String getCurrentModel() {
        return mCurrentModel;
    }

    /**
     * 获取当前播放模式的描述
     *
     * @return
     */
    public String getCurrentModelName() {
        switch (mCurrentModel) {
            case MODEL_ORDER:
                return "顺序播放";
            case MODEL_RANDOM:
                return "随机播放";
            case MODEL_SINGLE:
                return "单曲循环";
            default:
                return "未知模式";
        }
    }

    /**
     * 单曲循环
     *
     * @param currentPlay 当前播放的歌曲
     * @return
     */
    private AbsMusic singlePlay(AbsMusic currentPlay) {
        if (currentPlay == null && this.mPlayList.size() > 0) {
            //若当前播放的歌曲为null且播放列表中存在歌曲,则将列表中第一首歌曲返回
            currentPlay = this.mPlayList.get(0);
        }
        //否则返回当前的歌曲
        return currentPlay;
    }

    /***
     * 列表循环
     *
     * @param currentPlay 当前播放的歌曲
     * @return
     */
    private AbsMusic orderPlay(AbsMusic currentPlay) {
        //列表存在歌曲时
        if (this.mPlayList.size() > 0) {
            //获取保存的当前播放歌曲在列表中索引
            int currentIndex = getCurrentPlayIndex();
            if (currentPlay == null || currentIndex < 0 || currentIndex >= this.mPlayList.size()) {
                //当前播放歌曲为null或者获取的列表索引不合法
                //将索引重置
                currentIndex = -1;
            }
            currentIndex++;
            //确保索引不会超过列表范围
            //超过则返回列表的第一首
            if (currentIndex >= this.mPlayList.size()) {
                currentIndex = 0;
            }
            //保存当前得到的索引(即当前音乐的索引)
            setCurrentPlayIndex(currentIndex);
            //返回音乐
            return this.mPlayList.get(currentIndex);
        } else {
            return null;
        }
    }

    /**
     * 随机播放
     *
     * @return
     */
    private AbsMusic randomPlay() {
        if (mUnplayList.size() > 0) {
            //从非播放的列表中随机获取一首歌曲返回
            int randomIndex = mRandom.nextInt(mUnplayList.size());
            return mUnplayList.get(randomIndex);
        } else {
            return null;
        }
    }

    /***
     * 设置当前的音乐的索引,<font color="#ff9900"><b>若索引未在合法范围内,进抛出异常</b></font>
     *
     * @param index
     */
    public void setCurrentPlayIndex(int index) {
        if (index >= mPlayList.size() || index < 0) {
            throw new RuntimeException("index is illegal");
        }
        mCurrentPlayIndex = index;
    }

    /***
     * 获取当前的索引
     *
     * @return
     */
    public int getCurrentPlayIndex() {
        return mCurrentPlayIndex;
    }

    /**
     * 获取当前模式下下一首歌曲的索引
     *
     * @return
     */
    public AbsMusic getNextMusic(AbsMusic currentPlay) {
        switch (mCurrentModel) {
            case MODEL_SINGLE:
                currentPlay = singlePlay(currentPlay);
                break;
            case MODEL_ORDER:
                currentPlay = orderPlay(currentPlay);
                break;
            case MODEL_RANDOM:
                currentPlay = randomPlay();
                break;
            default:
                currentPlay = null;
                break;
        }
        return currentPlay;
    }

    @Override
    public void onAddHistoryMusic(AbsMusic music) {
        //历史列表添加新歌曲时
        //更新未播放歌曲列表及历史歌曲map
        if (music != null) {
            if (!mHistoryMap.containsKey(music.getKey())) {
                //添加新的历史歌曲
                mHistoryMap.put(music.getKey(), music);
            }
            //移除播放过的歌曲
            mUnplayList.remove(music);
            //当更新完未播放列表不存在数据时,添加所有的歌曲数据重新随机选取
            if (mUnplayList.size() <= 0) {
                mUnplayList.addAll(mPlayList);
                mHistoryMap.clear();
            }
        }
    }

    @Override
    public void onRemoveHistoryMusic(AbsMusic music, String removeState) {
        //移除历史列表中的歌曲
        if (music != null) {
            if (mHistoryMap.containsKey(music.getKey())) {
                mHistoryMap.remove(music.getKey());
            }
            //更新未播放列表
            int index = mUnplayList.indexOf(music);
            if (index == -1) {
                mUnplayList.add(music);
            }
        }
    }

    @Override
    public void onHistoryListClear() {
        updateHistoryList(null);
    }

    @Override
    public void onResetHistoryList(List<? extends AbsMusic> newList) {
        if (newList == null || newList.size() <= 0) {
            return;
        }
        updateMusicList(null, newList);
    }

    @Override
    public void onAddNewMusic(AbsMusic music) {
        //播放列表添加新的歌曲
        if (music != null) {
            int index = mUnplayList.indexOf(music);
            if (index == -1) {
                mUnplayList.add(music);
            }
        }
    }

    @Override
    public void onAppendMusicList(List<? extends AbsMusic> appendList) {
        if (appendList == null || appendList.size() <= 0) {
            return;
        }
        for (AbsMusic m : appendList) {
            onAddNewMusic(m);
        }
    }

    @Override
    public void onRemoveOldMusic(AbsMusic music) {
        //播放列表移除某些歌曲
        if (music != null) {
            int index = mUnplayList.indexOf(music);
            if (index != -1) {
                mUnplayList.remove(index);
            }
        }
    }

    @Override
    public void onRemoveMusicList(List<? extends AbsMusic> removeList) {
        if (removeList == null || removeList.size() <= 0) {
            return;
        }
        for (AbsMusic m : removeList) {
            onRemoveOldMusic(m);
        }
    }

    @Override
    public void onResetPlayList(List<? extends AbsMusic> newList) {
        updateMusicList(newList, null);
    }

    @Override
    public void onPlayListClear() {
        updatePlayList(null);
    }

}
