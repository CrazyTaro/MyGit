package com.crazytaro.bestapp.draw.params;/**
 * Created by xuhaolin on 15/8/25.
 */

import com.crazytaro.bestapp.draw.interfaces.ISeatParamsExport;
import com.crazytaro.bestapp.draw.interfaces.IStageParamsExport;

/**
 * created by xuhaolin at 2015/08/25
 * <p>外部参数设置接口</p>
 */
public class ExportParams {
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
}
