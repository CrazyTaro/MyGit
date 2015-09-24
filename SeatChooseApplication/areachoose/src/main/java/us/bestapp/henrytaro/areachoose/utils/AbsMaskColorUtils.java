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
     * 解析区域颜色及蒙板图层颜色之间的关系
     *
     * @param maskColorMap   用于存储区域颜色与蒙板图层颜色的散列表
     * @param areaEntityList 区域列表数据
     * @return
     */
    public abstract Map<Integer, Integer> parseAreaToMaskColor(Map<Integer, Integer> maskColorMap, List<AbsAreaEntity> areaEntityList);

    /**
     * 根据当前图片位置的颜色获取其对应的蒙板颜色(默认请返回透明色0,{@link android.graphics.Color#TRANSPARENT})
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
