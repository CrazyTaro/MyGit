package us.bestapp.henrytaro.params.interfaces;

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
     * 设置缩放最大值比,缩放最大倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="#ff9900"><b>使用默认参数{@link #DEFAULT_INT}可设置为原始默认值</b></font>,一般该参数大于1
     * <p>该缩放倍数是以默认高度为基数{@link #getHeight()}</p>
     *
     * @param large 放大倍数
     * @return 设置成功返回true, 否则返回false, 不改变原值
     */
    public boolean setLargeScaleRate(float large);

    /**
     * 设置缩放最小值比,缩放最小倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="#ff9900"><b>使用默认参数{@link #DEFAULT_FLOAT}可设置为原始默认值</b></font>,一般该参数在0-1之间
     * <p>该缩放倍数是以默认高度为基数{@link #getHeight()}</p>
     *
     * @param small 缩小比例
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setSmallScaleRate(float small);

//    /**
//     * 设置描述文字的颜色,此颜色是用于绘制时使用的(即当前绘制的文字必定使用此颜色,所以此颜色值是动态可变的)
//     * <p>设置座位描述文字时请勿使用此方法</p>
//     *
//     * @param color
//     */
//    public void setDescriptionColor(int color);
//
//    /**
//     * 获取描述文字的颜色(即当前绘制的文字必定使用此颜色,所以此颜色值是动态可变的)
//     */
//    public int getDescriptionColor();

    /**
     * 获取自动计算的描述文字字体大小，此值由当前参数height所决定，使字体的大小心保证与参数高度统一
     *
     * @return
     */
    public float getDescriptionSize();

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

    public float getWidth();

    /**
     * 设置宽度
     *
     * @param width
     */
    public void setWidth(float width);

    public float getHeight();

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    public void setHeight(float height);

    public float getRadius();

    /**
     * 设置圆角弧度，此处并不是以度数计算的，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param radius 圆角弧度
     */
    public void setRadius(float radius);

    /**
     * 获取此实例的自我复制
     *
     * @param newObj 用于存储拷贝数据的新的对象,若该对象为null会尝试创建一个新对象进行处理,<font color="#ff9900"><b>
     *               但创建的对象是基于基类,拷贝的数据也仅限于基类,同时若需要对继承子类的特有数据做更改则需要重写此类.</b></font>
     *               建议重写此类,创建一个子类的对象并作为参数传递进去即可
     * @return
     */
    public Object getClone(Object newObj);

}
