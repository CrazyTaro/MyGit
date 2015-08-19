package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>座位参数，包括座位绘制需要的各种参数</p>
 */
public class SeatParams {
    /**
     * 默认座位文字颜色
     */
    public static final int DEFAULT_SEAT_TEXT_COLOR = Color.BLACK;
    /**
     * 默认座位文字大小
     */
    public static final float DEFAULT_SEAT_TEXT_SIZE = 35 * 0.8f;
    /**
     * 默认座位颜色值
     */
    public static final int DEFAULT_SEAT_COLOR = Color.GRAY;
    /**
     * 默认座位宽度
     */
    public static final float DEFAULT_SEAT_WIDTH = 35f;
    /**
     * 默认座位高度
     */
    public static final float DEFAULT_SEAT_HEIGHT = 35f;
    /**
     * 默认主座位高度
     */
    public static final float DEFAULT_SEAT_MAIN_HEIGHT = 25f;
    /**
     * 默认次座位高度
     */
    public static final float DEFAULT_SEAT_MINOR_HEIGHT = 8f;
    /**
     * 默认主次座位间隔高度
     */
    public static final float DEFAULT_SEAT_HEIGHT_INTERVAL = 3f;
    /**
     * 默认座位列表中座位间的水平间隔宽度
     */
    public static final float DEFAULT_SEAT_HORIZONTAL_INTERVAL = 10f;
    /**
     * 默认座位列表中座位间的垂直间隔宽度
     */
    public static final float DEFAULT_SEAT_VERTICAL_INTERVAL = 40f;
    /**
     * 默认座位类型与其描述文字的间隔宽度
     */
    public static final float DEFAULT_SEAT_TEXT_INTERVAL = 10f;
    /**
     * 默认每个座位类型之前的间隔(此处的座位指包含了其描述文字的全部整体)
     */
    public static final float DEFAULT_SEAT_TYPE_INTERVAL = 50f;
    /**
     * 默认座位圆角度
     */
    public static final float DEFAULT_SEAT_RADIUS = 8f;
    /**
     * 默认整数值:-1
     */
    public static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    public static final float DEFAULT_FLOAT = -1;
    /**
     * 默认座位类型,分别为<font color="yellow"><b>可选,已选,已售</b></font>
     */
    public static int[] DEFAULT_SEAT_TYPE = {1, 2, 3};
    /**
     * 默认座位类型对应的颜色,分别为
     * <p>
     * <li>可选=<font color="black"><b>黑色</b></font></li>
     * <li>已选=<font color="red"><b>红色</b></font></li>
     * <li>已售=<font color="yellow"><b>黄色</b></font></li>
     * </p>
     */
    public static int[] DEFAULT_SEAT_TYPE_COLOR = {Color.BLACK, Color.RED, Color.YELLOW};
    /**
     * 默认座位类型描述,"可选,已选,已售"
     */
    public static String[] DEFAULT_SEAT_TYPE_DESC = {"可选", "已选", "已售"};
    /**
     * 座位默认基本类型,已选,<font color="yellow"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已</b></font>
     */
    public static int SEAT_TYPE_SELETED = 2;
    /**
     * 座位默认基本类型,未选,<font color="yellow"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已</b></font>
     */
    public static int SEAT_TYPE_UNSELETED = 1;
    /**
     * 座位默认基本类型,不可见,<font color="yellow"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已</b></font>,其值与{@link #SEAT_DRAW_TYPE_NO}保持一致
     */
    public static int SEAT_TYPE_UNSHOW = 0;
    /**
     * 座位默认基本类型,错误座位,即不存在的座位(可能是列表数据不存在,也可能是查询索引超过此列表数据等)
     */
    public static final int SEAT_TYPE_ERRO = -1;
    /**
     * 座位的绘制类型,不绘制
     */
    public static int SEAT_DRAW_TYPE_NO = 0;
    /**
     * 座位的绘制类型,默认绘制
     */
    public static final int SEAT_DRAW_TYPE_DEFAULT = 1;
    /**
     * 座位的绘制类型,座位绘制为图片
     */
    public static final int SEAT_DRAW_TYPE_IMAGE = 2;
    public static final int SEAT_DRAW_TYPE_THUMBNAIL = 3;


    private float mSeatWidth = DEFAULT_SEAT_WIDTH;
    //座位绘制区域高度
    private float mSeatHeight = DEFAULT_SEAT_HEIGHT;
    //主座位高度, 与次座位一起绘制显示为一个座位,显得好看一点,此参数不对外公开
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    //次座位高度
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    //主次座位之间的间隔
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
    private float mSeatRadius = DEFAULT_SEAT_RADIUS;

    private float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    private float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    private float mSeatTextInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    private float mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;

    private boolean mIsDrawSeat = true;
    private boolean mIsDrawSeatType = true;
    private int mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
    private int mSeatColor = DEFAULT_SEAT_COLOR;
    private int mLargeScaleRate = 24;
    private float mSmallScaleRate = 0.5f;

    private int[] mSeatTypeArrary = null;
    private int[] mSeatColorArrary = null;
    private String[] mSeatTypeDescription = null;
    private int[] mSeatImageID = null;
    private Bitmap[] mSeatImageBitmaps = null;

    private float mSeatTypeDescSize = DEFAULT_SEAT_TEXT_SIZE;
    private int mSeatTypeDescColor = DEFAULT_SEAT_TEXT_COLOR;

    private boolean mIsDrawThumbnail = false;
    private float mThumbnailRate = 0.1f;
    private float[] mValueHolder = null;
    private boolean mIsValueHold = false;
    private static SeatParams mInstance = null;

    private SeatParams() {
        resetSeatTypeWithColor();
        mSeatTypeDescription = DEFAULT_SEAT_TYPE_DESC;
    }

    /**
     * 获取座位绘制参数的单一实例,此参数包括管理的数据有座位类型及普通座位的绘制相关参数,
     * <font color="yellow"><b>所有与座位类型有关的方法都是包括了<font color ="white"><i>seatType</i></font>,
     * 所有与普通座位绘制相关的方法都只包括<font color ="white"><i>seat</i></font></b></font>
     *
     * @return
     */
    public static synchronized SeatParams getInstance() {
        if (mInstance == null) {
            mInstance = new SeatParams();
        }
        return mInstance;
    }

    private void showMsg(String msg) {
        Log.i("seatParams", msg);
    }

    public boolean getIsDrawThumbnail() {
        return this.mIsDrawThumbnail;
    }

    /**
     * 设置是否使用绘制缩略图的参数,缩略图的缩放比例只由宽度决定,高度是可变的
     *
     * @param isDrawThumbnail 是否绘制缩略图,<font color="yellow"><b>此参数为true,则所有的座位相关的绘制数据返回时将计算为缩略图的大小返回</b></font>
     * @param originalWidth   实际绘制界面的宽度
     * @param targetWidth     目标缩略图的宽度
     */
    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

    /**
     * 设置缩放最大值比,缩放最大倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="yellow"><b>使用默认参数{@link #DEFAULT_INT}可设置为原始默认值</b></font>,一般该参数大于1
     * <p>该缩放倍数是以默认高度为基数{@link #DEFAULT_SEAT_HEIGHT}</p>
     *
     * @param large 放大倍数
     * @return 设置成功返回true, 否则返回false, 不改变原值
     */
    public boolean setLargeScaleRate(int large) {
        if (large == DEFAULT_INT) {
            this.mLargeScaleRate = 24;
            return true;
        } else if (large > 0 && large * this.mSeatHeight <= 880) {
            this.mLargeScaleRate = large;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置缩放最小值比,缩放最小倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="yellow"><b>使用默认参数{@link #DEFAULT_FLOAT}可设置为原始默认值</b></font>,一般该参数在0-1之间
     * <p>该缩放倍数是以默认高度为基数{@link #DEFAULT_SEAT_HEIGHT}</p>
     *
     * @param small 缩小比例
     * @return 设置成功返回true, 否则返回false
     */
    public boolean setSmallScaleRate(float small) {
        if (small == DEFAULT_INT) {
            this.mSmallScaleRate = 0.5f;
            return true;
        } else if (small > 0 && small * this.mSeatHeight <= 880) {
            this.mSmallScaleRate = small;
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置座位基本类型常量,此方法与座位的类型并没有直接关系,设置的常量仅是方便用于处理数据而已
     *
     * @param seleted   已选座位
     * @param unSeleted 未选座位
     * @param unShow    不显示座位(不可见,即未绘制出来)
     */
    public static void setSeatTypeConstant(int seleted, int unSeleted, int unShow) {
        SEAT_TYPE_SELETED = seleted;
        SEAT_TYPE_UNSELETED = unSeleted;
        SEAT_TYPE_UNSHOW = unShow;
    }

    /**
     * 重置所有的座位基本类型为原始状态
     */
    public static void resetSeatTypeConstant() {
        SEAT_TYPE_SELETED = 2;
        SEAT_TYPE_UNSELETED = 1;
        SEAT_TYPE_UNSHOW = SEAT_DRAW_TYPE_NO;
    }

    /**
     * 设置是否绘制座位类型,<font color="yellow"><b>此处与是否绘制座位{@link #isDrawSeat()}是两个不同的方法,代表的意义不同</b></font>,
     * 此方法是对座位类型是否绘制的判断处理,而{@link #isDrawSeat()}是对普通座位(出售座位)是否绘制的判断处理
     *
     * @param isDrawSeatType
     */
    public void setIsDrawSeatType(boolean isDrawSeatType) {
        this.mIsDrawSeatType = isDrawSeatType;
    }

    /**
     * 获取座位类型是否进行绘制
     *
     * @return
     */
    public boolean isDrawSeatType() {
        return mIsDrawSeatType;
    }

    /**
     * 是否可以进行缩放,用于检测当前比例是否允许进行缩放,<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比例
     * @return 可以缩放返回true, 否则返回false
     */
    public boolean isCanScale(float scaleRate) {
        float newHeight = this.mSeatHeight * scaleRate;
        //由于座位的宽度是决定座位对应的文字
        //文字大小不允许超过800
        //超过800的都取消缩放
        //设置最大最小缩放值

        //此处使用默认值的24倍,840
        if (newHeight > DEFAULT_SEAT_HEIGHT * mLargeScaleRate || newHeight < DEFAULT_SEAT_HEIGHT * mSmallScaleRate) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate      新的缩放比
     * @param isTrueSetValue 是否将此次缩放结果记录为永久结果
     */
    public void setScaleRate(float scaleRate, boolean isTrueSetValue) {
        //创建缓存数据对象
        if (mValueHolder == null) {
            mValueHolder = new float[7];
        }
        if (!mIsValueHold) {
            showMsg("开始记录第一次数据");
            showMsg("new rate = " + scaleRate);
            //第一次更新数据记录下最原始的数据
            mValueHolder[0] = this.mSeatWidth;
            mValueHolder[1] = this.mSeatHeight;
            mValueHolder[2] = this.mSeatHeightInterval;
            mValueHolder[3] = this.mSeatVerticalInterval;
            mValueHolder[4] = this.mSeatTextInterval;
            mValueHolder[5] = this.mSeatTypeInterval;
            mValueHolder[6] = this.mSeatTypeDescSize;
            mIsValueHold = true;
        }
        //每一次变化都处理为相对原始数据的变化
        this.mSeatWidth = mValueHolder[0] * scaleRate;
        this.mSeatHeight = mValueHolder[1] * scaleRate;
        this.mSeatHeightInterval = mValueHolder[2] * scaleRate;
        this.mSeatVerticalInterval = mValueHolder[3] * scaleRate;
        this.mSeatTextInterval = mValueHolder[4] * scaleRate;
        this.mSeatTypeInterval = mValueHolder[5] * scaleRate;
        this.mSeatTypeDescSize = mValueHolder[6] * scaleRate;
        //自动计算主次座位高度
        this.autoCalculateSeatShapeHeight(this.mSeatHeight);

        //若确认更新数据,则将变化后的数据作为永久性数据进行缓存
        if (isTrueSetValue) {
            mValueHolder[0] = this.mSeatWidth;
            mValueHolder[1] = this.mSeatHeight;
            mValueHolder[2] = this.mSeatHeightInterval;
            mValueHolder[3] = this.mSeatVerticalInterval;
            mValueHolder[4] = this.mSeatTextInterval;
            mValueHolder[5] = this.mSeatTypeInterval;
            mValueHolder[6] = this.mSeatTypeDescSize;
            //重置记录标志
            mIsValueHold = false;
        }
    }

    public void resetSeatParams() {
        mInstance = new SeatParams();
    }

    /**
     * 获取当前座位是否绘制,<font color="yellow"><b>此处的座位是指普通座位,与{@link #isDrawSeatType()}中的座位类型是不同概念的</b></font>
     * <p>该返回的参数值来自于方法{@link #setSeatDrawType(int)}方法自动计算的结果,是否进行绘制是由方法根据普通座位的类型进行判断处理的</p>
     *
     * @return
     */
    public boolean isDrawSeat() {
        return mIsDrawSeat;
    }

    public float getSeatWidth() {
        if (mIsDrawThumbnail) {
            return mSeatWidth * mThumbnailRate;
        } else {
            return mSeatWidth;
        }
    }

    public float getSeatHeight() {
        if (mIsDrawThumbnail) {
            return mSeatWidth * mThumbnailRate;
        } else {
            return mSeatHeight;
        }
    }

    public float getSeatRadius() {
        if (mIsDrawThumbnail) {
            return mSeatRadius * mThumbnailRate;
        } else {
            return mSeatRadius;
        }
    }

    /**
     * 获取普通座位绘制之间的水平间隔
     *
     * @return
     */
    public float getSeatHorizontalInterval() {
        if (mIsDrawThumbnail) {
            return mSeatHorizontalInterval * mThumbnailRate;
        } else {
            return mSeatHorizontalInterval;
        }
    }

    /**
     * 获取普通座位绘制之间的垂直间隔
     *
     * @return
     */
    public float getSeatVerticalInterval() {
        if (mIsDrawThumbnail) {
            return mSeatVerticalInterval * mThumbnailRate;
        } else {
            return mSeatVerticalInterval;
        }
    }


    /**
     * 获取座位类型与其描述文字之间的水平间隔
     *
     * @return
     */
    public float getSeatTypeDescInterval() {
        if (mIsDrawThumbnail) {
            return mSeatTextInterval * mThumbnailRate;
        } else {
            return mSeatTextInterval;
        }
    }


    /**
     * 获取座位类型(包括描述文字为一整体)之间的水平间隔
     *
     * @return
     */
    public float getSeatTypeInterval() {
        if (mIsDrawThumbnail) {
            return mSeatTypeInterval * mThumbnailRate;
        } else {
            return mSeatTypeInterval;
        }
    }

    /**
     * 获取当前绘制的座位的颜色,<font color="yellow"><b>注意此处是当前绘制的座位的颜色,该颜色值只用于当前绘制的座位,此座位包括了普通座位及绘制座位类型时的示例座位</b></font>
     * <p>若使用默认的座位绘制方式,则应该保证在每次座位绘制之前设置该值,否则可能会使后面大量的座位使用同一个颜色值</p>
     *
     * @return
     */
    public int getSeatColor() {
        return mSeatColor;
    }

    /**
     * 获取座位参数类型对应的数据表示,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public int[] getSeatTypeArrary() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatTypeArrary != null) {
            int[] newArr = new int[mSeatTypeArrary.length];
            System.arraycopy(mSeatTypeArrary, 0, newArr, 0, mSeatTypeArrary.length);
            return newArr;
        } else {
            return null;
        }
    }

    /**
     * 获取座位类型对应的颜色数据,,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public int[] getSeatColorArrary() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatColorArrary != null) {
            int[] newArr = new int[mSeatColorArrary.length];
            System.arraycopy(mSeatColorArrary, 0, newArr, 0, mSeatColorArrary.length);
            return newArr;
        } else {
            return null;
        }
    }

    /**
     * 获取座位类型对应的描述文字,,<font color="yellow"><b>结果为拷贝对象,不是原始引用</b></font>
     *
     * @return
     */
    public String[] getSeatTypeDescription() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatTypeDescription != null) {
            String[] newArr = new String[mSeatTypeDescription.length];
            System.arraycopy(mSeatTypeDescription, 0, newArr, 0, mSeatTypeDescription.length);
            return newArr;
        } else {
            return null;
        }
    }

    /**
     * 获取座位类型描述文字的颜色
     *
     * @return
     */
    public int getSeatTypeDescColor() {
        return mSeatTypeDescColor;
    }

    /**
     * 设置座位类型描述文字的字体大小
     *
     * @return
     */
    public float getSeatTypeTextSize() {
        if (mIsDrawThumbnail) {
            return mSeatTypeDescSize * mThumbnailRate;
        } else {
            return mSeatTypeDescSize;
        }
    }

    /**
     * 返回座位类型的长度
     *
     * @return
     */
    public int getSeatTypeLength() {
        if (mSeatTypeArrary != null) {
            return mSeatTypeArrary.length;
        } else {
            return 0;
        }
    }

    /**
     * 获取当前座位类型的绘制的方式,<font color="yellow"><b>若为true,返回的绘制类型为实际的绘制类型(绝对不返回缩略图绘制方式),否则根据当前是否需要进行缩略图绘制返回对应的绘制方式(可能返回缩略图绘制方式)</b></font>
     * <p>
     * <li>{@link #SEAT_DRAW_TYPE_DEFAULT}默认绘制方式,座位分主次座位部分绘制,纯图形绘制</li>
     * <li>{@link #SEAT_DRAW_TYPE_IMAGE}图片绘制方式,使用座位类型对应的图片填充座位区域</li>
     * <li>{@link #SEAT_DRAW_TYPE_THUMBNAIL}缩略图绘制方式,此方式下数据返回缩略图绘制需要的参数</li>
     * </p>
     *
     * @param isGetOriginalDrawType 是否获取实际的绘制类型,<font color="yellow"><b>若为true,返回的绘制类型为实际的绘制类型(绝对不返回{@link #SEAT_DRAW_TYPE_THUMBNAIL}),否则根据当前是否需要进行缩略图绘制返回对应的绘制方式(可能返回{@link #SEAT_DRAW_TYPE_THUMBNAIL})</b></font>
     * @return
     */
    public int getSeatDrawType(boolean isGetOriginalDrawType) {
        if (mIsDrawThumbnail && !isGetOriginalDrawType) {
            return SEAT_DRAW_TYPE_THUMBNAIL;
        } else {
            return mSeatDrawType;
        }
    }

    /**
     * 获取设置的座位对应的图像资源ID,可能返回null
     *
     * @return
     */
    public int[] getSeatImageIDByCopy() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatImageID != null) {
            int[] newArr = new int[mSeatImageID.length];
            System.arraycopy(mSeatImageID, 0, newArr, 0, mSeatImageID.length);
            return newArr;
        } else {
            return null;
        }
    }

    /**
     * 获取设置的座位对应的图像资源bitmap,可能返回null
     *
     * @return
     */
    public Bitmap[] getSeatImageBitmapByCopy() {
        //引用类型的参数返回值为复制的新对象返回,而不是原引用返回
        if (mSeatImageBitmaps != null) {
            Bitmap[] newArr = new Bitmap[mSeatImageBitmaps.length];
            System.arraycopy(mSeatImageBitmaps, 0, newArr, 0, mSeatImageBitmaps.length);
            return newArr;
        } else {
            return null;
        }
    }

    /**
     * 设置绘制类型,<font color="yellow"><b>设置绘制方式为图像类型时,必须存在图片资源或者图像资源,否则抛出异常</b></font>,设置此方法前应该先设置图片资源ID{@link #setSeatImage(int[])}或图片对象{@link #setSeatImage(Bitmap[])}
     * <p>
     * <li>{@link #SEAT_DRAW_TYPE_DEFAULT},默认绘制方式,使用图形及颜色</li>
     * <li>{@link #SEAT_DRAW_TYPE_IMAGE},图片绘制方式,使用图片进行填充</li>
     * </p>
     *
     * @param drawType
     */
    public void setSeatDrawType(int drawType) {
        if (drawType == DEFAULT_INT) {
            this.mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
        } else if (drawType == SEAT_DRAW_TYPE_IMAGE && (mSeatImageID == null || mSeatImageBitmaps == null)) {
            throw new RuntimeException("设置绘制方式为图像类型时,必须存在图片资源或者图像资源!");
        } else {
            this.mSeatDrawType = drawType;
        }
    }

    /**
     * 设置图片资源ID,<font color="yellow"><b>该图片资源ID数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setSeatDrawType(int)})
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),当重新加载数据或者不存在图片资源时以资源ID数据为准</b></font></p>
     *
     * @param imageID
     */
    public void setSeatImage(int[] imageID) {
        if (imageID != null && imageID.length != mSeatTypeArrary.length) {
            throw new RuntimeException("设置座位图片length与座位类型length不符合");
        }
        mSeatDrawType = SEAT_DRAW_TYPE_IMAGE;
        //通过拷贝保存引用对象数据,而不是直接保存引用
        if (imageID != null) {
            mSeatImageID = new int[imageID.length];
            System.arraycopy(imageID, 0, mSeatImageID, 0, imageID.length);
        } else {
            mSeatImageID = null;
        }
    }

    /**
     * 设置图片资源,<font color="yellow"><b>该图片资源数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setSeatDrawType(int)})
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),若需要使用当前的图像数据同时防止被其它数据影响,请将imageID设置为null(详见,{@link #setSeatImage(int[])})</b></font></p>
     *
     * @param imageBitmap
     */
    public void setSeatImage(Bitmap[] imageBitmap) {
        if (imageBitmap != null && imageBitmap.length != mSeatTypeArrary.length) {
            throw new RuntimeException("设置座位图片length与座位类型length不符合");
        }
        mSeatDrawType = SEAT_DRAW_TYPE_IMAGE;
        //通过拷贝保存引用对象数据,而不是直接保存引用
        if (imageBitmap != null) {
            mSeatImageBitmaps = new Bitmap[imageBitmap.length];
            System.arraycopy(imageBitmap, 0, mSeatImageBitmaps, 0, imageBitmap.length);
        } else {
            mSeatImageBitmaps = null;
        }
    }

    /**
     * 设置座位类型描述文字的颜色值
     *
     * @param mSeatTextColor
     */
    public void setSeatTypeDescColor(int mSeatTextColor) {
        this.mSeatTypeDescColor = mSeatTextColor;
    }

    /**
     * 设置座位类型描述文字字体大小
     *
     * @param mSeatTextSize
     */
    public void setSeatTypeDescSize(float mSeatTextSize) {
        if (mSeatTextSize == DEFAULT_FLOAT) {
            this.mSeatTypeDescSize = DEFAULT_SEAT_TEXT_SIZE;
        } else {
            this.mSeatTypeDescSize = mSeatTextSize;
        }
    }

    public void setSeatWidth(float mSeatWidth) {
        if (mSeatWidth == DEFAULT_FLOAT) {
            this.mSeatWidth = DEFAULT_SEAT_WIDTH;
        } else {
            this.mSeatWidth = mSeatWidth;
        }
    }

    /**
     * 设置座位的高度,此方法的高度直接用于绘制图片座位的高度;且该方法会自动计算默认绘制方式的主次座位部分的高度
     *
     * @param mSeatHeight
     */
    public void setSeatHeight(float mSeatHeight) {
        if (mSeatHeight == DEFAULT_FLOAT) {
            this.mSeatHeight = DEFAULT_SEAT_HEIGHT;
        } else {
            this.mSeatHeight = mSeatHeight;
        }
        this.autoCalculateSeatShapeHeight(this.mSeatHeight);
    }

    /**
     * 设置默认座位主次部分的圆角度,使用图片绘制方式时可以忽略此参数
     *
     * @param mSeatRadius
     */
    public void setSeatRadius(float mSeatRadius) {
        if (mSeatRadius == DEFAULT_FLOAT) {
            this.mSeatRadius = DEFAULT_SEAT_RADIUS;
        } else {
            this.mSeatRadius = mSeatRadius;
        }
    }

    /**
     * 设置每个座位与邻近座位之间的水平间隔距离
     *
     * @param mSeatHorizontalInterval
     */
    public void setSeatHorizontalInterval(float mSeatHorizontalInterval) {
        if (mSeatHorizontalInterval == DEFAULT_FLOAT) {
            this.mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
        } else {
            this.mSeatHorizontalInterval = mSeatHorizontalInterval;
        }
    }

    /**
     * 设置每个座位与邻近座位之间的垂直间隔距离
     *
     * @param mSeatVerticalInterval
     */
    public void setSeatVerticalInterval(float mSeatVerticalInterval) {
        if (mSeatVerticalInterval == DEFAULT_FLOAT) {
            this.mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
        } else {
            this.mSeatVerticalInterval = mSeatVerticalInterval;
        }
    }

    /**
     * 设置座位与邻近文字之间的间隔,<font color="yellow"><b>此参数仅在绘制座位类型及其描述(座位带文字)的时候有效,在绘制普通座位(仅座位不存在文字)时无效</b></font>
     *
     * @param mSeatTextInterval
     */
    public void setSeatTextInterval(float mSeatTextInterval) {
        if (mSeatTextInterval == DEFAULT_FLOAT) {
            this.mSeatTextInterval = DEFAULT_SEAT_TEXT_INTERVAL;
        } else {
            this.mSeatTextInterval = mSeatTextInterval;
        }
    }

    /**
     * 设置座位类型之间的间隔,<font color="yellow"><b>此参数仅在绘制座位类型及其描述的时候有效,在绘制普通座位时无效</b></font>,设置普通座位之间的间隔请用{@link #setSeatHorizontalInterval(float)}
     *
     * @param mSeatTypeInterval
     */
    public void setSeatTypeInterval(float mSeatTypeInterval) {
        if (mSeatTypeInterval == DEFAULT_FLOAT) {
            this.mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;
        } else {
            this.mSeatTypeInterval = mSeatTypeInterval;
        }
    }

    /**
     * 设置是否绘制当前的座位
     *
     * @param mIsDrawSeat
     */
    public void setIsDrawSeat(boolean mIsDrawSeat) {
        this.mIsDrawSeat = mIsDrawSeat;
    }

    /**
     * 设置默认的不绘制座位的类型,默认值为0{@link #SEAT_DRAW_TYPE_NO},<font color="yellow"><b>如果不是必要的情况下,不建议修改该值,使用默认值即可</b></font>
     *
     * @param seatDrawNo
     */
    public static void setDefaultSeatDrawNoType(int seatDrawNo) {
        SEAT_DRAW_TYPE_NO = seatDrawNo;
    }

    /**
     * 根据座位类型来确定座位是否需要绘制，当座位类型为{@link #SEAT_DRAW_TYPE_NO}时，不绘制该座位
     * <p><font color="yellow"><b>{@link #SEAT_DRAW_TYPE_NO}该静态变量可改变设置,默认0为不绘制类型,可自定义设置</b></font></p>
     *
     * @param seatType 座位类型
     */
    public void setIsDrawSeat(int seatType) {
        if (seatType == SEAT_DRAW_TYPE_NO) {
            this.mIsDrawSeat = false;
        } else {
            this.mIsDrawSeat = true;
        }
    }

    /**
     * 设置绘制时使用的座位颜色，<font color="yellow"><b>该颜色并没有特别的意义，但绘制时使用的颜色必定是此颜色</b></font>
     * <p><font color="yellow"><b>在任何一次绘制座位之前都必须考虑是否需要调用此方法，否则绘制使用的颜色将是上一次绘制使用的颜色</b></font></p>
     *
     * @param mSeatColor
     */
    public void setSeatColor(int mSeatColor) {
        this.mSeatColor = mSeatColor;
    }

    /**
     * 设置自定义默认的座位类型
     *
     * @param firstSeatType  第一个座位类型
     * @param secondSeatType 第二个座位类型
     * @param thirdSeatType  第三个座位类型
     */
    public static void setDefaultSeatType(int firstSeatType, int secondSeatType, int thirdSeatType) {
        if (DEFAULT_SEAT_TYPE != null && DEFAULT_SEAT_TYPE.length == 3) {
            DEFAULT_SEAT_TYPE[0] = firstSeatType;
            DEFAULT_SEAT_TYPE[1] = secondSeatType;
            DEFAULT_SEAT_TYPE[2] = thirdSeatType;
        } else {
            throw new RuntimeException("默认座位类型不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    /**
     * 重置默认的座位参数,包括<font color="yellow"><b>座位类型,座位颜色,座位描述</b></font>
     */
    public static void resetDefaultSeatParams() {
        DEFAULT_SEAT_TYPE = new int[]{1, 2, 3};
        DEFAULT_SEAT_TYPE_COLOR = new int[]{Color.BLACK, Color.RED, Color.YELLOW};
        DEFAULT_SEAT_TYPE_DESC = new String[]{"可选", "已选", "已售"};
    }

    /**
     * 设置默认座位类型对应的颜色
     *
     * @param firstColor  可选座位
     * @param secondColor 已选座位
     * @param thirdColor  已售座位
     */
    public static void setDefaultSeatColor(int firstColor, int secondColor, int thirdColor) {
        //确保座位类型回到默认状态
        if (DEFAULT_SEAT_TYPE_COLOR != null && DEFAULT_SEAT_TYPE_COLOR.length == 3) {
            DEFAULT_SEAT_TYPE_COLOR[0] = firstColor;
            DEFAULT_SEAT_TYPE_COLOR[1] = secondColor;
            DEFAULT_SEAT_TYPE_COLOR[2] = thirdColor;
        } else {
            throw new RuntimeException("默认座位类型颜色不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    /**
     * 设置默认座位类型的描述
     *
     * @param firstDesc  可选座位描述
     * @param secondDesc 已选座位描述
     * @param thirdDesc  已售座位描述
     */
    public static void setDefaultSeatTypeDescription(String firstDesc, String secondDesc, String thirdDesc) {
        //确保座位类型回来默认状态
        if (DEFAULT_SEAT_TYPE_DESC != null && DEFAULT_SEAT_TYPE_DESC.length == 3) {
            DEFAULT_SEAT_TYPE_DESC[0] = firstDesc;
            DEFAULT_SEAT_TYPE_DESC[1] = secondDesc;
            DEFAULT_SEAT_TYPE_DESC[2] = thirdDesc;
        } else {
            throw new RuntimeException("默认座位描述变量不符合设置要求,请调用resetDefaultSeatParams()方法重置数据之后再尝试");
        }
    }

    /**
     * 设置/添加额外的座位类型、颜色及其类型对应的描述
     * <p><font color="yellow"><b>该方法保留了默认的座位类型及颜色参数,只是在其基础上添加了其它的类型与参数</b></font></p>
     *
     * @param seatExtraTypeArr  新增的座位类型，不可为null
     * @param colorExtraArr     新增的座位类型对应的颜色，不可为null
     * @param seatTypeExtraDesc 新增的座位类型对应的描述，可为null
     */
    public void setExtraSeatTypeWithColor(int[] seatExtraTypeArr, int[] colorExtraArr, String[] seatTypeExtraDesc) {
        if (seatExtraTypeArr != null && colorExtraArr != null && seatExtraTypeArr.length == colorExtraArr.length) {
            //创建新数组
            int[] newSeatTypeArr = new int[3 + seatExtraTypeArr.length];
            int[] newSeatColorArr = new int[3 + seatExtraTypeArr.length];
            String[] newSeatTypeDescription = new String[3 + seatExtraTypeArr.length];
            //载入默认类型及颜色参数
            System.arraycopy(DEFAULT_SEAT_TYPE, 0, newSeatTypeArr, 0, DEFAULT_SEAT_TYPE.length);
            System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, newSeatColorArr, 0, DEFAULT_SEAT_TYPE_COLOR.length);
            System.arraycopy(DEFAULT_SEAT_TYPE_DESC, 0, newSeatTypeDescription, 0, DEFAULT_SEAT_TYPE_DESC.length);

            //添加新增额外的类型与颜色参数
            for (int i = 3; i < newSeatTypeArr.length; i++) {
                newSeatTypeArr[i] = seatExtraTypeArr[i - 3];
                newSeatColorArr[i] = colorExtraArr[i - 3];
            }
            if (seatTypeExtraDesc != null && seatTypeExtraDesc.length == seatExtraTypeArr.length) {

                for (int i = 3; i < newSeatTypeDescription.length; i++) {
                    newSeatTypeDescription[i] = seatTypeExtraDesc[i - 3];
                }
            } else {
                throw new RuntimeException("座位类型描述不可为null,设置额外的座位类型描述length应与额外的座位类型length一致");
            }


            mSeatTypeArrary = newSeatTypeArr;
            mSeatColorArrary = newSeatColorArr;
            mSeatTypeDescription = newSeatTypeDescription;
        } else {
            throw new RuntimeException("设置额外座位类型及颜色失败,请确认参数不可为null且参数值的length必须相同");
        }
    }

    /**
     * 设置座位类型及其图片
     *
     * @param seatTypeArr
     * @param imageID
     */
    public void setSeatTypeWithImage(int[] seatTypeArr, int[] imageID) {
        if (seatTypeArr != null && imageID != null && seatTypeArr.length == imageID.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);
            mSeatImageID = new int[imageID.length];
            System.arraycopy(imageID, 0, mSeatImageID, 0, imageID.length);

            mSeatDrawType = SEAT_DRAW_TYPE_IMAGE;
        } else {
            throw new RuntimeException("设置新座位类型及图片ID失败,请确认参数不可为null");
        }
    }

    /**
     * 设置座位类型及其图片
     *
     * @param seatTypeArr
     * @param imageBitmap
     */
    public void setSeatTypeWithImage(int[] seatTypeArr, Bitmap[] imageBitmap) {
        if (seatTypeArr != null && imageBitmap != null && seatTypeArr.length == imageBitmap.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);
            mSeatImageBitmaps = new Bitmap[imageBitmap.length];
            System.arraycopy(imageBitmap, 0, mSeatImageBitmaps, 0, imageBitmap.length);

            mSeatDrawType = SEAT_DRAW_TYPE_IMAGE;
        } else {
            throw new RuntimeException("设置新座位类型及图片ID失败,请确认参数不可为null");
        }
    }

    /**
     * 设置所有座位的类型，颜色及其描述,<font color="yellow"><b>该方法会替换所有的座位对应的默认参数</b></font>
     *
     * @param seatTypeArr  新的座位类型
     * @param colorArr     新的座位类型对应的颜色
     * @param seatTypeDesc 新的座位类型对应的描述
     */
    public void setAllSeatTypeWithColor(int[] seatTypeArr, int[] colorArr, String[] seatTypeDesc) {
        if (seatTypeArr != null && colorArr != null && seatTypeArr.length == colorArr.length) {
            mSeatTypeArrary = new int[seatTypeArr.length];
            System.arraycopy(seatTypeArr, 0, mSeatTypeArrary, 0, seatTypeArr.length);
            mSeatColorArrary = new int[colorArr.length];
            System.arraycopy(colorArr, 0, mSeatColorArrary, 0, colorArr.length);

            if ((seatTypeDesc != null && seatTypeDesc.length == seatTypeArr.length)) {
                mSeatTypeDescription = new String[seatTypeDesc.length];
                System.arraycopy(seatTypeDesc, 0, mSeatTypeDescription, 0, seatTypeDesc.length);
            } else {
                throw new RuntimeException("座位类型描述不可为null,设置座位类型描述length应与座位类型length一致");
            }

            mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
        } else {
            throw new RuntimeException("设置新座位类型及颜色失败,请确认参数不可为null且参数值的length必须相同");
        }
    }

    /**
     * 重置所有的座位类型与参数,回到默认状态(三个座位类型及颜色参数),座位的绘制类型也重置为默认绘制方式
     * <p>
     * <li>可选座位</li>
     * <li>已选座位</li>
     * <li>已售座位</li>
     * </p>
     */
    public void resetSeatTypeWithColor() {
        //载入默认类型及颜色参数
        mSeatTypeArrary = new int[DEFAULT_SEAT_TYPE.length];
        mSeatColorArrary = new int[DEFAULT_SEAT_TYPE_COLOR.length];
        System.arraycopy(DEFAULT_SEAT_TYPE, 0, mSeatTypeArrary, 0, DEFAULT_SEAT_TYPE.length);
        System.arraycopy(DEFAULT_SEAT_TYPE_COLOR, 0, mSeatColorArrary, 0, DEFAULT_SEAT_TYPE_COLOR.length);

        mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
    }

    /**
     * 按指定宽高比例加载资源ID指定的图片到内存中
     *
     * @param context      上下文对象,用于获取资源对象
     * @param imageID      资源ID
     * @param targetWidth  目标图片的宽度(此处一般为座位宽度)
     * @param targetHeight 目标图片的高度(此处一般为座位的高度)
     * @return
     */
    public static Bitmap getScaleBitmap(Context context, int imageID, int targetWidth, int targetHeight) {
        try {
            //以流的形式加载比直接使用ID加载到消耗内存会少一些,并且可以指定宽高进行加载
            //加载资源文件到流
            InputStream in = context.getResources().openRawResource(imageID);
            //设置加载选项
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            //设置目标宽高
            options.outWidth = targetWidth;
            options.outHeight = targetHeight;
            //加载图片
            return BitmapFactory.decodeStream(in, null, options);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }


    /**
     * 加载座位图片
     *
     * @param context  上下文对象,用于加载图片
     * @param isReload 是否重新加载,若为true则以imageID为准,重新加载所有的bitmap,若为false则根据bitmap是否存在,若不存在则加载imageID的图片,存在则直接使用bitmap
     */
    public void loadSeatImage(Context context, boolean isReload) {
        if (mSeatImageID == null && isReload) {
            throw new RuntimeException("资源ID不存在,无法重新加载图片资源");
        }
        //检测imageID是否存在
        if (mSeatImageID != null) {
            //不需要重新加载且bitmap不为null
            if (!isReload && mSeatImageBitmaps != null) {
                boolean isNullObjeact = false;
                //检测是否bitmap数组为空数组
                for (Bitmap bitmap : mSeatImageBitmaps) {
                    if (bitmap == null) {
                        isNullObjeact = true;
                        break;
                    }
                }
                if (!isNullObjeact) {
                    //不存在空元素直直接使用该bitmap
                    return;
                }
            }
            if (mSeatImageBitmaps != null) {
                //存在空元素,尝试回收无用的图片,重新加载数据
                for (Bitmap bitmap : mSeatImageBitmaps) {
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
            }
            //存在空元素则重新加载数据
            mSeatImageBitmaps = new Bitmap[mSeatImageID.length];

            for (int i = 0; i < mSeatImageID.length; i++) {
                //按预期宽度比例加载图片
                //用于防止原图片太大加载的内存过大
                Bitmap bitmap = getScaleBitmap(context, mSeatImageID[i], (int) mSeatWidth, (int) mSeatHeight);
                mSeatImageBitmaps[i] = bitmap;
            }
        } else if (mSeatImageBitmaps != null) {
            return;
        } else {
            //即不存在资源ID,也不存在图片文件
            throw new RuntimeException("不存在可加载的图片资源或者已经加载的图片资源!");
        }
    }

    /**
     * 根据座位类型获取座位类型对应的颜色
     *
     * @param seatType 座位类型
     *                 <p>默认座位类型
     *                 <li>可选座位</li>
     *                 <li>已选座位</li>
     *                 <li>已售座位</li>
     *                 </p>
     * @return 返回对应的座位颜色, 若查询不到对应的座位类型颜色则返回默认颜色值 {@link #DEFAULT_SEAT_COLOR}
     */
    public int getSeatColorByType(int seatType) {
        if (mSeatTypeArrary != null) {
            for (int i = 0; i < mSeatTypeArrary.length; i++) {
                if (seatType == mSeatTypeArrary[i]) {
                    return mSeatColorArrary[i];
                }
            }
            return DEFAULT_SEAT_COLOR;
        } else {
            throw new RuntimeException("获取座位类型对应的颜色值失败!查询不到对应的座位类型或不存在该座位类型");
        }
    }

    /**
     * 根据座位类型获取座位绘制的图片
     *
     * @param context  上下文对象,用于加载图片
     * @param seatType 座位类型
     * @return 返回座位类型对应的图片, 可能为null
     */
    public Bitmap getSeatBitmapByType(Context context, int seatType) {
        loadSeatImage(context, false);
        if (mSeatTypeArrary != null) {
            for (int i = 0; i < mSeatTypeArrary.length; i++) {
                if (seatType == mSeatTypeArrary[i]) {
                    return mSeatImageBitmaps[i];
                }
            }
            return null;
        } else {
            throw new RuntimeException("获取座位类型对应的图片失败!查询不到对应的座位类型或不存在该座位类型");
        }
    }

    /**
     * 根据座位高度自动计算座位绘制的主座位与次座位高度
     *
     * @param seatHeight
     */
    public void autoCalculateSeatShapeHeight(float seatHeight) {
        float newRadius = seatHeight * 0.1f;
        this.mSeatRadius = newRadius > 20f ? 20f : newRadius;
        this.mMainSeatHeight = seatHeight * 0.75f;
        this.mMinorSeatHeight = seatHeight * 0.2f;
        this.mSeatHeightInterval = seatHeight * 0.05f;
    }

    /**
     * 获取计算后的图片绘制区域(即一个完整地座位占用的区域)
     *
     * @param imageRecft    图片绘制区域,可为null,用于重复利用
     * @param drawPositionX 绘制的X轴中心位置
     * @param drawPositionY 绘制的Y轴中心位置
     * @return
     */
    public RectF getSeatDrawImageRecf(RectF imageRecft, float drawPositionX, float drawPositionY) {
        if (imageRecft == null) {
            imageRecft = new RectF();
        }
        float imageWidth = this.getSeatWidth();
        float imageHeight = this.getSeatHeight();
        imageRecft.left = drawPositionX - imageWidth / 2;
        imageRecft.right = imageRecft.left + imageWidth;
        imageRecft.top = drawPositionY - imageHeight / 2;
        imageRecft.bottom = imageRecft.top + imageHeight;
        return imageRecft;
    }

    /**
     * 获取主座位绘制矩形或者次座位绘制矩形
     *
     * @param seatRectf     座位绘制矩形对象
     * @param drawPositionX 绘制座位的中心X轴位置
     * @param drawPositionY 绘制座位的中心Y轴位置
     * @param isMainSeat    true为获取主座位,false为获取次座位
     * @return
     */
    public RectF getSeatDrawDefaultRectf(RectF seatRectf, float drawPositionX, float drawPositionY, boolean isMainSeat) {
        if (seatRectf == null) {
            seatRectf = new RectF();
        }
        seatRectf.left = drawPositionX - this.getSeatWidth() / 2;
        seatRectf.right = seatRectf.left + this.getSeatWidth();

        seatRectf.top = drawPositionY - this.getSeatHeight() / 2;
        seatRectf.bottom = seatRectf.top + this.mMainSeatHeight;

        if (!isMainSeat) {
            seatRectf.top = seatRectf.bottom + this.getSeatHorizontalInterval() + this.mMinorSeatHeight / 2;
            seatRectf.bottom = seatRectf.top + this.mMinorSeatHeight;
        }

        return seatRectf;
    }

}
