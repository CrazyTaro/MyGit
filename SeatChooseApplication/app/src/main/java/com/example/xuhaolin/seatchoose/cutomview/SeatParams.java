package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

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
     * 座位的绘制类型,不绘制
     */
    public static final int SEAT_DRAW_TYPE_NO = 0;
    /**
     * 座位的绘制类型,默认绘制
     */
    public static final int SEAT_DRAW_TYPE_DEFAULT = 1;
    /**
     * 座位的绘制类型,座位绘制为图片
     */
    public static final int SEAT_DRAW_TYPE_IMAGE = 2;


    private float mSeatWidth = DEFAULT_SEAT_WIDTH;
    private float mSeatHeight = DEFAULT_SEAT_HEIGHT;
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
    private float mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    private float mSeatRadius = DEFAULT_SEAT_RADIUS;

    private float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    private float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    private float mSeatTextInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    private float mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;


    private boolean mIsDrawSeat = true;
    private int mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
    private int mSeatColor = DEFAULT_SEAT_COLOR;
    private int[] mSeatTypeArrary = null;
    private int[] mSeatColorArrary = null;
    private String[] mSeatTypeDescription = null;
    private int[] mSeatImageID = null;
    private Bitmap[] mSeatImageBitmaps = null;

    private float mSeatTextSize = DEFAULT_SEAT_TEXT_SIZE;
    private int mSeatTextColor = DEFAULT_SEAT_TEXT_COLOR;

    private static SeatParams mInstance = null;

    private SeatParams() {
        resetSeatTypeWithColor();
        mSeatTypeDescription = DEFAULT_SEAT_TYPE_DESC;
    }

    public static synchronized SeatParams getInstance() {
        if (mInstance == null) {
            mInstance = new SeatParams();
        }
        return mInstance;
    }

    public void setScaleRate(float scaleRate) {
        this.mSeatWidth *= scaleRate;
        this.mSeatHeight *= scaleRate;
        this.mSeatHeightInterval *= scaleRate;
        this.mSeatVerticalInterval *= scaleRate;
        this.mSeatTextInterval *= scaleRate;
        this.mSeatTypeInterval *= scaleRate;
        this.mSeatTextSize *= scaleRate;
        this.autoCalculateSeatShapeHeight(this.mSeatHeight);
    }

    public void resetSeatParams() {
        mInstance = new SeatParams();
    }

    public boolean getIsDrawSeat() {
        return mIsDrawSeat;
    }

    public float getSeatWidth() {
        return mSeatWidth;
    }

    public float getSeatHeight() {
        return mSeatHeight;
    }

    private float getMainSeatHeight() {
        return mMainSeatHeight;
    }

    private float getMinorSeatHeight() {
        return mMinorSeatHeight;
    }

    private float getSeatHeightInterval() {
        return mSeatHeightInterval;
    }

    private float getSeatTotalHeight() {
        return mSeatTotalHeight;
    }

    public float getSeatRadius() {
        return mSeatRadius;
    }

    public float getSeatHorizontalInterval() {
        return mSeatHorizontalInterval;
    }

    public float getSeatVerticalInterval() {
        return mSeatVerticalInterval;
    }

    public float getSeatTextInterval() {
        return mSeatTextInterval;
    }

    public float getSeatTypeInterval() {
        return mSeatTypeInterval;
    }

    public int getSeatColor() {
        return mSeatColor;
    }

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

    public int getSeatTextColor() {
        return mSeatTextColor;
    }

    public float getSeatTextSize() {
        return mSeatTextSize;
    }

    public int getSeatDrawType() {
        return mSeatDrawType;
    }

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

    public void setSeatDrawType(int drawType) {
        if (drawType == DEFAULT_INT) {
            this.mSeatDrawType = SEAT_DRAW_TYPE_DEFAULT;
        } else {
            this.mSeatDrawType = drawType;
        }
    }

    public void setImageSeat(int[] imageID) {
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

    public void setImageSeat(Bitmap[] imageBitmap) {
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
//        mSeatImageID = null;
    }

    public void setSeatTextColor(int mSeatTextColor) {
        if (mSeatTextColor == DEFAULT_INT) {
            this.mSeatTextColor = DEFAULT_SEAT_TEXT_COLOR;
        } else {
            this.mSeatTextColor = mSeatTextColor;
        }
    }

    public void setSeatTextSize(float mSeatTextSize) {
        if (mSeatTextSize == DEFAULT_FLOAT) {
            this.mSeatTextSize = DEFAULT_SEAT_TEXT_SIZE;
        } else {
            this.mSeatTextSize = mSeatTextSize;
        }
    }

    public void setSeatWidth(float mSeatWidth) {
        if (mSeatWidth == DEFAULT_FLOAT) {
            this.mSeatWidth = DEFAULT_SEAT_WIDTH;
        } else {
            this.mSeatWidth = mSeatWidth;
        }
    }

    public void setSeatHeight(float mSeatHeight) {
        if (mSeatHeight == DEFAULT_FLOAT) {
            this.mSeatHeight = DEFAULT_SEAT_HEIGHT;
        } else {
            this.mSeatHeight = mSeatHeight;
        }
        this.autoCalculateSeatShapeHeight(this.mSeatHeight);
    }

//    public void setMainSeatHeight(float mMainSeatHeight) {
//        if (mMainSeatHeight == DEFAULT_FLOAT) {
//            this.mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
//        } else {
//            this.mMainSeatHeight = mMainSeatHeight;
//        }
//        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
//    }

//    public void setMinorSeatHeight(float mMinorSeatHeight) {
//        if (mMinorSeatHeight == DEFAULT_FLOAT) {
//            this.mMinorSeatHeight = mMinorSeatHeight;
//        } else {
//            this.mMinorSeatHeight = mMinorSeatHeight;
//        }
//        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
//    }

//    public void setSeatHeightInterval(float mSeatHeightInterval) {
//        if (mSeatHeightInterval == DEFAULT_FLOAT) {
//            this.mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
//        } else {
//            this.mSeatHeightInterval = mSeatHeightInterval;
//        }
//        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
//    }

    public void setSeatRadius(float mSeatRadius) {
        if (mSeatRadius == DEFAULT_FLOAT) {
            this.mSeatRadius = DEFAULT_SEAT_RADIUS;
        } else {
            this.mSeatRadius = mSeatRadius;
        }
    }

    public void setSeatHorizontalInterval(float mSeatHorizontalInterval) {
        if (mSeatHorizontalInterval == DEFAULT_FLOAT) {
            this.mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
        } else {
            this.mSeatHorizontalInterval = mSeatHorizontalInterval;
        }
    }

    public void setSeatVerticalInterval(float mSeatVerticalInterval) {
        if (mSeatVerticalInterval == DEFAULT_FLOAT) {
            this.mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
        } else {
            this.mSeatVerticalInterval = mSeatVerticalInterval;
        }
    }

    public void setSeatTextInterval(float mSeatTextInterval) {
        if (mSeatTextInterval == DEFAULT_FLOAT) {
            this.mSeatTextInterval = DEFAULT_SEAT_TEXT_INTERVAL;
        } else {
            this.mSeatTextInterval = mSeatTextInterval;
        }
    }

    public void setSeatTypeInterval(float mSeatTypeInterval) {
        if (mSeatTypeInterval == DEFAULT_FLOAT) {
            this.mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;
        } else {
            this.mSeatTypeInterval = mSeatTypeInterval;
        }
    }

    public void setIsDrawSeat(boolean mIsDrawSeat) {
        this.mIsDrawSeat = mIsDrawSeat;
    }

    /**
     * 根据座位类型来确定座位是否需要绘制，当座位类型为{@link #SEAT_DRAW_TYPE_NO}时，不绘制该座位
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
        if (mSeatColor == DEFAULT_INT) {
            this.mSeatColor = DEFAULT_SEAT_COLOR;
        } else {
            this.mSeatColor = mSeatColor;
        }
    }

    /**
     * 设置自定义默认的座位类型
     *
     * @param firstSeatType  第一个座位类型
     * @param secondSeatType 第二个座位类型
     * @param thirdSeatType  第三个座位类型
     */
    public static void setDefaultSeatType(int firstSeatType, int secondSeatType, int thirdSeatType) {
        DEFAULT_SEAT_TYPE[0] = firstSeatType;
        DEFAULT_SEAT_TYPE[1] = secondSeatType;
        DEFAULT_SEAT_TYPE[2] = thirdSeatType;
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
    public static void setDefaultSeatColorArrary(int firstColor, int secondColor, int thirdColor) {
        //确保座位类型回到默认状态
        resetDefaultSeatParams();
        DEFAULT_SEAT_TYPE_COLOR[0] = firstColor;
        DEFAULT_SEAT_TYPE_COLOR[1] = secondColor;
        DEFAULT_SEAT_TYPE_COLOR[2] = thirdColor;
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
        resetDefaultSeatParams();
        DEFAULT_SEAT_TYPE_DESC[0] = firstDesc;
        DEFAULT_SEAT_TYPE_DESC[1] = secondDesc;
        DEFAULT_SEAT_TYPE_DESC[2] = thirdDesc;
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
            //存在空元素,尝试回收无用的图片,重新加载数据
            for (Bitmap bitmap : mSeatImageBitmaps) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
            //存在空元素则重新加载数据
            mSeatImageBitmaps = new Bitmap[mSeatImageID.length];

            for (int i = 0; i < mSeatImageID.length; i++) {
                //按预期宽度比例加载图片
                //用于防止原图片太大加载的内存过大
                Bitmap bitmap = getScaleBitmap(context, mSeatImageID[i], (int) this.getSeatWidth(), (int) this.getSeatHeight());
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
    private void autoCalculateSeatShapeHeight(float seatHeight) {
        this.mMainSeatHeight = seatHeight * 0.75f;
        this.mMinorSeatHeight = seatHeight * 0.2f;
        this.mSeatHeightInterval = seatHeight * 0.05f;
        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
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
    public RectF getSeatDrawRectf(RectF seatRectf, float drawPositionX, float drawPositionY, boolean isMainSeat) {
        if (seatRectf == null) {
            seatRectf = new RectF();
        }
        seatRectf.left = drawPositionX - this.mSeatWidth / 2;
        seatRectf.right = seatRectf.left + this.mSeatWidth;

        seatRectf.top = drawPositionY - this.mSeatTotalHeight / 2;
        seatRectf.bottom = seatRectf.top + this.mMainSeatHeight;

        if (!isMainSeat) {
            seatRectf.top = seatRectf.bottom + this.mSeatHeightInterval + this.mMinorSeatHeight / 2;
            seatRectf.bottom = seatRectf.top + this.mMinorSeatHeight;
        }

        return seatRectf;
    }

    /**
     * 座位操作接口(或部分参数需求)
     */
    interface ISeatOperation {
        /**
         * 获取出售座位开始绘制的第一行座位的Y轴坐标
         * <p><font color="yellow"><b><p>此处必须注意的地方是,这里的Y轴坐标是指top,而不是绘制位置的中心centerY</b></font></p>
         * <p><font color="yellow">此外,这里的Y轴坐标是原始绘制界面的坐标,而不是移动后的坐标(即在第一次绘制时把该坐标记录返回即可)</font></p>
         *
         * @return
         */
        public float getSellSeatsBeginDrawY();
    }
}
