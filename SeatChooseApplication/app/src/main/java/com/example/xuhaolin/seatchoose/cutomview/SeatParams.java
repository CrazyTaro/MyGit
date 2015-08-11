package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.graphics.Bitmap;
import android.graphics.Color;

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
//    /**
//     * 座位类型,可选的
//     */
//    public static final int DEFAULT_SEAT_TYPE_UNSELETED = 1;
//    /**
//     * 座位类型,已选的
//     */
//    public static final int DEFAULT_SEAT_TYPE_SELETED = 2;
//    /**
//     * 座位类型,已售
//     */
//    public static final int DEFAULT_SEAT_TYPE_SOLD = 3;
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
     * 座位类型，不绘制
     */
    public static final int DEFAULT_SEAT_TYPE_NOT_DRAW = 0;
    /**
     * 默认座位类型描述,"可选,已选,已售"
     */
    public static String[] DEFAULT_SEAT_TYPE_DESC = {"可选", "已选", "已售"};
//    /**
//     * 座位类型描述，可选座位，{@link #DEFAULT_SEAT_TYPE_UNSELETED}
//     */
//    public static final String DEFAULT_SEAT_UNSELETED_DESC = "可选";
//    /**
//     * 座位类型描述，已选座位，{@link #DEFAULT_SEAT_TYPE_SELETED}
//     */
//    public static final String DEFAULT_SEAT_SELETED_DESC = "已选";
//    /**
//     * 座位类型描述，已售座位，{@link #DEFAULT_SEAT_TYPE_SOLD}
//     */
//    public static final String DEFAULT_SEAT_SOLD_DESC = "已售";

    private float mSeatWidth = DEFAULT_SEAT_WIDTH;
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
    private float mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    private float mSeatRadius = DEFAULT_SEAT_RADIUS;

    private float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    private float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;
    private float mSeatTextInterval = DEFAULT_SEAT_TEXT_INTERVAL;
    private float mSeatTypeInterval = DEFAULT_SEAT_TYPE_INTERVAL;

    private boolean mIsImageSeat = false;
    private boolean mIsDrawSeat = true;
    private int mSeatColor = DEFAULT_SEAT_COLOR;
    private int[] mSeatTypeArrary = null;
    private int[] mSeatColorArrary = null;
    private int[] mSeatImageID = null;
    private Bitmap[] mSeatImageBitmaps = null;
    private String[] mSeatTypeDescription = null;

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

    public void resetSeatParams() {
        mInstance = new SeatParams();
    }

    public boolean getIsDrawSeat() {
        return mIsDrawSeat;
    }

    public float getSeatWidth() {
        return mSeatWidth;
    }

    public float getMainSeatHeight() {
        return mMainSeatHeight;
    }

    public float getMinorSeatHeight() {
        return mMinorSeatHeight;
    }

    public float getSeatHeightInterval() {
        return mSeatHeightInterval;
    }

    public float getSeatTotalHeight() {
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
        return mSeatTypeArrary;
    }

    public int[] getSeatColorArrary() {
        return mSeatColorArrary;
    }

    public String[] getSeatTypeDescription() {
        return mSeatTypeDescription;
    }

    public int getSeatTextColor() {
        return mSeatTextColor;
    }

    public float getSeatTextSize() {
        return mSeatTextSize;
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

    public void setMainSeatHeight(float mMainSeatHeight) {
        if (mMainSeatHeight == DEFAULT_FLOAT) {
            this.mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
        } else {
            this.mMainSeatHeight = mMainSeatHeight;
        }
        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    }

    public void setMinorSeatHeight(float mMinorSeatHeight) {
        if (mMinorSeatHeight == DEFAULT_FLOAT) {
            this.mMinorSeatHeight = mMinorSeatHeight;
        } else {
            this.mMinorSeatHeight = mMinorSeatHeight;
        }
        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    }

    public void setSeatHeightInterval(float mSeatHeightInterval) {
        if (mSeatHeightInterval == DEFAULT_FLOAT) {
            this.mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
        } else {
            this.mSeatHeightInterval = mSeatHeightInterval;
        }
        this.mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    }

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
     * 根据座位类型来确定座位是否需要绘制，当座位类型为{@link #DEFAULT_SEAT_TYPE_NOT_DRAW}时，不绘制该座位
     *
     * @param seatType 座位类型
     */
    public void setIsDrawSeat(int seatType) {
        if (seatType == DEFAULT_SEAT_TYPE_NOT_DRAW) {
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
            } else if (seatTypeExtraDesc != null && seatTypeExtraDesc.length != seatExtraTypeArr.length) {
                throw new RuntimeException("设置额外的座位类型描述length应与额外的座位类型length一致");
            }


            mSeatTypeArrary = newSeatTypeArr;
            mSeatColorArrary = newSeatColorArr;
            mSeatTypeDescription = newSeatTypeDescription;
        } else {
            throw new RuntimeException("设置额外座位类型及颜色失败,请确认前两个参数不可为null且参数值的length必须相同");
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
            mSeatTypeArrary = seatTypeArr;
            mSeatColorArrary = colorArr;

            if ((seatTypeDesc != null && seatTypeDesc.length == seatTypeArr.length) || seatTypeDesc == null) {
                mSeatTypeDescription = seatTypeDesc;
            } else {
                throw new RuntimeException("设置座位类型描述length应与座位类型length一致");
            }
        } else {
            throw new RuntimeException("设置新座位类型及颜色失败,请确认前两个参数不可为null且参数值的length必须相同");
        }
    }

    /**
     * 重置所有的座位类型与参数,回到默认状态(三个座位类型及颜色参数)
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

        /**
         * 获取座位绘制的实际高度
         *
         * @return
         */
        public float getSeatDrawHeight();
    }
}
