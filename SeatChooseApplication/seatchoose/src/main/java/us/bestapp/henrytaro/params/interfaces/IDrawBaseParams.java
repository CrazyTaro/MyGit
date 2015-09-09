package us.bestapp.henrytaro.params.interfaces;

/**
 * Created by xuhaolin on 15/9/8.<br/>
 * 绘制参数接口的基本接口,定义了绘制方法中使用的通用方法,<font ="#ff9900"><b>继承绘制类{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}自定义绘制方法时,
 * 绘制座位/舞台需要的数据操作接口中通用的数据操作接口来自于此接口</b></font>
 * <br/>{@since 一般不继承此接口, 通过继承其子接口 {@link IDrawSeatParams}/{@link IDrawStageParams}实现需要的方法}
 */
public interface IDrawBaseParams extends IBaseParams {
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
     * 设置缩放最大值比,缩放最大倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="#ff9900"><b>使用默认参数{@link IBaseParams#DEFAULT_INT}可设置为原始默认值</b></font>,一般该参数大于1
     * <p>该缩放倍数是以默认高度为基数</p>
     *
     * @param large 放大倍数
     * @return 设置成功返回true, 否则返回false, 不改变原值
     */
    public boolean setLargeScaleRate(int large);


    /**
     * 设置缩放最小值比,缩放最小倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="#ff9900"><b>使用默认参数{@link IBaseParams#DEFAULT_FLOAT}可设置为原始默认值</b></font>,一般该参数在0-1之间
     * <p>该缩放倍数是以默认高度为基数</p>
     *
     * @param small 缩小比例
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setSmallScaleRate(float small);

    /**
     * 获取最大的高度
     *
     * @return
     */
    public float getLargestHeight();

    /**
     * 获取最小的高度
     *
     * @return
     */
    public float getSmallestHeight();

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
     * 获取当前绘制的颜色
     *
     * @return
     */
    public int getColor();

    /**
     * 设置当前的绘制颜色值
     *
     * @param color
     */
    public void setColor(int color);

    /**
     * 是否可以进行缩放,用于检测当前比例是否允许进行缩放,<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比例
     * @return 可以缩放返回true, 否则返回false
     */
    public boolean isCanScale(float scaleRate);

    /**
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比
     * @param isTrueSet 是否将此次缩放结果记录为永久结果
     */
    public void setScaleRate(float scaleRate, boolean isTrueSet);

    /**
     * 获取当前的绘制界面与原始(最初设定的界面)的比例,当前界面/原始界面
     *
     * @return
     */
    public float getScaleRateCompareToOriginal();

    /**
     * 设置预设的缩放比为当前值(只用于双击缩放)
     *
     * @param isSetEnlarge 是否使用最大比例值替换当前值,true使用最大预设值替换当前值,false使用最小值替换当前值
     * @return
     */
    public float setOriginalValuesToReplaceCurrents(boolean isSetEnlarge);

    /**
     * 存储预设的缩放比,保证不管界面用户如何缩放在双击时都可以正常缩放到某个预设的比例
     *
     * @param copyObj 用于拷贝的存储对象,此参数可为null,若不为null则以此参数为源拷贝值
     */
    public void storeOriginalValues(Object copyObj);

    /**
     * 获取存储的原始座位数据,返回的数据类型由子类自身确定(子类通过创建内部类用于存储的方式进行数据存储)
     *
     * @return
     */
    public Object getOriginalValues();
}
