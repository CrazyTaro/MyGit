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
     * 默认文字颜色值
     */
    public static final int DEFAULT_COLOR_TEXT = Color.BLACK;
    /**
     * 默认文字大小
     */
    public static final float DEFAULT_TEXT_SIZE = 25f;
    /**
     * 默认座位圆角度
     */
    public static final float DEFAULT_SEAT_RADIUS = 8f;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 350f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 50f;
    /**
     * 默认舞台圆角度
     */
    public static final float DEFAULT_STAGE_RADIUS = 10f;
    /**
     * 默认舞台与顶端间距
     */
    public static final float DEFAULT_STAGE_MARGIN_TOP = 15f;
    /**
     * 默认整数值:-1
     */
    public static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    public static final float DEFAULT_FLOAT = -1;

    private SeatParams mSeatParams = null;

    private Paint mPaint = null;
    private PointF mWHPoint = null;
    //主座位,两者结合让座位看起来比较好看而已...
    private RectF mMainSeatRectf = null;
    //次座位,两者结合让座位看起来比较好看而已...
    private RectF mMinorSeatRectf = null;

    private float mStageWidth = DEFAULT_STAGE_WIDTH;
    private float mStageHeight = DEFAULT_STAGE_HEIGHT;
    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageRadius = DEFAULT_STAGE_RADIUS;

    private int mStageColor = Color.GREEN;
    private int mTextColor = DEFAULT_COLOR_TEXT;
    private float mTextSize = DEFAULT_TEXT_SIZE;

    private float mBeginDrawPositionY = 0f;
    private float mAfterDrawPositionY = 0f;
    private float mBeginDrawPositionX = 0f;
    private float mAfterDrawPositionX = 0f;

    private int[][] mSeatMap = {
            {1, 2, 3, 1, 2, 3, 1, 1, 1, 3, 1, 1, 1, 2, 2, 2},
            {1, 2, 3, 1, 2, 3, 1, 1, 1, 3, 1, 1, 1, 2, 2, 2},
            {1, 2, 3, 1, 2, 3, 1, 1, 1, 1, 3, 1, 1, 2, 2, 2},
            {1, 2, 3, 1, 2, 3, 1, 1, 3, 1, 1, 1, 1, 2, 2, 2},
            {1, 2, 3, 1, 2, 3, 1, 1, 1, 1, 1, 3, 1, 2, 2, 2},
            {1, 2, 3, 1, 2, 3, 1, 1, 1, 3, 1, 1, 1, 2, 2, 2}
    };

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

        resetSeatParams();
    }

    /**
     * 设置文字通用参数,<font color="yellow"><b>所有参数使用默认值请传入{@link #DEFAULT_INT}</b></font>
     *
     * @param textColor 文字颜色
     * @param textSize  文字大小
     */
    public void setTextParams(int textColor, float textSize) {

        if (textColor == DEFAULT_INT) {
            mTextColor = DEFAULT_COLOR_TEXT;
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
     * 重置座位绘制使用的参数(使用默认值)
     */
    public void resetSeatParams() {
        mSeatParams = new SeatParams();
    }

    /**
     * 设置座位绘制使用的参数
     *
     * @param params 自定义座位绘制的参数
     */
    public void setSeatParams(SeatParams params) {
        if (params != null) {
            mSeatParams = params;
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
            mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            mStageMarginTop = stageMarginTop;
        }

        if (stageColor == DEFAULT_INT) {
            mStageColor = Color.GREEN;
        } else {
            mStageColor = stageColor;
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
            seatCenterX = drawXPosition + interval / 2 + mSeatParams.getSeatWidth() / 2;
            //计算文字开始绘制的X轴位置
            textBeginDrawX = drawXPosition - interval / 2 - textLength;
        } else {
            //将文字绘制在座位的右边
            seatCenterX = drawXPosition - interval / 2 - mSeatParams.getSeatWidth() / 2;
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
        totalSeatHeight = mSeatParams.getSeatHeightInterval() + mSeatParams.getMainSeatHeight() + mSeatParams.getMinorSeatHeight();
        mMainSeatRectf.left = drawPositionX - mSeatParams.getSeatWidth() / 2;
        mMainSeatRectf.right = mMainSeatRectf.left + mSeatParams.getSeatWidth();
        mMinorSeatRectf.left = mMainSeatRectf.left;
        mMinorSeatRectf.right = mMainSeatRectf.right;

        mMainSeatRectf.top = drawPositionY - totalSeatHeight / 2;
        mMainSeatRectf.bottom = mMainSeatRectf.top + mSeatParams.getMainSeatHeight();
        mMinorSeatRectf.top = mMainSeatRectf.bottom + mSeatParams.getSeatHeightInterval();
        mMinorSeatRectf.bottom = mMinorSeatRectf.top + mSeatParams.getMinorSeatHeight();

        //填充座位并绘制
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mSeatParams.getSeatColor());
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (mMainSeatRectf.left > 0 || mMainSeatRectf.right > 0 || mMainSeatRectf.top > 0 || mMainSeatRectf.bottom > 0) {
            canvas.drawRoundRect(mMainSeatRectf, mSeatParams.getSeatRadius(), mSeatParams.getSeatRadius(), paint);
        }
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (mMainSeatRectf.left > 0 || mMainSeatRectf.right > 0 || mMainSeatRectf.top > 0 || mMainSeatRectf.bottom > 0) {
            canvas.drawRoundRect(mMinorSeatRectf, mSeatParams.getSeatRadius(), mSeatParams.getSeatRadius(), paint);
        }
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
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (stageRectf.left > 0 || stageRectf.right > 0 || stageRectf.top > 0 || stageRectf.bottom > 0) {
            canvas.drawRoundRect(stageRectf, mStageRadius, mStageRadius, paint);
        }

        paint.setTextSize(textSize);
        paint.setColor(mTextColor);
        paint.setStyle(Paint.Style.STROKE);

        textLength = paint.measureText(stageText);
        textBeginDrawX = stageRectf.centerX() - textLength / 2;
        textBeginDrawY = stageRectf.centerY() + textSize / 3;
        //文字绘制不做限制
        //文字本身绘制范围小,另外需要重新计算两个参数才能进行判断
        canvas.drawText(stageText, textBeginDrawX, textBeginDrawY, paint);
    }

    /**
     * 绘制售票的座位
     *
     * @param canvase       画板
     * @param paint         画笔
     * @param seatMap       座位数组表
     * @param drawPositionX 开始绘制的中心X轴位置(第一行座位,中心绘制位置)
     * @param drawPositionY 开始绘制的中心Y轴位置(第一行座位,中心绘制位置)
     */
    private void drawSellSeats(Canvas canvase, Paint paint, int[][] seatMap, float drawPositionX, float drawPositionY) {
        if (seatMap == null) {
            return;
        }
        //列数
        int columnLength = seatMap[0].length;
        //行数
        int rowLength = seatMap.length;
        int middleColumnNum = 0;
        float beginDrawLeftX = 0f;
        float beginDrawRightX = 0f;
        float beginDrawY = 0f;

        if ((columnLength & 0x1) == 0) {
            //偶数列
            //向左边绘制X轴开始点
            beginDrawLeftX = drawPositionX - mSeatParams.getSeatHorizontalInterval() / 2 - mSeatParams.getSeatWidth() / 2;
            //向右边绘制X轴开始点
            beginDrawRightX = drawPositionX + mSeatParams.getSeatHorizontalInterval() / 2 + mSeatParams.getSeatWidth() / 2;
            beginDrawY = drawPositionY;
            middleColumnNum = columnLength / 2;
            for (int[] seatList : seatMap) {
                //画左边的座位
                //绘制的时候由start位置到end位置
                //从start到end每一个位置都会绘制,所以要保证start/end位置的数据在数组中
                //即数组不可越界
                drawHorizontalSeatList(canvase, paint, beginDrawLeftX, beginDrawY, seatList, middleColumnNum - 1, 0 - 1);
                //画右边的座位
                drawHorizontalSeatList(canvase, paint, beginDrawRightX, beginDrawY, seatList, middleColumnNum + 1, seatList.length);
                //增加Y轴的绘制高度
                beginDrawY += mSeatParams.getSeatVerticalInterval() + mSeatParams.getSeatTotalHeight() / 2;
            }
        } else {
            //奇数列
        }
    }

    /**
     * 绘制一行水平的座位表,<font color="yellow"><b>向一个固定的方向绘制</b></font>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 该行座位开始绘制的第一个座位中心X轴位置
     * @param drawPositionY 该行座位开始绘制的第一个座位中心Y轴位置
     * @param seatList      单行座位列表
     * @param start         座位列表中开始绘制的索引,<font color="yellow"><b>此索引位置的座位将被绘制</b></font>
     * @param end           座位列表中最后绘制的索引,<font color="yellow"><b>此索引位置的座位将被绘制</b></font>
     */
    private void drawHorizontalSeatList(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, int[] seatList, int start, int end) {
        float beginDrawX = drawPositionX;
        //从大到小则为向左绘制,增量为负值-1
        //从小到大则为向右绘制,增量为正值+1
        int increment = (start - end) > 0 ? -1 : 1;
        //绘制座位
        if (seatList != null && seatList.length > 0) {
            try {
                int seatType = 0;
                int seatColor = SeatParams.DEFAULT_SEAT_COLOR;
                int i = start;
                do {
                    //获取对应位置座位的类型
                    seatType = seatList[i];
                    //获取座位类型对应的颜色
                    seatColor = mSeatParams.getSeatColorByType(seatType);
                    //设置绘制座位的颜色
                    mSeatParams.setSeatColor(seatColor);
                    //绘制单个座位
                    drawSeat(canvas, paint, beginDrawX, drawPositionY);
                    //计算下一个绘制座位的X轴位置
                    //由于此处绘制的座位是同一行的,所以仅X轴位置改变,Y轴位置不会改变
                    beginDrawX += increment * (mSeatParams.getSeatWidth() + mSeatParams.getSeatHorizontalInterval());
                    //通过增量改变下一个绘制座位的索引
                    i += increment;
                } while (i != end);
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }
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

        this.setStageParams(DEFAULT_FLOAT, DEFAULT_FLOAT, DEFAULT_FLOAT, Color.GREEN);
        drawY = mBeginDrawPositionY + mStageMarginTop + mStageHeight / 2;
        drawX = mBeginDrawPositionX + viewCenterX;
        drawStage(canvas, mPaint, drawX, drawY, "舞台");


        drawY = drawY + mStageHeight / 2 + mSeatParams.getSeatTotalHeight() / 2 + 20;
        drawX = mBeginDrawPositionX + viewCenterX;
        mSeatParams.setSeatColor(Color.BLACK);
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "锁定", 10f, false);

        mSeatParams.setSeatColor(Color.RED);
        drawX = mBeginDrawPositionX + viewCenterX - viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "可选", 10f, false);

        mSeatParams.setSeatColor(Color.YELLOW);
        drawX = mBeginDrawPositionX + viewCenterX + viewCenterX / 2;
        drawSeatWithNearText(canvas, mPaint, drawX, drawY, "已选", 10f, false);

        drawX = mBeginDrawPositionX + viewCenterX;
        drawY = drawY + mSeatParams.getSeatTotalHeight() / 2 + 30;
        drawSellSeats(canvas, mPaint, mSeatMap, drawX, drawY);
    }

}
