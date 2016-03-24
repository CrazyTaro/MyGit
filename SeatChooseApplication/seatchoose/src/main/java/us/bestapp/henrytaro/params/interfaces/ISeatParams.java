package us.bestapp.henrytaro.params.interfaces;

import java.util.List;

import us.bestapp.henrytaro.params.baseparams.BaseDrawStyle;

/**
 * Created by xuhaolin on 2015/8/24.
 * 座位参数设置接口,此接口用于公开可进行设置的参数方法,不允许访问和设置的方法没有在此接口公开(此接口仅仅相当于一个中间转换而已)<br/>
 * <br/>
 * <font color="#ff9900"><b>关于标签及样式的设定,默认情况下{@link us.bestapp.henrytaro.params.baseparams.BaseSeatParams}预存了4个样式,
 * 其中包括<br/>
 * {@link #TAG_OPTIONAL_SEAT} 可选标签样式<br/>
 * {@link #TAG_SELECTE_SEAT} 已选标签样式<br/>
 * {@link #TAG_LOCK_SEAT} 锁定标签样式<br/>
 * {@link #TAG_COUPLE_OPTIONAL_SEAT} 情侣标签样式<br/></b></font>
 * <br/>
 * 其余的样式并没有预存,但是一样可以使用{@link #TAG_ERROR_SEAT}/{@link #TAG_UNSHOW_SEAT}两个标签,因为这两个样式默认是不进行绘制的,
 * 获取某个标签对象的样式若不存在的情况下,并不会做任何的绘制,所以需要绘制的样式请确保提供的样式及标签是有效的<br/>
 */
public interface ISeatParams extends IBaseParams {
    /**
     * 锁定座位标签(即不可选)
     */
    public static final String TAG_LOCK_SEAT = "tag_lock_seat";
    /**
     * 可选座位标签
     */
    public static final String TAG_OPTIONAL_SEAT = "tag_optional_seat";
    /**
     * 已选座位标签
     */
    public static final String TAG_SELECTE_SEAT = "tag_selecte_seat";
    /**
     * 情侣座位标签(未选中前的样式,选中样式同已选)
     */
    public static final String TAG_COUPLE_OPTIONAL_SEAT = "tag_couple_seat";
    /**
     * 错误座位标签
     */
    public static final String TAG_ERROR_SEAT = "tag_error_seat";
    /**
     * 不显示座位标签
     */
    public static final String TAG_UNSHOW_SEAT = "tag_unshow_seat";

    /**
     * 获取普通座位绘制之间的水平间隔
     *
     * @return
     */
    public float getSeatHorizontalInterval();

    /**
     * 设置每个座位与邻近座位之间的水平间隔距离
     *
     * @param mSeatHorizontalInterval
     */
    public void setSeatHorizontalInterval(float mSeatHorizontalInterval);

    /**
     * 获取普通座位绘制之间的垂直间隔
     *
     * @return
     */
    public float getSeatVerticalInterval();

    /**
     * 设置每个座位与邻近座位之间的垂直间隔距离
     *
     * @param mSeatVerticalInterval
     */
    public void setSeatVerticalInterval(float mSeatVerticalInterval);

    /**
     * 设置座位与邻近文字之间的间隔,<font color="#ff9900"><b>此参数仅在绘制座位类型及其描述(座位带文字)的时候有效,在绘制普通座位(仅座位不存在文字)时无效</b></font>
     *
     * @param mSeatTextInterval
     */
    public void setDrawStyleDescInterval(float mSeatTextInterval);

    /**
     * 获取座位与邻近文字之间的间隔,<font color="#ff9900"><b>此参数仅在绘制座位类型及其描述(座位带文字)的时候有效,在绘制普通座位(仅座位不存在文字)时无效</b></font>
     *
     * @return
     */
    public float getDrawStyleDescInterval();

    /**
     * 获取座位类型(包括描述文字为一整体)之间的水平间隔
     *
     * @return
     */
    public float getDrawStyleInterval();

    /**
     * 设置座位类型之间的间隔,<font color="#ff9900"><b>此参数仅在绘制座位类型及其描述的时候有效,在绘制普通座位时无效</b></font>.
     * <p>
     * 设置普通座位之间的间隔请用{@link #setSeatHorizontalInterval(float)}<br/>
     * 设置普通座位上下之间的间隔请用{@link #setSeatVerticalInterval(float)}<br/>
     * </p>
     *
     * @param mSeatTypeInterval
     */
    public void setDrawStyleInterval(float mSeatTypeInterval);


    /**
     * 返回座位类型的长度
     *
     * @return
     */
    public int getDrawStyleLength();


    /**
     * 设置是否绘制座位类型
     *
     * @param isDraw
     */
    public void setIsDrawSampleStyle(boolean isDraw);

    /**
     * 获取是否绘制座位类型标志
     *
     * @return
     */
    public boolean isDrawDrawStyle();

    /**
     * 获取座位类型样式的所有标签,获取的标签不一定是按存入的顺序,因为内部是通过map的方式存放数据的
     *
     * @return
     */
    public List<String> getDrawStyleTags();

    /**
     * 获取指定标签的座位类型样式,若不存在返回null
     *
     * @param typeTag
     * @return
     */
    public BaseDrawStyle getDrawStyle(String typeTag);

    /**
     * 新增一个座位类型样式及其标签,标签不可与其它标签重复，否则原有的样式会被替换<br/>
     * <font color="#ff9900"><b>此处要注意的是,默认情况下{@link us.bestapp.henrytaro.params.baseparams.BaseSeatParams}预存了4个样式,
     * 其中包括<br/>
     * {@link #TAG_OPTIONAL_SEAT} 可选标签样式<br/>
     * {@link #TAG_SELECTE_SEAT} 已选标签样式<br/>
     * {@link #TAG_LOCK_SEAT} 锁定标签样式<br/>
     * {@link #TAG_COUPLE_OPTIONAL_SEAT} 情侣标签样式<br/></b></font>
     * <br/>
     * 其余的样式并没有预存,但是一样可以使用{@link #TAG_ERROR_SEAT}/{@link #TAG_UNSHOW_SEAT}两个标签,因为这两个样式默认是不进行绘制的,
     * 获取某个标签对象的样式若不存在的情况下,并不会做任何的绘制,所以需要绘制的样式请确保提供的样式及标签是有效的<br/>
     *
     * @param typeTag  标签
     * @param newStyle 新样式
     * @return 返回被替换下来的样式对象, 若此标签不存在样式, 则返回null
     */
    public BaseDrawStyle addNewDrawStyle(String typeTag, BaseDrawStyle newStyle);

    /**
     * 通过标签移除一个座位类型样式
     *
     * @param typeTag
     * @return 返回被移除的样式对象, 若此标签不存在样式, 则返回null
     */
    public BaseDrawStyle removeDrawStyle(String typeTag);

    /**
     * 清除所有的样式及其标签
     */
    public void clearDrawStyles();

    /**
     * 设置是否按指定顺序绘制座位类型样式
     *
     * @param isInOrder
     * @param tagInOrder 按某种顺序排序好的座位类型样式的标签,<font color="#ff9900"><b>此处应该注意,
     *                   若isInOrder为true,则参数才有效且不可为null,否则没有任何意义;并且此时绘制的座位样式数量及实际内容是以提供的标签为主,
     *                   即若提供顺序的标签完全不匹配实际任何一个样式,则不会有任何一个样式将会被绘制</b></font>
     */
    public void setIsDrawStyleByOrder(boolean isInOrder, List<String> tagInOrder);

    /**
     * 获取是否按指定顺序绘制座位类型样式
     *
     * @return
     */
    public boolean isDrawStyleByOrder();

    /**
     * 是否按指定顺序获取所有样式
     *
     * @param isInOrder
     * @return
     */
    public List<BaseDrawStyle> getDrawStyles(boolean isInOrder);
}
