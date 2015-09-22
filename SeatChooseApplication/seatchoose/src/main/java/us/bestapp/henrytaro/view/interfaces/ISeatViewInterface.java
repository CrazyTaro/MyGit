package us.bestapp.henrytaro.view.interfaces;

import java.util.List;

import us.bestapp.henrytaro.draw.interfaces.ISeatDrawInterface;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.view.SeatChooseView;

/**
 * Created by lenovo on 2015/8/24.
 * <p>{@link SeatChooseView}实现的接口,用于提供对外设置参数的一些方法</p>
 */
public interface ISeatViewInterface {

    /**
     * 从被选中的座位中将某个座位移除<br/>
     * <font color="#ff9900"><b>此处需要注意的是,是否添加和移除一个座位,是通过{@link AbsSeatEntity#equals(Object)}来确定的,
     * 该方法已被覆盖并实现了默认的确定方案，若需要处理特殊的方法请重写该方法</b></font><br/>
     * <br/>
     * {@link AbsSeatEntity#equals(Object)}默认进行比较确定的为该对象的引用,及该对象的行列索引与行列号
     */
    public boolean removeSeat(AbsSeatEntity seatEntity);

    /**
     * 将选中的座位加入到选中座位列表中<br/>
     * <font color="#ff9900"><b>此处需要注意的是,是否添加和移除一个座位,是通过{@link AbsSeatEntity#equals(Object)}来确定的,
     * 该方法已被覆盖并实现了默认的确定方案，若需要处理特殊的方法请重写该方法</b></font>,重复的座位将不再添加<br/>
     * <br/>
     * {@link AbsSeatEntity#equals(Object)}默认进行比较确定的为该对象的引用,及该对象的行列索引与行列号
     */
    public boolean addSeat(AbsSeatEntity seatEntity);

    /**
     * 获取座位绘制数据处理接口
     *
     * @return
     */
    public ISeatDrawInterface getSeatDrawInterface();

    /**
     * 设置每次选座最多可以选择的座位个数
     *
     * @param mostCount
     */
    public void setMostSeletedCount(int mostCount);

    /**
     * 获取当前已经被选择的座位数量,当不存在选中座位时,返回null
     * <p>其中point对象中，x为该选中座位的行索引值,y为该选中座位的列索引值</p>
     */
    public List<AbsSeatEntity> getSeletedSeats();

    /**
     * 设置座位选中监听事件
     *
     * @param eventListener {@link ISeatChooseEvent},选择座位回调的事件
     */
    public void setISeatChooseEvent(ISeatChooseEvent eventListener);

}
