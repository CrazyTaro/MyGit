package us.bestapp.player.testapplication.player.interfaces;

import java.util.List;

import us.bestapp.player.testapplication.player.model.AbsMusic;

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
    public List<AbsMusic> getHistoryList();

    /**
     * 设置播放历史列表
     */
    public void setHistoryList(List<? extends AbsMusic> historyList);

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
     * 历史列表更新回调事件
     */
    public interface IHistoryLisUpdateListener {
        /**
         * 历史列表添加新歌曲
         */
        void onAddHistoryMusic(AbsMusic music);

        /**
         * 历史列表移除歌曲
         *
         * @param music
         * @param removeState 表示当前移除歌曲事件发生的位置,提供更精确的处理判断,{@link #STATE_REMOVE_ADD},{@link #STATE_REMOVE_UPDATE}
         */
        void onRemoveHistoryMusic(AbsMusic music, String removeState);

        /**
         * 历史列表清空事件
         */
        void onHistoryListClear();

        /**
         * 设置新的历史列表
         * @param newList
         */
        void onResetHistoryList(List<? extends AbsMusic> newList);
    }
}
