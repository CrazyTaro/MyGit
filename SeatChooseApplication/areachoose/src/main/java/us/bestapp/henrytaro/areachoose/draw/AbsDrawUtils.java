package us.bestapp.henrytaro.areachoose.draw;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.areachoose.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AbsDrawUtils extends AbsTouchEventHandle {
    private Context mContext = null;
    private View mDrawView = null;
    private List<AbsAreaEntity> mAreaList = null;

    private Bitmap mFtBitmap = null;
    private Bitmap mBgBitmap = null;
    private Bitmap mMaskBitmap = null;

    private RectF mDstRectf = null;
    private RectF mScrRectf = null;
    private RectF mDrawRectf = null;
    private Paint mPaint = null;

    private boolean mIsFirstDraw = true;
    private boolean mIsMaskSuccess = false;
    private float mOriginalScaleRate = 0;
    private float mRectfScaleRate = 0;
    private float mBitmapScaleRate = 1;
    protected PointF mWHPoint = null;


    //是否第一次存放偏移量
    private boolean mIsFirstStorePoint = false;
    //上一次的缩放比例
    private float mLastScaleRate = 1f;
    /**
     * 用户触摸偏移量
     */
    protected float mBeginDrawOffsetY = 0f;
    protected float mBeginDrawOffsetX = 0f;
    //用于暂时存放在开始移动缩放前的X/Y偏移量
    private PointF mOffsetPoint = new PointF();
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

    public AbsDrawUtils(Context context, View drawView) {
        this.mContext = context;
        this.mDrawView = drawView;

        mDrawView.setOnTouchListener(this);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inMutable = true;

        mDstRectf = new RectF();
        mScrRectf = new RectF();
        mDrawRectf = new RectF();

        mPaint = new Paint();
    }

    public void setAreaList(List<AbsAreaEntity> areaList) {
        this.mAreaList = areaList;
    }

    public boolean setDrawBitmap(int frontBmpID, int backBmpID) {
        try {
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

    public boolean setDrawBitmap(Bitmap frontBitmap, Bitmap backBitmap) {
        if (frontBitmap == null || backBitmap == null) {
            return false;
        } else {
            mFtBitmap = frontBitmap;
            mBgBitmap = backBitmap;
            mMaskBitmap = mBgBitmap.copy(mBgBitmap.getConfig(), true);
            return true;
        }
    }

    protected boolean createSoldOutBitmap(List<AbsAreaEntity> areaList) {
        if (mFtBitmap == null || mBgBitmap == null || mMaskBitmap == null) {
            throw new RuntimeException("尚未设置图片");
        }
        if (areaList == null || areaList.size() <= 0) {
            return false;
        } else {
            List<Integer> soldOutColorList = new ArrayList<>();
            for (AbsAreaEntity area : areaList) {
                if (area.isSoldOut()) {
                    soldOutColorList.add(area.getAreaColor());
                }
            }
            int lastColor = 0;

            for (int i = 0; i < mMaskBitmap.getWidth(); i++) {
                for (int j = 0; j < mMaskBitmap.getHeight(); j++) {
                    int color = mMaskBitmap.getPixel(i, j);
                    for (int soldOutColor : soldOutColorList) {
                        if (color == soldOutColor) {
                            mMaskBitmap.setPixel(i, j, Color.BLACK);
                            break;
                        } else {
                            mMaskBitmap.setPixel(i, j, Color.TRANSPARENT);
                        }
                    }
                }
            }

            return true;
        }
    }

    protected Bitmap getMutableBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isMutable()) {
            return bitmap;
        } else {
            return bitmap.copy(bitmap.getConfig(), true);
        }
    }

    public void getWidthAndHeight() {
        if (mWHPoint == null) {
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


    public void drawCanvas(Canvas canvas) {
        if (mFtBitmap != null && mIsFirstDraw) {
            getWidthAndHeight();

            float dstWidth = 0f;
            float dstHeight = 0f;

            mOriginalScaleRate = mWHPoint.x / mFtBitmap.getWidth();
            mRectfScaleRate = mOriginalScaleRate;
            dstWidth = mFtBitmap.getWidth() * mOriginalScaleRate;
            dstHeight = mFtBitmap.getHeight() * mOriginalScaleRate;

            mDstRectf.left = mWHPoint.x / 2 - dstWidth / 2;
            mDstRectf.right = mDstRectf.left + dstWidth;
            mDstRectf.top = mWHPoint.y / 2 - dstHeight / 2;
            mDstRectf.bottom = mDstRectf.top + dstHeight;

            mIsMaskSuccess = createSoldOutBitmap(mAreaList);
            mIsFirstDraw = false;

            mScrRectf.left = mDstRectf.left;
            mScrRectf.right = mDstRectf.right;
            mScrRectf.top = mDstRectf.top;
            mScrRectf.bottom = mDstRectf.bottom;
        }

        mDrawRectf.top = mDstRectf.top + mBeginDrawOffsetY;
        mDrawRectf.bottom = mDstRectf.bottom + mBeginDrawOffsetY;
        mDrawRectf.left = mDstRectf.left + mBeginDrawOffsetX;
        mDrawRectf.right = mDstRectf.right + mBeginDrawOffsetX;
        if (mFtBitmap != null) {
            canvas.drawBitmap(mFtBitmap, null, mDrawRectf, null);
        }
        if (mIsMaskSuccess && mMaskBitmap != null) {
            canvas.drawBitmap(mMaskBitmap, null, mDrawRectf, null);
        }
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

    protected boolean isCanScale(float newScaleRate) {
        int width = mFtBitmap.getWidth();
        int height = mFtBitmap.getHeight();

        float scaleWidth = width * mBitmapScaleRate * newScaleRate;
        float scaleHeight = height * mBitmapScaleRate * newScaleRate;

        if (scaleWidth > width * 4 || scaleWidth < width * 0.5f) {
            return false;
        } else {
            return true;
        }
    }

    protected void setScaleRate(float newScaleRate, boolean isTrueSet) {
        float newWidth = mFtBitmap.getWidth() * mBitmapScaleRate * newScaleRate;
        float newHeight = mFtBitmap.getWidth() * mBitmapScaleRate * newScaleRate;
        getWidthAndHeight();

        mDstRectf.left = mWHPoint.x / 2 - newWidth / 2;
        mDstRectf.right = mDstRectf.left + newWidth;
        mDstRectf.top = mWHPoint.y / 2 - newHeight / 2;
        mDstRectf.bottom = mDstRectf.top + newHeight;

        if (isTrueSet) {
            mBitmapScaleRate *= newScaleRate;
            mRectfScaleRate = mDstRectf.width() / mFtBitmap.getWidth();
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

        if (this.isCanScale(newScaleRate)) {
            mLastScaleRate = newScaleRate;
        } else {
            return;
        }

        if (isTrueSetValue) {
            //若缩放比不合法且当前缩放为最后一次缩放(up事件),则将上一次的缩放比作为此次的缩放比,用于记录数据
            //且将最后的缩放比设为1(因为已经达到缩放的上限,再缩放也不会改变,所以比例使用1)
            //此处不作此操作会导致在缩放的时候达到最大值后放手,再次缩放会在最开始的时候复用上一次缩放的结果(有闪屏的效果...)
            newScaleRate = mLastScaleRate;
            mLastScaleRate = 1;
        }


        //设置缩放的数据
        //最后一次缩放比为1时,其实与原界面是相同的
        this.setScaleRate(newScaleRate, isTrueSetValue);

//        //判断是否已经存放了移动前的偏移数据
//        if (!mIsFirstStorePoint) {
//            //相对当前屏幕中心的X轴偏移量
//            mOffsetPoint.x = mBeginDrawOffsetX + mWHPoint.x / 2;
//            //相对当前屏幕中心的Y轴偏移量
//            //原来的偏移量是以Y轴顶端为偏移值
//            mOffsetPoint.y = mBeginDrawOffsetY + mWHPoint.y / 2;
//            mIsFirstStorePoint = true;
//        }
//        //根据缩放比计算新的偏移值
//        mBeginDrawOffsetX = newScaleRate * mOffsetPoint.x - mWHPoint.x / 2;
//        //绘制使用的偏移值是相对Y轴顶端而言,所以必须减去半个屏幕的高度(此部分在保存offsetPoint的时候添加了)
//        mBeginDrawOffsetY = newScaleRate * mOffsetPoint.y - mWHPoint.y / 2;
//        //是否进行up事件,是保存数据当前计算的最后数据
//        if (isTrueSetValue) {
//            mOffsetPoint.x = mBeginDrawOffsetX + mWHPoint.x / 2;
//            mOffsetPoint.y = mBeginDrawOffsetY + mWHPoint.y / 2;
//            //重置记录标志亦是
//            mIsFirstStorePoint = false;
//        }
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
//            //新的开始绘制的界面中心X轴坐标
//            float newStartDrawCenterX = newDrawPositionX + mWHPoint.x / 2;
//            //新的开始绘制的界面最顶端
//            float newStartDrawCenterY = newDrawPositionY + mWHPoint.y / 2;

            //当前绘制的最左边边界坐标大于0(即边界已经显示在屏幕上时),且移动方向为向右移动
            if (Math.abs(newDrawPositionX) > (mDstRectf.width() / 2 - mWHPoint.x / 2)) {
                //保持原来的偏移量不变
                newDrawPositionX = mBeginDrawOffsetX;
            }
            //当前绘制的顶端坐标大于0且移动方向为向下移动
            if (Math.abs(newDrawPositionY) > (mDstRectf.height() / 2 - mWHPoint.y / 2)) {
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
        if (mFtBitmap == null || mBgBitmap == null || mAreaList == null) {
            return;
        }

        float x = event.getX();
        float y = event.getY();

        float distanceX = x - mWHPoint.x / 2;
        float distanceY = y - mWHPoint.y / 2;
        float dstX = x - mDstRectf.left;
        float dstY = y - mDstRectf.top;


        int orlx = 0;
        int orly = 0;
        orlx = (int) (dstX / mRectfScaleRate);
        orly = (int) (dstY / mRectfScaleRate);
        if (orlx > mScrRectf.left && orlx < mScrRectf.right && orly > mScrRectf.top && orly < mScrRectf.bottom) {

            int color = mBgBitmap.getPixel(orlx, orly);
            Log.i("color", color + "");

            for (AbsAreaEntity area : mAreaList) {
                if (color == area.getAreaColor()) {
                    Toast.makeText(mContext, "选中颜色为:" + area.getAreaColorName() + "/选中区为:" + area.getAreaName(), Toast.LENGTH_SHORT).show();
                }
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
