package us.bestapp.henrytaro.params.interfaces;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;

import us.bestapp.henrytaro.params.SeatParams;

/**
 * Created by xuhaolin on 15/9/8.<br/>
 * 绘制座位时需要的参数接口,继承自基础绘制接口{@link IDrawBaseParams},继承自公开接口{@link ISeatParams},<font ="#ff9900"><b>继承绘制类{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}自定义绘制方法时,
 * 绘制座位需要的数据操作接口来自于此接口</b></font>
 */
public interface IDrawSeatParams extends ISeatParams, IDrawBaseParams {

    /**
     * 根据座位类型来确定座位是否需要绘制
     *
     * @param seatType 座位类型
     */
    public void setIsDraw(int seatType);

    /**
     * 获取自动计算并分离的seatParams,用于座位类型的分批绘制
     *
     * @param seatTypeRowCount 座位类型绘制的行数
     * @return
     */
    public SeatParams[] getAutoSeparateParams(int seatTypeRowCount);


    /**
     * 根据座位类型获取座位类型对应的颜色
     *
     * @param seatType 座位类型
     *                 <p>默认座位类型
     *                 可选座位<br/>
     *                 已选座位<br/>
     *                 已售座位<br/>
     *                 </p>
     * @return 返回对应的座位颜色, 若查询不到对应的座位类型颜色则返回默认颜色值
     */
    public int getSeatColorByType(int seatType);

    /**
     * 根据座位类型获取座位绘制的图片
     *
     * @param context  上下文对象,用于加载图片
     * @param seatType 座位类型
     * @return 返回座位类型对应的图片, 可能为null
     */
    public Bitmap getSeatBitmapByType(Context context, int seatType);

    /**
     * 获取计算后的图片绘制区域(即一个完整地座位占用的区域)
     *
     * @param imageRecft    图片绘制区域,可为null,用于重复利用
     * @param drawPositionX 绘制的X轴中心位置
     * @param drawPositionY 绘制的Y轴中心位置
     * @return
     */
    public RectF getSeatDrawImageRecf(RectF imageRecft, float drawPositionX, float drawPositionY);


    /**
     * 获取主座位绘制矩形或者次座位绘制矩形
     *
     * @param seatRectf     座位绘制矩形对象
     * @param drawPositionX 绘制座位的中心X轴位置
     * @param drawPositionY 绘制座位的中心Y轴位置
     * @param isMainSeat    true为获取主座位,false为获取次座位
     * @return
     */
    public RectF getSeatDrawDefaultRectf(RectF seatRectf, float drawPositionX, float drawPositionY, boolean isMainSeat);
}
