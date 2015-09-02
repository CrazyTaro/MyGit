package us.bestapp.henrytaro.params;

import us.bestapp.henrytaro.params.interfaces.IGlobleParamsExport;
import us.bestapp.henrytaro.params.interfaces.ISeatParamsExport;
import us.bestapp.henrytaro.params.interfaces.IStageParamsExport;

/**
 * created by xuhaolin at 2015/08/25
 * <p>外部参数设置类,实现了全局参数设置接口{@link IGlobleParamsExport},全局参数的设置在此类实现</p>
 */
public final class ExportParams {
    private IGlobleParamsExport mGlobleParams = null;
    private ISeatParamsExport mSeatParams = null;
    private IStageParamsExport mStageParams = null;

    public ExportParams(ISeatParamsExport seatParams, IStageParamsExport stageParams, IGlobleParamsExport globleParams) {
        this.mSeatParams = seatParams;
        this.mStageParams = stageParams;
        this.mGlobleParams = globleParams;
    }

    /**
     * 创建默认的外部参数接口,座位参数及舞台参数全部使用原始的数据
     */
    public ExportParams() {
        this.mSeatParams = new SeatParams();
        this.mStageParams = new StageParams();
        this.mGlobleParams = new GlobleParams();
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

    /**
     * 获取全局参数设置接口
     *
     * @return
     */
    public IGlobleParamsExport getGlobleParams() {
        return this.mGlobleParams;
    }
}
