package us.bestapp.henrytaro.params.interfaces;

/**
 * Created by xuhaolin on 15/9/8.
 */
public interface IDrawStageParams extends IStageParams, IDrawBaseParams {
    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    public float getStageTotalHeight();
}
