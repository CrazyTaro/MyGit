package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/6.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:
 * Description:
 */
public class SeatChooseView extends View {
    /**
     * 默认颜色值:黑
     */
    private static final int DEFAULT_COLOR = Color.BLACK;
    /**
     * 默认文字大小:35
     */
    private static final float DEFAULT_TEXT_SIZE = 35f;
    /**
     * 默认座位宽度:50
     */
    private static final float DEFAULT_SEAT_WIDTH = 50f;
    /**
     * 默认主座位高度:35
     */
    private static final float DEFAULT_SEAT_MAIN_HEIGHT = 35f;
    /**
     * 默认次座位高度:10
     */
    private static final float DEFAULT_SEAT_MINOR_HEIGHT = 10f;
    /**
     * 默认主次座位间隔高度:3
     */
    private static final float DEFAULT_SEAT_HEIGHT_INTERVAL = 3f;
    /**
     * 默认座位圆角度:8
     */
    private static final float DEFAULT_SEAT_RADIUS = 8f;
    /**
     * 默认舞台宽度:300
     */
    private static final float DEFAULT_STAGE_WIDTH = 300f;
    /**
     * 默认舞台高度:50
     */
    private static final float DEFAULT_STAGE_HEIGHT = 60f;
    /**
     * 默认舞台圆角度:20
     */
    private static final float DEFAULT_STAGE_RADIUS = 20f;
    /**
     * 默认整数值:-1
     */
    private static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    private static final float DEFAULT_FLOAT = -1;
    /**
     * 座位类型,可选的
     */
    private static final int SEAT_TYPE_UNSELETED = -1;
    /**
     * 座位类型,已售
     */
    private static final int SEAT_TYPE_SOLD = 0;
    /**
     * 座位类型,已选的
     */
    private static final int SEAT_TYPE_SELETED = 1;

    private Paint mPaint = null;
    private PointF mWHPoint = null;
    //主座位,两者结合让座位看起来比较好看而已...
    private RectF mMainSeatRectf = null;
    //次座位,两者结合让座位看起来比较好看而已...
    private RectF mMinorSeatRectf = null;

    private float mStageWidth = DEFAULT_STAGE_WIDTH;
    private float mStageHeight = DEFAULT_STAGE_HEIGHT;
    private float mStageMarginTop = 20f;
    private float mStageRadius = DEFAULT_STAGE_RADIUS;
    private float mBeginDrawHeight = 0f;

    private float mSeatWidth = DEFAULT_SEAT_WIDTH;
    private float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    private float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    private float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
    private float mSeatRadius = DEFAULT_SEAT_RADIUS;

    private int mStageColor = DEFAULT_COLOR;
    private int mSeatColor = DEFAULT_COLOR;
    private int mTextColor = DEFAULT_COLOR;
    private float mTextSize = DEFAULT_TEXT_SIZE;

    public SeatChooseView(Context context) {
        super(context);
        initial();
    }

    public SeatChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    private void initial() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //创建主座位与次要座位
        //此部分仅仅是让座位看起来比较好看而已....
        mMainSeatRectf = new RectF();
        mMinorSeatRectf = new RectF();
    }

    /**
     * 设置座位及文字通用参数,<font color="yellow"><b>所有参数使用默认值请传入{@link #DEFAULT_INT}/{@link #DEFAULT_FLOAT}</b></font>
     *
     * @param seatColor 座位颜色
     * @param textColor 文字颜色
     * @param textSize  文字大小
     */
    public void setSeatAndTextParams(int seatColor, int textColor, float textSize) {
        if (seatColor == DEFAULT_INT) {
            mSeatColor = DEFAULT_COLOR;
        } else {
            mSeatColor = seatColor;
        }

        if (textColor == DEFAULT_INT) {
            mTextColor = DEFAULT_COLOR;
        } else {
            mTextColor = textColor;
        }

        if (textSize == DEFAULT_FLOAT) {
            mTextSize = DEFAULT_TEXT_SIZE;
        } else {
            mTextSize = textSize;
        }
    }

    /**
     * 设置舞台通用参数,<font color="yellow"><b>所有参数使用默认值请传入{@link #DEFAULT_INT}/{@link #DEFAULT_FLOAT}</b></font>
     *
     * @param stageWidth     舞台宽度
     * @param stageHeight    舞台高度
     * @param stageMarginTop 舞台与最顶端距离
     * @param stageColor     舞台颜色
     */
    public void setStageParams(float stageWidth, float stageHeight, float stageMarginTop, int stageColor) {
        if (stageWidth == DEFAULT_FLOAT) {
            mStageWidth = DEFAULT_STAGE_WIDTH;
        } else {
            mStageWidth = stageWidth;
        }

        if (stageHeight == DEFAULT_FLOAT) {
            mStageHeight = DEFAULT_STAGE_HEIGHT;
        } else {
            mStageHeight = stageHeight;
        }

        if (stageMarginTop == DEFAULT_FLOAT) {
            mStageMarginTop = 20f;
        } else {
            mStageMarginTop = stageMarginTop;
        }

        if (stageColor == DEFAULT_INT) {
            mStageColor = DEFAULT_COLOR;
        } else {
            mStageColor = stageColor;
        }
    }

    /**
     * 初始化座位参数,<font color="yellow"><b>所有参数使用默认值请传入{@link #DEFAULT_INT}/{@link #DEFAULT_FLOAT}</b></font>
     *
     * @param seatWidth          座位宽度
     * @param mainSeatHeight     主座位高度
     * @param minorSeatHeight    次座位高度
     * @param seatHeightInterval 主次座位间隔高度
     * @param seatColor          座位颜色
     */
    public void initialSeatParams(float seatWidth, float mainSeatHeight, float minorSeatHeight, float seatHeightInterval, float seatRadius, int seatColor) {
        if (seatWidth == DEFAULT_FLOAT) {
            mSeatWidth = DEFAULT_SEAT_WIDTH;
        } else {
            mSeatWidth = seatWidth;
        }

        if (minorSeatHeight == DEFAULT_FLOAT) {
            mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
        } else {
            mMinorSeatHeight = minorSeatHeight;
        }

        if (mainSeatHeight == DEFAULT_FLOAT) {
            mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
        } else {
            mMainSeatHeight = mainSeatHeight;
        }

        if (seatHeightInterval == DEFAULT_FLOAT) {
            mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;
        } else {
            mSeatHeightInterval = seatHeightInterval;
        }

        if (seatRadius == DEFAULT_FLOAT) {
            mSeatRadius = DEFAULT_SEAT_RADIUS;
        } else {
            mSeatRadius = seatRadius;
        }

        if (seatColor == DEFAULT_INT) {
            mSeatColor = DEFAULT_COLOR;
        } else {
            mSeatColor = seatColor;
        }
    }

    /**
     * 获取View显示的宽高
     *
     * @return {@link PointF},返回point对象,point.x为宽,point.y为高
     */
    private PointF getWidthAndHeight() {
        //获取view计算需要显示的宽高
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            //无法取得计算结果则获取实际宽高
            width = this.getWidth();
            height = this.getHeight();
        }
        PointF WHPoint = new PointF();
        WHPoint.x = width;
        WHPoint.y = height;
        return WHPoint;
    }

    /**
     * 获取舞台绘制的矩形数据
     *
     * @param centerX   控件的中心X轴位置
     * @param marginTop 舞台绘制与顶部保持的高度
     * @param width     舞台绘制的宽度
     * @param height    舞台绘制的高度(与字体大小相关)
     * @return
     */
    private RectF getStageRectF(float centerX, float marginTop, float width, float height) {
        RectF stageRectf = new RectF();
        stageRectf.top = marginTop;
        stageRectf.bottom = stageRectf.top + height;
        //计算舞台的左右开始绘制的位置
        stageRectf.left = centerX - width / 2;
        stageRectf.right = centerX + width / 2;
        return stageRectf;
    }

    /**
     * 获取文字绘制的XY位置,<b>此处用于处理的是在某个位置绘制<font color="yellow">居中显示的文字</font></b>
     * <p>给定该位置之后该方法会计算出居中显示的文字实际应该开始绘制的XY坐标</p>
     *
     * @param textSize  字体大小
     * @param drawText  需要绘制的文字
     * @param xPosition 文字绘制的X轴位置
     * @param yPosition 文字绘制的Y轴位置
     * @return
     */
    private PointF getTextDrawXY(float textSize, String drawText, float xPosition, float yPosition) {
        mPaint.setTextSize(textSize);
        float textLength = mPaint.measureText(drawText);

        PointF textDrawPointf = new PointF();
        textDrawPointf.x = xPosition - textLength / 2;
        textDrawPointf.y = yPosition + textSize / 3;
        return textDrawPointf;
    }

    /**
     * 同时绘制座位与其相邻的文字,文字位置可选绘制在座位的左边或者右边
     *
     * @param canvas           画板
     * @param paint            画笔
     * @param drawXPosition    座位与文字绘制位置的<font color="yellow"><b>X轴中心(仅提供需要绘制到的X轴中心坐标即可)</b></font>
     * @param drawYPosition    座位与文字绘制位置的<font color="yellow"><b>Y轴中心(仅提供需要绘制到的Y轴中心坐标即可)</b></font>
     * @param text             需要绘制的文字
     * @param interval         文字与座位之前的间隔
     * @param isDrawTextOfLeft 是否将文字绘制在座位的左边,<font color="yellow"><b>true为文字绘制在座位左边,false为文字绘制在座位右边</b></font>
     */
    private void drawSeatWithNearText(Canvas canvas, Paint paint, float drawXPosition, float drawYPosition, String text, float interval, boolean isDrawTextOfLeft) {
        //座位绘制的中心X轴
        float seatCenterX = 0f;
        //座位绘制的中心Y轴
        float seatCenterY = 0f;
        //绘制文字的长度
        float textLength = 0f;
        //文字开始绘制的X轴位置
        float textBeginDrawX = 0f;
        //文字开始绘制的Y轴位置
        float textBeginDrawY = 0f;

        //计算绘制文字的长度
        paint.setTextSize(mTextSize);
        textLength = paint.measureText(text);

        if (isDrawTextOfLeft) {
            //将文字绘制在座位的左边
            //计算座位绘制的中心X轴位置
            seatCenterX = drawXPosition + interval / 2 + mSeatWidth / 2;
            //计算文字开始绘制的X轴位置
            textBeginDrawX = drawXPosition - interval / 2 - textLength;
        } else {
            //将文字绘制在座位的右边
            seatCenterX = drawXPosition - interval / 2 - mSeatWidth / 2;
            textBeginDrawX = drawXPosition + interval / 2;
        }
        //计算文字的Y轴位置(由于文字只考虑绘制在左边和右边,所以文字的绘制高度是不会受影响的)
        textBeginDrawY = drawYPosition + mTextSize / 3;
        seatCenterY = drawYPosition;

        //绘制文字
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mTextColor);
        canvas.drawText(text, textBeginDrawX, textBeginDrawY, paint);
        //绘制座位
        drawSeat(canvas, paint, seatCenterX, seatCenterY);
    }

    /**
     * 绘制座位
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 座位绘制的中心X轴坐标
     * @param drawPositionY 座位绘制的中心Y轴坐标
     */
    private void drawSeat(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY) {
        //座位的占拒的总高度(两部分座位)
        float totalSeatHeight = 0f;
        //计算座位占用的总高度
        totalSeatHeight = mSeatHeightInterval + mMainSeatHeight + mMinorSeatHeight;
        mMainSeatRectf.left = drawPositionX - mSeatWidth / 2;
        mMainSeatRectf.right = mMainSeatRectf.left + mSeatWidth;
        mMinorSeatRectf.left = mMainSeatRectf.left;
        mMinorSeatRectf.right = mMainSeatRectf.right;

        mMainSeatRectf.top = drawPositionY - totalSeatHeight / 2;
        mMainSeatRectf.bottom = mMainSeatRectf.top + mMainSeatHeight;
        mMinorSeatRectf.top = mMainSeatRectf.bottom + mSeatHeightInterval;
        mMinorSeatRectf.bottom = mMinorSeatRectf.top + mMinorSeatHeight;

        //填充座位并绘制
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mSeatColor);
        canvas.drawRoundRect(mMainSeatRectf, mSeatRadius, mSeatRadius, paint);
        canvas.drawRoundRect(mMinorSeatRectf, mSeatRadius, mSeatRadius, paint);
    }

    /**
     * 绘制舞台及其文字,<font color="yellow"><b>此方法中舞台文字的大小为自动计算,设置文字大小无法起作用</b></font>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 舞台绘制位置的中心X轴
     * @param drawPositionY 舞台绘制位置的中心Y轴
     * @param stageText     舞台绘制的文字
     */
    private void drawStage(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, String stageText) {
        RectF stageRectf = new RectF();
        float textBeginDrawX = 0f;
        float textBeginDrawY = 0f;
        float textLength = 0f;
        //设置文字大小为舞台高度小一点(保证文字可以显示在舞台范围内)
        float textSize = mStageHeight - 10;

        stageRectf.left = drawPositionX - mStageWidth / 2;
        stageRectf.right = stageRectf.left + mStageWidth;
        stageRectf.top = drawPositionY - mStageHeight / 2;
        stageRectf.bottom = stageRectf.top + mStageHeight;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mStageColor);
        canvas.drawRoundRect(stageRectf, mStageRadius, mStageRadius, paint);

        paint.setTextSize(textSize);
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.STROKE);

        textLength = paint.measureText(stageText);
        textBeginDrawX = stageRectf.centerX() - textLength / 2;
        textBeginDrawY = stageRectf.centerY() + textSize / 3;
        canvas.drawText(stageText, textBeginDrawX, textBeginDrawY, paint);
    }

    private void drawSellSeats(int[][] seatMap, float drawPositionX, float drawPositionY) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mWHPoint == null) {
            mWHPoint = getWidthAndHeight();
        }
        float viewCenterX = mWHPoint.x / 2;
        float viewCenterY = mWHPoint.y / 2;

        float drawX = 0f;
        float drawY = 0f;

        drawX = viewCenterX;
        drawY = mStageMarginTop + mStageHeight / 2;
        this.setStageParams(DEFAULT_FLOAT, DEFAULT_FLOAT, DEFAULT_FLOAT, Color.GREEN);
        drawStage(canvas, mPaint, drawX, drawY, "舞台");

        mBeginDrawHeight = mStageMarginTop + mStageHeight + 20;

        drawY = mBeginDrawHeight + (mMainSeatHeight + mMinorSeatHeight) / 2;
        drawX = viewCenterX;
        this.setSeatAndTextParams(DEFAULT_INT, DEFAULT_INT, DEFAULT_FLOAT);
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "锁定", 10f, false);

        this.setSeatAndTextParams(Color.RED, DEFAULT_INT, DEFAULT_FLOAT);
        drawX = viewCenterX - viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "可选", 10f, false);

        this.setSeatAndTextParams(Color.YELLOW, DEFAULT_INT, DEFAULT_FLOAT);
        drawX = viewCenterX + viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "已选", 10f, false);
    }
}
