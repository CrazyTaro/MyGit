package com.crazytaro.bestapp.view;

import android.graphics.Point;

import com.crazytaro.bestapp.draw.utils.SeatParams;
import com.crazytaro.bestapp.draw.utils.StageParams;

import java.util.List;

/**
 * Created by lenovo on 2015/8/24.
 */
public interface ISeatChoose {
    /**
     * 重置座位绘制使用的参数(使用默认值)
     */
    public void resetParams();

    /**
     * 设置座位绘制的数据,该二维表中的存放的应该为该位置的座位对应的座位类型,<font color="yellow"><b>此方法是将数据拷贝下来,修改数据请重新设置,不要在原引用数据上修改，或者使用{@link #updateSeatTypeInMap(int, int, int)}方法更新数据</b></font>
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
     * 设置座位绘制使用的参数
     *
     * @param params 自定义座位绘制的参数
     */
    public void setSeatParams(SeatParams params);

    /**
     * 设置舞台绘制使用的参数
     *
     * @param params 自定义舞台绘制的参数
     */
    public void setStageParams(StageParams params);

    /**
     * 获取座位参数对象，可进行座位参数设置
     *
     * @return
     */
    public SeatParams getSeatParams();

    /**
     * 获取舞台参数对象，可进行舞台参数设置
     *
     * @return
     */
    public StageParams getStageParams();

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
     * 设置是否保持缩略图的显示，若设为false，则缩略图只在拖动界面或者是缩放时显示，当界面静止是不显示缩略图
     *
     * @param isShowAlways
     */
    public void setIsShowThumbnailAlways(boolean isShowAlways);

    /**
     * 设置是否绘制缩略图
     *
     * @param isDraw
     */
    public void setIsDrawThumbnail(boolean isDraw);

    /**
     * 设置座位选中监听事件
     *
     * @param eventListener
     */
    public void setISeatChooseEvent(ISeatChooseEvent eventListener);

    /**
     * 是否显示日志
     *
     * @param isShow
     */
    public void setIsShowLog(boolean isShow);
}
