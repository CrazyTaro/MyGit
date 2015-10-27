package us.bestapp.henrytaro.player.interfaces;

import java.util.Comparator;
import java.util.List;

import us.bestapp.henrytaro.player.model.AbsTrack;

/**
 * Created by xuhaolin on 15/10/21.
 */
public interface IPlayListHandle {
    /**
     * 设置排序规则
     */
    public void setComparator(Comparator<? super AbsTrack> cp);

    /***
     * 添加新音乐,参数为null时不操作
     *
     * @param absTrack
     * @return
     */
    public boolean addNewTrack(AbsTrack absTrack);

    /**
     * 添加音乐,若强制性添加音乐则不管当前音乐是否在列表中已存在,都直接添加到列表中,<font color="#ff9900"><b>建议非必要情况下不要使用此方法</b></font>
     *
     * @param absTrack
     * @param isMandatory 是否强制性添加
     * @return
     */
    public boolean addNewTrack(AbsTrack absTrack, boolean isMandatory);

    /**
     * 设置新的播放列表,此方法会清除原有的播放列表
     */
    public void setPlayList(List<? extends AbsTrack> playList);

    /**
     * 清空播放列表
     */
    public void clearPlayList();

    /**
     * 批量添加音乐到播放列表中
     *
     * @param appendList  新增的列表音乐
     * @param isMandatory 是否强制添加
     */
    public void appendPlayList(List<? extends AbsTrack> appendList, boolean isMandatory);

    /**
     * 移除旧的音乐
     */
    public boolean removeOldTrack(AbsTrack absTrack);

    /**
     * 批量移除播放列表
     *
     * @param removeList
     */
    public void removeOldTrackList(List<? extends AbsTrack> removeList);

    /***
     * 获取当前播放列表
     *
     * @param isNeedSort 是否进行排序,若为false,则不排序(参数2无效)
     * @param cp         排序规则,当此参数不为null时,排序规则使用此参数;当此参数为null时且isNeedSort为true,使用默认排序规则(即创建播放列表时的排序规则)
     * @return
     */
    public List<AbsTrack> getPlayList(boolean isNeedSort, Comparator<? super AbsTrack> cp);


    /**
     * 获取播放列表,该播放列表不进行排序等操作,返回当前实际的列表,此方法即为{@link #getPlayList(boolean, Comparator)}方法中的默认处理方式,isNeedSort=false,cp=null
     */
    public List<AbsTrack> getPlayList();

    /**
     * 获取当前的音乐
     */
    public AbsTrack getCurrentTrack();

    /**
     * 搜索音乐
     *
     * @param searchResult 搜索结果,此参数可为null,当为null时创建一个新的列表;当不为null时使用该参数
     * @param keyStr       搜索关键字
     * @return
     */
    public List<AbsTrack> searchTrack(List<AbsTrack> searchResult, String keyStr);

    /**
     * 列表是否为空
     */
    public boolean isPlayListEmpty();


    /**
     * 添加更新列表监听事件+
     */
    public void addPlayListUpdateListener(IPlayListUpdateListener listener);


    /**
     * 移除更新列表监听事件
     */
    public void removePlayListUpdateListener(IPlayListUpdateListener listener);


    /**
     * 更新列表监听事件
     */
    public interface IPlayListUpdateListener {
        /**
         * 添加新音乐到列表中时回调
         *
         * @param track 新音乐对象
         */
        void onAddNewTrack(AbsTrack track);

        /**
         * 批量添加播放列表
         *
         * @param appendList
         */
        void onAppendTrackList(List<? extends AbsTrack> appendList);

        /**
         * 从列表中移除旧音乐
         *
         * @param track 旧音乐(已被移除)
         */
        void onRemoveOldTrack(AbsTrack track);

        /**
         * 批量移除播放列表
         *
         * @param removeList
         */
        void onRemoveTrackList(List<? extends AbsTrack> removeList);

        /**
         * 设置新音乐列表
         *
         * @param newList 新列表
         */
        void onResetPlayList(List<? extends AbsTrack> newList);

        /**
         * 清空播放列表
         */
        void onPlayListClear();
    }
}
