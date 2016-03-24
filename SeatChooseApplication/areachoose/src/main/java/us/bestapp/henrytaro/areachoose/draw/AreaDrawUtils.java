package us.bestapp.henrytaro.areachoose.draw;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.bestapp.henrytaro.areachoose.draw.interfaces.IAreaDrawInterfaces;
import us.bestapp.henrytaro.areachoose.draw.interfaces.IAreaEventHandle;
import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;
import us.bestapp.henrytaro.areachoose.utils.AbsMaskColorUtils;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AreaDrawUtils extends AbsTouchEventHandle implements IAreaDrawInterfaces {
    private Context mContext = null;
    private View mDrawView = null;
    private AbsMaskColorUtils mMaskColorUtils = null;
    private IAreaEventHandle mAreaEventHandle = null;
    private Map<Integer, AbsAreaEntity> mAreaMap = null;
    //区域列表
    private List<AbsAreaEntity> mAreaList = null;

    //前景图像
    private Bitmap mFtBitmap = null;
    //背景图像
    private Bitmap mBgBitmap = null;
    //蒙板图层
    private Bitmap mMaskBitmap = null;

    //目标显示区域
    private RectF mDstRectf = null;
    //图像原始区域大小(即图像大小)
    private RectF mScrRectf = null;
    //绘制的实际区域(包括了偏移值)
    private RectF mDrawRectf = null;
    //缓存目标显示区域
    private RectF mTempRectf = null;
    private Paint mPaint = null;

    //是否第一次绘制界面
    private boolean mIsFirstDraw = true;
    //是否初始化蒙板层成功
    private boolean mIsMaskSuccess = false;
    private boolean mIsAllAlpha = true;
    //图像默认适应控件屏幕大小,此值为图像大小与屏幕大小的比例关系
    private float mOriginalScaleRate = 0;
    //当前图像与当前目标显示区域的大小比例
    private float mRectfScaleRate = 0;
    //屏幕宽高存储点
    protected PointF mWHPoint = null;

    //上一次的缩放比例
    private float mLastScaleRate = 1f;
    /**
     * 用户触摸偏移量
     */
    protected float mBeginDrawOffsetY = 0f;
    protected float mBeginDrawOffsetX = 0f;
    //多点触控缩放按下坐标
    private float mScaleFirstDownX = 0f;
    private float mScaleFirstDownY = 0f;
    private float mScaleSecondDownX = 0f;
    private float mScaleSecondDownY = 0f;
    private float mScaleFirstUpX = 0f;
    private float mScaleFirstUpY = 0f;
    private float mScaleSecondUpX = 0f;
    private float mScaleSecondUpY = 0f;
    //按下事件的坐标
    private float mDownX = 0f;
    private float mDownY = 0f;
    //抬起事件的坐标
    private float mUpX = 0f;
    private float mUpY = 0f;
    //是否已经移动过(满足移动条件)
    private boolean mIsMoved = false;

    public AreaDrawUtils(Context context, View drawView) {
        this.mContext = context;
        this.mDrawView = drawView;

        mDrawView.setOnTouchListener(this);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inMutable = true;

        mDstRectf = new RectF();
        mScrRectf = new RectF();
        mDrawRectf = new RectF();
        mTempRectf = new RectF();

        mPaint = new Paint();
    }

    @Override
    public void initial(List<AbsAreaEntity> areaList, int frontBmpID, int backBmpID, IAreaEventHandle event) {
        this.setAreaList(areaList);
        this.setDrawBitmap(frontBmpID, backBmpID);
        this.mAreaEventHandle = event;
        initialBitmap();
    }

    @Override
    public void initial(List<AbsAreaEntity> areaList, Bitmap frontBmp, Bitmap backBmp, IAreaEventHandle event) {
        this.setAreaList(areaList);
        this.setDrawBitmap(frontBmp, backBmp);
        this.mAreaEventHandle = event;
        initialBitmap();
    }

    /**
     * 设置区域列表数据
     *
     * @param areaList
     */
    protected void setAreaList(List<AbsAreaEntity> areaList) {
        if (mAreaMap == null) {
            mAreaMap = new HashMap<>();
        } else {
            mAreaMap.clear();
        }
        for (AbsAreaEntity area : areaList) {
            mAreaMap.put(area.getAreaColor(), area);
        }
        this.mAreaList = areaList;
    }

    /**
     * 设置图像资源ID,此方法会加载资源ID并使用{@link #setDrawBitmap(Bitmap, Bitmap)}设置图像资源
     *
     * @param frontBmpID 前景显示的图片ID
     * @param backBmpID  用于处理数据的图片ID
     * @return
     */
    protected boolean setDrawBitmap(int frontBmpID, int backBmpID) {
        try {
            //加载资源ID
            Bitmap frontBitmap = BitmapFactory.decodeResource(mContext.getResources(), frontBmpID);
            Bitmap backBitmap = BitmapFactory.decodeResource(mContext.getResources(), backBmpID);
            if (frontBitmap == null || backBitmap == null) {
                return false;
            } else {
                return this.setDrawBitmap(frontBitmap, backBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置图像资源,<font color="#ff9900"><b>此方法若在{@link #setDrawBitmap(int, int)}之后使用将会覆盖原来的资源ID加载的图像资源</b></font>
     *
     * @param frontBitmap 前景显示的图像
     * @param backBitmap  用于处理数据的图像
     * @return
     */
    protected boolean setDrawBitmap(Bitmap frontBitmap, Bitmap backBitmap) {
        if (frontBitmap == null || backBitmap == null) {
            return false;
        } else {
            mFtBitmap = frontBitmap;
            mBgBitmap = backBitmap;
            //创建蒙板图层(资源来自背景处理图像)
            mMaskBitmap = mBgBitmap.copy(mBgBitmap.getConfig(), true);
            return true;
        }
    }

    /**
     * 初始化图像,<font color="#ff9900"><b>此方法很重要,此方法只能在设置完图像及区域列表之后调用,否则数据将无法正常获取或者显示</b></font>
     *
     * @return
     */
    protected void initialBitmap() {
        if (mFtBitmap == null || mBgBitmap == null || mMaskBitmap == null || mAreaMap == null) {
            return;
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (mAreaEventHandle != null) {
                        mAreaEventHandle.onStartLoadMaskBitmap();
                    }
                    //创建蒙板图层
                    mIsMaskSuccess = createMaskBitmap(mAreaList);
                    if (mAreaEventHandle != null) {
                        mAreaEventHandle.onFinishLoadMaskBitmap(mIsMaskSuccess);
                    }
                }
            }).start();
            return;
        }
    }

    /**
     * 创建蒙板图像
     *
     * @param areaList 区域列表信息
     * @return
     */
    protected boolean createMaskBitmap(List<AbsAreaEntity> areaList) {
        if (mFtBitmap == null || mBgBitmap == null || mMaskBitmap == null) {
            throw new RuntimeException("尚未设置图片");
        } else if (mMaskColorUtils == null) {
            throw new RuntimeException("尚未设置图层蒙板工具类");
        }
        if (areaList == null || areaList.size() <= 0) {
            return false;
        } else {
            //遍历蒙板图像进行颜色替换处理
            for (int i = 0; i < mMaskBitmap.getWidth(); i++) {
                for (int j = 0; j < mMaskBitmap.getHeight(); j++) {
                    //获取当前位置的像素颜色值
                    int color = mMaskBitmap.getPixel(i, j);
                    int newColor = mMaskColorUtils.getMaskColor(color);
                    if (color != newColor) {
                        mMaskBitmap.setPixel(i, j, newColor);
                    }
//                    if (newColor != Color.TRANSPARENT && mIsAllAlpha) {
//                        mIsAllAlpha = false;
//                    }
                }
            }
            return true;
        }
    }

    /**
     * 获取可编辑的图像
     *
     * @param bitmap 原不可编辑的图像(res资源加载的图像默认为不可编辑)
     * @return
     */
    protected Bitmap getMutableBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isMutable()) {
            return bitmap;
        } else {
            return bitmap.copy(bitmap.getConfig(), true);
        }
    }

    /**
     * 获取控件的宽高,为了得到正确的数据,建议第一次使用是在绘制的过程中,保证控件的宽高是计算出来且有效的;
     * 此方法仅会处理一次,除非参数强制要求重新处理
     *
     * @param isReload 是否强制重新获取数据
     */
    protected void getWidthAndHeight(boolean isReload) {
        if (mWHPoint == null || isReload) {
            mWHPoint = new PointF();
            int width = 0;
            int height = 0;
            width = mDrawView.getWidth();
            height = mDrawView.getHeight();

            if (width <= 0 || height <= 0) {
                width = mDrawView.getMeasuredWidth();
                height = mDrawView.getHeight();
            }
            mWHPoint.x = width;
            mWHPoint.y = height;
        }
    }

    @Override
    public void drawCanvas(Canvas canvas) {
        //确保前景图像存在且是第一次绘制
        //同时必须已经处理了蒙板图像层
        if (mFtBitmap != null && mIsFirstDraw && mIsMaskSuccess) {
            getWidthAndHeight(false);

            float dstWidth = 0f;
            float dstHeight = 0f;

            //计算当前图像适应屏幕的比例
            mOriginalScaleRate = mWHPoint.x / mFtBitmap.getWidth();
            //计算目标图像显示宽高(即绘制区域)
            dstWidth = mFtBitmap.getWidth() * mOriginalScaleRate;
            dstHeight = mFtBitmap.getHeight() * mOriginalScaleRate;

            //创建图像显示区域
            mDstRectf.left = mWHPoint.x / 2 - dstWidth / 2;
            mDstRectf.right = mDstRectf.left + dstWidth;
            mDstRectf.top = mWHPoint.y / 2 - dstHeight / 2;
            mDstRectf.bottom = mDstRectf.top + dstHeight;

            //取消第一次绘制的标志
            mIsFirstDraw = false;

            //创建原始图像区域的数据
            mScrRectf.left = mWHPoint.x / 2 - mFtBitmap.getWidth() / 2;
            mScrRectf.right = mScrRectf.left + mFtBitmap.getWidth();
            mScrRectf.top = mWHPoint.y / 2 - mFtBitmap.getHeight() / 2;
            mScrRectf.bottom = mScrRectf.top + mFtBitmap.getHeight();

            //创建缓存目标显示区域
            mTempRectf.left = mDstRectf.left;
            mTempRectf.right = mDstRectf.right;
            mTempRectf.top = mDstRectf.top;
            mTempRectf.bottom = mDstRectf.bottom;
        }

        //创建绘制区域
        //此处是处理了偏移量
        mDrawRectf.top = mDstRectf.top + mBeginDrawOffsetY;
        mDrawRectf.bottom = mDstRectf.bottom + mBeginDrawOffsetY;
        mDrawRectf.left = mDstRectf.left + mBeginDrawOffsetX;
        mDrawRectf.right = mDstRectf.right + mBeginDrawOffsetX;

        //当前前景图像存在才进行绘制
        if (mFtBitmap != null) {
            canvas.drawBitmap(mFtBitmap, null, mDrawRectf, null);
        }
        //当前蒙板图像及蒙板处理成功才进行绘制
        if (mIsMaskSuccess && mMaskBitmap != null ) {
            if (mMaskColorUtils != null) {
                mPaint.setAlpha(mMaskColorUtils.getAlpha());
            }
            canvas.drawBitmap(mMaskBitmap, null, mDrawRectf, mPaint);
        }
    }

    @Override
    public void setAreaMaskColorUtils(AbsMaskColorUtils utils) {
        this.mMaskColorUtils = utils;
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
     * 是否可以进行缩放
     *
     * @param newScaleRate 新的缩放比例
     * @return
     */
    protected boolean isCanScale(float newScaleRate) {
        getWidthAndHeight(false);
        //获取前景图像的原始大小(即图像的原始大小,背景图也是与前景图像是相同的)
        float width = mWHPoint.x;
        float height = mWHPoint.y;

        //计算新的缩放后的大小
        //此处使用了图像相对本身的缩放比例 * 新的缩放比例
        float scaleWidth = mTempRectf.width() * newScaleRate;
        float scaleHeight = mTempRectf.height() * newScaleRate;

        //缩放后的大小为原图的4位或者小于0.5倍则不合法
        if (scaleWidth > width * 4 || scaleWidth < width * 0.5f) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置缩放比
     *
     * @param newScaleRate 新的缩放比
     * @param isTrueSet    是否保存当前缩放后的具体数据
     */
    protected void setScaleRate(float newScaleRate, boolean isTrueSet) {
        float newWidth = mTempRectf.width() * newScaleRate;
        float newHeight = mTempRectf.height() * newScaleRate;
        getWidthAndHeight(false);

        //计算新的图像目标显示区域(以屏幕中心为基准)
        mDstRectf.left = mWHPoint.x / 2 - newWidth / 2;
        mDstRectf.right = mDstRectf.left + newWidth;
        mDstRectf.top = mWHPoint.y / 2 - newHeight / 2;
        mDstRectf.bottom = mDstRectf.top + newHeight;

        //若需要保存数据,则对处理进行保存
        if (isTrueSet) {
            //将确定缩放的结果数据保存到缓存中
            mTempRectf.left = mDstRectf.left;
            mTempRectf.right = mDstRectf.right;
            mTempRectf.top = mDstRectf.top;
            mTempRectf.bottom = mDstRectf.bottom;
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

        //是否可进行绽放
        if (this.isCanScale(newScaleRate)) {
            mLastScaleRate = newScaleRate;
        } else if (isTrueSetValue) {
            //若缩放比不合法且当前缩放为最后一次缩放(up事件),则将上一次的缩放比作为此次的缩放比,用于记录数据
            //且将最后的缩放比设为1(因为已经达到缩放的上限,再缩放也不会改变,所以比例使用1)
            //此处不作此操作会导致在缩放的时候达到最大值后放手,再次缩放会在最开始的时候复用上一次缩放的结果(有闪屏的效果...)
            newScaleRate = mLastScaleRate;
            mLastScaleRate = 1;
        } else {
            return;
        }

        //设置缩放的数据
        //最后一次缩放比为1时,其实与原界面是相同的
        this.setScaleRate(newScaleRate, isTrueSetValue);

        //重绘工作
        mDrawView.postInvalidate();
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

            //当前移动方向为向右,且绘制界面已达到左边界
            if ((moveDistanceX > 0 && mDrawRectf.left >= 0) ||
                    //当前移动方向为向左,且绘制界面已达到右边界
                    (moveDistanceX < 0 && mDrawRectf.right <= mWHPoint.x)) {
                //保持原来的偏移量不变
                newDrawPositionX = mBeginDrawOffsetX;
            }
            //当前移动方向为向上,且绘制界面已达到下边界
            if ((moveDistanceY < 0 && mDrawRectf.bottom <= mWHPoint.y) ||
                    //当前移动方向为向下,且绘制界面已达到上边界
                    (moveDistanceY > 0 && mDrawRectf.top >= 0)) {
                //保持原来的Y轴偏移量
                newDrawPositionY = mBeginDrawOffsetY;
            }

            //其它情况正常移动重绘
            //当距离确实有效地改变了再进行重绘制,否则原界面不变,减少重绘的次数
            if (newDrawPositionX != mBeginDrawOffsetX || newDrawPositionY != mBeginDrawOffsetY || invalidateAction == MotionEvent.ACTION_UP) {
                mBeginDrawOffsetX = newDrawPositionX;
                mBeginDrawOffsetY = newDrawPositionY;
                mDrawView.postInvalidate();
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

    @Override
    public void onSingleClickByDistance(MotionEvent event) {
        if (mFtBitmap == null || mBgBitmap == null || mAreaMap == null) {
            return;
        }
        //计算当前目标显示区域与实际上的图像区域的比例
        mRectfScaleRate = mDstRectf.width() / mFtBitmap.getWidth();

        float x = event.getX();
        float y = event.getY();

        //计算单击点在目标显示区域中的相对位置
        float dstX = x - mDrawRectf.left;
        float dstY = y - mDrawRectf.top;

        int orlx = 0;
        int orly = 0;
        //计算单击点映射到图像区域的相对位置
        orlx = (int) (dstX / mRectfScaleRate);
        orly = (int) (dstY / mRectfScaleRate);
        //判断当前单击点是否在图像区域的有效位置内
        if (orlx >= 0 && orlx < mBgBitmap.getWidth() && orly >= 0 && orly < mBgBitmap.getHeight()) {
            //获取图像区域内单击点的像素颜色值
            int color = mBgBitmap.getPixel(orlx, orly);
            //对像素值进行分析处理
//            for (AbsAreaEntity area : mAreaList) {
//                if (color == area.getAreaColor()) {
//                    if (mAreaEventHandle != null) {
//                        mAreaEventHandle.onAreaChoose(area);
//                    }
//                }
//            }
            AbsAreaEntity area = mAreaMap.get(color);
            if (area != null && mAreaEventHandle != null) {
                mAreaEventHandle.onAreaChoose(area);
            }
        }
    }

    @Override
    public void onDoubleClickByTime() {

    }

    @Override
    public void onDoubleClickByDistance() {

    }
}
