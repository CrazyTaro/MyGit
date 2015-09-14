package us.bestapp.henrytaro.draw.interfaces;

import android.graphics.Canvas;

import us.bestapp.henrytaro.entity.interfaces.IMapEntity;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;

/**
 * Created by xuhaolin on 15/9/1.
 * 座位绘制与数据处理接口
 */
public interface ISeatDrawInterface {

    /**
     * 界面绘制,该方法提供给View调用,view.invalidate()本身也是重新调用此方法进行绘制
     * <p>此方法是先绘制实际界面,再绘制缩略图(若需要绘制的话)</p>
     *
     * @param canvas view画板
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    public void drawCanvas(Canvas canvas);

    /**
     * 设置座位Map
     *
     * @param seatMap
     */
    public void setSeatDrawMap(IMapEntity seatMap);

    /**
     * 获取当前绘制使用的座位表
     *
     * @return
     */
    public IMapEntity getSeatDrawMap();

    /**
     * 更新座位Map中的数据
     *
     * @param type             座位类型
     * @param rowIndexInMap    map中的行索引
     * @param columnIndexInMap map中的列索引
     * @return
     */
    public boolean updateSeatIMap(int type, int rowIndexInMap, int columnIndexInMap);

    /**
     * 获取map指定位置中的区域内的座位类型(可能该位置不存在实际的座位)
     *
     * @param rowIndex    map中的行
     * @param columnIndex map中的列
     * @return
     */
    public int getSeatTypeInMap(int rowIndex, int columnIndex);


    /**
     * 设置座位信息监听事件
     *
     * @param mInterface {@link ISeatInformationListener}
     */
    public void setSeatInformationListener(ISeatInformationListener mInterface);

    /**
     * 获取座位参数设置
     *
     * @return
     */
    public ISeatParams getSeatParams();

    /**
     * 获取舞台参数设置
     *
     * @return
     */
    public IStageParams getStageParams();

    /**
     * 获取全局参数设置
     *
     * @return
     */
    public IGlobleParams getGlobleParams();


    /**
     * 重置座位绘制使用的参数,使用默认参数值
     * (包括座位参数{@link ISeatParams},舞台参数{@link IStageParams},全局参数{@link IGlobleParams})
     * <p>此类中处理的座位参数及舞台参数是由内部管理的,不开放.
     * 设置参数值可通过获取设置参数的对象进行设置</p><br/>
     * {@link #getSeatParams()},座位设置<br/>
     * {@link #getStageParams()},舞台设置<br/>
     * {@link #getGlobleParams()},全局参数设置<br/>
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
