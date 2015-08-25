package com.crazytaro.bestapp.draw.params;/**
 * Created by xuhaolin on 15/8/25.
 */

import com.crazytaro.bestapp.draw.interfaces.IGlobleParamsExport;
import com.crazytaro.bestapp.draw.interfaces.ISeatParamsExport;
import com.crazytaro.bestapp.draw.interfaces.IStageParamsExport;

/**
 * created by xuhaolin at 2015/08/25
 * <p>外部参数设置接口</p>
 */
public class ExportParams implements IGlobleParamsExport {
    ISeatParamsExport mSeatParams = null;
    IStageParamsExport mStageParams = null;

    public ExportParams(ISeatParamsExport seatParams, IStageParamsExport stageParams) {
        this.mSeatParams = seatParams;
        this.mStageParams = stageParams;
    }

    /**
     * 创建默认的外部参数接口,座位参数及舞台参数全部使用原始的数据
     */
    public ExportParams() {
        this.mSeatParams = new SeatParams();
        this.mStageParams = new StageParams();
    }


    /**
     * 获取座位参数设置接口
     *
     * @return
     */
    public ISeatParamsExport getSeatParams() {
        return this.mSeatParams;
    }

    /**
     * 获取舞台参数设置接口
     *
     * @return
     */
    public IStageParamsExport getStageParams() {
        return this.mStageParams;
    }

    @Override
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha) {
        return BaseParams.setThumbnailBackgroundColorWithAlpha(color, alpha);
    }

    @Override
    public void setCanvasBackgroundColor(int bgColor) {
        BaseParams.setCanvasBackgroundColor(bgColor);
    }

    @Override
    public int getCanvasBackgroundColor() {
        return BaseParams.getCanvasBackgroundColor();
    }

    @Override
    public int getThumbnailBgAlpha() {
        return BaseParams.getThumbnailBgAlpha();
    }

    @Override
    public int getThumbnailBackgroundColor() {
        return BaseParams.getThumbnailBackgroundColor();
    }
}
