package us.bestapp.henrytaro.view.interfaces;

import android.graphics.Point;

import java.util.List;

import us.bestapp.henrytaro.draw.interfaces.ISeatHandleInterfaces;
import us.bestapp.henrytaro.view.SeatChooseView;

/**
 * Created by lenovo on 2015/8/24.
 * <p>{@link SeatChooseView}实现的接口,用于提供对外设置参数的一些方法</p>
 */
public interface ISeatViewInterface {

    /**
     * 获取座位绘制数据处理接口
     *
     * @return
     */
    public ISeatHandleInterfaces getSeatHandleInterface();

    /**
     * 设置每次选座最多可以选择的座位个数
     *
     * @param mostCount
     */
    public void setMostSeletedCount(int mostCount);

    /**
     * 获取当前已经被选择的座位数量
     * <p>其中point对象中，x为该选中座位的行索引值,y为该选中座位的列索引值</p>
     */
    public List<Point> getSeletedSeats();

    /**
     * 设置座位选中监听事件
     *
     * @param eventListener {@link ISeatChooseEvent},选择座位回调的事件
     */
    public void setISeatChooseEvent(ISeatChooseEvent eventListener);

}
