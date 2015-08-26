package com.crazytaro.bestapp.draw.interfaces;

import android.graphics.Color;

/**
 * Created by xuhaolin on 15/8/25.
 * 全局设置参数接口,所有接口来自于{@link com.crazytaro.bestapp.draw.params.BaseParams},此接口内的方法是独立且涉及全局数据的设置的,是独立对外开放的接口
 * <p>此接口与{@link IBaseParamsExport}不同,此接口为设置全局性的参数,而{@link IBaseParamsExport}是设置具体参数类型(座位/舞台)对应的相同的参数(宽/高等)</p>
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
