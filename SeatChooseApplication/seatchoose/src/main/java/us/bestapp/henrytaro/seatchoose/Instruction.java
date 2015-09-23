package us.bestapp.henrytaro.seatchoose;

import us.bestapp.henrytaro.seatchoose.draw.utils.AbsDrawUtils;
import us.bestapp.henrytaro.seatchoose.draw.utils.SimpleDrawUtils;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.seatchoose.entity.example.EgSeatMap;
import us.bestapp.henrytaro.seatchoose.params.SeatParams;
import us.bestapp.henrytaro.seatchoose.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.seatchoose.params.baseparams.BaseStageParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.seatchoose.params.interfaces.IStageParams;

/**
 * Created by xuhaolin on 15/9/15.
 * 程序结构说明
 */
public interface Instruction {
    /**
     * 关于{@link AbsDrawUtils}<br/>
     * AbsDrawUtils是绘制界面的主要操作类,所有的参数提供来自外部,并且在此类中内部处理并绘制结果;<br/>
     * 自定义绘制的内部只要继承AbsDrawUtils,重写其抽象方法:绘制普通(非图片)座位及舞台的方法即可;<br/>
     * 默认并不提供任何的绘制模型,若需要使用简单的绘制模型请使用{@link SimpleDrawUtils};<br/>
     * <br/>
     */
    public void aboutAbsDrawUtils();

    /**
     * 关于{@link BaseSeatParams}<br/>
     * BaseSeatParams是绘制所需要的座位参数,这是不可变的,除非不使用AbsDrawUtils;<br/>
     * 使用BaseSeatParams需要注意的是,继承此类实现其子类可以实现自定义的一些参数处理;比如更新默认绘制的座位形状等;<br/>
     * 继承此类的子类必须存在默认的构造函数,否则会导致内部的创建失败!;<br/>
     * 继承此类的子类需要注意{@link BaseSeatParams#getClone(Object)}方法,对于此复制自身的方法来说,
     * 如果不需要的复制的话可以忽略不处理,若需要请注意是否需要处理某些特别的情况(比如重新初始化一些数据等),请参考{@link SeatParams}<br/>
     */
    public void aboutSeatParams();


    /**
     * 关于{@link BaseStageParams}<br/>
     * BaseStageParams是绘制所需要的舞台参数,这是不可变的,除非不使用AbsDrawUtils;<br/>
     * 使用BaseStageParams需要注意的是,默认的舞台绘制方式是长方形,若需要额外处理舞台形状应该重写{@link BaseStageParams#getStagePathPoint(float, float)}<br/>
     */
    public void aboutStageParams();


    /**
     * 关于其它<br/>
     * 所有参数对外设置接口来自于{@link ISeatParams}与{@link IStageParams}<br/>
     * 全局参数的设置接口来自于{@link IGlobleParams}<br/>
     * 需要测试数据时可以直接使用二维表数据进行类型测试,通过使用默认提供的测试对象即可{@link EgSeatMap}<br/>
     * 需要自定义SeatMap类型用于显示界面请继承{@link AbsMapEntity}抽象列表类;{@link AbsRowEntity}抽象行类;
     * {@link AbsSeatEntity}抽象座位类,此三个抽象类是基于绘制类AbsDrawUtils所需要的,必须继承自此三个抽象类才可以正常处理并显示数据<br/>
     */
    public void others();
}
