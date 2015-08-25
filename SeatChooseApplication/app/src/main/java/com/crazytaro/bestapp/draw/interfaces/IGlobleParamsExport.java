package com.crazytaro.bestapp.draw.interfaces;

import android.graphics.Color;

/**
 * Created by xuhaolin on 15/8/25.
 */
public interface IGlobleParamsExport {

    /**
     * 设置缩略图背景色及透明度
     *
     * @param color 颜色值,颜色值不作任何检测(颜色默认值为{@link Color#BLACK})
     * @param alpha 透明度,透明度必须在0-255之间,用默认值请用参数{@link IBaseParamsExport#DEFAULT_INT}
     * @return
     */
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha);

    /**
     * 设置背景色
     *
     * @param bgColor
     */
    public void setCanvasBackgroundColor(int bgColor);

    /**
     * 获取背景色
     *
     * @return
     */
    public int getCanvasBackgroundColor();

    /**
     * 获取缩略图背景色透明度
     *
     * @return
     */
    public int getThumbnailBgAlpha();

    /**
     * 获取缩略图背景色
     *
     * @return
     */
    public int getThumbnailBackgroundColor();
}
