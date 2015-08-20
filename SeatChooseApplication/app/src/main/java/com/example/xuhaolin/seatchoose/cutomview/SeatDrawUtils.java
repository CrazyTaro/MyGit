package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/6.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xuhaolin in 2015-08-07
 * 座位绘制工具类,用于处理各种座位/舞台绘制的方法,并实现View默认的触摸处理事件
 */
public class SeatDrawUtils extends AbsTouchEventHandle {

    //座位参数
    private SeatParams mSeatParams = null;
    //舞台参数
    private StageParams mStageParams = null;

    private Paint mPaint = null;
    private PointF mWHPoint = null;
    //主座位,两者结合让座位看起来比较好看而已...
    private RectF mMainSeatRectf = null;
    //次座位,两者结合让座位看起来比较好看而已...
    private RectF mMinorSeatRectf = null;
    //图片座位位置
    private RectF mImageRectf = null;
    private ISeatInformationListener mISeatInformationListener = null;

    //实际界面绘制的高度
    private float mCanvasHeight = 0f;
    //实际界面绘制的宽度
    private float mCanvasWidth = 0f;
    //原始偏移量
    private float mOriginalOffsetX = 0f;
    private float mOriginalOffsetY = 0f;
    //用户触摸偏移量
    private boolean mIsCanvasMoving = false;
    private boolean mIsScaleRedraw = true;
    private float mBeginDrawOffsetY = 0f;
    private float mBeginDrawOffsetX = 0f;

    //多点触控缩放按下坐标
    private float mScaleFirstDownX = 0f;
    private float mScaleFirstDownY = 0f;
    private float mScaleSecondDownX = 0f;
    private float mScaleSecondDownY = 0f;
    private float mScaleFirstUpX = 0f;
    private float mScaleFirstUpY = 0f;
    private float mScaleSecondUpX = 0f;
    private float mScaleSecondUpY = 0f;
    //默认的缩放比例
    private float mLastScaleRate = 1f;
    //按下事件的坐标
    private float mDownX = 0f;
    private float mDownY = 0f;
    //抬起事件的坐标
    private float mUpX = 0f;
    private float mUpY = 0f;
    //是否绘制缩略图
    private boolean mIsDrawThumbnail = true;
    //是否已经移动过(满足移动条件)
    private boolean mIsMoved = false;
    //默认座位类型被绘制的行数
    private int mSeatTypeRowCount = 1;

    private Context mContext = null;
    //绑定的用于绘制界面的View
    private View mDrawView = null;
    //绘制方法
    private static SeatDrawUtils mInstance = null;
    //座位列表
    private int[][] mSeatMap = null;

    /**
     * 获取单一的实例,只有第一次初始化需要两个参数,其它时候获取该对象参数值可为null
     * <p><font color="yellow"><b>请注意此方法会直接设置绘制View的onTouch监听事件,如果不需要onTouch事件,请重新取消View的onTouch事件;
     * 如果需要同时处理自定义onTouch事件及使用本方法中的onTouch事件,请在自定义的事件中调用本方法中的onTouch事件以便处理</b></font></p>
     *
     * @param context  上下文对象
     * @param drawView 需要进行绘制的View,<font color="yellow"><b>建议使用自定义的View,且view为空白view即可</b></font>
     * @return
     */
    public static synchronized SeatDrawUtils getInstance(Context context, View drawView) {
        if (mInstance == null) {
            mInstance = new SeatDrawUtils(context, drawView);
        }
        return mInstance;
    }

    /**
     * 初始化对象
     *
     * @param context  上下文对象
     * @param drawView 需要进行绘制的View,用于绑定并将结果绘制在该View上
     */
    private SeatDrawUtils(Context context, View drawView) {
        if (context == null || drawView == null) {
            throw new RuntimeException("初始化中context及drawView参数不可为null,该参数都是必需的");
        }
        mContext = context;
        mDrawView = drawView;
        initial();
    }

    //初始化数据
    private void initial() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        //创建主座位与次要座位
        //此部分仅仅是让座位看起来比较好看而已....
        mMainSeatRectf = new RectF();
        mMinorSeatRectf = new RectF();
        mImageRectf = new RectF();

        mSeatParams = SeatParams.getInstance();
        mStageParams = StageParams.getInstance();

        //设置监听事件,实际事件分配来自于抽象父类
        mDrawView.setOnTouchListener(this);
    }

    /**
     * 设置座位绘制的数据,该二维表中的存放的应该为该位置的座位对应的座位类型,<font color="yellow"><b>此方法是将数据拷贝下来,修改数据请重新设置,不要在原引用数据上修改</b></font>
     * <p>此方法应该在View绘制前被调用,否则将获取不到绘制数据</p>
     *
     * @param seatMap 座位列表
     */
    public void setSeatDrawMap(int[][] seatMap) {
        if (seatMap != null && seatMap.length > 0) {
            mSeatMap = new int[seatMap.length][seatMap[0].length];
            for (int i = 0; i < seatMap.length; i++) {
                System.arraycopy(seatMap[i], 0, mSeatMap[i], 0, seatMap[i].length);
            }
        }
    }

    /**
     * 更新座位列表中的数据
     *
     * @param seatType    座位类型
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     * @return 更新成功返回true, 否则返回false
     */
    public boolean updateSeatTypeInMap(int seatType, int rowIndex, int columnIndex) {
        if (mSeatMap != null && rowIndex < mSeatMap.length && columnIndex < mSeatMap[0].length) {
            mSeatMap[rowIndex][columnIndex] = seatType;
            mDrawView.invalidate();
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取座位列表中某行某列的座位类型数据,可能返回{@link SeatParams#SEAT_TYPE_ERRO}(无效值)
     *
     * @param rowIndex    座位行索引
     * @param columnIndex 座位列索引
     * @return 返回该位置的座位类型, 若无法获取返回-1
     */
    public int getSeatTypeInSeatMap(int rowIndex, int columnIndex) {
        if (mSeatMap != null && rowIndex < mSeatMap.length && columnIndex < mSeatMap[0].length) {
            return mSeatMap[rowIndex][columnIndex];
        } else {
            return SeatParams.SEAT_TYPE_ERRO;
        }
    }

    /**
     * 获取座位列表数据
     *
     * @return
     */
    public int[][] getSeatDrawMap() {
        if (mSeatMap != null && mSeatMap.length > 0) {
            int[][] cloneSeatMap = new int[mSeatMap.length][mSeatMap[0].length];
            for (int i = 0; i < mSeatMap.length; i++) {
                System.arraycopy(mSeatMap[i], 0, cloneSeatMap[i], 0, mSeatMap[i].length);
            }
            return cloneSeatMap;
        } else {
            //虽然可能mSeatMap不为null,但只要他的行数不大于0,实际上也是没有数据
            return null;
        }
    }

    /**
     * 设置座位信息监听事件
     *
     * @param mInterface {@link ISeatInformationListener}
     */
    public void setSeatInformationListener(ISeatInformationListener mInterface) {
        mISeatInformationListener = mInterface;
    }

    /**
     * 重置座位绘制使用的参数(使用默认值)
     */
    public void resetSeatParams() {
        mSeatParams.resetSeatParams();
    }

    /**
     * 设置座位绘制使用的参数
     *
     * @param params 自定义座位绘制的参数
     * @deprecated
     */
    public void setSeatParams(SeatParams params) {
        if (params != null) {
            mSeatParams = params;
        }
    }

    /**
     * 设置舞台绘制使用的参数
     *
     * @param params 自定义舞台绘制的参数
     * @deprecated
     */
    public void setStageParams(StageParams params) {
        if (params != null) {
            mStageParams = params;
        }
    }

    /**
     * 重置舞台参数
     */
    public void resetStageParams() {
        mStageParams.resetStageParams();
    }

    /**
     * 设置是否绘制缩略图
     *
     * @param isDraw
     */
    public void setmIsDrawThumbnail(boolean isDraw) {
        this.mIsDrawThumbnail = isDraw;
    }

    /**
     * 获取View显示的宽高
     *
     * @return {@link PointF},返回point对象,point.x为宽,point.y为高
     */
    protected PointF getWidthAndHeight() {
        //获取view计算需要显示的宽高
        int width = mDrawView.getMeasuredWidth();
        int height = mDrawView.getMeasuredHeight();
        if (width <= 0 || height <= 0) {
            //无法取得计算结果则获取实际宽高
            width = mDrawView.getWidth();
            height = mDrawView.getHeight();
        }
        PointF WHPoint = new PointF();
        WHPoint.x = width;
        WHPoint.y = height;
        return WHPoint;
    }

    /**
     * 计算需要绘制的文字占用的宽度
     *
     * @param textSize 文字大小
     * @param drawText 绘制的文字内容,当该参数值为null时返回0
     * @return
     */
    protected float getTextLength(float textSize, String drawText) {
        if (drawText == null) {
            return 0;
        }
        mPaint.setTextSize(textSize);
        return mPaint.measureText(drawText);
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
     * @param seatType         座位类型
     * @param isDrawTextOfLeft 是否将文字绘制在座位的左边,<font color="yellow"><b>true为文字绘制在座位左边,false为文字绘制在座位右边</b></font>
     */
    protected void drawSeatWithNearText(Canvas canvas, Paint paint, float drawXPosition, float drawYPosition, String text, float interval, int seatType, boolean isDrawTextOfLeft) {
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
        if (text == null) {
            textLength = 0;
        } else {
            paint.setTextSize(mSeatParams.getSeatTypeTextSize());
            textLength = paint.measureText(text);
        }

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
        textBeginDrawY = drawYPosition + mSeatParams.getSeatTypeTextSize() / 3;
        seatCenterY = drawYPosition;

        if (text != null) {
            //绘制文字
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mSeatParams.getSeatTypeDescColor());
            canvas.drawText(text, textBeginDrawX, textBeginDrawY, paint);
        }
        if (mSeatParams.getSeatDrawType(false) == SeatParams.SEAT_DRAW_TYPE_IMAGE) {
            //绘制图片座位
            drawImageSeat(canvas, paint, seatCenterX, seatCenterY, seatType);
        } else {
            //绘制普通座位
            drawSeat(canvas, paint, seatCenterX, seatCenterY);
        }
    }

    /**
     * 图片方式绘制座位
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 座位绘制的中心X轴位置,centerX(参数值意义同{@link #drawSeat(Canvas, Paint, float, float)})
     * @param drawPositionY 座位绘制的中心Y轴位置,centerY(参数值意义同{@link #drawSeat(Canvas, Paint, float, float)})
     * @param seatType      座位类型,用于区分使用的座位图片
     */
    protected void drawImageSeat(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, int seatType) {
        mImageRectf = mSeatParams.getSeatDrawImageRecf(mImageRectf, drawPositionX, drawPositionY);
        //当图片范围可见时才进行绘制
        if (isRectfCanSeen(mImageRectf)) {
            Bitmap seatImage = mSeatParams.getSeatBitmapByType(mContext, seatType);
            if (seatImage != null) {
                //当获取到的座位图片不为空时才进行绘制
                canvas.drawBitmap(seatImage, null, mImageRectf, paint);
            }
        }
    }

    /**
     * 判断当前的矩形区域是否可见,<font color="yellow"><b>此方法仅可用于座位,因为舞台可能放大后不四个点都不在屏幕内,但屏幕内可见一部分的舞台</b></font>
     *
     * @param rectF 矩形区域
     * @return
     */
    protected boolean isRectfCanSeen(RectF rectF) {
        if (rectF != null) {
            PointF[] angles = new PointF[4];
            //左上角
            angles[0] = new PointF(rectF.left, rectF.top);
            //左下角
            angles[1] = new PointF(rectF.left, rectF.bottom);
            //右上角
            angles[2] = new PointF(rectF.right, rectF.top);
            //右下角
            angles[3] = new PointF(rectF.right, rectF.bottom);

            for (PointF point : angles) {
                //任何一个点在可见屏幕内即进行绘制
                if ((point.x > 0 && point.x <= mWHPoint.x) && (point.y > 0 && point.y <= mWHPoint.y)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    /**
     * 默认方式绘制座位
     * <p><font color="yellow"><b>该方法绘制座位时会根据seatParams确定是否需要对该座位进行绘制，请注意</b></font></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 座位绘制的中心X轴坐标
     * @param drawPositionY 座位绘制的中心Y轴坐标
     */
    protected void drawSeat(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY) {
        if (!mSeatParams.isDrawSeat()) {
            return;
        }
        //若当前的绘制类型是缩略图,则只绘制区域内的小方块作为座位显示(由于缩略图很小,没必要绘制很复杂,反正也看不清楚...)
        if (mSeatParams.getSeatDrawType(false) == SeatParams.SEAT_DRAW_TYPE_THUMBNAIL) {
            //获取绘制的区域
            mImageRectf = mSeatParams.getSeatDrawImageRecf(mImageRectf, drawPositionX, drawPositionY);
            paint.setStyle(Paint.Style.FILL);
            //填充颜色
            paint.setColor(mSeatParams.getSeatColor());
            //绘制圆角矩形
            canvas.drawRoundRect(mImageRectf, mSeatParams.getSeatRadius(), mSeatParams.getSeatRadius(), paint);
        } else {
            //座位的占拒的总高度(两部分座位)
            mMainSeatRectf = mSeatParams.getSeatDrawDefaultRectf(mMainSeatRectf, drawPositionX, drawPositionY, true);
            mMinorSeatRectf = mSeatParams.getSeatDrawDefaultRectf(mMinorSeatRectf, drawPositionX, drawPositionY, false);

            //填充座位并绘制
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mSeatParams.getSeatColor());


            //默认的绘制方式
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMainSeatRectf, mSeatParams.getSeatRadius(), mSeatParams.getSeatRadius(), paint);
            }
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMinorSeatRectf, mSeatParams.getSeatRadius(), mSeatParams.getSeatRadius(), paint);
            }
        }
    }

    /**
     * 绘制舞台及其文字,<font color="yellow"><b>此方法中舞台文字的大小为自动计算,文字大小由舞台高度决定</b></font>,若无法绘制返回drawPositionY,若绘制成功,返回的Y轴坐标位置将为一下次可直接绘制的坐标值(即已经将最后一行之后的间隔距离计算在内)
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 舞台绘制位置的中心X轴
     * @param drawPositionY 舞台绘制位置的中心Y轴
     * @return 返回绘制后下一个元素开始绘制的Y轴中心坐标, <font color="yellow"><b>若无法绘制返回drawPositionY,若绘制成功,返回的Y轴坐标位置将为一下次可直接绘制的坐标值(即已经将最后一行之后的间隔距离计算在内)</b></font>
     */
    protected float drawStage(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY) {
        if (mStageParams == null || !mStageParams.isDrawStage()) {
            return drawPositionY;
        }
        RectF stageRectf = new RectF();
        boolean isDrawImageStage = false;
        float textBeginDrawX = 0f;
        float textBeginDrawY = 0f;
        float textLength = 0f;
        //设置文字大小为舞台高度小一点(保证文字可以显示在舞台范围内)
        float textSize = mStageParams.getStageHeight() * 0.8f;

        stageRectf.left = drawPositionX - mStageParams.getStageWidth() / 2;
        stageRectf.right = stageRectf.left + mStageParams.getStageWidth();
        stageRectf.top = drawPositionY - mStageParams.getStageHeight() / 2;
        stageRectf.bottom = stageRectf.top + mStageParams.getStageHeight();

        if (mStageParams.getStageDrawType() == StageParams.STAGE_DRAW_TYPE_IMAGE) {
            //绘制图片舞台
            mStageParams.loadStageImage(mContext, false);
            Bitmap bitmap = mStageParams.getStageImage();
            if (bitmap != null) {
                canvas.drawBitmap(mStageParams.getStageImage(), null, stageRectf, paint);
                //绘制成功
                isDrawImageStage = true;
            }
        }
        //没有绘制过图片舞台,尝试默认绘制方式
        if (!isDrawImageStage) {
            //绘制默认舞台
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mStageParams.getStageColor());
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (stageRectf.left > 0 || stageRectf.right > 0 || stageRectf.top > 0 || stageRectf.bottom > 0) {
                canvas.drawRoundRect(stageRectf, mStageParams.getStageRadius(), mStageParams.getStageRadius(), paint);
            }
        }

        //绘制舞台文字
        String stageText = mStageParams.getStageText();
        if (stageText != null) {
            paint.setTextSize(textSize);
            paint.setColor(mStageParams.getStageTextColor());
            paint.setStyle(Paint.Style.FILL);

            textLength = paint.measureText(stageText);
            textBeginDrawX = stageRectf.centerX() - textLength / 2;
            textBeginDrawY = stageRectf.centerY() + textSize / 3;
            //文字绘制不做限制
            //文字本身绘制范围小,另外需要重新计算两个参数才能进行判断
            canvas.drawText(stageText, textBeginDrawX, textBeginDrawY, paint);
        }

        float nextDrawBeginY = drawPositionY + +mStageParams.getStageHeight() / 2 + mStageParams.getStageMarginBottom();
        if (mSeatParams != null && mSeatParams.isDrawSeatType()) {
            nextDrawBeginY += mSeatParams.getSeatHeight() / 2;
        }
        return nextDrawBeginY;
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
    protected void drawSellSeats(Canvas canvase, Paint paint, int[][] seatMap, float drawPositionX, float drawPositionY) {
        if (seatMap == null || seatMap.length <= 0) {
            return;
        }
        //列数
        int columnLength = seatMap[0].length;
        int leftBeginColumn = 0;
        int rightBeginColumn = 0;
        float beginDrawLeftX = 0f;
        float beginDrawRightX = 0f;
        float beginDrawY = 0f;

        if ((columnLength & 0x1) == 0) {
            //偶数列
            //向左边绘制X轴开始点
            beginDrawLeftX = drawPositionX - mSeatParams.getSeatHorizontalInterval() / 2 - mSeatParams.getSeatWidth() / 2;
            //向右边绘制X轴开始点
            beginDrawRightX = drawPositionX + mSeatParams.getSeatHorizontalInterval() / 2 + mSeatParams.getSeatWidth() / 2;
            //此处从中心位置的列数开始计算
            //向左开始绘制列数，当前指定索引列将被绘制
            //因此索引数为列数-1
            leftBeginColumn = columnLength / 2 - 1;
            //向右开始绘制列数，当前指定索引列将被绘制
            rightBeginColumn = leftBeginColumn + 1;
        } else {
            //奇数列
            //向左边绘制X轴开始点
            //包含绘制中心的座位
            beginDrawLeftX = drawPositionX;
            //向右边绘制X轴开始点
            //不包含绘制中心的座位（否则中心位置的座位将被重绘）
            beginDrawRightX = drawPositionX + mSeatParams.getSeatHorizontalInterval() + mSeatParams.getSeatWidth();
            //此处从中心位置的列数开始计算
            //向左开始绘制列数，当前指定索引列将被绘制
            //奇数列，中心位置的列由向左绘制完成，因此不需要索引值-1
            leftBeginColumn = columnLength / 2;
            //向右开始绘制列数，当前指定索引列将被绘制
            rightBeginColumn = leftBeginColumn + 1;
        }
        beginDrawY = drawPositionY;
        for (int i = 0; i < seatMap.length; i++) {
            //画左边的座位
            //绘制的时候由start位置到end位置
            //从start到end每一个位置都会绘制,所以要保证start位置的数据在数组中,end位置的数据不会被绘制
            //即数组不可越界
            drawHorizontalSeatList(canvase, paint, beginDrawLeftX, beginDrawY, seatMap[i], leftBeginColumn, 0 - 1, i);
            //画右边的座位
            drawHorizontalSeatList(canvase, paint, beginDrawRightX, beginDrawY, seatMap[i], rightBeginColumn, seatMap[i].length, i);
            //增加Y轴的绘制高度
            beginDrawY += mSeatParams.getSeatVerticalInterval() + mSeatParams.getSeatHeight();
        }

        if (!mSeatParams.getIsDrawThumbnail()) {
            //记录界面高度值
            mCanvasHeight = beginDrawY - this.getDrawBeginY()
                    //附加部分,用于边界的空白
                    + mSeatParams.getSeatHeight();
        }
    }

    private void drawTitleColumn(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, int columnCount) {
        //TODO:绘制列数
    }

    /**
     * 绘制一行水平的座位表,<font color="yellow"><b>向一个固定的方向绘制</b></font>
     *
     * @param canvas          画板
     * @param paint           画笔
     * @param drawPositionX   该行座位开始绘制的第一个座位中心X轴位置
     * @param drawPositionY   该行座位开始绘制的第一个座位中心Y轴位置
     * @param seatList        单行座位列表
     * @param start           座位列表中开始绘制的索引,<font color="yellow"><b>此索引位置的座位将被绘制</b></font>
     * @param end             座位列表中最后绘制的索引,<font color="yellow"><b>此索引位置的座位不被绘制</b></font>
     * @param currentRowIndex 当前绘制座位的行索引,用于绘制对应的行数,<font color="yellow"><b>不需要绘制行数请使用负值</b></font>
     */
    protected void drawHorizontalSeatList(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, int[] seatList, int start, int end, int currentRowIndex) {
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
                    //设置座位是否需要被绘制
                    mSeatParams.setIsDrawSeat(seatType);
                    //绘制单个座位
                    if (mSeatParams.getSeatDrawType(false) == SeatParams.SEAT_DRAW_TYPE_IMAGE) {
                        drawImageSeat(canvas, paint, beginDrawX, drawPositionY, seatType);
                    } else {
                        drawSeat(canvas, paint, beginDrawX, drawPositionY);
                    }
                    //计算下一个绘制座位的X轴位置
                    //由于此处绘制的座位是同一行的,所以仅X轴位置改变,Y轴位置不会改变
                    beginDrawX += increment * (mSeatParams.getSeatWidth() + mSeatParams.getSeatHorizontalInterval());
                    //通过增量改变下一个绘制座位的索引
                    i += increment;
                } while (i != end);
                //当前不是绘制缩略图
                //当前行数有效且绘制的方向为从右向左绘制时(即绘制到最开始的座位)
                //尝试绘制当前的行数
                if (!mSeatParams.getIsDrawThumbnail() && currentRowIndex >= 0 && increment < 0) {
                    //增大行数与座位之间的间隔
                    beginDrawX += increment * mSeatParams.getSeatWidth();
                    paint.setTextSize((float) (mSeatParams.getSeatHeight() * 0.8));
                    paint.setColor(mSeatParams.getSeatTypeDescColor());
                    paint.setStyle(Paint.Style.FILL);
                    //调整文字显示的Y轴坐标
                    float drawTextY = drawPositionY + mSeatParams.getSeatTypeTextSize() / 3;
                    canvas.drawText(String.valueOf((currentRowIndex + 1)), beginDrawX, drawTextY, paint);

                    //计算新的界面宽度值
                    //此处只在这个地方计算是因为在最左边还需要绘制一列行数文字
                    float newCanvasWidth =
                            //最左边边界到中心X轴位置的宽度(即总宽度的一半)
                            (Math.abs(beginDrawX - this.getDrawCenterX(mWHPoint.x))
                                    //附加边界空白
                                    + mSeatParams.getSeatWidth()) * 2;
                    //记录新的界面宽度值,保存最大值
                    mCanvasWidth = newCanvasWidth > mCanvasWidth ? newCanvasWidth : mCanvasWidth;
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 自动计算并绘制座位类型及其邻近文字的方法,该方法不适用于可移动界面
     * <p><font color="yellow">由于是自动计算,所以移动界面后座位类型位置还是不变,所以不推荐使用(特别需要情况下可用),但绘制的结果是保证适应屏幕界面的</font></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionY 开始绘制的座位类型中心Y轴位置,centerY
     * @deprecated 该方法可用, 但不建议使用, 在界面移动中无法达到预期效果
     */
    protected void drawAutoExampleSeatType(Canvas canvas, Paint paint, float drawPositionY) {
        if (mSeatParams == null) {
            return;
        }
        int seatTypeCount = mSeatParams.getSeatTypeArrary().length;
        float viewWidth = mDrawView.getWidth();
        float seatTextInterval = mSeatParams.getSeatTypeDescInterval();
        float eachSeatTypeDrawWidth = viewWidth / seatTypeCount;
        float drawPositionX = eachSeatTypeDrawWidth / 2;
        String[] seatTypeDesc = mSeatParams.getSeatTypeDescription();
        String text = null;
        for (int i = 0; i < seatTypeCount; i++) {
            int seatType = mSeatParams.getSeatTypeArrary()[i];
            int seatColor = mSeatParams.getSeatColorByType(seatType);
            mSeatParams.setIsDrawSeat(seatType);
            mSeatParams.setSeatColor(seatColor);
            if (seatTypeDesc != null) {
                text = mSeatParams.getSeatTypeDescription()[i];
            } else {
                text = null;
            }
            drawSeatWithNearText(canvas, paint, drawPositionX, drawPositionY, text, seatTextInterval, seatType, false);
            drawPositionX += eachSeatTypeDrawWidth;
        }
    }

    /**
     * 根据指定的行数自动计算并绘制座位类型及其描述文字,将所有的座位类型按指定行数分开绘制,并返回绘制后的Y轴坐标位置,
     * <font color="yellow"><b>若无法绘制返回drawPositionY,若绘制成功,返回的Y轴坐标位置将为一下次可直接绘制的坐标值(即已经将最后一行之后的间隔距离计算在内)</b></font>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionY 开始绘制的Y轴坐标的中心位置,centerY
     * @param rowCount      预定需要绘制的行数
     * @return 返回绘制后的Y轴坐标位置, <font color="yellow"><b>若无法绘制返回drawPositionY,若绘制成功,返回的Y轴坐标位置将为一下次可直接绘制的坐标值(即已经将最后一行之后的间隔距离计算在内)</b></font>
     */

    protected float drawSeatTypeByAuto(Canvas canvas, Paint paint, float drawPositionY, int rowCount) {
        if (mSeatParams == null || rowCount <= 0 || !mSeatParams.isDrawSeatType()) {
            return drawPositionY;
        }
        //保证原始的数据,座位类型/座位颜色/座位描述/座位图片ID/座位图片Bitmap
        int[] originSeatTypeArr = mSeatParams.getSeatTypeArrary();
        int[] originSeatTypeColorArr = mSeatParams.getSeatColorArrary();
        String[] originSeatTypeDesc = mSeatParams.getSeatTypeDescription();
        int[] originSeatImageIDArr = mSeatParams.getSeatImageIDByCopy();
        Bitmap[] originSeatImageBimapArr = mSeatParams.getSeatImageBitmapByCopy();
        //不存在座位类型,则直接返回,不进行绘制
        if (originSeatTypeArr == null || originSeatTypeArr.length <= 0) {
            return drawPositionY;
        }
        //获取所有的座位类型总数
        int totalSeatTypeCount = originSeatTypeArr.length;
        //计算每一行需要绘制的座位数量
        int eachRowSeatTypeCount = totalSeatTypeCount / rowCount;
        //若计算结果为0,说明不足一行,按一行绘制
        if (eachRowSeatTypeCount == 0) {
            drawSingleRowExampleSeatType(canvas, paint, drawPositionY);
            drawPositionY += mSeatParams.getSeatHeight() + mSeatParams.getSeatVerticalInterval();
            //记录绘制的座位类型行数
            mSeatTypeRowCount = 1;
        } else {
            //记录绘制的座位类型行数
            mSeatTypeRowCount = rowCount;
            //否则是多行绘制
            //按每行处理的座位数量创建新的数据,座位类型/座位颜色/座位描述/座位图片ID/座位图片Bitmap
            int[] newSeatTypeArr = new int[eachRowSeatTypeCount];
            String[] newSeatTypeDescArr = new String[eachRowSeatTypeCount];
            int[] newSeatTypeColorArr = null;
            int[] newSeatImageIdArr = null;
            Bitmap[] newSeatImageBitmapArr = null;
            if (originSeatTypeColorArr != null) {
                newSeatTypeColorArr = new int[eachRowSeatTypeCount];
            }
            if (originSeatImageIDArr != null) {
                newSeatImageIdArr = new int[eachRowSeatTypeCount];
            }
            if (originSeatImageBimapArr != null) {
                newSeatImageBitmapArr = new Bitmap[eachRowSeatTypeCount];
            }

            //绘制每一行
            for (int i = 0; i < rowCount; i++) {
                //当前行是否最后一行
                //若是则进行特殊处理
                //由于座位类型数量奇偶数是不确定的,所以有可能每行数量绘制会忽略最后一个类型
                //因此最后一行为除去以上所有已绘制行的数量,即为最后一行的座位数量
                if (i + 1 == rowCount) {
                    //创建新的数据容器(原本的数据容器可能数据量不同)
                    newSeatTypeArr = new int[totalSeatTypeCount - eachRowSeatTypeCount * i];
                    newSeatTypeDescArr = new String[totalSeatTypeCount - eachRowSeatTypeCount * i];
                }

                //正常情况下处理每一行绘制的数量及情况
                //记录通用的类型与描述(此两部分为不论哪种绘制方式都是需要的)
                for (int j = 0; j < newSeatTypeArr.length; j++) {
                    //同理处理通用的数据
                    newSeatTypeArr[j] = originSeatTypeArr[eachRowSeatTypeCount * i + j];
                    newSeatTypeDescArr[j] = originSeatTypeDesc[eachRowSeatTypeCount * i + j];
                }
                //分类处理特殊的数据
                if (mSeatParams.getSeatDrawType(true) == SeatParams.SEAT_DRAW_TYPE_IMAGE) {
                    for (int j = 0; j < eachRowSeatTypeCount; j++) {
                        //加载图片资源ID
                        if (originSeatImageIDArr != null) {
                            newSeatImageIdArr[j] = originSeatImageIDArr[eachRowSeatTypeCount * i + j];
                        }
                        //加载图片资源
                        if (originSeatImageBimapArr != null) {
                            newSeatImageBitmapArr[j] = originSeatImageBimapArr[eachRowSeatTypeCount * i + j];
                        }
                    }
                } else {
                    ///加载座位类型颜色
                    if (newSeatTypeColorArr != null) {
                        for (int j = 0; j < newSeatTypeArr.length; j++) {
                            newSeatTypeColorArr[j] = originSeatTypeColorArr[eachRowSeatTypeCount * i + j];
                        }
                    }
                }
                //将新数据填充到原座位参数中
                if (mSeatParams.getSeatDrawType(true) == SeatParams.SEAT_DRAW_TYPE_IMAGE) {
                    //填充数据必须注意,imageID及imageBitmap不允许对象为null
                    if (newSeatImageIdArr != null) {
                        mSeatParams.setSeatTypeWithImage(newSeatTypeArr, newSeatImageIdArr);
                    }
                    if (newSeatImageBitmapArr != null) {
                        mSeatParams.setSeatTypeWithImage(newSeatTypeArr, newSeatImageBitmapArr);
                    }
                } else {
                    mSeatParams.setAllSeatTypeWithColor(newSeatTypeArr, newSeatTypeColorArr, newSeatTypeDescArr);
                }
                //绘制当前行的座位参数
                drawSingleRowExampleSeatType(canvas, paint, drawPositionY);
                //Y轴坐标移动增量,用于下一行的绘制
                drawPositionY += mSeatParams.getSeatHeight() + mSeatParams.getSeatVerticalInterval();
            }
        }
        //绘制完毕将原始数据填充回座位参数中
        if (mSeatParams.getSeatDrawType(true) == SeatParams.SEAT_DRAW_TYPE_IMAGE) {
            if (originSeatImageIDArr != null) {
                mSeatParams.setSeatTypeWithImage(originSeatTypeArr, originSeatImageIDArr);
            }
            if (originSeatImageBimapArr != null) {
                mSeatParams.setSeatTypeWithImage(originSeatTypeArr, originSeatImageBimapArr);
            }
        } else {
            mSeatParams.setAllSeatTypeWithColor(originSeatTypeArr, originSeatTypeColorArr, originSeatTypeDesc);
        }
        return drawPositionY;
    }

    /**
     * 绘制单行的座位类型及其描述文字
     * <p><font color="yellow"><b>此方法绘制时以参数设置固定的座位类型间的间隔为基准进行绘制,不保证绘制结果会完全适应屏幕</b></font></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionY 座位绘制的中心Y轴位置
     */
    protected void drawSingleRowExampleSeatType(Canvas canvas, Paint paint, float drawPositionY) {
        if (mSeatParams == null) {
            //若座位参数为null,则不作任何绘制
            return;
        }
        //座位类型的个数
        int seatTypeCount = mSeatParams.getSeatTypeArrary().length;
        //当前view的宽度
        float viewWidth = mDrawView.getWidth();
        //座位的宽度
        float seatWidth = mSeatParams.getSeatWidth();
        //座位与邻近文字的距离
        float seatTextInterval = mSeatParams.getSeatTypeDescInterval();
        //座位类型之前的距离,每个座位(此处包含文字)绘制之间的距离
        float seatTypeInterval = mSeatParams.getSeatTypeInterval();
        //开始绘制的位置
        //此处的开始绘制是指第一个座位类型在当前行中绘制的中心位置
        float drawPositionX = 0f;
        //所有绘制座位的文字总长度
        //用于后面计算开始绘制的位置
        float totalTextLength = 0f;
        //所有座位类型的总长度
        //此处的座位包含了其邻近的文字
        float allSeatTypeWidth = 0f;
        //座位类型对应的描述文字数组
        String[] seatTypeDesc = mSeatParams.getSeatTypeDescription();


        if (seatTypeDesc != null) {
            for (String text : seatTypeDesc) {
                //计算所有文字长度
                totalTextLength += this.getTextLength(mSeatParams.getSeatTypeTextSize(), text);
            }
        } else {
            totalTextLength = 0f;
        }
        //计算所有座位(包含文字及座位之前的间隔)长度
        allSeatTypeWidth = totalTextLength + (mSeatParams.getSeatWidth()
                + mSeatParams.getSeatTypeDescInterval()) * seatTypeCount
                + mSeatParams.getSeatTypeInterval() * (seatTypeCount - 1);
        String firstText = seatTypeDesc == null ? null : seatTypeDesc[0];
        float firstSeatTypeTotalWidth = seatWidth + seatTypeInterval + this.getTextLength(mSeatParams.getSeatTypeTextSize(), firstText);
        //计算开始绘制的X轴位置
        drawPositionX = this.getDrawCenterX(mWHPoint.x)
                - allSeatTypeWidth / 2
                //以上为第一个座位类型开始绘制的X轴坐标,需要的是第一个座位绘制的X轴中心坐标，因此需要计算第一个座位(包括文字)占用的宽度的一半
                //此时才为X轴的中心绘制的坐标
                + firstSeatTypeTotalWidth / 2;

        String drawText = null;
        //当前绘制文字的长度
        float textLength = 0f;
        //下一个可能绘制文字的长度
        float nextTextLength = 0f;
        for (int i = 0; i < seatTypeCount; i++) {
            //设置座位类型及颜色
            int seatType = mSeatParams.getSeatTypeArrary()[i];
            int seatColor = mSeatParams.getSeatColorByType(seatType);
            mSeatParams.setIsDrawSeat(seatType);
            mSeatParams.setSeatColor(seatColor);
            //加载座位类型描述文字及计算其长度
            if (seatTypeDesc != null) {
                drawText = mSeatParams.getSeatTypeDescription()[i];
                textLength = this.getTextLength(mSeatParams.getSeatTypeTextSize(), drawText);
            } else {
                drawText = null;
                textLength = 0f;
            }
            //绘制座位及文字
            drawSeatWithNearText(canvas, paint, drawPositionX, drawPositionY, drawText, seatTextInterval, seatType, false);

            //获取并计算下一个绘制文字的长度
            //用于计算下一个绘制座位的X轴中心位置
            int nextTextIndex = i + 1;
            if (nextTextIndex < seatTypeCount && seatTypeDesc != null) {
                nextTextLength = this.getTextLength(mSeatParams.getSeatTypeTextSize(), seatTypeDesc[nextTextIndex]);
            } else {
                nextTextLength = 0;
            }
            //计算下一个绘制座位的X轴中心位置
            drawPositionX += (seatWidth + seatTypeInterval + textLength) / 2
                    + (seatWidth + seatTypeInterval + nextTextLength) / 2;
        }
    }

    /**
     * 设置初始绘制的XY轴偏移量,默认偏移量是0,且绘制的原始位置在视图的X轴中心,Y轴的最上方(centerX,topY)
     *
     * @param beginDrawPositionX X轴初始偏移量
     * @param begindrawPositionY Y轴初始偏移量
     */

    public void setOriginalOffset(float beginDrawPositionX, float begindrawPositionY) {
        mOriginalOffsetX = beginDrawPositionX;
        mOriginalOffsetY = begindrawPositionY;
    }

    /**
     * 获取舞台开始绘制的Y轴位置
     *
     * @return
     */
    protected float getDrawBeginY() {
        //初始偏移量
        float beginDrawCenterY = mOriginalOffsetY
                //用户可能进行移动的偏移量
                + mBeginDrawOffsetY;
        //当绘制缩略图时,由于缩略图固定在左上角
        //所以不需要任何的原始偏移量或者是用户产生的偏移量
        //用户的偏移量影响不在缩略图上表现出来
        //而是在缩略图的显示区域中表现出来
        if (mSeatParams.getIsDrawThumbnail()) {
            beginDrawCenterY = 0f;
        }
        if (mStageParams.isDrawStage()) {
            //舞台与顶端的距离
            beginDrawCenterY += mStageParams.getStageMarginTop()
                    //舞台的自身高度一半
                    //此处返回的是舞台实际需要绘制的位置的Y轴中心坐标
                    //因此需要返回舞台自身ymyar一半
                    + mStageParams.getStageHeight() / 2;
        } else {
            if (mSeatParams.isDrawSeatType()) {
                //不绘制舞台需要绘制座位类型
                //舞台底端与座位类型的高度
                beginDrawCenterY += mStageParams.getStageMarginBottom()
                        //当前座位绘制的高度的一半
                        + mSeatParams.getSeatHeight() / 2;
            } else {
                //即不绘制舞台也不绘制座位类型
                //每行座位垂直方向的间隔高度
                beginDrawCenterY += mSeatParams.getSeatVerticalInterval();
            }
        }
        return beginDrawCenterY;
    }

    /**
     * 获取缩略图的宽,默认为屏幕宽的1/3
     *
     * @return
     */
    protected float getThumbnailWidth() {
        if (mWHPoint == null) {
            mWHPoint = this.getWidthAndHeight();
        }
        return mWHPoint.x / 3;
    }

    /**
     * 获取显示区域的高度
     *
     * @param originalCanvasWidth 主界面(非缩略图)的实际界面宽度,<font color="yellow"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @return
     */
    protected float getShowRectfBeginY(float originalCanvasWidth) {
        //用户可能进行移动的偏移量
        //取绝对值是在缩略图中垂直方向的偏移量必须是正数
        return Math.abs(mBeginDrawOffsetY * (this.getThumbnailWidth() / originalCanvasWidth));
    }

    /**
     * 获取显示区域中心X轴位置
     *
     * @param originalCanvasWidth 主界面(非缩略图)的实际界面宽度,<font color="yellow"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @return
     */
    protected float getShowRectfCenterX(float originalCanvasWidth) {
        //此处要注意的是,在实际界面的偏移量中,当向左移动时偏移量是正值
        //向右移动时偏移量是负值
        //而缩略图是完整并一直显示在屏幕上的
        //所以向左移动偏移量应该是负值(相对中心X轴位置来说)
        //向右移动偏移量应该是正值,刚好与实际情况相反,所以要注意此处使用了-1
//        return (mBeginDrawOffsetX * -1 + mWHPoint.x) * (this.getThumbnailWidth() / originalCanvasWidth);

        mSeatParams.setIsDrawThumbnail(false, SeatParams.DEFAULT_FLOAT, SeatParams.DEFAULT_FLOAT);
        float canvasCenterX = this.getDrawCenterX(mWHPoint.x);
        float centerXDistance = mWHPoint.x / 2 - canvasCenterX;

        mSeatParams.setIsDrawThumbnail(true, SeatParams.DEFAULT_FLOAT, SeatParams.DEFAULT_FLOAT);
        float thumbnailCenterX = this.getDrawCenterX(mWHPoint.x);
        return thumbnailCenterX + centerXDistance * (this.getThumbnailWidth() / originalCanvasWidth);
    }

    /**
     * 获取在缩略图中显示当前屏幕所在区域的矩形区域
     *
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="yellow"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @param originalCanvasHeight 主界面的实际界面高度,同上
     * @return 返回显示区域
     */
    protected RectF getShowRectfInThumbnail(float originalCanvasWidth, float originalCanvasHeight) {
        //获取当前缩略图的实际大小
        float targetWidth = this.getThumbnailWidth();
        //计算缩略图与实际界面的缩放比
        float thumbnailRate = targetWidth / originalCanvasWidth;
        float targetHeight = thumbnailRate * originalCanvasHeight;
        //计算屏幕宽占用总界面宽的比例
        float widthRate = 0f;
        //计算屏幕高战胜总界面高的比例
        float heightRate = 0f;

        //实际界面本身宽小于屏幕宽时,在缩略图中显示的区域宽则与缩略图的宽相同
        //因为缩略图中的显示区域表示的就是屏幕
        if (originalCanvasWidth < mWHPoint.x) {
            widthRate = 1;
        } else {
            widthRate = mWHPoint.x / originalCanvasWidth;
        }
        //高同上
        if (originalCanvasHeight < mWHPoint.y) {
            heightRate = 1;
        } else {
            heightRate = mWHPoint.y / originalCanvasHeight;
        }


        //创建显示区域
        RectF showRectf = new RectF();
        showRectf.top = this.getShowRectfBeginY(originalCanvasWidth);
        showRectf.bottom = showRectf.top + targetHeight * heightRate;
        //获取当前显示区域的中心X轴位置
        showRectf.left = this.getShowRectfCenterX(originalCanvasWidth)
                //显示区域的宽度的一半
                - (targetWidth * widthRate / 2);
        showRectf.right = showRectf.left + targetWidth * widthRate;
        return showRectf;
    }


    /**
     * 开始绘制缩略图并初始化部分工作
     * <p><font color="yellow"><b>缩略图的绘制大小由view宽度决定,一般为view宽度的1/3,即宽度保持不变地占用了控件宽度的1/3,此参数可进行设置,但不建议更改</b></font></p>
     *
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="yellow"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @param originalCanvasHeight 主界面的实际界面高度,同上
     * @return 返回缩略图绘制需要占用的空间大小
     */
    protected RectF beginDrawThumbnail(float originalCanvasWidth, float originalCanvasHeight) {
        if (mWHPoint == null) {
            mWHPoint = this.getWidthAndHeight();
        }
        //获取缩略图实际绘制的宽度
        float targetWidth = this.getThumbnailWidth();
        //计算缩放比
        float thumbnailRate = targetWidth / originalCanvasWidth;
        //设置绘制缩略图标志
        mSeatParams.setIsDrawThumbnail(true, originalCanvasWidth, targetWidth);
        mStageParams.setIsDrawThumbnail(true, originalCanvasWidth, targetWidth);
        //创建缩略图绘制区域
        RectF thumbnailRectf = new RectF();
        thumbnailRectf.top = 0;
        thumbnailRectf.left = 0;
        thumbnailRectf.bottom = thumbnailRectf.top + originalCanvasHeight * thumbnailRate;
        thumbnailRectf.right = thumbnailRectf.left + targetWidth;
        return thumbnailRectf;
    }

    /**
     * 完成绘制缩略图的工作
     */
    protected void finishDrawThumbnail() {
        //取消缩略图的绘制标志
        //防止下一次更新界面无法绘制出正常的界面
        mSeatParams.setIsDrawThumbnail(false, SeatParams.DEFAULT_FLOAT, SeatParams.DEFAULT_FLOAT);
        mStageParams.setIsDrawThumbnail(false, StageParams.DEFAULT_FLOAT, StageParams.DEFAULT_FLOAT);
    }

    /**
     * 获取绘制的中心X轴位置坐标,所有的绘制元素默认开始的绘制位置都是在X轴的中心位置(Y轴重新计算)
     *
     * @param viewWidth
     * @return
     */
    protected float getDrawCenterX(float viewWidth) {
        if (mSeatParams.getIsDrawThumbnail()) {
            return this.getThumbnailWidth() / 2;
        } else {
            return mOriginalOffsetX + mBeginDrawOffsetX + viewWidth / 2;
        }
    }

    /**
     * 绘制之前进行的数据初始化或者必须的数据处理
     */
    protected void beginDraw() {
        this.mCanvasWidth = 0f;
        this.mCanvasHeight = 0f;
    }

    /**
     * 完成绘制之后调用的方法,用于处理部分重置的变量等
     */
    protected void finishDraw() {
        this.mIsScaleRedraw = false;
    }

    /**
     * 界面绘制,该方法提供给View调用,view.invaliate本身也是重新调用此方法进行绘制
     *
     * @param canvas view画板
     */
    public void onDraw(Canvas canvas) {
        if (mWHPoint == null) {
            mWHPoint = getWidthAndHeight();
        }
        this.beginDraw();

        drawNormalCanvas(canvas, mPaint, mWHPoint.y);
        //判断当前是否需要显示缩略图
        //当界面基本在当前屏幕范围内时,不显示缩略图
        if (mCanvasWidth > (mWHPoint.x + 30f) || mCanvasHeight > (mWHPoint.y + 30f)) {
            //当界面在移动中时显示缩略图,否则不显示缩略图
            drawThumbnail(canvas, mPaint, mCanvasWidth, mCanvasHeight);
        }

        this.finishDraw();
    }

    /**
     * 获取绘制中心轴虚线的坐标线点
     *
     * @param centerX 中心X轴位置
     * @param height  此次绘制区域的高度,对实际界面而言是屏幕,对缩略图而言是缩略图的高度
     * @return
     */
    protected float[] getCenterDotLine(float centerX, float height) {
        //判断是否绘制缩略图,是则将线段长缩短为1/10
        float lineLength = mSeatParams.getIsDrawThumbnail() ? 2f : 20f;
        //计算需要的线段数
        int lineCount = ((int) (height / lineLength) + 1) / 2;
        //线段偏移量(即没有绘制的线段总长度)
        float offsetDistance = 0;
        float[] dotLine = new float[4 * lineCount];
        //动态创建绘制点
        for (int i = 0; i < lineCount; i++) {
            dotLine[4 * i + 0] = centerX;
            dotLine[4 * i + 1] = offsetDistance + i * lineLength;
            dotLine[4 * i + 2] = centerX;
            dotLine[4 * i + 3] = offsetDistance + (i + 1) * lineLength;
            offsetDistance += lineLength;
        }
        return dotLine;
    }

    /**
     * 绘制正常的界面
     *
     * @param canvas     画板
     * @param paint      画笔
     * @param viewHeight 绘制界面的高度(主要用于绘制中心轴虚线可用),实际绘制界面高度为屏幕高度
     */
    protected void drawNormalCanvas(Canvas canvas, Paint paint, float viewHeight) {
        float drawX = 0f;
        float drawY = 0f;
        //开始绘制舞台
        drawY = this.getDrawBeginY();
        drawX = this.getDrawCenterX(mWHPoint.x);
        drawY = drawStage(canvas, paint, drawX, drawY);
        //开始绘制座位类型
        drawY = drawSeatTypeByAuto(canvas, paint, drawY, 1);
        //开始绘制普通出售座位
        drawSellSeats(canvas, paint, mSeatMap, drawX, drawY);

        float[] dotline = this.getCenterDotLine(drawX, viewHeight);
        paint.setColor(Color.RED);
        //根据界面要求高度绘制虚线
        canvas.drawLines(dotline, paint);
    }

    /**
     * 绘制缩略图
     *
     * @param canvas               画板
     * @param paint                画笔
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="yellow"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @param originalCanvasHeight 主界面的实际高度,同上
     */
    protected void drawThumbnail(Canvas canvas, Paint paint, float originalCanvasWidth, float originalCanvasHeight) {
        //若不需要绘制缩略图,则不绘制
        if (!mIsDrawThumbnail) {
            return;
        }
        RectF thumbnailRectf = this.beginDrawThumbnail(originalCanvasWidth, originalCanvasHeight);
        RectF showRecf = this.getShowRectfInThumbnail(originalCanvasWidth, originalCanvasHeight);

        //绘制缩略图底色(半透明黑色)
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(150);
        canvas.drawRect(thumbnailRectf, paint);
        paint.setAlpha(0);

        //绘制缩略图
        drawNormalCanvas(canvas, paint, thumbnailRectf.height());

        //绘制当前选区框
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);
        canvas.drawRect(showRecf, paint);

        this.finishDrawThumbnail();
    }

    /**
     * 获取单击位置的座位索引值
     *
     * @param clickPositionX     单击位置的X轴位置
     * @param clickPositionY     单击位置的Y轴位置
     * @param beginDrawPositionY 出售座位开始绘制的坐标,<font color="yellow"><b>此处的坐标是指第一行开始绘制的出售座位的Y轴坐标,即top而不是centerY</b></font>
     *                           <p>在实际中应该是除去舞台的高度(包括其间隔占用的高度)及座位类型(同理包括其间隔占用的高度)的高度</p>
     * @param seatColumnCount    座位列数,<font color="yellow"><b>列数,在二维表中应该是table[0].length</b></font>
     * @param seatRowCount       座位行数,<font color="yellow"><b>行数,在二维表中应该是table.length</b></font>
     * @return {@link Point},返回座位在表中对应的行列索引值,若单击点不在有效区域则返回null
     */
    protected Point getClickSeatByPosition(float clickPositionX, float clickPositionY, float beginDrawPositionY, int seatColumnCount, int seatRowCount) {
        //计算列的索引
        int clickColumn = calculateColumnIndex(clickPositionX, seatColumnCount);
        //计算行的索引
        int clickRow = calculateRowIndex(clickPositionY, beginDrawPositionY, seatRowCount);
        showMsg("click", "row = " + clickRow + "/column = " + clickColumn);
        //座位有效则进行处理
        if (clickColumn != -1 && clickRow != -1) {
            //返回的座位对应的索引值点
            Point clickSeatIndex = new Point();
            clickSeatIndex.x = clickRow;
            clickSeatIndex.y = clickColumn;
            return clickSeatIndex;
        } else {
            //否则返回null
            return null;
        }
    }

    /**
     * 计算行索引(从高度计算),通过计算当前的Y轴坐标与实际的绘制的普通座位的高度的间隔计算行数
     *
     * @param clickPositionY     单击位置相对的Y轴坐标
     * @param beginDrawPositionY 实际出售座位开始绘制的高度
     * @param seatRowCount       座位表中行数
     * @return 返回计算得到的行索引值, 当不存在时返回-1
     */
    protected int calculateRowIndex(float clickPositionY, float beginDrawPositionY, int seatRowCount) {
        //计算当前单击位置与开始绘制位置的距离
        //用于后面计算在哪一行
        float clickYInterval = clickPositionY - beginDrawPositionY;
        //上一次计算距离结果
        float lastYInterval = clickYInterval;
        //当前计算距离结果
        float currentYInterval = clickYInterval;
        //当前模拟单击到的行索引
        int clickRow = 0;
        //增量
        //由于计算从第一行开始,所以行肯定只向下面的方向进行搜索
        //所以增量不存在两种情况,此处选择负值作为处理
        int increament = -1;
        //计算出的初始距离小于0,说明当前单击位置在第一行座位的上方,不可能会选中有效区域,直接返回不存在
        if (clickYInterval < 0) {
            return -1;
        }


        //重复计算
        for (; ; ) {
            //当前行值已经超过座位表行总数或不在合理范围内,直接返回不存在
            if (clickRow < 0 || clickRow >= seatRowCount) {
                return -1;
            }
            //计算当前的距离
            //此处使用的间隔区域是座位的有效高度(有效区域)
            currentYInterval += increament * mSeatParams.getSeatHeight();
            //对单击位置进行判断是否在当前的距离范围内
            if (isClickedInInterval(clickYInterval, lastYInterval, currentYInterval)) {
                //在该座位区域内返回有效值
                return clickRow;
            } else {
                //不在该区域内则尝试继续计算
                lastYInterval = currentYInterval;
            }
            //再次计算当前的距离
            //此处使用的间隔区域是座位之间的间隔高度(无效区域)
            currentYInterval += increament * mSeatParams.getSeatVerticalInterval();
            if (isClickedInInterval(clickYInterval, lastYInterval, currentYInterval)) {
                //此处与上面不一样的原因是因为
                //在座位绘制过程中分两个部分,一个是座位的绘制(座位有效高度)
                //一个是座位之间的间隔(无效高度)
                //当单击的位置在当前的无效间隔中时,虽然该单击是有效的,但是并没有选中任何有效的区域(即座位)
                //因此返回不存在
                //此时已经查询到单击的位置,所以不需要再查询计算下去,直接返回不存在
                return -1;
            } else {
                //不在该区域内则继续计算
                lastYInterval = currentYInterval;
            }
            //一轮计算结束,行数增1
            //一轮计算以一个座位的高度及其邻近一个间隔距离为单位
            clickRow++;
        }
    }

    /**
     * 计算列索引(从宽度计算),通过计算当前的坐标值与实际绘制的X轴中心位置开始的坐标之间的间隔计算出当前的列数
     *
     * @param clickPositionX  单击位置的X轴坐标
     * @param seatColumnCount 座位表中列数
     * @return 返回计算得到的列索引值, 当不存在时返回-1
     */
    protected int calculateColumnIndex(float clickPositionX, int seatColumnCount) {
        //获取界面开始绘制的中心X轴位置
        //整个界面的绘制是从中心位置的X轴开始的,此处的X轴包括偏移后的X轴
        float centerX = this.getDrawCenterX(mWHPoint.x);
        //计算单击位置X轴与中心X轴位置
        float centerXInterval = clickPositionX - centerX;
        //上一次距离
        float lastXInterval = centerXInterval;
        //当前距离
        float currentXInterval = centerXInterval;
        //模拟当前被单击的列索引
        int clickColumn = 0;
        //增量
        //增量由单击位置的X坐标与中心位置的X坐标的距离关系决定
        //当单击位置在中心X轴的左边时,增量为正
        //反之,增量为负
        int increament = centerXInterval > 0 ? -1 : 1;
        //判断列数奇偶进行必要的处理
        if ((seatColumnCount & 0x1) == 0) {
            //偶数列
            if (centerXInterval > 0) {
                //当前单击位置在中心位置的右边
                //从中心位置的右边的位置开始计算
                clickColumn = seatColumnCount / 2;
            } else {
                //当前单击位置在中心位置的左边
                //从中心位置的左边的位置开始计算
                clickColumn = seatColumnCount / 2 - 1;
            }
            //去除中心位置两个座位的间隔的一半
            //到达中座位单击位置方向的第一个座位开始的位置
            currentXInterval += increament * mSeatParams.getSeatHorizontalInterval() / 2;
            //判断是否在区域间隔内(此部分为无效区域)
            //由于偶数列中心位置并不存在座位,所以单击点在此处则该部分为无效区域
            //直接返回不存在
            if (isClickedInInterval(centerXInterval, lastXInterval, currentXInterval)) {
                return -1;
            } else {
                lastXInterval = currentXInterval;
            }
            //计算当前距离
            currentXInterval += increament * (mSeatParams.getSeatWidth());
        } else {
            //奇数列
            //取中间的列数索引
            clickColumn = seatColumnCount / 2;
            //计算去除中心位置的座位的宽度的一半
            lastXInterval = centerXInterval;
            currentXInterval += increament * mSeatParams.getSeatWidth() / 2;
        }

        //重复计算
        for (; ; ) {
            //当前列索引不合理,直接返回不存在
            if (clickColumn >= seatColumnCount || clickColumn < 0) {
                return -1;
            }
            //判断单击点所在的区域
            //同行计算原理,见方法 calculateRowIndex
            if (isClickedInInterval(centerXInterval, lastXInterval, currentXInterval)) {
                return clickColumn;
            } else {
                lastXInterval = currentXInterval;
            }
            currentXInterval += increament * mSeatParams.getSeatHorizontalInterval();
            if (isClickedInInterval(centerXInterval, lastXInterval, currentXInterval)) {
                return -1;
            } else {
                lastXInterval = currentXInterval;
            }
            currentXInterval += increament * mSeatParams.getSeatWidth();
            //根据增量计算新的列索引
            clickColumn += increament * -1;
        }
    }

    /**
     * 判断单击点是否在当前的指定的区域间隔内
     *
     * @param originalInterval 原始的单击点与默认点的距离,此参数仅使用其正负值做一个处理标志，数值本身并没有实际作用
     * @param lastInterval     上一次检测过的区域间隔
     * @param currentInterval  当前需要被检测的区域间隔
     * @return 单击点在当前的区域内返回true, 否则返回false;
     */
    protected boolean isClickedInInterval(float originalInterval, float lastInterval, float currentInterval) {
        //单击点在默认点的右方或者下方
        if (originalInterval > 0) {
            //上一次指定点与单击点的区域间隔为正(单击点还在上一个指定点的右边)
            //当前指定点与单击点的区域间隔为负(单击点在当前指定点的左边)
            //因此当前指定点与上一次指定点之间的间隔区域(即currentInterval)包含了单击点
            //返回true
            if (lastInterval >= 0 && currentInterval <= 0) {
                return true;
            } else {
                //其它任何情况都不足以说明单击点在当前指定的区域内,返回false
                return false;
            }
        } else {
            //单击点在默认点的左方或者上方
            //同理见上说明
            if (lastInterval < 0 && currentInterval > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 多点触摸的重绘,是否重绘由实际缩放的比例决定
     *
     * @param newScaleRate   新的缩放比例,该比例可能为1(通常情况下比例为1不缩放,没有意义)
     * @param isTrueSetValue 是否将此次缩放结果永久性记录为新的原始数据
     */
    protected void invalidateInMultiPoint(float newScaleRate, boolean isTrueSetValue) {
        //当前后的缩放比与上一次缩放比相同时不进行重绘,防止反复多次地重绘..
        //如果是最后一次(up事件),除非是不能绘制,否则必定重绘并记录缩放比
        if (newScaleRate == 1 && !isTrueSetValue) {
            return;
        }
        //是否缩放成功
        //任何一个缩放不成功都不可以进行重绘,舞台与座位的缩放应该是同步的
        if (mSeatParams.isCanScale(newScaleRate) && mStageParams.isCanScale(newScaleRate)) {
            mLastScaleRate = newScaleRate;
        } else if (isTrueSetValue) {
            //若缩放比不合法且当前缩放为最后一次缩放(up事件),则将上一次的缩放比作为此次的缩放比,用于记录数据
            //且将最后的缩放比设为1(因为已经达到缩放的上限,再缩放也不会改变,所以比例使用1)
            //此处不作此操作会导致在缩放的时候达到最大值后放手,再次缩放会在最开始的时候复用上一次缩放的结果(有闪屏的效果...)
            newScaleRate = mLastScaleRate;
            mLastScaleRate = 1;
        } else {
            showMsg("已达到缩放的最大值");
            return;
        }

        //设置缩放的数据
        //最后一次缩放比为1时,其实与原界面是相同的
        mSeatParams.setScaleRate(newScaleRate, isTrueSetValue);
        mStageParams.setScaleRate(newScaleRate, isTrueSetValue);
        //开始缩放的重绘的标志
        mIsScaleRedraw = true;
        mDrawView.invalidate();
    }

    /**
     * 根据移动的距离计算是否重新绘制
     *
     * @param moveDistanceX X轴方向的移动距离(可负值)
     * @param moveDistanceY Y轴方向的移动距离(可负值)
     */

    protected boolean invalidateInSinglePoint(float moveDistanceX, float moveDistanceY) {
        //此处做大于5的判断是为了避免在检测单击事件时
        //有可能会有很微小的变动,避免这种情况下依然会造成移动的效果
        if (Math.abs(moveDistanceX) > 5 || Math.abs(moveDistanceY) > 5) {

            //新的偏移量
            float newDrawPositionX = mBeginDrawOffsetX + moveDistanceX;
            float newDrawPositionY = mBeginDrawOffsetY + moveDistanceY;
            //新的开始绘制的界面中心X轴坐标
            float newStartDrawCenterX = newDrawPositionX + mOriginalOffsetX + mWHPoint.x / 2;
            //新的开始绘制的界面最顶端
            float newStartDrawY = newDrawPositionY + mOriginalOffsetY;

            //当前绘制的最左边边界坐标大于0(即边界已经显示在屏幕上时),且移动方向为向右移动
            if (((newStartDrawCenterX - mCanvasWidth / 2) > 0 && moveDistanceX > 0)
                    //当前绘制的最右边边界坐标小于view宽度(即边界已经显示在屏幕上),且移动方向 为向左移动
                    || ((newStartDrawCenterX + mCanvasWidth / 2) < mWHPoint.x && moveDistanceX < 0)) {
                //保持原来的偏移量不变
                newDrawPositionX = mBeginDrawOffsetX;
            }
            //当前绘制的顶端坐标大于0且移动方向为向下移动
            if ((newStartDrawY > 0 && moveDistanceY > 0)
                    //当前绘制的最底端坐标大于view高度且移动方向向上移动时
                    || ((newStartDrawY + mCanvasHeight) < mWHPoint.y && moveDistanceY < 0)) {
                //保持原来的Y轴偏移量
                newDrawPositionY = mBeginDrawOffsetY;
            }

            //其它情况正常移动重绘
            //当距离确实有效地改变了再进行重绘制,否则原界面不变,减少重绘的次数
            if (newDrawPositionX != mBeginDrawOffsetX || newDrawPositionY != mBeginDrawOffsetY) {
                mBeginDrawOffsetX = newDrawPositionX;
                mBeginDrawOffsetY = newDrawPositionY;
                mDrawView.invalidate();
                //一旦重绘了则进行重绘标志,并通知处理事件
                mIsMoved = true;
                if (mISeatInformationListener != null) {
                    mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_MOVE);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void singleTouchEventHandle(MotionEvent event, int extraMotionEvent) {
        //单点触控
        int action = event.getAction();
        //用于记录此处事件中新界面与旧界面之间的相对移动距离
        float moveDistanceX = 0f;
        float moveDistanceY = 0f;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //特别处理额外的事件
                //此处处理的事件是在单击事件并移动中用户进行了多点触摸
                //此时尝试结束进行移动界面,并将当前移动的结果固定下来作为新的界面(效果与直接抬起结束触摸相同)
                //并不作任何操作(因为在单击后再进行多点触摸无法分辨需要进行处理的事件是什么)
                if (extraMotionEvent == MotionEvent.ACTION_UP) {
                    mDownX = 0f;
                    mDownY = 0f;
                    mUpX = 0f;
                    mUpY = 0f;
                    mIsMoved = false;
                    return;
                }

                mUpX = event.getX();
                mUpY = event.getY();
                moveDistanceX = mUpX - mDownX;
                moveDistanceY = mUpY - mDownY;
                //此次移动加数据量达到足够的距离触发移动事件
                //若数据量太小无法满足移动事件的处理,不重置此次的数据留到下一次再使用
                if (invalidateInSinglePoint(moveDistanceX, moveDistanceY)) {
                    //重置移动操作完的数据,以防出现不必要的错误
                    mDownX = event.getX();
                    mDownY = event.getY();
                }
                mUpX = 0f;
                mUpY = 0f;
                break;
            case MotionEvent.ACTION_UP:
                mUpX = event.getX();
                mUpY = event.getY();
                moveDistanceX = mUpX - mDownX;
                moveDistanceY = mUpY - mDownY;

                invalidateInSinglePoint(moveDistanceX, moveDistanceY);
                if (!mIsMoved) {
                    if (mISeatInformationListener != null) {
                        mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CLICK);
                    }
                    //界面没有被移动过,则处理为单击事件
                    float clickPositionX = event.getX();
                    float clickPositionY = event.getY();
                    //计算当前绘制出售座位开始绘制的Y轴坐标位置(在原始界面)
                    float beginDrawPositionY = getSellSeatsBeginDrawY(mSeatTypeRowCount);
                    if (mSeatMap != null && mSeatMap.length > 0) {
                        //计算单击位置的座位索引值
                        Point clickSeatPoint = getClickSeatByPosition(clickPositionX, clickPositionY, beginDrawPositionY, mSeatMap[0].length, mSeatMap.length);
                        //计算得到的座位索引值在有效范围内
                        //行索引应该<=列表行数
                        //列索引应该<=列表列数
                        if (clickSeatPoint != null && clickSeatPoint.x < mSeatMap.length && clickSeatPoint.y < mSeatMap[0].length) {
                            if (mISeatInformationListener != null) {
                                //更新座位状态
                                mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CHOOSE_SEAT);
                                //回调选中的座位
                                mISeatInformationListener.chooseSeatSuccess(clickSeatPoint.x, clickSeatPoint.y);
                            }
                        } else {
                            if (mISeatInformationListener != null) {
                                mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CHOOSE_NOTHING);
                                mISeatInformationListener.chosseSeatFail();
                            }
                        }
                    } else {
                        //列表数据不合法,直接返回失败
                        if (mISeatInformationListener != null) {
                            mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CHOOSE_NOTHING);
                            mISeatInformationListener.chosseSeatFail();
                        }
                    }
                }
                //移动操作完把数据还原初始状态,以防出现不必要的错误
                mDownX = 0f;
                mDownY = 0f;
                mUpX = 0f;
                mUpY = 0f;
                mIsMoved = false;
                break;
        }
    }

    @Override
    public void doubleTouchEventHandle(MotionEvent event, int extraMotionEvent) {
        //使用try是为了防止获取系统的触摸点坐标失败
        //该部分可能为系统的问题
        try {
            int action = event.getAction();
            float newScaleRate = 0f;
            switch (action & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_POINTER_DOWN:
                    mScaleFirstDownX = event.getX(0);
                    mScaleFirstDownY = event.getY(0);
                    mScaleSecondDownX = event.getX(1);
                    mScaleSecondDownY = event.getY(1);

                    break;
                case MotionEvent.ACTION_MOVE:
                    mScaleFirstUpX = event.getX(0);
                    mScaleFirstUpY = event.getY(0);
                    mScaleSecondUpX = event.getX(1);
                    mScaleSecondUpY = event.getY(1);

                    newScaleRate = this.getScaleRate(mScaleFirstDownX, mScaleFirstDownY, mScaleSecondDownX, mScaleSecondDownY,
                            mScaleFirstUpX, mScaleFirstUpY, mScaleSecondUpX, mScaleSecondUpY);
                    invalidateInMultiPoint(newScaleRate, false);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mScaleFirstUpX = event.getX(0);
                    mScaleFirstUpY = event.getY(0);
                    mScaleSecondUpX = event.getX(1);
                    mScaleSecondUpY = event.getY(1);

                    newScaleRate = this.getScaleRate(mScaleFirstDownX, mScaleFirstDownY, mScaleSecondDownX, mScaleSecondDownY,
                            mScaleFirstUpX, mScaleFirstUpY, mScaleSecondUpX, mScaleSecondUpY);
                    invalidateInMultiPoint(newScaleRate, true);

                    mScaleFirstDownX = 0;
                    mScaleFirstDownY = 0;
                    mScaleSecondDownX = 0;
                    mScaleSecondDownY = 0;
                    mScaleFirstUpX = 0;
                    mScaleFirstUpY = 0;
                    mScaleSecondUpX = 0;
                    mScaleSecondUpY = 0;
                    break;
            }
        } catch (IllegalArgumentException e) {
        }
    }

    @Override
    public void singleClick() {

    }

    @Override
    public void doubleClick() {

    }

    /**
     * 根据坐标值计算需要缩放的比例,<font color="yellow"><b>返回值为移动距离与按下距离的商,move/down</b></font>
     *
     * @param firstDownX  多点触摸按下的pointer_1_x
     * @param firstDownY  多点触摸按下的pointer_1_y
     * @param secondDownX 多点触摸按下的pointer_2_x
     * @param secondDownY 多点触摸按下的pointer_2_y
     * @param firstUpX    多点触摸抬起或移动的pointer_1_x
     * @param firstUpY    多点触摸抬起或移动的pointer_1_y
     * @param secondUpX   多点触摸抬起或移动的pointer_2_x
     * @param secondUpY   多点触摸抬起或移动的pointer_2_y
     * @return
     */
    protected float getScaleRate(float firstDownX, float firstDownY, float secondDownX, float secondDownY,
                                 float firstUpX, float firstUpY, float secondUpX, float secondUpY) {
        //计算平方和
        double downDistance = Math.pow(Math.abs((firstDownX - secondDownX)), 2) + Math.pow(Math.abs(firstDownY - secondDownY), 2);
        double upDistance = Math.pow(Math.abs((firstUpX - secondUpX)), 2) + Math.pow(Math.abs(firstUpY - secondUpY), 2);
        //计算比例
        double newRate = Math.sqrt(upDistance) / Math.sqrt(downDistance);
        //计算与上一个比例的差
        //差值超过阀值则使用该比例,否则返回原比例
        if (newRate > 0.02 && newRate < 10) {
            //保存当前的缩放比为上一次的缩放比
            return (float) newRate;
        }
        return 1;
    }

    /**
     * 获取实际出售座位开始绘制的Y轴顶端坐标,top
     * <p><font color="yellow"><b><p>此处必须注意的地方是,这里的Y轴坐标是指top,而不是绘制位置的中心centerY</b></font></p>
     * <p><font color="yellow">此外,这里的Y轴坐标是原始绘制界面的坐标,而不是移动后的坐标(即在第一次绘制时把该坐标记录返回即可)</font></p>
     *
     * @param seatTypeRowCount 座位类型绘制了几行,<font color="yellow"><b>此处有这个参数是因为前面设计座位类型是可以被反复绘制多次的</b></font>,
     *                         这种情况是为了解决当座位类型很多时(如可能有5个以上的座位类型),则一行可能绘制不了或者会造成绘制结果不清晰,因此座位允许自定义选择绘制行数,
     *                         用户可以自主拆分座位类型并分批进行绘制
     * @return
     */
    protected float getSellSeatsBeginDrawY(int seatTypeRowCount) {
        float beginDrawY = this.getDrawBeginY();
        if (mStageParams.isDrawStage()) {
            beginDrawY += mStageParams.getStageHeight() / 2 + mStageParams.getStageMarginBottom();
        }
        if (mSeatParams.isDrawSeatType()) {
            beginDrawY += (mSeatParams.getSeatHeight() + mSeatParams.getSeatVerticalInterval()) * seatTypeRowCount - mSeatParams.getSeatHeight() / 2;
        }
        return beginDrawY;
    }

    /**
     * 座位信息监听接口
     */
    public interface ISeatInformationListener {
        /**
         * 当前座位状态,处理座位移动事件
         */
        public static final int STATUS_MOVE = 1;
        /**
         * 当前座位状态,处理座位区域单击事件,<font color="yellow"><b>事件为单击事件且在座位区域内,但座位此时不一定被选中单击,可能单击在无效区域</b></font>
         */
        public static final int STATUS_CLICK = 2;
        /**
         * 当前座位状态,处理座位被单击事件,<font color="yellow"><b>座位已被选中</b></font>
         */
        public static final int STATUS_CHOOSE_SEAT = 3;
        /**
         * 当前座位状态,处理座位区域被单击事件,<font color="yellow"><b>座位空白区域被选中,无任何座位被选中</b></font>
         */
        public static final int STATUS_CHOOSE_NOTHING = 4;

        /**
         * 当前座位区域的状态
         * <p>
         * <li>{@link #STATUS_MOVE},座位移动</li>
         * <li>{@link #STATUS_CLICK},座位区域被单击</li>
         * <li>{@link #STATUS_CHOOSE_SEAT},座位被选中</li>
         * <li>{@link #STATUS_CHOOSE_NOTHING},座位未被选中,单击在空白区域</li>
         * </p>
         *
         * @param status 座位状态
         */
        public void seatStatus(int status);

        /**
         * 选择座位成功,即选中某个座位
         *
         * @param rowIndex    座位在列表中的行索引
         * @param columnIndex 座位在列表中的列索引
         */
        public void chooseSeatSuccess(int rowIndex, int columnIndex);

        /**
         * 选择座位失败
         */
        public void chosseSeatFail();
    }
}
