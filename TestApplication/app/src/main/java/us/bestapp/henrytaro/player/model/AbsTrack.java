package us.bestapp.henrytaro.player.model;

import android.graphics.Bitmap;

/**
 * Created by xuhaolin on 15/10/18.
 * 音乐抽象基类
 */
public abstract class AbsTrack<K> {
    public int index;

    /***
     * 判断给定的对象与当前对象是否同一类型
     *
     * @param obj
     * @return
     */
    public boolean isInstanceOf(Object obj) {
        //基础的equals判断
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (obj instanceof AbsTrack) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (isInstanceOf(o)) {
            AbsTrack m = (AbsTrack) o;
            //判断两个音乐是否相同,通过key确定
            return this.getKey().equals(m.getKey());
        } else {
            return false;
        }
    }

    /**获取当前歌曲的url或者地址*/
    public abstract String getUrlOrFilePath();

    public abstract String getTrackName();

    public abstract String getTrackArtist();

    public abstract Bitmap getTrackAblumn();

    /**
     * 获取当前音乐的唯一标识(此值不同时两个对象equals结果为false),作为区分不同音乐的标识
     *
     * @return
     */
    public abstract K getKey();

    /**
     * 判断当前音乐是否符合给定字符串的匹配(此方法主要是针对搜索使用)
     *
     * @param keyStr 关键字符串
     * @return 匹配成功返回true, 否则返回false
     */
    public abstract boolean isMatchSearch(String keyStr);
}
