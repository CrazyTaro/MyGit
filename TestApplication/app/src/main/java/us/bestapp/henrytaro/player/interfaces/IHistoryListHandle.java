package us.bestapp.henrytaro.player.interfaces;

import java.util.List;

import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/23.
 */
public interface IHistoryListHandle {
    /**
     * 在添加上一首到列表中时移除
     */
    public static final String STATE_REMOVE_ADD = "state_remove_add";
    /**
     * 在更新列表上限时移除
     */
    public static final String STATE_REMOVE_UPDATE = "state_remove_update";

    /**
     * 获取播放历史列表
     */
    public List<AbsTrack> getHistoryList();

    /**
     * 设置播放历史列表
     */
    public void setHistoryList(List<? extends AbsTrack> historyList);

    /**
     * 清空历史列表
     */
    public void clearHistoryList();

    /**
     * 移除播放监听事件
     */
    public void removeHistoryUpdateListener(IHistoryLisUpdateListener listener);

    /**
     * 添加更新历史列表时的回调事件
     */
    public void addHistoryListUpdateListener(IHistoryLisUpdateListener listener);

    /**
     * 设置历史列表的上限,count小于0时无上限,大于0列表上限为该值,默认无上限
     */
    public void setHistoryLisUpperLimit(int count);

    /**
     * 判断当前列表是否存在上限,存在上限返回true,否则返回false
     */
    public boolean isHistoryListUpperLimit();

    /**
     * 判断当前列表是否已达到上限(无上限也是未达到上限)
     */
    public boolean isHistoryListFull();

    /**
     * 判断当前播放的音乐是否是历史音乐(该操作只记录通过previous播放的歌曲情况,查看某歌曲是否历史列表中的歌曲可以直接获取getHistoryList.contains(AbsTrack))
     *
     * @param currentTrack 当前播放的音乐
     * @return true表示当前播放的歌曲即为历史列表中的歌曲, false表示当前播放的音乐不在历史列表中,
     * <font color="#ff9900"><b>此处是指该音乐不是用户通过上一首的操作而播放的历史列表中的歌曲,用户通过单击进行播放的歌曲即使在历史中也不属于此情况;从列表中直接单击进行播放的歌曲无论如何都不算播放历史列表的歌曲,该歌曲即便该歌曲已经存在历史列表中,依然会将其在历史列表中的位置调整到最前,即为最近播放过的歌曲</b></font>
     */
    public boolean isPlayingHistoryTrackByPrevious(AbsTrack currentTrack);

    /**
     * 历史列表更新回调事件
     */
    public interface IHistoryLisUpdateListener {
        /**
         * 历史列表添加新歌曲
         */
        void onAddHistoryTrack(AbsTrack track);

        /**
         * 历史列表移除歌曲
         *
         * @param track
         * @param removeState 表示当前移除歌曲事件发生的位置,提供更精确的处理判断,{@link #STATE_REMOVE_ADD},{@link #STATE_REMOVE_UPDATE}
         */
        void onRemoveHistoryTrack(AbsTrack track, String removeState);

        /**
         * 历史列表清空事件
         */
        void onHistoryListClear();

        /**
         * 设置新的历史列表
         *
         * @param newList
         */
        void onResetHistoryList(List<? extends AbsTrack> newList);
    }
}
