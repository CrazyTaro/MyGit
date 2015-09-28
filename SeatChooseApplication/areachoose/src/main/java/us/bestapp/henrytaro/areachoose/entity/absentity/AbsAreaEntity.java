package us.bestapp.henrytaro.areachoose.entity.absentity;/**
 * Created by xuhaolin on 15/9/23.
 */

import java.io.Serializable;

/**
 * Created by xuhaolin on 15/9/23.
 */
public abstract class AbsAreaEntity implements Serializable{

    /**
     * 获取区域名
     *
     * @return
     */
    public abstract String getAreaName();

    /**
     * 获取区域颜色16进制值
     *
     * @return
     */
    public abstract String getAreaColorHXStr();

    /**
     * 获取区域颜色(整型数值)
     *
     * @return
     */
    public abstract int getAreaColor();

    /**
     * 获取区域是否售完
     *
     * @return
     */
    public abstract boolean isSoldOut();
}
