package us.bestapp.henrytaro.draw.utils;/**
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

import us.bestapp.henrytaro.draw.interfaces.ISeatDrawHandle;
import us.bestapp.henrytaro.draw.interfaces.ISeatInformationListener;
import us.bestapp.henrytaro.entity.interfaces.IRowEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatMapEntity;
import us.bestapp.henrytaro.params.BaseParams;
import us.bestapp.henrytaro.params.GlobleParams;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.StageParams;
import us.bestapp.henrytaro.params.interfaces.IDrawSeatParams;
import us.bestapp.henrytaro.params.interfaces.IDrawStageParams;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;

/**
 * @author xuhaolin
 * @version 6.0
 *          <p/>
 *          Created by xuhaolin in 2015-08-07
 *          <p/>
 *          <p>座位绘制工具类,用于处理各种座位/舞台绘制的方法,并实现View默认的触摸处理事件</p>
 *          <p><font color="#ff9900"><b>如果需要定制特殊的绘制,可以继承此类,通过组合提供的{@code protected}绘制方法可以更加灵活地处理</b></font>,
 *          如果通过继承此类完成定制的绘制时,需要注意重写部分方法以达到正确的绘制结果</p>
 *          <p><b>此类默认的绘制流程如下:使用参数{@link StageParams}在顶端中心位置绘制舞台,并以舞台为起点,依次向下绘制座位类型{@code seatType}及普通座位{@code sellSeat};
 *          基本座位类型及普通座位参数均来自于{@link SeatParams}</b>,
 *          当自定义绘制时需要更改舞台及座位绘制流程等时,请注意重写部分方法,<font color="#ff9900">此类型方法将以{@code @since}标注</font></p>
 *          <br/>
 *          <p>所有{@code protected}方法都是绘制时需要的,对外公开可以进行设置的方法只允许从{@code public}方法中进行设置</p>
 */
public class SeatDrawUtils extends AbsTouchEventHandle implements ISeatDrawHandle {
    private IGlobleParams mGlobleParams = null;
    //座位参数
    private IDrawSeatParams mSeatParams = null;
    //舞台参数
    private IDrawStageParams mStageParams = null;

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
    //用于暂时存放在开始移动缩放前的X/Y偏移量
    private PointF mOffsetPoint = new PointF();
    //当前选中的座位行列值
    private Point mCurrentSeletedSeat = new Point();
    //是否第一次存放偏移量
    private boolean mIsFirstStorePoint = false;
    //默认的缩放比例
    private float mLastScaleRate = 1f;
    //按下事件的坐标
    private float mDownX = 0f;
    private float mDownY = 0f;
    //抬起事件的坐标
    private float mUpX = 0f;
    private float mUpY = 0f;

    //是否已经移动过(满足移动条件)
    private boolean mIsMoved = false;

    private Context mContext = null;
    //绑定的用于绘制界面的View
    private View mDrawView = null;
    //座位列表
    private ISeatMapEntity mSeatDrawMap = null;

    private boolean mIsDrawSeletedRowColumn = false;

    /**
     * 构造函数,设置此绘制类绑定的view并设置用于绘制的座位参数及舞台参数对象;
     * 当参数seat/stage为null时,会尝试创建默认的参数作为初始参数使用
     *
     * @param context  上下文对象
     * @param drawView 需要进行绘制的View,用于绑定并将结果绘制在该View上
     * @param seat     座位参数，可为null
     * @param stage    舞台参数，可为null
     * @param globle   全局参数,此参数不可为null
     */
    public SeatDrawUtils(Context context, View drawView, IDrawSeatParams seat, IDrawStageParams stage, IGlobleParams globle) {
        if (context == null || drawView == null || globle == null) {
            throw new RuntimeException("初始化中context及drawView,全局参数globle不可为null,该参数都是必需的");
        }
        mContext = context;
        mDrawView = drawView;
        mSeatParams = seat;
        mStageParams = stage;
        mGlobleParams = globle;
        initial();
    }

    /**
     * 构造函数,设置此绘制类绑定的view并设置用于绘制的座位参数及舞台参数对象;
     * 同时使用默认的绘制参数进行绘制详见{@link #SeatDrawUtils(Context, View, IDrawSeatParams, IDrawStageParams, IGlobleParams)}
     *
     * @param context  上下文对象
     * @param drawView 需要进行绘制的View,用于绑定并将结果绘制在该View上
     */
    public SeatDrawUtils(Context context, View drawView) {
        this(context, drawView, new SeatParams(), new StageParams(), new GlobleParams());
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

        //设置监听事件,实际事件分配来自于抽象父类
        mDrawView.setOnTouchListener(this);
    }


    @Override
    public void setSeatInformationListener(ISeatInformationListener mInterface) {
        mISeatInformationListener = mInterface;
    }

    @Override
    public ISeatParams getSeatParams() {
        return this.mSeatParams;
    }

    @Override
    public IStageParams getStageParams() {
        return this.mStageParams;
    }

    @Override
    public IGlobleParams getGlobleParams() {
        return this.mGlobleParams;
    }

    @Override
    public void resetParams() {
        this.mSeatParams = new SeatParams();
        this.mStageParams = new StageParams();
        this.mGlobleParams = new GlobleParams();
    }

    @Override
    public void setOriginalOffset(float beginDrawPositionX, float begindrawPositionY) {
        mOriginalOffsetX = beginDrawPositionX;
        mOriginalOffsetY = begindrawPositionY;
    }

    @Override
    public void setIsShowLog(boolean isShowLog, String tag) {
        super.setIsShowLog(isShowLog, tag);
    }

    /**
     * 获取View显示的宽高
     *
     * @return {@link PointF},返回point对象,point.x为宽,point.y为高
     */
    protected PointF getWidthAndHeight() {
        //获取view计算需要显示的宽高
        int width = mDrawView.getWidth();
        int height = mDrawView.getHeight();
        if (width <= 0 || height <= 0) {
            //无法取得计算结果则获取实际宽高
            width = mDrawView.getMeasuredWidth();
            height = mDrawView.getMeasuredHeight();
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
     * @param params           座位类型绘制的参数
     * @param drawXPosition    座位与文字绘制位置的<font color="#ff9900"><b>X轴中心(仅提供需要绘制到的X轴中心坐标即可)</b></font>
     * @param drawYPosition    座位与文字绘制位置的<font color="#ff9900"><b>Y轴中心(仅提供需要绘制到的Y轴中心坐标即可)</b></font>
     * @param text             需要绘制的文字
     * @param interval         文字与座位之前的间隔
     * @param seatType         座位类型
     * @param isDrawTextOfLeft 是否将文字绘制在座位的左边,<font color="#ff9900"><b>true为文字绘制在座位左边,false为文字绘制在座位右边</b></font>
     */
    protected void drawSeatWithNearText(Canvas canvas, Paint paint, IDrawSeatParams params, float drawXPosition, float drawYPosition, String text, float interval, int seatType, boolean isDrawTextOfLeft) {
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
            paint.setTextSize(params.getDescriptionSize());
            textLength = paint.measureText(text);
        }

        if (isDrawTextOfLeft) {
            //将文字绘制在座位的左边
            //计算座位绘制的中心X轴位置
            seatCenterX = drawXPosition + interval / 2 + params.getWidth() / 2;
            //计算文字开始绘制的X轴位置
            textBeginDrawX = drawXPosition - interval / 2 - textLength;
        } else {
            //将文字绘制在座位的右边
            seatCenterX = drawXPosition - interval / 2 - params.getWidth() / 2;
            textBeginDrawX = drawXPosition + interval / 2;
        }
        //计算文字的Y轴位置(由于文字只考虑绘制在左边和右边,所以文字的绘制高度是不会受影响的)
        textBeginDrawY = drawYPosition + params.getDescriptionSize() / 3;
        seatCenterY = drawYPosition;

        if (text != null) {
            //绘制文字
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(params.getDescriptionColor());
            canvas.drawText(text, textBeginDrawX, textBeginDrawY, paint);
        }
        if (params.getDrawType(true) == BaseParams.DRAW_TYPE_IMAGE) {
            //绘制图片座位
            drawImageSeat(canvas, paint, mSeatParams, seatCenterX, seatCenterY, seatType);
        } else {
            //绘制普通座位
            drawSeat(canvas, paint, params, seatCenterX, seatCenterY, seatType);
        }
    }

    /**
     * 图片方式绘制座位
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param params        座位绘制的参数
     * @param drawPositionX 座位绘制的中心X轴位置,centerX(参数值意义同{@link #drawSeat(Canvas, Paint, IDrawSeatParams, float, float, int)})
     * @param drawPositionY 座位绘制的中心Y轴位置,centerY(参数值意义同{@link #drawSeat(Canvas, Paint, IDrawSeatParams, float, float, int)})
     * @param seatType      座位类型,用于区分使用的座位图片
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected void drawImageSeat(Canvas canvas, Paint paint, IDrawSeatParams params, float drawPositionX, float drawPositionY, int seatType) {
        mImageRectf = params.getSeatDrawImageRecf(mImageRectf, drawPositionX, drawPositionY);
        //当图片范围可见时才进行绘制
        if (isRectfCanSeen(mImageRectf)) {
            Bitmap seatImage = params.getSeatBitmapByType(mContext, seatType);
            if (seatImage != null) {
                //当获取到的座位图片不为空时才进行绘制
                canvas.drawBitmap(seatImage, null, mImageRectf, paint);
            }
        }
    }

    /**
     * 判断当前的矩形区域是否可见,<font color="#ff9900"><b>此方法仅可用于座位,因为舞台可能放大后不四个点都不在屏幕内,但屏幕内可见一部分的舞台</b></font>
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
     * 绘制缩略图座位
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param seatParams    绘制参数
     * @param drawPositionX 座位绘制的中心X轴坐标
     * @param drawPositionY 座位绘制的中心Y轴坐标
     * @param seatType      座位类型
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected void drawThumbnailSeat(Canvas canvas, Paint paint, IDrawSeatParams seatParams, float drawPositionX, float drawPositionY, int seatType) {
        //绘制单个座位
        //若当前的绘制类型是缩略图,则只绘制区域内的小方块作为座位显示(由于缩略图很小,没必要绘制很复杂,反正也看不清楚...)
        //获取座位类型对应的颜色
        int seatColor = seatParams.getSeatColorByType(seatType);
        //设置绘制座位的颜色
        seatParams.setColor(seatColor);

        //获取绘制的区域
        mImageRectf = seatParams.getSeatDrawImageRecf(mImageRectf, drawPositionX, drawPositionY);
        paint.setStyle(Paint.Style.FILL);
        //填充颜色
        paint.setColor(seatParams.getColor());
        if (isRectfCanSeen(mImageRectf)) {
            //绘制圆角矩形
            canvas.drawRoundRect(mImageRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
    }

    /**
     * 绘制普通座位
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param seatParams    绘制参数
     * @param drawPositionX 座位绘制的中心X轴坐标
     * @param drawPositionY 座位绘制的中心Y轴坐标
     * @param seatType      座位类型
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected void drawNormalSeat(Canvas canvas, Paint paint, IDrawSeatParams seatParams, float drawPositionX, float drawPositionY, int seatType) {
        int seatColor = seatParams.getSeatColorByType(seatType);
        //设置绘制座位的颜色
        seatParams.setColor(seatColor);

        //座位的占拒的总高度(两部分座位)
        mMainSeatRectf = seatParams.getSeatDrawDefaultRectf(mMainSeatRectf, drawPositionX, drawPositionY, true);
        mMinorSeatRectf = seatParams.getSeatDrawDefaultRectf(mMinorSeatRectf, drawPositionX, drawPositionY, false);

        //填充座位并绘制
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(seatParams.getColor());


        //默认的绘制方式
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (isRectfCanSeen(mMainSeatRectf)) {
            canvas.drawRoundRect(mMainSeatRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (isRectfCanSeen(mMainSeatRectf)) {
            canvas.drawRoundRect(mMinorSeatRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
    }

    /**
     * 默认方式绘制座位
     * <p><font color="#ff9900"><b>该方法绘制座位时会根据seatParams及seatType确定是否需要对该座位进行绘制，请注意</b></font></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param seatParams    绘制参数
     * @param drawPositionX 座位绘制的中心X轴坐标
     * @param drawPositionY 座位绘制的中心Y轴坐标
     * @param seatType      座位类型
     */
    protected void drawSeat(Canvas canvas, Paint paint, IDrawSeatParams seatParams, float drawPositionX, float drawPositionY, int seatType) {
        if (seatParams == null) {
            return;
        }
        seatParams.setIsDraw(seatType);
        if (!seatParams.getIsDraw()) {
            return;
        }

        if (mSeatParams.getDrawType(false) == BaseParams.DRAW_TYPE_IMAGE) {
            //绘制图片类型
            drawImageSeat(canvas, paint, mSeatParams, drawPositionX, drawPositionY, seatType);
        } else if (seatParams.getDrawType(false) == BaseParams.DRAW_TYPE_THUMBNAIL) {
            //若当前的绘制类型是缩略图,则只绘制区域内的小方块作为座位显示(由于缩略图很小,没必要绘制很复杂,反正也看不清楚...)
            //绘制缩略图
            drawThumbnailSeat(canvas, paint, seatParams, drawPositionX, drawPositionY, seatType);
        } else {
            //绘制普通座位
            drawNormalSeat(canvas, paint, seatParams, drawPositionX, drawPositionY, seatType);
        }
    }

    /**
     * 绘制舞台及其文字,<font color="#ff9900"><b>此方法中舞台文字的大小为自动计算,文字大小由舞台高度决定</b></font>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 舞台绘制位置的中心X轴
     * @param drawPositionY 舞台绘制位置的中心Y轴
     */
    protected void drawStage(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY) {
        if (mStageParams == null || !mStageParams.getIsDraw()) {
            return;
        }
        RectF stageRectf = new RectF();
        boolean isDrawImageStage = false;
        float textBeginDrawX = 0f;
        float textBeginDrawY = 0f;
        float textLength = 0f;
        //设置文字大小为舞台高度小一点(保证文字可以显示在舞台范围内)
        float textSize = mStageParams.getDescriptionSize();

        stageRectf.left = drawPositionX - mStageParams.getWidth() / 2;
        stageRectf.right = stageRectf.left + mStageParams.getWidth();
        stageRectf.top = drawPositionY - mStageParams.getHeight() / 2;
        stageRectf.bottom = stageRectf.top + mStageParams.getHeight();

        if (mStageParams.getDrawType(false) == BaseParams.DRAW_TYPE_IMAGE) {
            //绘制图片舞台
            Bitmap bitmap = mStageParams.getImage(mContext);
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, null, stageRectf, paint);
                //绘制成功
                isDrawImageStage = true;
            }
        }
        //缩略图的绘制也是使用默认的绘制方式,图片绘制到很小的的时候完全看不清楚而已很难分辨
        //没有绘制过图片舞台,尝试默认绘制方式
        if (!isDrawImageStage) {
            //绘制默认舞台
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(mStageParams.getColor());
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (stageRectf.left > 0 || stageRectf.right > 0 || stageRectf.top > 0 || stageRectf.bottom > 0) {
                canvas.drawRoundRect(stageRectf, mStageParams.getRadius(), mStageParams.getRadius(), paint);
            }
        }

        //绘制舞台文字
        String stageText = mStageParams.getStageDescription();
        if (stageText != null) {
            paint.setTextSize(textSize);
            paint.setColor(mStageParams.getDescriptionColor());
            paint.setStyle(Paint.Style.FILL);

            textLength = paint.measureText(stageText);
            textBeginDrawX = stageRectf.centerX() - textLength / 2;
            textBeginDrawY = stageRectf.centerY() + textSize / 3;
            //文字绘制不做限制
            //文字本身绘制范围小,另外需要重新计算两个参数才能进行判断
            canvas.drawText(stageText, textBeginDrawX, textBeginDrawY, paint);
        }
    }

    /**
     * 绘制售票的座位,此方法绘制的座位来源于{@link #setSeatDrawMap(ISeatMapEntity)},
     * 通过二维表中的座位类型参数进行绘制,座位绘制使用的参数请通过{@link SeatParams}参数提前设置.
     * <p><b>座位的绘制方式是从X轴正中心的位置(Y轴自定义)开始绘制,按map列表中提供的数据一行一行向下进行绘制,
     * 其中每一行的绘制调用了两次方法{@link #drawHorizontalSeatList(Canvas, Paint, float, float, IRowEntity, int, int)}进行完成,
     * 在绘制一行时,从中心位置开始向左右两端分别绘制,每一次调用{@code drawHorizontalSeatList}仅仅只绘制从中心到两端中某一端的部分,
     * 即只有半行,因此需要绘制两次</b></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param seatMap       座位数组表
     * @param drawPositionX 开始绘制的中心X轴位置(第一行座位,中心绘制位置)
     * @param drawPositionY 开始绘制的中心Y轴位置(第一行座位,中心绘制位置)
     */
    protected void drawSellSeats(Canvas canvas, Paint paint, ISeatMapEntity seatMap, float drawPositionX, float drawPositionY) {
        if (seatMap == null || seatMap.getRowCount() <= 0) {
            return;
        }
        //列数
        int columnLength = seatMap.getMaxColumnCount();
        int leftBeginColumn = 0;
        int rightBeginColumn = 0;
        float beginDrawLeftX = 0f;
        float beginDrawRightX = 0f;
        float beginDrawY = 0f;

        //计算绘制的值
        if ((columnLength & 0x1) == 0) {
            //偶数列
            //向左边绘制X轴开始点
            beginDrawLeftX = drawPositionX - mSeatParams.getSeatHorizontalInterval() / 2 - mSeatParams.getWidth() / 2;
            //向右边绘制X轴开始点
            beginDrawRightX = drawPositionX + mSeatParams.getSeatHorizontalInterval() / 2 + mSeatParams.getWidth() / 2;
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
            beginDrawRightX = drawPositionX + mSeatParams.getSeatHorizontalInterval() + mSeatParams.getWidth();
            //此处从中心位置的列数开始计算
            //向左开始绘制列数，当前指定索引列将被绘制
            //奇数列，中心位置的列由向左绘制完成，因此不需要索引值-1
            leftBeginColumn = columnLength / 2;
            //向右开始绘制列数，当前指定索引列将被绘制
            rightBeginColumn = leftBeginColumn + 1;
        }


        //开始绘制
        beginDrawY = drawPositionY;
        if (mGlobleParams.getIsDrawColumnNumber()) {
            //下移一行,实际需要绘制的座位,空出的一行为列数绘制需要
            beginDrawY += mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval();
        }

        for (int i = 0; i < seatMap.getRowCount(); i++) {
            //获取行数据对象接口
            IRowEntity seatRow = seatMap.getSeatRowInMap(i);
            //对行数据进行检测判断是否需要绘制
            if (seatRow != null && seatRow.getIsDraw() && seatRow.getIsEmpty()) {
                //当前行需要绘制但数据为空
                //直接空出一行
                beginDrawY += mSeatParams.getSeatVerticalInterval() + mSeatParams.getHeight();
            } else if (seatRow != null && seatRow.getIsDraw()) {
                //画左边的座位
                //绘制的时候由start位置到end位置
                //从start到end每一个位置都会绘制,所以要保证start位置的数据在数组中,end位置的数据不会被绘制
                //即数组不可越界
                drawHorizontalSeatList(canvas, paint, beginDrawLeftX, beginDrawY, seatRow, leftBeginColumn, 0 - 1);
                //画右边的座位
                drawHorizontalSeatList(canvas, paint, beginDrawRightX, beginDrawY, seatRow, rightBeginColumn, columnLength);
                //增加Y轴的绘制高度
                beginDrawY += mSeatParams.getSeatVerticalInterval() + mSeatParams.getHeight();
            }
        }

        if (!mSeatParams.getIsDrawThumbnail()) {
            //记录界面高度值
            mCanvasHeight = beginDrawY - this.getCanvasDrawBeginY(mGlobleParams.getSeatTypeRowCount())
                    //附加部分,用于边界的空白
                    + mSeatParams.getHeight();
        }
    }


    /**
     * 绘制悬浮的行数显示
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 开始绘制的X轴中心位置
     * @param drawPositionY 开始绘制的Y轴起始位置,即座位开始绘制的第一行相同的Y坐标
     * @param edgeY         Y轴边界值,在此处特指顶端可显示的最top值,<font color="#ff9900">此值向下的部分可显示</font>
     * @param rowCount      行数(即seatMap.length)
     */
    protected void drawFloatTitleRowNumber(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, float edgeY, int rowCount) {
        float textLength = 0f;
        //设定文字最大值为80
        float textSize = mSeatParams.getDescriptionSize() > 80 ? 80 : mSeatParams.getDescriptionSize();
        float beginDrawX = drawPositionX;
        float beginDrawY = drawPositionY;

        //计算行数背景
        mImageRectf.left = drawPositionX - mSeatParams.getHeight() / 2;
        mImageRectf.right = mImageRectf.left + mSeatParams.getHeight();
        mImageRectf.top = drawPositionY - mSeatParams.getHeight() / 2 - mSeatParams.getSeatVerticalInterval() / 2;
        mImageRectf.bottom = mImageRectf.top + (mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval()) * rowCount;

        //超过边界值不绘制
        if (mImageRectf.top < edgeY) {
            mImageRectf.top = edgeY;
        } else if (mImageRectf.top < (0 - mSeatParams.getRadius())) {
            //当顶端超过屏幕,只取屏幕部分绘制,其它忽略
            mImageRectf.top = -mSeatParams.getRadius();
        }
        //当底端超过屏幕,只取屏幕部分绘制,其它忽略,否则可能造成数据太大无法绘制的问题
        if (mImageRectf.bottom > (mWHPoint.y + mSeatParams.getRadius())) {
            mImageRectf.bottom = mWHPoint.y + mSeatParams.getRadius();
        }

        //绘制透明背景
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mGlobleParams.getRowNumberBackgroundColor());
        paint.setAlpha(mGlobleParams.getRowNumberAlpha());
        canvas.drawRoundRect(mImageRectf, mSeatParams.getRadius(), mSeatParams.getRadius(), mPaint);

        //绘制文字
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mGlobleParams.getRowNumberTextColor());
        //字体最大使用80
        paint.setTextSize(textSize);
        for (int i = 0; i < rowCount; i++) {
            textLength = this.getTextLength(mSeatParams.getDescriptionSize(), String.valueOf(i + 1));
            beginDrawX -= textLength / 2;
            beginDrawY += mSeatParams.getDescriptionSize() / 3;

            //超过边界值的不绘制
            if (beginDrawY > edgeY + textSize) {
                canvas.drawText(String.valueOf(i + 1), beginDrawX, beginDrawY, paint);
            }

            beginDrawX = drawPositionX;
            beginDrawY = drawPositionY + (i + 1) * (mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval());
        }
    }

    /**
     * 绘制悬浮的列数,此方法依赖于{@link #drawHorizontalColumnNumber(Canvas, Paint, float, float, float, int, int, boolean)}
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 绘制列的中心X轴位置,通过方法{@link #getDrawCenterX(float)}获取最好
     * @param drawPositionY 绘制列的中心Y轴位置,若需要绘制则通过{@link #getSellSeatDrawCenterY(int, boolean)}获取<br/>
     *                      此处需要注意的是,列数的绘制与普通座位是统一的,在同一个绘制区域内,如果需要绘制列,则第一行座位不绘制,下移到第二行开始绘制,
     *                      若不需要绘制列,则第一行绘制为座位而不是列数,此部分由{@link IGlobleParams#getIsDrawColumnNumber()}设置
     * @param edgeX         X轴边界值,此处特指在左边可显示的最left值,<font color="#ff9900">此值往右的部分可显示</font>
     * @param columnCount   绘制列的数量
     * @param columnStyle   绘制列的类型,此处暂时未完善,此值暂时无用,保留//TODO
     */
    protected void drawFloatTitleColumnNumber(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, float edgeX, int columnCount, int columnStyle) {
        int middleColumnToLeft = 0;
        int middleColumnToRight = 0;
        //背景一半的宽度
        float halfWidth = 0f;
        float beginLeftDrawX = 0f;
        float beginRightDrawX = 0f;
        if ((columnCount & 0x1) == 0) {
            //偶数列
            //左右两边列数一样,所以是平均的
            middleColumnToLeft = columnCount / 2;
            //计算背影宽度一半的值
            //按座位宽度及间隔计算
            halfWidth = (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval()) * middleColumnToLeft
                    //偶数列中间是间隔,加上一半的间隔
                    + mSeatParams.getSeatHorizontalInterval() / 2;
            beginLeftDrawX = drawPositionX - mSeatParams.getSeatHorizontalInterval() / 2 - mSeatParams.getWidth() / 2;
        } else {
            //奇数列
            //此列数是中间列,因此数据要加1(直接除结果为前小半部分,不包括中间列)
            middleColumnToLeft = columnCount / 2 + 1;
            //计算背影宽度一半的值
            //按座位宽度及间隔计算
            halfWidth = (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval()) * middleColumnToLeft
                    //奇数列中间是座位,减去一半的座位宽度(此部分超过了中间线)
                    - mSeatParams.getWidth() / 2;
            beginLeftDrawX = drawPositionX;
        }
        middleColumnToRight = middleColumnToLeft + 1;
        beginRightDrawX = beginLeftDrawX + mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval();

        //计算绘制背景
        mImageRectf.left = drawPositionX - halfWidth;
        mImageRectf.right = mImageRectf.left + (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval()) * columnCount;
        mImageRectf.top = drawPositionY - mSeatParams.getHeight() / 2;
        mImageRectf.bottom = mImageRectf.top + mSeatParams.getHeight();
        //当左边界不在屏幕内时,只取屏幕内绘制
        if (mImageRectf.left < edgeX) {
            mImageRectf.left = edgeX;
        } else if (mImageRectf.left < (0 - mSeatParams.getRadius())) {
            mImageRectf.left = -mSeatParams.getRadius();
        }
        //当右边界不在屏幕内时,只取屏幕内绘制
        if (mImageRectf.right > (mWHPoint.x + mSeatParams.getRadius())) {
            mImageRectf.right = mWHPoint.x + mSeatParams.getRadius();
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mGlobleParams.getColumnNumberBackgroundColor());
        paint.setAlpha(mGlobleParams.getColumnNumberAlpha());
        canvas.drawRoundRect(mImageRectf, mSeatParams.getRadius(), mSeatParams.getRadius(), paint);

        //绘制文字列
        //从右向左绘制
        this.drawHorizontalColumnNumber(canvas, paint, beginLeftDrawX, drawPositionY, edgeX, middleColumnToLeft, 0, false);
        //从左向右绘制
        this.drawHorizontalColumnNumber(canvas, paint, beginRightDrawX, drawPositionY, edgeX, middleColumnToRight, columnCount + 1, true);
    }

    /**
     * 在sellSeat第一行绘制列数,此处的绘制方式同{@link #drawHorizontalSeatList(Canvas, Paint, float, float, IRowEntity, int, int)},原理相同;<br/>
     * 绘制行数分为两部分,从中轴线开始向左右两边绘制,所以需要调用此方法两次;
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 开始绘制的位置的中心X坐标
     * @param drawPositionY 开始绘制的位置的Y坐标
     * @param edgeX
     * @param from          开始绘制的列,此索引将会绘制,<font color="#ff9900">此处不是以0为起始值,而是以1为起始值,即绘制的值即为给定的值)</font>
     * @param to            最终绘制的列,此索引将不会绘制,同上
     * @param isLeftToRight 是否从左向右绘制
     */
    protected void drawHorizontalColumnNumber(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, float edgeX, int from, int to, boolean isLeftToRight) {
        //计算增量
        int increament = isLeftToRight ? 1 : -1;
        //总数
        int totalCount = Math.abs(from - to);
        int currentDrawColumn = 0;
        //文字大小最大值80
        float textSize = mSeatParams.getDescriptionSize() > 80 ? 80 : mSeatParams.getDescriptionSize();
        float textLength = 0f;
        //文字开始绘制的X坐标
        float textDrawX = 0f;
        //绘制的X中心坐标
        float beginDrawX = drawPositionX;
        //文本绘制的底端Y坐标
        float beginDrawY = drawPositionY + mSeatParams.getHeight() / 3;

        //通用文本设置
        paint.setColor(mGlobleParams.getColumnNumberTextColor());
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < totalCount; i++) {
            //计算当前绘制的列文本
            currentDrawColumn = from + i * increament;
            //计算文本长度
            textLength = this.getTextLength(mSeatParams.getDescriptionSize(), String.valueOf(currentDrawColumn));
            //计算文本开始绘制的X坐标,所以将文本向左推移一半的文本长度
            textDrawX = beginDrawX - textLength / 2;

            if (textDrawX > edgeX) {
                //绘制文本
                canvas.drawText(String.valueOf(currentDrawColumn), textDrawX, beginDrawY, paint);
            }
            //移动到下一个文本绘制的中心位置
            beginDrawX += increament * (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval());
        }
    }

    /**
     * 绘制一行水平的座位表,<font color="#ff9900"><b>向一个固定的方向绘制</b></font>
     * <p><b>此方法从(drawPositionX,drawPositionY)的位置开始向某个方向绘制,
     * 绘制方向由start/end决定,当start&gt;end时,向左绘制,当start&lt;end时,向右绘制,
     * 此处的start与end是在列表数据seatList中的索引值</b></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionX 该行座位开始绘制的第一个座位中心X轴位置
     * @param drawPositionY 该行座位开始绘制的第一个座位中心Y轴位置
     * @param seatRow       单行座位列表
     * @param start         座位列表中开始绘制的索引,<font color="#ff9900"><b>此索引位置的座位将被绘制</b></font>
     * @param end           座位列表中最后绘制的索引,<font color="#ff9900"><b>此索引位置的座位不被绘制</b></font>
     * @return 若需要绘制行, 则返回true, 不需要绘制行, 返回false
     */
    protected boolean drawHorizontalSeatList(Canvas canvas, Paint paint, float drawPositionX, float drawPositionY, IRowEntity seatRow, int start, int end) {
        if (seatRow == null || !seatRow.getIsDraw()) {
            return false;
        }
        float beginDrawX = drawPositionX;
        //从大到小则为向左绘制,增量为负值-1
        //从小到大则为向右绘制,增量为正值+1
        int increment = (start - end) > 0 ? -1 : 1;
        //绘制座位
        if (!seatRow.getIsEmpty()) {
            try {
                int seatType = 0;
                int i = start;
                do {
                    //尝试获取数据进行处理,若无法获取到数据则将位置计算到最后
                    //获取对应位置座位的类型
                    ISeatEntity seat = seatRow.getSeat(i);
                    if (seat == null) {
                        //计算下一个绘制座位的X轴位置
                        //由于此处绘制的座位是同一行的,所以仅X轴位置改变,Y轴位置不会改变
                        beginDrawX += Math.abs(i - end) * increment * (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval());
                        break;
                    }
                    seatType = seat.getType();
                    //设置座位是否需要被绘制
                    mSeatParams.setIsDraw(seatType);
                    //绘制单个座位
                    drawSeat(canvas, paint, mSeatParams, beginDrawX, drawPositionY, seatType);
                    //计算下一个绘制座位的X轴位置
                    //由于此处绘制的座位是同一行的,所以仅X轴位置改变,Y轴位置不会改变
                    beginDrawX += increment * (mSeatParams.getWidth() + mSeatParams.getSeatHorizontalInterval());
                    //通过增量改变下一个绘制座位的索引
                    i += increment;
                } while (i != end);

                //若需要绘制行数,则将每行的左右边界多留一个座位宽度的空白
                if (mGlobleParams.getIsDrawRowNumber()) {
                    beginDrawX += increment * mSeatParams.getHeight() * 1.5f;
                }

                //计算新的界面宽度值
                //此处只在这个地方计算是因为在最左边还需要绘制一列行数文字
                float newCanvasWidth =
                        //最左边边界到中心X轴位置的宽度(即总宽度的一半)
                        (Math.abs(beginDrawX - this.getDrawCenterX(mWHPoint.x))
                                //附加边界空白
                                + mSeatParams.getWidth()) * 2;
                //记录新的界面宽度值,保存最大值
                mCanvasWidth = newCanvasWidth > mCanvasWidth ? newCanvasWidth : mCanvasWidth;
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 自动计算并绘制座位类型及其邻近文字的方法,该方法不适用于可移动界面
     * <p><font color="#ff9900">由于是自动计算,所以移动界面后座位类型位置还是不变,所以不推荐使用(特别需要情况下可用),但绘制的结果是保证适应屏幕界面的</font></p>
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
            if (seatTypeDesc != null) {
                text = mSeatParams.getSeatTypeDescription()[i];
            } else {
                text = null;
            }
            drawSeatWithNearText(canvas, paint, mSeatParams, drawPositionX, drawPositionY, text, seatTextInterval, seatType, false);
            drawPositionX += eachSeatTypeDrawWidth;
        }
    }

    /**
     * 根据指定的行数自动计算并绘制座位类型及其描述文字,将所有的座位类型按指定行数分开绘制,并返回绘制后的Y轴坐标位置,
     * <font color="#ff9900"><b>若无法绘制返回drawPositionY,若绘制成功,返回的Y轴坐标位置将为一下次可直接绘制的坐标值(即已经将最后一行之后的间隔距离计算在内)</b></font>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionY 开始绘制的Y轴坐标的中心位置,centerY
     * @param rowCount      预定需要绘制的行数,此值将会被记录下用于后续处理,详见部分方法{@link #getCanvasDrawBeginY(int)}
     */

    protected void drawSeatTypeByAuto(Canvas canvas, Paint paint, float drawPositionY, int rowCount) {
        if (mSeatParams == null || rowCount <= 0 || !mSeatParams.getIsDraw()) {
            return;
        }

        //获取计算生成的分批绘制的座位类型
        SeatParams[] separateSeatTypeParams = mSeatParams.getAutoSeparateParams(rowCount);
        if (separateSeatTypeParams != null) {
            for (SeatParams params : separateSeatTypeParams) {
                drawSingleRowExampleSeatType(canvas, paint, drawPositionY, params);
                //Y轴坐标移动增量,用于下一行的绘制
                drawPositionY += params.getHeight() + params.getSeatVerticalInterval();
            }
        }
    }

    /**
     * 绘制单行的座位类型及其描述文字
     * <p><font color="#ff9900"><b>此方法绘制时以参数设置固定的座位类型间的间隔为基准进行绘制,不保证绘制结果会完全适应屏幕</b></font></p>
     *
     * @param canvas        画板
     * @param paint         画笔
     * @param drawPositionY 座位绘制的中心Y轴位置
     * @param params        用于绘制座位类型的参数
     */
    protected void drawSingleRowExampleSeatType(Canvas canvas, Paint paint, float drawPositionY, SeatParams params) {
        if (params == null) {
            //若座位参数为null,则不作任何绘制
            return;
        }
        //座位类型的个数
        int seatTypeCount = params.getSeatTypeLength();
        //座位的宽度
        float seatWidth = params.getWidth();
        //座位与邻近文字的距离
        float seatTextInterval = params.getSeatTypeDescInterval();
        //座位类型之前的距离,每个座位(此处包含文字)绘制之间的距离
        float seatTypeInterval = params.getSeatTypeInterval();
        //开始绘制的位置
        //此处的开始绘制是指第一个座位类型在当前行中绘制的中心位置
        float drawPositionX = 0f;
        //所有绘制座位的文字总长度
        //用于后面计算开始绘制的位置
        float totalTextLength = 0f;
        //所有座位类型的总长度
        //此处的座位包含了其邻近的文字
        float allSeatTypeWidth = 0f;
        int[] seatTypeArr = params.getSeatTypeArrary();
        //座位类型对应的描述文字数组
        String[] seatTypeDesc = params.getSeatTypeDescription();

        if (seatTypeDesc != null) {
            for (String text : seatTypeDesc) {
                //计算所有文字长度
                totalTextLength += this.getTextLength(params.getDescriptionSize(), text);
            }
        } else {
            totalTextLength = 0f;
        }
        //计算所有座位(包含文字及座位之前的间隔)长度
        allSeatTypeWidth = totalTextLength + (params.getWidth()
                + params.getSeatTypeDescInterval()) * seatTypeCount
                + params.getSeatTypeInterval() * (seatTypeCount - 1);
        String firstText = seatTypeDesc == null ? null : seatTypeDesc[0];
        float firstSeatTypeTotalWidth = seatWidth + seatTypeInterval + this.getTextLength(params.getDescriptionSize(), firstText);
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
            int seatType = seatTypeArr[i];
            int seatColor = params.getSeatColorByType(seatType);
            params.setIsDraw(seatType);
            params.setColor(seatColor);
            //加载座位类型描述文字及计算其长度
            if (seatTypeDesc != null) {
                drawText = seatTypeDesc[i];
                textLength = this.getTextLength(params.getDescriptionSize(), drawText);
            } else {
                drawText = null;
                textLength = 0f;
            }
            //绘制座位及文字
            drawSeatWithNearText(canvas, paint, params, drawPositionX, drawPositionY, drawText, seatTextInterval, seatType, false);

            //获取并计算下一个绘制文字的长度
            //用于计算下一个绘制座位的X轴中心位置
            int nextTextIndex = i + 1;
            if (nextTextIndex < seatTypeCount && seatTypeDesc != null) {
                nextTextLength = this.getTextLength(params.getDescriptionSize(), seatTypeDesc[nextTextIndex]);
            } else {
                nextTextLength = 0;
            }
            //计算下一个绘制座位的X轴中心位置
            drawPositionX += (seatWidth + seatTypeInterval + textLength) / 2
                    + (seatWidth + seatTypeInterval + nextTextLength) / 2;
        }
    }

    /**
     * 获取第一行座位绘制的Y轴中心位置,此方法用于处理列数绘制时定位绘制位置<br/>
     * 座位区域开始绘制的Y轴坐标可通过方法{@link #getSellSeatDrawCenterY(int, boolean)}获取,<font color="#ff9900"><b>但此方法获取的坐标值不一定是第一行座位绘制的坐标值</b>,
     * 此处获取的坐标值可能是列数绘制的坐标位置(若需要绘制列数),若不需要绘制列数才是第一行座位绘制的Y轴中心位置</font>
     *
     * @param seatTypeCount
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getFirstSellSeatDrawCenterY(int seatTypeCount) {
        float drawCenterY = this.getSellSeatDrawCenterY(seatTypeCount, false);
        if (mGlobleParams.getIsDrawColumnNumber()) {
            drawCenterY += mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval();
        }
        return drawCenterY;
    }

    /**
     * 获取座位类型绘制的Y轴中心位置(此处指第一行绘制的座位类型)
     *
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getSeatTypeDrawCenterY() {
        //初始偏移量
        float beginDrawCenterY = mOriginalOffsetY
                //用户可能进行移动的偏移量
                + mBeginDrawOffsetY;
        if (mSeatParams.getIsDrawThumbnail()) {
            beginDrawCenterY = 0f;
        }
        if (mStageParams.getIsDraw()) {
            beginDrawCenterY += mStageParams.getStageTotalHeight();
        } else {
            beginDrawCenterY += mStageParams.getStageMarginBottom();
        }
        if (mSeatParams.getIsDraw()) {
            beginDrawCenterY += mStageParams.getHeight() / 2;
        }
        return beginDrawCenterY;
    }

    /**
     * 获取实际出售座位开始绘制的Y轴中心位置,centerY<br/>
     * 此处需要注意的是,此方法获取的是座位区域开始绘制的Y轴中心位置,但不一定是第一行座位开始绘制的位置,
     * 若需要绘制列数,则需要此坐标值为列数绘制的位置(第一行座位在列数绘制后才进行绘制)
     *
     * @param seatTypeRowCount 座位类型绘制了几行,<font color="#ff9900"><b>此处有这个参数是因为前面设计座位类型是可以被反复绘制多次的</b></font>,
     *                         这种情况是为了解决当座位类型很多时(如可能有5个以上的座位类型),则一行可能绘制不了或者会造成绘制结果不清晰,因此座位允许自定义选择绘制行数,
     *                         用户可以自主拆分座位类型并分批进行绘制
     * @param isBaseOriginal   <font color="#ff9900"><b>是否依赖于最原始的值</b></font>,若为true,则忽略所有的偏移量,返回的结果为初始偏移量为0的高度;
     *                         若为fale则返回的值包含了所有有偏移量,即为实际应该显示的位置
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getSellSeatDrawCenterY(int seatTypeRowCount, boolean isBaseOriginal) {
        //初始偏移量
        float beginDrawCenterY = mOriginalOffsetY
                //用户可能进行移动的偏移量
                + mBeginDrawOffsetY;

        if (isBaseOriginal) {
            beginDrawCenterY = 0f;
        }

        if (mSeatParams.getIsDrawThumbnail()) {
            beginDrawCenterY = 0f;
        }
        if (mStageParams.getIsDraw()) {
            beginDrawCenterY += mStageParams.getStageTotalHeight();
        } else {
            beginDrawCenterY += mStageParams.getStageMarginBottom();
        }
        if (mSeatParams.getIsDraw()) {
            beginDrawCenterY += (mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval()) * seatTypeRowCount;
        }
        beginDrawCenterY += mSeatParams.getHeight() / 2;

        return beginDrawCenterY;
    }

    /**
     * 获取舞台开始绘制的Y轴中心位置
     *
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getStageDrawCenterY() {
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
        if (mStageParams.getIsDraw()) {
            //舞台与顶端的距离
            beginDrawCenterY += mStageParams.getStageMarginTop()
                    //舞台的自身高度一半
                    //此处返回的是舞台实际需要绘制的位置的Y轴中心坐标
                    //因此需要返回舞台自身ymyar一半
                    + mStageParams.getHeight() / 2;
        } else {
            beginDrawCenterY = 0f;
        }
        return beginDrawCenterY;
    }

    /**
     * 获取实际出售座位开始绘制的Y轴顶端坐标,top
     * <p><font color="#ff9900"><b><p>此处必须注意的地方是,这里的Y轴坐标是指top,而不是绘制位置的中心centerY</b></font></p>
     * <p><font color="#ff9900">此外,这里的Y轴坐标是原始绘制界面的坐标,而不是移动后的坐标(即在第一次绘制时把该坐标记录返回即可)</font></p>
     *
     * @param seatTypeRowCount 座位类型绘制了几行,<font color="#ff9900"><b>此处有这个参数是因为前面设计座位类型是可以被反复绘制多次的</b></font>,
     *                         这种情况是为了解决当座位类型很多时(如可能有5个以上的座位类型),则一行可能绘制不了或者会造成绘制结果不清晰,因此座位允许自定义选择绘制行数,
     *                         用户可以自主拆分座位类型并分批进行绘制
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getSellSeatDrawBeginY(int seatTypeRowCount) {
        float beginY = this.getSellSeatDrawCenterY(seatTypeRowCount, false) - mSeatParams.getHeight() / 2;
        //绘制列数再下移一行
        if (mGlobleParams.getIsDrawColumnNumber()) {
            beginY += mSeatParams.getHeight() + mSeatParams.getSeatVerticalInterval();
        }
        return beginY;
    }

    /**
     * 获取界面绘制开始的Y轴位置
     *
     * @param seatTypeRowCount 座位类型绘制的行数
     * @return
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getCanvasDrawBeginY(int seatTypeRowCount) {
        if (mStageParams.getIsDraw()) {
            return this.getStageDrawCenterY() - mStageParams.getHeight() / 2 - mStageParams.getStageMarginTop();
        } else if (mSeatParams.getIsDraw()) {
            return this.getSeatTypeDrawCenterY() - mSeatParams.getHeight() / 2 - mStageParams.getStageMarginBottom();
        } else {
            return this.getSellSeatDrawBeginY(seatTypeRowCount);
        }
    }

    /**
     * 获取缩略图的宽,默认为屏幕宽的1/3,需要设置缩略图的宽请调用方法{@link IGlobleParams#setThumbnailWidthRate(float)}
     *
     * @return
     */
    protected float getThumbnailWidth() {
        if (mWHPoint == null) {
            mWHPoint = this.getWidthAndHeight();
        }
        return mWHPoint.x * mGlobleParams.getThumbnailWidthRate();
    }

    /**
     * 获取显示区域开始绘制的Y轴坐标
     *
     * @param originalCanvasWidth 主界面(非缩略图)的实际界面宽度,<font color="#ff9900"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
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
     * @param originalCanvasWidth 主界面(非缩略图)的实际界面宽度,<font color="#ff9900"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @return
     */
    protected float getShowRectfCenterX(float originalCanvasWidth) {
        //此处要注意的是,在实际界面的偏移量中,当向左移动时偏移量是正值
        //向右移动时偏移量是负值
        //而缩略图是完整并一直显示在屏幕上的
        //所以向左移动偏移量应该是负值(相对中心X轴位置来说)
        //向右移动偏移量应该是正值,刚好与实际情况相反,所以要注意此处使用了-1

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
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="#ff9900"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
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
        //若当前的界面高度小于屏幕高度,即缩略图显示框完全显示缩略图
        //将显示框起始高度设为0,保证显示框包含整个缩略图
        if (heightRate == 1) {
            showRectf.top = 0;
        } else {
            showRectf.top = this.getShowRectfBeginY(originalCanvasWidth);
        }
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
     * <p><font color="#ff9900"><b>缩略图的绘制大小由view宽度决定,一般为view宽度的1/3,即宽度保持不变地占用了控件宽度的1/3</b></font></p>
     *
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="#ff9900"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
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
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected float getDrawCenterX(float viewWidth) {
        if (mSeatParams.getIsDrawThumbnail()) {
            return this.getThumbnailWidth() / 2;
        } else {
            return mOriginalOffsetX + mBeginDrawOffsetX + viewWidth / 2;
        }
    }

    /**
     * 绘制之前进行的数据初始化或者必须的数据处理,绘制画板背景色
     *
     * @param canvas 画板
     */
    protected void beginDraw(Canvas canvas, Paint paint) {
        //重置界面绘制的宽高
        this.mCanvasWidth = 0f;
        this.mCanvasHeight = 0f;
        //绘制背景色
        canvas.drawColor(mGlobleParams.getCanvasBackgroundColor());
    }

    /**
     * 完成绘制之后调用的方法,用于处理部分重置的变量等
     */
    protected void finishDraw() {
        this.mIsScaleRedraw = false;
    }

    @Override
    public void drawCanvas(Canvas canvas) {
        if (mWHPoint == null) {
            mWHPoint = getWidthAndHeight();
        }
        //初始化绘制工作
        this.beginDraw(canvas, mPaint);

        //绘制正常实际的界面
        drawNormalCanvas(canvas, mPaint, mWHPoint.y);
        if (mSeatDrawMap != null && mSeatDrawMap.getRowCount() > 0) {
            //绘制行列显示条
            drawColumnAndRowNumber(canvas, mPaint, mCanvasWidth, mSeatDrawMap.getMaxColumnCount(), mSeatDrawMap.getRowCount());
        }

        //判断当前是否需要显示缩略图
        //当界面基本在当前屏幕范围内时,不显示缩略图
        if (mCanvasWidth > (mWHPoint.x + 30f) || mCanvasHeight > (mWHPoint.y + 30f)) {
            //当界面在移动中时显示缩略图,否则不显示缩略图
            drawThumbnail(canvas, mPaint, mCanvasWidth, mCanvasHeight);
        }

        //绘制选中提醒界面
        if (mIsDrawSeletedRowColumn) {
            drawSeletedRowColumnToast(canvas, mPaint, mCurrentSeletedSeat.x, mCurrentSeletedSeat.y);
        }

        this.finishDraw();
    }

    @Override
    public void setSeatDrawMap(ISeatMapEntity seatMap) {
        //设置新的座位列表
        this.mSeatDrawMap = seatMap;
        //重绘
        this.mDrawView.post(new InvalidateRunnable(mDrawView, MotionEvent.ACTION_UP));
    }

    @Override
    public ISeatMapEntity getSeatDrawMap() {
        return this.mSeatDrawMap;
    }

    @Override
    public boolean updateSeatIMap(int type, int rowIndexInMap, int columnIndexInMap) {
        return this.mSeatDrawMap.updateSeatType(type, rowIndexInMap, columnIndexInMap);
    }

    @Override
    public int getSeatTypeInMap(int rowIndex, int columnIndex) {
        return this.mSeatDrawMap.getSeatType(rowIndex, columnIndex);
    }

    /**
     * 绘制行列显示条
     *
     * @param canvas              画板
     * @param paint               画笔
     * @param originalCanvasWidth 实际绘制界面的宽度
     * @param columnCount         列数
     * @param rowCount            行数
     */
    protected void drawColumnAndRowNumber(Canvas canvas, Paint paint, float originalCanvasWidth, int columnCount, int rowCount) {
        //计算列的Y轴绘制坐标
        //实际界面绘制的高度(受到偏移量的影响)
        float theoryColumnDrawCenterY = this.getSellSeatDrawCenterY(mGlobleParams.getSeatTypeRowCount(), false);
        //固定位置的绘制高度,不受偏移量影响
        float fixColumnDrawCenterY = this.getSellSeatDrawCenterY(mGlobleParams.getSeatTypeRowCount(), true);
        //若当前界面列绘制的高度大于固定位置的高度,则按实际界面绘制(界面可能下移)
        //否则按固定位置,此情况即为界面被上移了
        float drawCenterY = 0f;


        //当前理论位置在固定位置之下,使用理论值
        if (theoryColumnDrawCenterY >= fixColumnDrawCenterY) {
            drawCenterY = theoryColumnDrawCenterY;
        } else if (theoryColumnDrawCenterY < fixColumnDrawCenterY && (theoryColumnDrawCenterY - mSeatParams.getHeight()) >= 0) {
            //当前理论位置在固定位置之上,同时理论位置到边界的距离大于固定偏移量
            //使用理论值
            drawCenterY = theoryColumnDrawCenterY;
        } else if (theoryColumnDrawCenterY < fixColumnDrawCenterY && (theoryColumnDrawCenterY - mSeatParams.getHeight()) < 0) {
            //当前理论位置在固定位置之上,同时理论位置到边界的距离小于固定偏移量
            //此时使用理论值会使列被遮挡,所以使用固定值
            //此方式保证了在理论值切换到固定值时过渡值是一致的
            //因此不会出现不平滑的移动现象
            drawCenterY = mSeatParams.getHeight();
        } else {
            showMsg("draw", "列数第三种情况......");
        }


        //计算行的X轴绘制坐标
        float theoryRowDrawCenterX = this.getDrawCenterX(mWHPoint.x) - (originalCanvasWidth / 2) + mSeatParams.getDescriptionSize();
        //固定悬浮值,此值虽然是动态的(在全局中会变动)
        //但是使用此值可以使程序在最边界的时候可以平滑切换
        float fixRowDrawCenterX = mSeatParams.getWidth();
        float drawCenterX = 0f;
        //当前理论位置在固定位置右边,说明被右移了,按理论位置绘制
        if (theoryRowDrawCenterX >= fixRowDrawCenterX) {
            drawCenterX = theoryRowDrawCenterX;
        } else if (theoryRowDrawCenterX < fixRowDrawCenterX && (theoryRowDrawCenterX - fixRowDrawCenterX) >= 0) {
            //当前理论位置在固定位置左边,同时理论位置到边界的距离没有超过固定悬浮值,
            //使用理论绘制
            drawCenterX = theoryRowDrawCenterX;
        } else if (theoryRowDrawCenterX < fixRowDrawCenterX && (theoryRowDrawCenterX - fixRowDrawCenterX) < 0) {
            //当前理论位置在固定位置左边,同时理论位置到边界的距离小于固定悬浮值
            //此时按理论值绘制则行数会被遮挡,所以按固定值绘制
            //此方式保证了在理论值切换到固定值时过渡值是一致的
            //因此不会出现不平滑的移动现象
            drawCenterX = fixRowDrawCenterX;
        } else {
            showMsg("draw", "行数第三种情况");
        }

        //行数绘制的左边界值,防止行列的叠加
        float edgeX = drawCenterX + mSeatParams.getHeight() / 2;
        //列数绘制的右边界值,防止行列的叠加
        float edgeY = drawCenterY + mSeatParams.getHeight() / 2;
        if (mGlobleParams.getIsDrawColumnNumber()) {
            //绘制列数
            this.drawFloatTitleColumnNumber(canvas, paint, this.getDrawCenterX(mWHPoint.x), drawCenterY, edgeX, columnCount, 0);
        }
        if (mGlobleParams.getIsDrawRowNumber()) {
            //绘制行数
            this.drawFloatTitleRowNumber(canvas, paint, drawCenterX, this.getFirstSellSeatDrawCenterY(mGlobleParams.getSeatTypeRowCount()), edgeY, rowCount);
        }
    }

    /**
     * 绘制选中座位提醒界面
     *
     * @param canvas      画板
     * @param paint       画笔
     * @param rowIndex    选中座位行索引
     * @param columnIndex 选中座位列索引
     */
    protected void drawSeletedRowColumnToast(Canvas canvas, Paint paint, int rowIndex, int columnIndex) {
        //行
        int firstNumber = 0;
        //列
        int secondNumber = 0;
        String format = mGlobleParams.getNotificationFormat();
        if (format == null) {
            throw new RuntimeException("格式化字符串为null的情况下不可显示选中区域的提醒");
        }
        //绘制的文字
        if (mGlobleParams.getIsRowFirst()) {
            //行显示在前
            firstNumber = rowIndex + 1;
            secondNumber = columnIndex + 1;
        } else {
            //列显示在前
            firstNumber = columnIndex + 1;
            secondNumber = rowIndex + 1;
        }
        String drawStr = String.format(mGlobleParams.getNotificationFormat(), firstNumber, secondNumber);
        //理论的文本长度
        float theoryTextLength = mWHPoint.x * 0.7f;
        //从文本长度计算文本大小
        float textSize = theoryTextLength / drawStr.length();
        //实际文本长度
        float realTextLength = this.getTextLength(textSize, drawStr);
        //文本开始绘制的X坐标
        float beginDrawTextX = mWHPoint.x / 2 - realTextLength / 2;
        //文本绘制后的底端Y坐标
        float begindrawTextY = mWHPoint.y / 2 + textSize / 3;

        //文本提醒的背景
        RectF rowColumnRectf = new RectF();
        rowColumnRectf.top = mWHPoint.y / 2 - textSize / 2 - 10;
        rowColumnRectf.bottom = rowColumnRectf.top + textSize + 20;
        rowColumnRectf.left = mWHPoint.x / 2 - theoryTextLength / 2;
        rowColumnRectf.right = rowColumnRectf.left + theoryTextLength;

        //绘制半透明背景
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setAlpha(150);
        canvas.drawRoundRect(rowColumnRectf, 20f, 20f, paint);

        //绘制文本
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        canvas.drawText(drawStr, beginDrawTextX, begindrawTextY, paint);
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
     * 绘制正常的界面,缩略图的绘制{@link #drawThumbnail(Canvas, Paint, float, float)}本身是依赖于此方法的,
     * <b>此若需要自定义绘制类,通过继承此类后修改绘制流程则基本必须重写此类</b>
     * <br/>
     * <br/>
     * <p>此方法的绘制流程大致如下:
     * <br/>
     * <br/>
     * 1.获取X轴的中心位置(需要计算偏移量在内){@link #getDrawCenterX(float)}<br/>
     * 2.获取舞台绘制的高度(X轴是永远固定从中心位置开始的){@link #getStageDrawCenterY()}<br/>
     * 3.绘制舞台{@link #drawStage(Canvas, Paint, float, float)}<br/>
     * 4.获取座位类型绘制的高度{@link #getSeatTypeDrawCenterY()}<br/>
     * 5.绘制座位类型{@link #drawSeatTypeByAuto(Canvas, Paint, float, int)}<br/>
     * 6.获取普通座位绘制的高度{@link #getSellSeatDrawCenterY(int, boolean)}<br/>
     * 7.绘制普通座位{@link #drawSellSeats(Canvas, Paint, ISeatMapEntity, float, float)}<br/>
     * 8.绘制中心分界线{@link #getCenterDotLine(float, float)}<br/>
     * </p>
     *
     * @param canvas     画板
     * @param paint      画笔
     * @param viewHeight 绘制界面的高度(主要用于绘制中心轴虚线可用),实际绘制界面高度为屏幕高度
     * @since <font color="#ff9900"><b>继承此类时该方法可能需要重写</b></font>
     */
    protected void drawNormalCanvas(Canvas canvas, Paint paint, float viewHeight) {
        float drawX = 0f;
        float drawY = 0f;
        //开始绘制舞台
        drawY = this.getStageDrawCenterY();
        drawX = this.getDrawCenterX(mWHPoint.x);
        drawStage(canvas, paint, drawX, drawY);
        //开始绘制座位类型
        drawY = this.getSeatTypeDrawCenterY();
        drawSeatTypeByAuto(canvas, paint, drawY, mGlobleParams.getSeatTypeRowCount());
        //开始绘制普通出售座位
        drawY = this.getSellSeatDrawCenterY(mGlobleParams.getSeatTypeRowCount(), false);
        drawSellSeats(canvas, paint, mSeatDrawMap, drawX, drawY);

        float[] dotline = this.getCenterDotLine(drawX, viewHeight);
        paint.setColor(Color.RED);
        //根据界面要求高度绘制虚线
        canvas.drawLines(dotline, paint);
    }

    /**
     * 绘制缩略图,此方法绘制依赖于{@link #drawNormalCanvas(Canvas, Paint, float)}
     *
     * @param canvas               画板
     * @param paint                画笔
     * @param originalCanvasWidth  主界面(非缩略图)的实际界面宽度,<font color="#ff9900"><b>此处不是指view的宽度,是canvas绘制出来的宽度</b></font>
     * @param originalCanvasHeight 主界面的实际高度,同上
     */
    protected void drawThumbnail(Canvas canvas, Paint paint, float originalCanvasWidth, float originalCanvasHeight) {

        //若不需要绘制缩略图,则不绘制
        //移动过程中会强制要求显示重绘缩略图
        //因此需要获取是否需要绘制缩略图以判断是否确实需要绘制
        if (!mGlobleParams.getIsDrawThumbnail() || !mGlobleParams.getIsAllowDrawThumbnail()) {
            return;
        }
        RectF thumbnailRectf = this.beginDrawThumbnail(originalCanvasWidth, originalCanvasHeight);
        RectF showRecf = this.getShowRectfInThumbnail(originalCanvasWidth, originalCanvasHeight);

        //绘制缩略图底色(半透明黑色)
        paint.setColor(mGlobleParams.getThumbnailBackgroundColor());
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(mGlobleParams.getThumbnailBgAlpha());
        canvas.drawRect(thumbnailRectf, paint);

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
     * @param beginDrawPositionY 出售座位开始绘制的坐标,<font color="#ff9900"><b>此处的坐标是指第一行开始绘制的出售座位的Y轴坐标,即top而不是centerY</b></font>
     *                           <p>在实际中应该是除去舞台的高度(包括其间隔占用的高度)及座位类型(同理包括其间隔占用的高度)的高度</p>
     * @param seatColumnCount    座位列数,<font color="#ff9900"><b>列数,在二维表中应该是table[0].length</b></font>
     * @param seatRowCount       座位行数,<font color="#ff9900"><b>行数,在二维表中应该是table.length</b></font>
     * @return {@link Point},返回座位在表中对应的行列索引值,若单击点不在有效区域则返回null
     */
    protected Point getClickSeatByPosition(float clickPositionX, float clickPositionY, float beginDrawPositionY, int seatColumnCount, int seatRowCount) {
        //计算列的索引
        int clickColumn = calculateColumnIndex(clickPositionX, seatColumnCount);
        //计算行的索引
        int clickRow = calculateRowIndex(clickPositionY, beginDrawPositionY, seatRowCount);
        showMsg("单击座位结果:row = " + clickRow + "/column = " + clickColumn);
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
    private int calculateRowIndex(float clickPositionY, float beginDrawPositionY, int seatRowCount) {
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
            currentYInterval += increament * mSeatParams.getHeight();
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
    private int calculateColumnIndex(float clickPositionX, int seatColumnCount) {
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
            currentXInterval += increament * (mSeatParams.getWidth());
        } else {
            //奇数列
            //取中间的列数索引
            clickColumn = seatColumnCount / 2;
            //计算去除中心位置的座位的宽度的一半
            lastXInterval = centerXInterval;
            currentXInterval += increament * mSeatParams.getWidth() / 2;
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
            currentXInterval += increament * mSeatParams.getWidth();
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
    private boolean isClickedInInterval(float originalInterval, float lastInterval, float currentInterval) {
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
     * @param newScaleRate     新的缩放比例,该比例可能为1(通常情况下比例为1不缩放,没有意义)
     * @param invalidateAction 重绘的动作标志
     */
    private void invalidateInMultiPoint(float newScaleRate, int invalidateAction) {
        //当前后的缩放比与上一次缩放比相同时不进行重绘,防止反复多次地重绘..
        //如果是最后一次(up事件),除非是不能绘制,否则必定重绘并记录缩放比
        boolean isTrueSetValue = invalidateAction == MotionEvent.ACTION_POINTER_UP;
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
            if (mISeatInformationListener != null) {
                mISeatInformationListener.scaleMaximum();
            }
            return;
        }

        //设置缩放的数据
        //最后一次缩放比为1时,其实与原界面是相同的
        mSeatParams.setScaleRate(newScaleRate, isTrueSetValue);
        mStageParams.setScaleRate(newScaleRate, isTrueSetValue);
        //开始缩放的重绘的标志
        mIsScaleRedraw = true;

        //判断是否已经存放了移动前的偏移数据
        if (!mIsFirstStorePoint) {
            //相对当前屏幕中心的X轴偏移量
            mOffsetPoint.x = mBeginDrawOffsetX;
            //相对当前屏幕中心的Y轴偏移量
            //原来的偏移量是以Y轴顶端为偏移值
            mOffsetPoint.y = mBeginDrawOffsetY - mWHPoint.y / 2;
            mIsFirstStorePoint = true;
        }
        //根据缩放比计算新的偏移值
        mBeginDrawOffsetX = newScaleRate * mOffsetPoint.x;
        //绘制使用的偏移值是相对Y轴顶端而言,所以必须减去半个屏幕的高度(此部分在保存offsetPoint的时候添加了)
        mBeginDrawOffsetY = newScaleRate * mOffsetPoint.y + mWHPoint.y / 2;
        //是否进行up事件,是保存数据当前计算的最后数据
        if (isTrueSetValue) {
            mOffsetPoint.x = mBeginDrawOffsetX;
            mOffsetPoint.y = mBeginDrawOffsetY - mWHPoint.y / 2;
            //重置记录标志亦是
            mIsFirstStorePoint = false;
        }
        //重绘工作
        mDrawView.post(new InvalidateRunnable(mDrawView, invalidateAction));
    }

    /**
     * 根据移动的距离计算是否重新绘制
     *
     * @param moveDistanceX    X轴方向的移动距离(可负值)
     * @param moveDistanceY    Y轴方向的移动距离(可负值)
     * @param invalidateAction 重绘的行为标志
     */

    private boolean invalidateInSinglePoint(float moveDistanceX, float moveDistanceY, int invalidateAction) {
        //此处处理的是按是否进行移动过(默认移动范围为5像素)来确认是否是单击事件
        //而不是按单击事件来确定
        if (Math.abs(moveDistanceX) > 5 || Math.abs(moveDistanceY) > 5) {
            //判断当前重绘时是否由move事件触发,若非move事件触发则将移动标志设置为false
            if (invalidateAction == MotionEvent.ACTION_MOVE) {
                mIsMoved = true;
            } else {
                mIsMoved = false;
            }
        }
        //此处做大于5的判断是为了避免在检测单击事件时
        //有可能会有很微小的变动,避免这种情况下依然会造成移动的效果
        if (Math.abs(moveDistanceX) > 5 || Math.abs(moveDistanceY) > 5 || invalidateAction == MotionEvent.ACTION_UP) {

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
            if (newDrawPositionX != mBeginDrawOffsetX || newDrawPositionY != mBeginDrawOffsetY || invalidateAction == MotionEvent.ACTION_UP) {
                mBeginDrawOffsetX = newDrawPositionX;
                mBeginDrawOffsetY = newDrawPositionY;
                mDrawView.post(new InvalidateRunnable(mDrawView, invalidateAction));
                if (mISeatInformationListener != null) {
                    mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_MOVE);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSingleTouchEventHandle(MotionEvent event, int extraMotionEvent) {
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
                    //已经移动过且建议处理为up事件时
                    if (mIsMoved) {
                        //处理为up事件
                        invalidateInSinglePoint(0, 0, MotionEvent.ACTION_UP);
                        mDownX = 0f;
                        mDownY = 0f;
                        mUpX = 0f;
                        mUpY = 0f;
                        //取消移动重绘的标志
                        mIsMoved = false;
                    }
                    return;
                }

                mUpX = event.getX();
                mUpY = event.getY();
                moveDistanceX = mUpX - mDownX;
                moveDistanceY = mUpY - mDownY;
                //此次移动加数据量达到足够的距离触发移动事件
                //若数据量太小无法满足移动事件的处理,不重置此次的数据留到下一次再使用
                if (invalidateInSinglePoint(moveDistanceX, moveDistanceY, MotionEvent.ACTION_MOVE)) {
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

                invalidateInSinglePoint(moveDistanceX, moveDistanceY, MotionEvent.ACTION_UP);
                //移动操作完把数据还原初始状态,以防出现不必要的错误
                mDownX = 0f;
                mDownY = 0f;
                mUpX = 0f;
                mUpY = 0f;
                mIsMoved = false;
                break;
        }
    }

    /**
     * 双击放大缩小操作
     */
    private void doubleClickScale() {
        //若不允许双击放大缩小,则直接返回
        if (!mGlobleParams.getIsEnabledDoubleClickScale()) {
            return;
        }

        //当前缩放比,当前的界面相对原始界面的比例
        float currentScaleRate = mSeatParams.getScaleRateCompareToOriginal();
        //新的缩放比,用于处理偏移量
        float newScaleRate = 0f;
        //当前缩放比大于3,比默认最大缩放值大了,缩放到默认最大缩放值
        if (currentScaleRate > 3) {
            newScaleRate = mSeatParams.setOriginalValuesToReplaceCurrents(true);
            mStageParams.setOriginalValuesToReplaceCurrents(true);
        } else {
            //当前缩放比小于1.5,接近最小缩放值,缩放到默认最大值
            if (currentScaleRate >= 1 && currentScaleRate < 1.5) {
                newScaleRate = mSeatParams.setOriginalValuesToReplaceCurrents(true);
                mStageParams.setOriginalValuesToReplaceCurrents(true);
            } else {
                //当前缩放比大于1.5小于3,或者是小于1,在最大缩放值与最小缩放值之前,缩放到默认最小值
                newScaleRate = mSeatParams.setOriginalValuesToReplaceCurrents(false);
                mStageParams.setOriginalValuesToReplaceCurrents(false);
            }
        }
        //记录当前的值,写入存储信息(以便下次缩放使用,不是特指双击缩放,而是包括任何方式的缩放)
        mSeatParams.setScaleRate(1, true);
        mStageParams.setScaleRate(1, true);

        //存放移动前的偏移数据
        //相对当前屏幕中心的X轴偏移量
        mOffsetPoint.x = mBeginDrawOffsetX;
        //相对当前屏幕中心的Y轴偏移量
        //原来的偏移量是以Y轴顶端为偏移值
        mOffsetPoint.y = mBeginDrawOffsetY - mWHPoint.y / 2;


        //根据缩放比计算新的偏移值
        mBeginDrawOffsetX = newScaleRate * mOffsetPoint.x;
        //绘制使用的偏移值是相对Y轴顶端而言,所以必须减去半个屏幕的高度(此部分在保存offsetPoint的时候添加了)
        mBeginDrawOffsetY = newScaleRate * mOffsetPoint.y + mWHPoint.y / 2;
        //是否进行up事件,是保存数据当前计算的最后数据
        mOffsetPoint.x = mBeginDrawOffsetX;
        mOffsetPoint.y = mBeginDrawOffsetY - mWHPoint.y / 2;

        //重绘工作
        mDrawView.post(new InvalidateRunnable(mDrawView, MotionEvent.ACTION_UP));

        //当双击事件触发后,取消双击事件,否则可能再次造成触发
        this.cancelEvent(EVENT_DOUBLE_CLICK_DISTANCE);
    }

    /**
     * 单击选择座位事件
     *
     * @param event
     */
    private void singleClickChooseSeat(MotionEvent event) {
        if (mISeatInformationListener != null) {
            mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CLICK);
        }
        //界面没有被移动过,则处理为单击事件
        float clickPositionX = event.getX();
        float clickPositionY = event.getY();
        //计算当前绘制出售座位开始绘制的Y轴坐标位置(在原始界面)
        //此处获取的是开始绘制的出售座位的Y轴坐标顶端,top,不是centerY
        float beginDrawPositionY = this.getSellSeatDrawBeginY(mGlobleParams.getSeatTypeRowCount());
        if (mSeatDrawMap != null && mSeatDrawMap.getRowCount() > 0) {
            //计算单击位置的座位索引值
            Point clickSeatPoint = getClickSeatByPosition(clickPositionX, clickPositionY, beginDrawPositionY, mSeatDrawMap.getMaxColumnCount(), mSeatDrawMap.getRowCount());
            //计算得到的座位索引值在有效范围内
            //行索引应该<=列表行数
            //列索引应该<=列表列数
            if (clickSeatPoint != null && clickSeatPoint.x < mSeatDrawMap.getRowCount() && clickSeatPoint.y < mSeatDrawMap.getColumnCount(clickSeatPoint.x)) {
                if (mISeatInformationListener != null) {
                    //更新座位状态
                    mISeatInformationListener.seatStatus(ISeatInformationListener.STATUS_CHOOSE_SEAT);
                    //通知选中map中的有效区域
                    mISeatInformationListener.chooseInMapSuccess(clickSeatPoint.x, clickSeatPoint.y);
                    //绘制选中座位通知显示

                    //获取当前选中区域的座位类型
                    ISeatEntity seat = mSeatDrawMap.getSeatInfo(clickSeatPoint.x, clickSeatPoint.y);
                    int seatType = SeatParams.SEAT_TYPE_ERROR;
                    //判断当前的座位是否是实际有效座位
                    if (seat != null) {
                        seatType = seat.getType();
                    }
                    //座位类型非不可显示或者错误类型则进行通知
                    if (seatType != SeatParams.SEAT_TYPE_UNSHOW && seatType != SeatParams.SEAT_TYPE_ERROR) {
                        //通知选中实际的有效座位
                        mISeatInformationListener.chooseSeatSuccess(seat.getRowNumber(), seat.getColumnNumber(), clickSeatPoint.x, clickSeatPoint.y);
                        mCurrentSeletedSeat = clickSeatPoint;
                        //显示当前选中座位的通知
                        if (mGlobleParams.getIsDrawSeletedRowColumnNotification()) {
                            mDrawView.post(new InvalidateRunnable(mDrawView, MotionEvent.ACTION_MASK));
                        }
                    }
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

    @Override
    public void onMultiTouchEventHandle(MotionEvent event, int extraMotionEvent) {
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
                    invalidateInMultiPoint(newScaleRate, MotionEvent.ACTION_MOVE);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mScaleFirstUpX = event.getX(0);
                    mScaleFirstUpY = event.getY(0);
                    mScaleSecondUpX = event.getX(1);
                    mScaleSecondUpY = event.getY(1);

                    newScaleRate = this.getScaleRate(mScaleFirstDownX, mScaleFirstDownY, mScaleSecondDownX, mScaleSecondDownY,
                            mScaleFirstUpX, mScaleFirstUpY, mScaleSecondUpX, mScaleSecondUpY);
                    invalidateInMultiPoint(newScaleRate, MotionEvent.ACTION_POINTER_UP);

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
    public void onSingleClickByTime(MotionEvent event) {
    }

    /**
     * 单击某个区域选择座位
     */
    @Override
    public void onSingleClickByDistance(MotionEvent event) {
        this.singleClickChooseSeat(event);
    }

    @Override
    public void onDoubleClickByTime() {
    }

    /**
     * 双击放大缩小
     */
    @Override
    public void onDoubleClickByDistance() {
        this.doubleClickScale();
    }

    /**
     * 根据坐标值计算需要缩放的比例,<font color="#ff9900"><b>返回值为移动距离与按下距离的商,move/down</b></font>
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
     * 重绘的post方法
     */
    protected class InvalidateRunnable implements Runnable {
        //重绘的动作标志
        private int mInvalidateAciton = MotionEvent.ACTION_CANCEL;
        //需要重绘的view
        private View mInvalidateView = null;

        /**
         * 创建重绘方法实例
         *
         * @param invalidateView   重绘的view
         * @param invalidateAction 重绘的动作标志(此处使用的是当前触摸的动作标志)
         *                         <p>
         *                         {@link MotionEvent#ACTION_UP},单击抬起<br/>
         *                         {@link MotionEvent#ACTION_MOVE},单击移动<br/>
         *                         {@link MotionEvent#ACTION_MASK},通知更新座位提醒的绘制<br/>
         *                         {@link MotionEvent#ACTION_CANCEL},取消更新座位提醒的绘制<br/>
         *                         </p>
         */
        public InvalidateRunnable(View invalidateView, int invalidateAction) {
            this.mInvalidateView = invalidateView;
            this.mInvalidateAciton = invalidateAction;
        }

        @Override
        public void run() {
            if (mInvalidateView != null) {
                switch (mInvalidateAciton) {
                    case MotionEvent.ACTION_POINTER_UP:
                    case MotionEvent.ACTION_UP:
                        //是否允许长期存在缩略图
                        mGlobleParams.setIsAllowDrawThumbnail(false);
                        //延迟500毫秒重绘,使结束移动时依然可以保持显示一小段时间
                        mInvalidateView.postInvalidateDelayed(1000);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动过程强制重绘
                        mGlobleParams.setIsAllowDrawThumbnail(true);
                        mInvalidateView.invalidate();
                        break;
                    case MotionEvent.ACTION_MASK:
                        //选中行列绘制提醒
                        mIsDrawSeletedRowColumn = true;
                        mInvalidateView.invalidate();
                        mInvalidateView.postDelayed(new InvalidateRunnable(mInvalidateView, MotionEvent.ACTION_CANCEL), 1500);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        //选中行列取消绘制提醒
                        mIsDrawSeletedRowColumn = false;
                        mInvalidateView.invalidate();
                        break;
                }
            }
        }
    }
}
