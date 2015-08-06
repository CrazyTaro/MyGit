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
    private static final int DEFAULT_COLOR = Color.BLACK;
    private static final float DEFAULT_TEXT_SIZE = 35f;

    private Paint mPaint = null;
    private PointF mWHPoint = null;

    private float mSeatImgRadius = 8f;
    private float mStageImgRadius = 20f;
    private float mBeginDrawHeight = 0f;

    private float mSeatWidth = 50f;
    private float mMainSeatHeight = 35f;
    private float mMinorSeatHeight = 10f;
    private float mSeatHeightInterval = 3f;

    private int mSeatColor = Color.BLACK;
    private int mTextColor = Color.BLACK;
    private float mTextSize = 35f;

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
    }

    private void setSeatAndTextParams(int seatColor, int textColor, float textSize) {
        if (seatColor == -1) {
            mSeatColor = DEFAULT_COLOR;
        } else {
            mSeatColor = seatColor;
        }

        if (textColor == -1) {
            mTextColor = DEFAULT_COLOR;
        } else {
            mTextColor = textColor;
        }

        if (textSize == -1) {
            mTextSize = DEFAULT_TEXT_SIZE;
        } else {
            mTextSize = textSize;
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
     * @param drawXPosition    座位与文字绘制位置的<b>X轴中心(仅提供需要绘制到的X轴中心坐标即可)</b>
     * @param drawYPosition    座位与文字绘制位置的<b>Y轴中心(仅提供需要绘制到的Y轴中心坐标即可)</b>
     * @param text             需要绘制的文字
     * @param interval         文字与座位之前的间隔
     * @param isDrawTextOfLeft 是否将文字绘制在座位的左边,<b>true为文字绘制在座位左边,false为文字绘制在座位右边</b>
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
        //创建主座位与次要座位
        //此部分仅仅是让座位看起来比较好看而已....
        RectF mainSeatRectf = new RectF();
        RectF minorSeatRectf = new RectF();
        //座位的占拒的总高度(两部分座位)
        float totalSeatHeight = 0f;

        totalSeatHeight = mSeatHeightInterval + mMainSeatHeight + mMinorSeatHeight;
        mainSeatRectf.left = drawPositionX - mSeatWidth / 2;
        mainSeatRectf.right = mainSeatRectf.left + mSeatWidth;
        minorSeatRectf.left = mainSeatRectf.left;
        minorSeatRectf.right = mainSeatRectf.right;

        mainSeatRectf.top = drawPositionY - totalSeatHeight / 2;
        mainSeatRectf.bottom = mainSeatRectf.top + mMainSeatHeight;
        minorSeatRectf.top = mainSeatRectf.bottom + mSeatHeightInterval;
        minorSeatRectf.bottom = minorSeatRectf.top + mMinorSeatHeight;

        //填充座位并绘制
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mSeatColor);
        canvas.drawRoundRect(mainSeatRectf, mSeatImgRadius, mSeatImgRadius, paint);
        canvas.drawRoundRect(minorSeatRectf, mSeatImgRadius, mSeatImgRadius, paint);
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
        RectF stage = getStageRectF(viewCenterX, 30, 300, 60);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);
        canvas.drawRoundRect(stage, mStageImgRadius, mStageImgRadius, mPaint);
        mBeginDrawHeight += 30;
        mBeginDrawHeight += stage.height();

        PointF textDrawPointf = getTextDrawXY(35, "舞台", stage.centerX(), stage.centerY());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(35);
        mPaint.setColor(Color.BLACK);
        canvas.drawText("舞台", textDrawPointf.x, textDrawPointf.y, mPaint);

        mBeginDrawHeight += 30;
        drawY = mBeginDrawHeight + 30;
        drawX = viewCenterX;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "锁定", 10f, false);

        drawX = viewCenterX - viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "可选", 10f, false);

        drawX = viewCenterX + viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "已选", 10f, false);
    }
}
