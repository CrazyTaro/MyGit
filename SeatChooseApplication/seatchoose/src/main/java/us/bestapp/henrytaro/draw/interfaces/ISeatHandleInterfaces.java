package us.bestapp.henrytaro.draw.interfaces;

import android.graphics.Canvas;

import us.bestapp.henrytaro.params.ExportParams;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.interfaces.IGlobleParamsExport;
import us.bestapp.henrytaro.params.interfaces.ISeatParamsExport;
import us.bestapp.henrytaro.params.interfaces.IStageParamsExport;

/**
 * Created by xuhaolin on 15/9/1.
 * 座位绘制与数据处理接口
 */
public interface ISeatHandleInterfaces {

    /**
     * 界面绘制,该方法提供给View调用,view.invalidate()本身也是重新调用此方法进行绘制
     * <p>此方法是先绘制实际界面,再绘制缩略图(若需要绘制的话)</p>
     *
     * @param canvas view画板
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    public void drawCanvas(Canvas canvas);


    /**
     * 设置座位绘制的数据,该二维表中的存放的应该为该位置的座位对应的座位类型,<font color="#ff9900"><b>此方法是将数据拷贝下来,修改数据请重新设置,不要在原引用数据上修改</b></font>，
     * 或者使用{@link #updateSeatTypeInMap(int, int, int)}方法更新数据
     * <p>此方法应该在View绘制前被调用,否则将获取不到绘制数据</p>
     *
     * @param seatMap 座位列表
     */
    public void setSeatDrawMap(int[][] seatMap);

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
     * 获取座位列表中某行某列的座位类型数据,可能返回{@link SeatParams#SEAT_TYPE_ERRO}(无效值)
     *
     * @param rowIndex    座位行索引
     * @param columnIndex 座位列索引
     * @return 返回该位置的座位类型, 若无法获取返回-1
     */
    public int getSeatTypeInSeatMap(int rowIndex, int columnIndex);

    /**
     * 获取座位列表数据
     *
     * @return
     */
    public int[][] getSeatDrawMap();

    /**
     * 设置座位信息监听事件
     *
     * @param mInterface {@link ISeatInformationListener}
     */
    public void setSeatInformationListener(ISeatInformationListener mInterface);


    /**
     * 获取参数设置对象，可进行座位/舞台及全局参数设置
     *
     * @return
     */
    public ExportParams getExportParams();

    /**
     * 重置座位绘制使用的参数,使用默认参数值
     * <p>(包括座位参数{@link ISeatParamsExport},<br/>
     * 舞台参数{@link IStageParamsExport},<br/>
     * 全局参数{@link IGlobleParamsExport})</p>
     * <p>此类中处理的座位参数及舞台参数是由内部管理的,不开放.
     * 设置参数值可用方法{@link #getExportParams()}获取设置参数的对象进行设置</p>
     */
    public void resetParams();

    /**
     * 设置初始绘制的XY轴偏移量,默认偏移量是0,且绘制的原始位置在视图的X轴中心,Y轴的最上方(centerX,topY)
     *
     * @param beginDrawPositionX X轴初始偏移量
     * @param begindrawPositionY Y轴初始偏移量
     */

    public void setOriginalOffset(float beginDrawPositionX, float begindrawPositionY);

    /**
     * 设置是否显示log
     *
     * @param isShow
     * @param tag    tag为显示log的标志,可为null,tag为null时使用默认标志"touch_event"
     */
    public void setIsShowLog(boolean isShow, String tag);
}
