package us.bestapp.henrytaro.areachoose.utils;/**
 * Created by xuhaolin on 15/9/24.
 */

import android.graphics.Color;

import java.util.List;
import java.util.Map;

import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/24.
 */
public class AreaMaskColorUtils extends AbsMaskColorUtils {

    public AreaMaskColorUtils(List<AbsAreaEntity> areaList) {
        super(areaList);
    }

    /**
     * 构造函数
     *
     * @param areaList 区域列表
     * @param alpha    蒙板图层颜色透明度
     */
    public AreaMaskColorUtils(List<AbsAreaEntity> areaList, int alpha) {
        super(areaList, alpha);
    }

    @Override
    public Map<Integer, Integer> parseAreaToMaskColor(Map<Integer, Integer> maskColorMap, List<AbsAreaEntity> areaEntityList) {
        for (AbsAreaEntity area : areaEntityList) {
            if (area.isSoldOut()) {
                maskColorMap.put(area.getAreaColor(),Color.BLACK);
            }
        }
        return maskColorMap;
    }
}
