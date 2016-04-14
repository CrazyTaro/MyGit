package us.bestapp.henrytaro.params.interfaces;

import android.graphics.PointF;

/**
 * Created by xuhaolin on 2015/8/24.
 * 基本参数设置接口,所有的接口为通用接口,分别为座位与舞台两种不同的数据设置而提供的通用接口,且这一部分的接口为对外公开设置可用
 * <br/>{@since 一般不继承此接口, 通过继承其子接口 {@link ISeatParams}/{@link IStageParams}实现需要的方法}
 */
public interface IBaseParams {
    /**
     * 默认整数值:-1
     */
    public static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    public static final float DEFAULT_FLOAT = -1;
    /**
     * 绘制方式,默认方式,使用颜色及图形绘制
     */
    public static final int DRAW_TYPE_DEFAULT = -1;
    /**
     * 绘制方式,使用图片填充
     */
    public static final int DRAW_TYPE_IMAGE = -2;
    /**
     * 绘制方式,缩略图模式
     */
    public static final int DRAW_TYPE_THUMBNAIL = 1;
    /**
     * 座位的绘制类型,不绘制
     */
    public static final int DRAW_TYPE_NO = 0;

    /**
     * 错误类型,包括座位或者舞台或者任何继承或实现此接口的类都可以使用<br/>
     * TYPE_ERROR = {@value}
     */
    public static final int TYPE_ERROR = -1;

    /**
     * 设置默认值，当默认值需要改变时，必须调用此方法;一般情况下,建议只调用此方法一次(除非默认值需要改变)</br>
     * 这里的默认值指的是在初次绘制界面时参数使用的值</br>
     * 重复设置默认值但不需要对某些值进行改变时,使用{@link #DEFAULT_FLOAT}或者{@link #DEFAULT_INT}即可不替换原值.
     *
     * @param defaultWidth   默认宽度
     * @param defaultHeight  默认高度
     * @param defaultRadius  默认圆角弧度
     * @param defaultColor   默认颜色
     * @param largeScaleRate 默认最大缩放比例
     * @param smallScaleRate 默认最小缩放比例
     */
    public void setDefault(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor, float largeScaleRate, float smallScaleRate);

    /**
     * 获取设置默认的宽高值,此值也是缩放最大小值的比较基础.pointF.x为宽度,pointF.y为高度
     *
     * @return
     */
    public PointF getDefaultWidthAndHeight();

    /**
     * 设置缩放可达的最大比例,此比例基于{@link #setDefault(float, float, float, int, float, float)}中的宽高,缩放时最大值不允许超过此比例与 default_width/height 的乘积(max = large*default_width/height).
     *
     * @param large 最大比例,通用于宽高
     * @return 设置成功返回true, 否则返回false, 不改变原值
     */
    public boolean setLargeScaleRate(float large);

    /**
     * 获取最大的缩放比例
     *
     * @return
     */
    public float getLargeScaleRate();

    /**
     * 获取宽的边界值,即最大值及最小值,最大值存放在pointF.x中,最小值存放在pointF.y中
     *
     * @return
     */
    public PointF getEdgeScaleWidth();

    /**
     * 设置缩放可达的最小缩放比例,使用原理同{@link #setLargeScaleRate(float)}
     *
     * @param small 最小缩放比例,通用于宽高
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setSmallScaleRate(float small);

    /**
     * 获取最小的宽大小
     *
     * @return
     */
    public float getSmallScaleRate();

    /**
     * 获取高的边界值,即最大值及最小值,最大值存放在pointF.x中,最小值存放在pointF.y中
     *
     * @return
     */
    public PointF getEdgeScaleHeight();

    /**
     * 获取自动计算的描述文字字体大小，此值由当前height或width所决定(宽高中的最小边)，使字体的大小保证与参数高度统一
     *
     * @param textRate 0-1之间,不可取0.该参数有效时返回当前宽高中最小边与该参数的积;默认值为0.8f,使用默认值可用{@link #DEFAULT_FLOAT},任何不合法数据都会使用默认值
     * @return
     */
    public float getDescriptionSize(float textRate);

    /**
     * 设置绘制方式,使用此方法时必须注意,在设置图片之后可以通过此方法设置为默认的绘制模式;<br/>
     * 同样的也可以在绘制默认模式下设置为图片绘制模式;但这种方法存在一定的危险性;<br/>
     * 当设置为图片绘制模式但不存在图片时将会抛出异常;<br/>
     * <font color="#ff9900"><b>请确保在调用此方法设置为图片绘制模式时已经设置好了图片</b><br/>
     * <br/>
     *
     * @param drawType 绘制方式
     *                 <p>
     *                 {@link #DRAW_TYPE_DEFAULT},默认绘制方式,使用图形及颜色绘制<br/>
     *                 {@link #DRAW_TYPE_IMAGE},图片绘制方式<br/>
     *                 </p>
     */
    public void setDrawType(int drawType);

    /**
     * 获取绘制的方式
     * <p>
     * {@link #DRAW_TYPE_DEFAULT}默认绘制方式,使用图形及颜色绘制<br/>
     * {@link #DRAW_TYPE_IMAGE}图片绘制方式,使用图片填充<br/>
     * {@link #DRAW_TYPE_THUMBNAIL}缩略图绘制模式<br/>
     * </p>
     *
     * @param isGetOriginalDrawType 是否获取实际的绘制类型(存在缩略图的情况下,缩略图不属于实际的绘制方式中的任何一种),true返回实际绘制类型,false返回缩略图绘制模式(如果允许绘制缩略图的话)
     * @return
     */
    public int getDrawType(boolean isGetOriginalDrawType);

    /**
     * 设置是否使用绘制缩略图的参数,缩略图的缩放比例只由宽度决定,高度是可变的<br/>
     * <font color="#ff9900"><b>只有当设置为true时,后面两个参数才有效,否则无效</b></font>
     *
     * @param isDrawThumbnail 是否绘制缩略图,<font color="#ff9900"><b>此参数为true,则所有的座位相关的绘制数据返回时将计算为缩略图的大小返回</b></font>
     * @param originalWidth   实际绘制界面的宽度
     * @param targetWidth     目标缩略图的宽度
     */
    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth);

    /**
     * 获取是否绘制缩略图
     *
     * @return
     */
    public boolean isDrawThumbnail();

    /**
     * 设置是否进行对象绘制
     *
     * @param isDraw
     */
    public void setIsDraw(boolean isDraw);

    /**
     * 是否绘制对象
     *
     * @return 返回true绘制, 返回false不绘制
     */
    public boolean isDraw();

    /**
     * 获取当前需要绘制的宽,当绘制缩略图时,获取的为缩略图绘制数据,缩略图宽度;
     * 当绘制正常的界面时,获取的为正常的绘制数据,是正常宽度
     *
     * @return
     */
    public float getDrawWidth();

    /**
     * 获取非缩略图时的宽度,即正常宽度
     *
     * @return
     */
    public float getWidthNotInThumbnail();

    /**
     * 设置宽度
     *
     * @param width
     */
    public boolean seDrawWidth(float width);

    /**
     * 获取当前需要绘制的高,当绘制缩略图时,获取的为缩略图绘制数据,缩略图高度;
     * 当绘制正常的界面时,获取的为正常的绘制数据,是正常高度
     *
     * @return
     */
    public float getDrawHeight();

    /**
     * 获取非缩略图时的高度,即正常高度
     *
     * @return
     */
    public float getHeightNotInThumbnail();

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    public boolean seDrawHeight(float height);

    /**
     * 获取当前需要绘制的角度,可能是缩略图状态也可能不是
     *
     * @return
     */
    public float getDrawRadius();

    /**
     * 设置圆角弧度，此处并不是以度数计算的，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param radius 圆角弧度
     */
    public void setDrawRadius(float radius);

}
