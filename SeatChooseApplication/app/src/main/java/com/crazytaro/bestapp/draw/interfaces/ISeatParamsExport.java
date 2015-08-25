package com.crazytaro.bestapp.draw.interfaces;

import android.graphics.Bitmap;

/**
 * Created by xuhaolin on 2015/8/24.
 */
public interface ISeatParamsExport extends IBaseParamsExport {
    /**
     * 获取设置的对应的图像资源bitmap,可能返回null
     *
     * @return
     */
    public Bitmap[] getImageBitmap();

    /**
     * 获取设置的对应的图像资源ID,可能返回null
     *
     * @return
     */
    public int[] getImageID();

    /**
     * 设置座位基本类型常量,此方法与座位的类型并没有直接关系,设置的常量仅是方便用于处理数据而已
     *
     * @param seleted   已选座位
     * @param unSeleted 未选座位
     * @param unShow    不显示座位(不可见,即未绘制出来)
     */
    public void setSeatTypeConstant(int seleted, int unSeleted, int unShow);

    /**
     * 重置所有的座位基本类型为原始状态
     */
    public void resetSeatTypeConstant();

    /**
     * 重置默认的座位参数,包括<font color="yellow"><b>座位类型,座位颜色,座位描述</b></font>
     */
    public void resetDefaultSeatParams();

    /**
     * 重置所有的座位类型与参数,回到默认状态(三个座位类型及颜色参数),座位的绘制类型也重置为默认绘制方式
     * <p>
     * <li>可选座位</li>
     * <li>已选座位</li>
     * <li>已售座位</li>
     * </p>
     */
    public void resetSeatTypeWithColor();

    /**
     * 设置是否绘制座位类型,<font color="yellow"><b>此处与是否绘制座位是两个不同的方法,代表的意义不同</b></font>,此方法是对座位类型是否绘制的判断处理
     *
     * @param isDrawSeatType
     */
    public void setIsDrawSeatType(boolean isDrawSeatType);

    /**
     * 获取座位类型是否进行绘制
     *
     * @return
     */
    public boolean isDrawSeatType();

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
     * 获取座位类型与其描述文字之间的水平间隔
     *
     * @return
     */
    public float getSeatTypeDescInterval();

    /**
     * 设置座位与邻近文字之间的间隔,<font color="yellow"><b>此参数仅在绘制座位类型及其描述(座位带文字)的时候有效,在绘制普通座位(仅座位不存在文字)时无效</b></font>
     *
     * @param mSeatTextInterval
     */
    public void setSeatTextInterval(float mSeatTextInterval);

    /**
     * 获取座位类型(包括描述文字为一整体)之间的水平间隔
     *
     * @return
     */
    public float getSeatTypeInterval();

    /**
     * 设置座位类型之间的间隔,<font color="yellow"><b>此参数仅在绘制座位类型及其描述的时候有效,在绘制普通座位时无效</b></font>.
     * <p>
     * <li>设置普通座位之间的间隔请用{@link #setSeatHorizontalInterval(float)}</li>
     * <li>设置普通座位上下之间的间隔请用{@link #setSeatVerticalInterval(float)}</li>
     * </p>
     *
     * @param mSeatTypeInterval
     */
    public void setSeatTypeInterval(float mSeatTypeInterval);

    /**
     * 获取座位参数类型对应的数据表示,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public int[] getSeatTypeArrary();

    /**
     * 获取座位类型对应的颜色数据,,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public int[] getSeatColorArrary();

    /**
     * 获取座位类型对应的描述文字,,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public String[] getSeatTypeDescription();

    /**
     * 返回座位类型的长度
     *
     * @return
     */
    public int getSeatTypeLength();

    /**
     * 设置自定义默认的座位类型
     * <p>设置全新的默认座位类型后，建议设置{@link #setSeatTypeConstant(int, int, int)},方便数据处理及以防出错</p>
     *
     * @param firstSeatType  第一个座位类型
     * @param secondSeatType 第二个座位类型
     * @param thirdSeatType  第三个座位类型
     */
    public void setDefaultSeatType(int firstSeatType, int secondSeatType, int thirdSeatType);

    /**
     * 设置默认座位类型对应的颜色
     *
     * @param firstColor  可选座位
     * @param secondColor 已选座位
     * @param thirdColor  已售座位
     */
    public void setDefaultSeatColor(int firstColor, int secondColor, int thirdColor);

    /**
     * 设置默认座位类型的描述
     *
     * @param firstDesc  可选座位描述
     * @param secondDesc 已选座位描述
     * @param thirdDesc  已售座位描述
     */
    public void setDefaultSeatTypeDescription(String firstDesc, String secondDesc, String thirdDesc);

    /**
     * 设置默认的不绘制座位的类型,默认值为0{@link ISeatParamsExport#DRAW_TYPE_NO},<font color="yellow"><b>如果不是必要的情况下,不建议修改该值,使用默认值即可</b></font>
     *
     * @param seatDrawNo
     */
    public void setDefaultSeatDrawNoType(int seatDrawNo);

    /**
     * 设置/添加额外的座位类型、颜色及其类型对应的描述
     * <p><font color="yellow"><b>该方法保留了默认的座位类型及颜色参数,只是在其基础上添加了其它的类型与参数</b></font></p>
     *
     * @param seatExtraTypeArr  新增的座位类型，不可为null
     * @param colorExtraArr     新增的座位类型对应的颜色，不可为null
     * @param seatTypeExtraDesc 新增的座位类型对应的描述，可为null
     */
    public void setExtraSeatTypeWithColor(int[] seatExtraTypeArr, int[] colorExtraArr, String[] seatTypeExtraDesc);

    /**
     * 设置座位类型及其图片
     *
     * @param seatTypeArr
     * @param imageID
     */
    public void setSeatTypeWithImage(int[] seatTypeArr, int[] imageID);

    /**
     * 设置座位类型及其图片
     *
     * @param seatTypeArr
     * @param imageBitmap
     */
    public void setSeatTypeWithImage(int[] seatTypeArr, Bitmap[] imageBitmap);

    /**
     * 设置所有座位的类型，颜色及其描述,<font color="yellow"><b>该方法会替换所有的座位对应的默认参数</b></font>
     * <p>设置全新的座位类型后，建议设置{@link #setSeatTypeConstant(int, int, int)},方便数据处理及以防出错</p>
     *
     * @param seatTypeArr  新的座位类型
     * @param colorArr     新的座位类型对应的颜色
     * @param seatTypeDesc 新的座位类型对应的描述
     */
    public void setAllSeatTypeWithColor(int[] seatTypeArr, int[] colorArr, String[] seatTypeDesc);

    /**
     * 设置图片资源ID,<font color="yellow"><b>该图片资源ID数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)} )
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),当重新加载数据或者不存在图片资源时以资源ID数据为准</b></font></p>
     *
     * @param imageID
     */
    public void setImage(int[] imageID);

    /**
     * 设置图片资源,<font color="yellow"><b>该图片资源数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)})
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),若需要使用当前的图像数据同时防止被其它数据影响,请将imageID设置为null(详见,{@link #setImage(int[])})</b></font></p>
     *
     * @param imageBitmap
     */
    public void setImage(Bitmap[] imageBitmap);
}