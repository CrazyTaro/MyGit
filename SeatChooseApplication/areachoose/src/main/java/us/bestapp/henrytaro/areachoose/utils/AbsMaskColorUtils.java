package us.bestapp.henrytaro.areachoose.utils;/**
 * Created by xuhaolin on 15/9/24.
 */

import android.graphics.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/24.
 */
public abstract class AbsMaskColorUtils {
    private Map<Integer, Integer> mMaskColorMap = null;
    private int mAlpha = 0;

    /**
     * 构造函数
     *
     * @param areaList 区域列表
     * @param alpha    蒙板图层颜色透明度
     */
    public AbsMaskColorUtils(List<AbsAreaEntity> areaList, int alpha) {
        if (alpha < 0 || alpha > 255) {
            throw new RuntimeException("透明度值只能是0-255");
        } else {
            mAlpha = alpha;
        }
        if (areaList != null) {
            //创建颜色对应的Map
            mMaskColorMap = new HashMap<>();
            mMaskColorMap = parseAreaToMaskColor(mMaskColorMap, areaList);
        } else {
            throw new RuntimeException("区域列表不可为null");
        }
    }

    /**
     * 对颜色值进行设定透明度的处理
     *
     * @param color
     * @return
     */
    public int getColor(int color) {
        return (mAlpha << 24) | ((0 << 24) & color);
    }

    /**
     * 对颜色值进行设定透明度的处理
     *
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public int getColor(int red, int green, int blue) {
        return (mAlpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * 解析16进制颜色值,并对颜色值进行可选的透明度处理
     *
     * @param colorHXStr   16进制颜色值
     * @param isForceAlpha 是否强制对颜色值进行透明度处理,若为true,则以设定的透明度替换原颜色值的透明度(不管原颜色值是否透明等);若false则完整地保留该颜色值
     * @return
     */
    public int getColor(String colorHXStr, boolean isForceAlpha) {
        if (!isForceAlpha) {
            return Color.parseColor(colorHXStr);
        } else {
            return (Color.parseColor(colorHXStr) & (0 << 24)) | mAlpha << 24;
        }
    }

    /**
     * 构造函数,创建并解析区域颜色与其蒙板图层颜色的关系表
     *
     * @param areaList
     */
    public AbsMaskColorUtils(List<AbsAreaEntity> areaList) {
        if (areaList != null) {
            //创建颜色对应的Map
            mMaskColorMap = new HashMap<>();
            mMaskColorMap = parseAreaToMaskColor(mMaskColorMap, areaList);
        } else {
            throw new RuntimeException("区域列表不可为null");
        }
    }

    /**
     * 获取蒙板图层的透明度
     *
     * @return
     */
    public int getAlpha() {
        return this.mAlpha;
    }

    /**
     * 设置蒙板图层的透明度
     *
     * @param alpha
     */
    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
    }

    /**
     * 解析区域颜色及蒙板图层颜色之间的关系,对于颜色值的使用可用预设的获取颜色的方法进行设置(该方法对颜色值进行透明度处理)<br/>
     * {@link #getColor(String, boolean)},16进制设置颜色值<br/>
     * {@link #getColor(int)},对颜色值透明度进行处理<br/>
     * {@link #getColor(int, int, int)},对颜色值透明度进行处理<br/>
     *
     * @param maskColorMap   用于存储区域颜色与蒙板图层颜色的散列表
     * @param areaEntityList 区域列表数据
     * @return
     */
    public abstract Map<Integer, Integer> parseAreaToMaskColor(Map<Integer, Integer> maskColorMap, List<AbsAreaEntity> areaEntityList);

    /**
     * 根据当前图片位置的颜色获取其对应的蒙板颜色(默认返回透明色0,{@link android.graphics.Color#TRANSPARENT})
     *
     * @param pixelColor 当前图片中扫描到的像素的颜色值
     * @return
     */
    public int getMaskColor(int pixelColor) {
        if (this.mMaskColorMap == null) {
            return Color.TRANSPARENT;
        } else {
            if (!mMaskColorMap.containsKey(pixelColor)) {
                return Color.TRANSPARENT;
            } else {
                return mMaskColorMap.get(pixelColor);
            }
        }
    }
}
