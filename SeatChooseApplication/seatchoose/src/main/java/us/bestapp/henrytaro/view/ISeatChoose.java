package us.bestapp.henrytaro.view;

import android.graphics.Point;

import java.util.List;

import us.bestapp.henrytaro.draw.params.ExportParams;

/**
 * Created by lenovo on 2015/8/24.
 * <p>{@link SeatChooseView}实现的接口,用于提供对外设置参数的一些方法</p>
 */
public interface ISeatChoose {
    /**
     * 重置座位绘制使用的参数(使用默认值)
     */
    public void resetParams();

    /**
     * 设置座位绘制的数据,该二维表中的存放的应该为该位置的座位对应的座位类型,<font color="#ff9900"><b>此方法是将数据拷贝下来,修改数据请重新设置,不要在原引用数据上修改，或者使用{@link #updateSeatTypeInMap(int, int, int)}方法更新数据</b></font>
     * <p>此方法应该在View绘制前被调用,否则将获取不到绘制数据</p>
     *
     * @param seatMap 座位列表
     */
    public void setSeatMap(int[][] seatMap);

    /**
     * 更新座位列表中的数据
     *
     * @param seatType    座位类型
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return 更新成功返回true, 否则返回false
     */
    public boolean updateSeatTypeInMap(int seatType, int rowIndex, int columnIndex);

    /**
     * 获取设置参数对象，可进行座位/舞台参数设置
     *
     * @return
     */
    public ExportParams getParams();

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

    /**
     * 是否显示日志
     *
     * @param isShow
     * @param tag
     */
    public void setIsShowLog(boolean isShow, String tag);
}
