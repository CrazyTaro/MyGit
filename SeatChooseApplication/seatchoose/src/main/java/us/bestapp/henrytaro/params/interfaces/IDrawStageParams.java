package us.bestapp.henrytaro.params.interfaces;

/**
 * Created by xuhaolin on 15/9/8.<br/>
 * 绘制舞台时需要的参数接口,继承自基础绘制接口{@link IDrawBaseParams},继承自公开接口{@link IStageParams},<font ="#ff9900"><b>继承绘制类{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}自定义绘制方法时,
 * 绘制舞台需要的数据操作接口来自于此接口</b></font>
 */
public interface IDrawStageParams extends IStageParams, IDrawBaseParams {
    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    public float getStageTotalHeight();
}
