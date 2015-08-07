package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.graphics.Color;

/**
 * Author:
 * Description:
 */
public class SeatParams {
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
     * 座位类型,可选的
     */
    public static final int SEAT_TYPE_UNSELETED = 1;
    /**
     * 座位类型,已选的
     */
    public static final int SEAT_TYPE_SELETED = 2;
    /**
     * 座位类型,已售
     */
    public static final int SEAT_TYPE_SOLD = 3;

    private float mSeatWidth = DEFAULT_SEAT_WIDTH;
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
    private float mSeatTotalHeight = mMainSeatHeight + mMinorSeatHeight + mSeatHeightInterval;
    private float mSeatRadius = DEFAULT_SEAT_RADIUS;

    private float mSeatHorizontalInterval = DEFAULT_SEAT_HORIZONTAL_INTERVAL;
    private float mSeatVerticalInterval = DEFAULT_SEAT_VERTICAL_INTERVAL;

    private int mSeatColor = DEFAULT_SEAT_COLOR;
    private int[] mSeatTypeArrary = null;
    private int[] mSeatColorArrary = null;


    public SeatParams() {
        resetSeatTypeWithColor();
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

    public int getSeatColor() {
        return mSeatColor;
    }

    public int[] getSeatTypeArrary() {
        return mSeatTypeArrary;
    }

    public int[] getSeatColorArrary() {
        return mSeatColorArrary;
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

    public void setSeatColor(int mSeatColor) {
        if (mSeatColor == DEFAULT_INT) {
            this.mSeatColor = DEFAULT_SEAT_COLOR;
        } else {
            this.mSeatColor = mSeatColor;
        }
    }

    /**
     * 设置座位默认类型对应的颜色
     *
     * @param unSelectedSeatColor 可选座位,{@link #SEAT_TYPE_UNSELETED}
     * @param seletedSeatColor    已选座位,{@link #SEAT_TYPE_SELETED}
     * @param soldSeatColor       已售座位,{@link #SEAT_TYPE_SOLD}
     */
    public void setSeatColorArrary(int unSelectedSeatColor, int seletedSeatColor, int soldSeatColor) {
        if (mSeatColorArrary != null) {
            mSeatColorArrary[0] = unSelectedSeatColor;
            mSeatColorArrary[1] = seletedSeatColor;
            mSeatColorArrary[2] = soldSeatColor;
        }
    }

    /**
     * 设置/添加额外的座位类型及颜色,<font color="yellow"><b>该方法保留了默认的座位类型及颜色参数,只是在其基础上添加了其它的类型与参数</b></font>
     *
     * @param seatExtraTypeArr 新增的座位类型
     * @param colorExtraArr    新增的座位类型对应的颜色
     */
    public void setExtraSeatTypeWithColor(int[] seatExtraTypeArr, int[] colorExtraArr) {
        if (seatExtraTypeArr != null && colorExtraArr != null && seatExtraTypeArr.length == colorExtraArr.length) {
            //创建新数组
            int[] newSeatTypeArr = new int[3 + seatExtraTypeArr.length];
            int[] newSeatColorArr = new int[3 + colorExtraArr.length];
            //载入默认类型及颜色参数
            newSeatTypeArr[0] = SEAT_TYPE_UNSELETED;
            newSeatTypeArr[1] = SEAT_TYPE_SELETED;
            newSeatTypeArr[2] = SEAT_TYPE_SOLD;

            newSeatColorArr[0] = Color.BLACK;
            newSeatColorArr[1] = Color.RED;
            newSeatColorArr[2] = Color.YELLOW;
            //添加新增额外的类型与颜色参数
            for (int i = 3; i < newSeatTypeArr.length; i++) {
                newSeatTypeArr[i] = seatExtraTypeArr[i - 3];
                newSeatColorArr[i] = colorExtraArr[i - 3];
            }

            mSeatTypeArrary = newSeatTypeArr;
            mSeatColorArrary = newSeatColorArr;
        } else {
            throw new RuntimeException("设置额外座位类型及颜色失败,请确认所有参数不可为null且参数值的length必须相同");
        }
    }

    /**
     * 设置所有座位的类型及颜色,<font color="yellow"><b>该方法会替换所有的座位默认类型及颜色参数</b></font>
     *
     * @param seatTypeArr 新的座位类型
     * @param colorArr    新的座位类型对应的颜色
     */
    public void setAllSeatTypeWithColor(int[] seatTypeArr, int[] colorArr) {
        if (seatTypeArr != null && colorArr != null && seatTypeArr.length == colorArr.length) {
            mSeatTypeArrary = seatTypeArr;
            mSeatColorArrary = colorArr;
        } else {
            throw new RuntimeException("设置新座位类型及颜色失败,请确认所有参数不可为null且参数值的length必须相同");
        }
    }

    /**
     * 重置所有的座位类型与参数,回到默认状态(三个座位类型及颜色参数)
     * <p>
     * <li>{@link #SEAT_TYPE_UNSELETED} 可选座位</li>
     * <li>{@link #SEAT_TYPE_SELETED} 已选座位</li>
     * <li>{@link #SEAT_TYPE_SOLD} 已售座位</li>
     * </p>
     */
    public void resetSeatTypeWithColor() {
        mSeatTypeArrary = new int[3];
        mSeatColorArrary = new int[3];
        //载入默认类型及颜色参数
        mSeatTypeArrary[0] = SEAT_TYPE_UNSELETED;
        mSeatTypeArrary[1] = SEAT_TYPE_SELETED;
        mSeatTypeArrary[2] = SEAT_TYPE_SOLD;

        mSeatColorArrary[0] = Color.BLACK;
        mSeatColorArrary[1] = Color.RED;
        mSeatColorArrary[2] = Color.YELLOW;
    }

    /**
     * 根据座位类型获取座位类型对应的颜色
     *
     * @param seatType 座位类型
     *                 <p>默认座位类型
     *                 <li>{@link #SEAT_TYPE_UNSELETED} 可选座位</li>
     *                 <li>{@link #SEAT_TYPE_SELETED} 已选座位</li>
     *                 <li>{@link #SEAT_TYPE_SOLD} 已售座位</li>
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
}