package us.bestapp.henrytaro.seatchoose.params.baseparams;/**
 * Created by xuhaolin on 15/9/10.
 */

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;

import us.bestapp.henrytaro.seatchoose.utils.StringUtils;
import us.bestapp.henrytaro.seatchoose.params.interfaces.IStageParams;

/**
 * Created by xuhaolin on 15/9/10.<br/>
 * 舞台绘制参数基本类,该类继承{@link AbsBaseParams}及对外公开接口{@link IStageParams},此接口所有的方法用于对外公开提供给用户调用;
 * 其余此类中的 {@code public} 方法均是用于提供给绘制时使用的方法,需要自定义参数类时请继承此方法重写部分方法即可
 */
public class BaseStageParams extends AbsBaseParams implements IStageParams {
    /**
     * 默认舞台颜色
     */
    public static final int DEFAULT_STAGE_COLOR = Color.WHITE;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 600f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 60f;
    /**
     * 默认舞台圆角度
     */
    public static final float DEFAULT_STAGE_RADIUS = 10f;
    /**
     * 默认舞台与顶端间距
     */
    public static final float DEFAULT_STAGE_MARGIN_TOP = 30f;
    /**
     * 默认舞台下方空白高度（与座位的间隔）
     */
    public static final float DEFAULT_STAGE_MARGIN_BOTTOM = 50f;

    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;

    private OriginalValuesHolder mOriginalHolder = null;
    //用于缩放暂存舞台数据
    private float[] mValueHolder = null;
    //用于检测缩放时是否已暂存数据
    private boolean mIsValueHold = false;
    //默认资源
    private BaseDrawStyle mStageStyle = null;


    public BaseStageParams() {
        super(DEFAULT_STAGE_WIDTH, DEFAULT_STAGE_HEIGHT, DEFAULT_STAGE_RADIUS, DEFAULT_STAGE_COLOR);
        initial();
    }

    protected void initial() {
        this.setLargeScaleWidth(this.getWidth() * 3f);
        this.setSmallScaleWidth(this.getWidth() * 0.1f);
        this.storeOriginalValues(null);

        mStageStyle = new BaseDrawStyle(null, true, DEFAULT_STAGE_COLOR, DEFAULT_STAGE_COLOR, Color.BLACK, "舞台", DEFAULT_INT, null);
    }


    @Override
    public boolean isCanScale(float scaleRate) {
        //保存原值
        float oldHeight = this.getHeight();
        float oldWidth = this.getWidth();

        //计算新值
        float newHeight = oldHeight * scaleRate;
        float newWidth = oldWidth * scaleRate;

        //设置暂时性的新值
        this.setHeight(newHeight, false);
        this.setWidth(newWidth, false);

        //计算新的文字大小,若其值大于880,则不允许进行缩放
        float textSize = this.getDescriptionSize();
        this.setHeight(oldHeight, false);
        this.setWidth(oldWidth, false);
        //图形不可大于此值
        if (newWidth > 4096 || newHeight > 4096 || textSize > 880 || !super.isCanScale(scaleRate)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void updateWidthAndHeightWhenSet(float width, float height) {

    }

    @Override
    public float getDescriptionSize() {
        if (StringUtils.isNullOrEmpty(this.getStageDescription())) {
            return 0;
        } else {
            //计算理论字体大小,以舞台宽度为标准
            float theoryTextLength = this.getWidth() - this.getWidth() * 0.2f;
            float textSize = theoryTextLength / this.getStageDescription().length();
            //理论值大于舞台高度时,以舞台高度为标准
            if (textSize > this.getHeight()) {
                return this.getHeight() * 0.8f;
            } else {
                return textSize;
            }
        }
    }

    @Override
    public void setScaleRate(float scaleRate, boolean isTrueSet) {
        if (mValueHolder == null) {
            mValueHolder = new float[4];
        }
        if (!mIsValueHold) {
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = true;
        }
        this.setWidth(mValueHolder[0] * scaleRate, false);
        this.setHeight(mValueHolder[1] * scaleRate, false);
        this.mStageMarginTop = mValueHolder[2] * scaleRate;
        this.mStageMarginBottom = mValueHolder[3] * scaleRate;
        if (isTrueSet) {
            mValueHolder[0] = this.getWidth();
            mValueHolder[1] = this.getHeight();
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = false;
        }
    }

    @Override
    public BaseDrawStyle getDrawStyle() {
        return mStageStyle;
    }

    @Override
    public String getStageDescription() {
        return mStageStyle.description;
    }

    @Override
    public void setStageDescription(String text) {
        this.mStageStyle.description = text;
    }

    @Override
    public float getStageMarginTop() {
        if (super.isDrawThumbnail()) {
            return mStageMarginTop * super.getThumbnailRate();
        } else {
            return mStageMarginTop;
        }
    }

    @Override
    public void setStageMarginTop(float mStageMarginTop) {
        if (mStageMarginTop == DEFAULT_FLOAT) {
            this.mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            this.mStageMarginTop = mStageMarginTop;
        }
    }

    @Override
    public float getStageMarginBottom() {
        if (super.isDrawThumbnail()) {
            return mStageMarginBottom * super.getThumbnailRate();
        } else {
            return mStageMarginBottom;
        }
    }

    @Override
    public void setStageMarginBottom(float mStageMarginBottom) {
        if (mStageMarginBottom == DEFAULT_FLOAT) {
            this.mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
        } else {
            this.mStageMarginBottom = mStageMarginBottom;
        }
    }

    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    public float getStageTotalHeight() {
        return this.getHeight() + this.getStageMarginBottom() + this.getStageMarginTop();
    }

    /**
     * 获取舞台绘制的路径
     *
     * @param drawCenterX  舞台绘制位置的X轴中心
     * @param drawCenterY  舞台绘制位置的Y轴中心
     * @param isClosedPath 是否闭合路径
     * @return
     */
    public Path getStageDrawPath(float drawCenterX, float drawCenterY, boolean isClosedPath) {
        Path stagePath = new Path();
        PointF[] points = this.getStagePathPoint(drawCenterX, drawCenterY);
        for (int i = 0; i < points.length; i++) {
            PointF p = points[i];
            if (i == 0) {
                //移动到第一个点
                stagePath.moveTo(p.x, p.y);
            } else {
                //连接到其它点
                stagePath.lineTo(p.x, p.y);
            }
        }
        if (isClosedPath) {
            //闭合路径
            stagePath.close();
        }
        return stagePath;
    }

    public void setAutoCalculateToFixScreen(float viewWidth) {
        float eachWidth = viewWidth * 2 / 3;
        float thisWidth = eachWidth;
        float thisHeight = thisWidth * 0.1f;
        float thisRadius = thisWidth * 0.05f;
        this.setDefault(thisWidth, thisHeight, thisRadius, DEFAULT_STAGE_COLOR);
    }


    /**
     * 获取舞台绘制的路径,默认是长方形,<font color="#ff9900"><b>若需要更改舞台的绘制形状,覆盖此方法即可</b></font>
     *
     * @param drawCenterX 舞台绘制位置的中心X轴坐标
     * @param drawCenterY 舞台绘制位置的中心Y轴坐标
     * @return
     */
    protected PointF[] getStagePathPoint(float drawCenterX, float drawCenterY) {
        PointF[] points = new PointF[4];

        for (int i = 0; i < 4; i++) {
            points[i] = new PointF();
        }

        //左上角
        points[0].x = drawCenterX - this.getWidth() / 2;
        points[0].y = drawCenterY - this.getHeight() / 2;

        //左下角
        points[1].x = points[0].x;
        points[1].y = points[0].y + this.getHeight();

        //右下角
        points[2].x = drawCenterX + this.getWidth() / 2;
        points[2].y = points[1].y;

        //右上角
        points[3].x = points[2].x + this.getHeight();
        points[3].y = points[2].y - this.getHeight();

        return points;
    }

    @Override
    public float getScaleRateCompareToOriginal() {
        if (mOriginalHolder != null) {
            return this.getWidth() / mOriginalHolder.width;
        } else {
            return DEFAULT_FLOAT;
        }
    }

    @Override
    public float setOriginalValuesToReplaceCurrents(float fixScaleRate) {
        float oldScaleRate = 0f;
        float targetScaleRate = fixScaleRate;
//        if (fixScaleRate) {
//            targetScaleRate = 2f;
//        } else {
//            targetScaleRate = 1f;
//        }
        oldScaleRate = mOriginalHolder.width * targetScaleRate / this.getWidth();

        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mStageMarginTop = mOriginalHolder.marginTop * targetScaleRate;
        this.mStageMarginBottom = mOriginalHolder.marginBottom * targetScaleRate;

        return oldScaleRate;
    }

    /**
     * 未使用
     *
     * @param targetScaleRate
     */
    public void setNewParamsValues(float targetScaleRate) {
        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mStageMarginTop = mOriginalHolder.marginTop * targetScaleRate;
        this.mStageMarginBottom = mOriginalHolder.marginBottom * targetScaleRate;
    }

    @Override
    public void storeOriginalValues(Object copyObj) {
        if (mOriginalHolder == null) {
            mOriginalHolder = new OriginalValuesHolder();
        }
        if (copyObj == null) {
            mOriginalHolder.width = this.getWidth();
            mOriginalHolder.height = this.getHeight();
            mOriginalHolder.radius = this.getRadius();
            mOriginalHolder.marginTop = this.getStageMarginTop();
            mOriginalHolder.marginBottom = this.getStageMarginBottom();
        } else if (copyObj instanceof BaseStageParams.OriginalValuesHolder) {
            OriginalValuesHolder newHolder = (OriginalValuesHolder) copyObj;

            mOriginalHolder.width = newHolder.width;
            mOriginalHolder.height = newHolder.height;
            mOriginalHolder.radius = newHolder.radius;
            mOriginalHolder.marginTop = newHolder.marginTop;
            mOriginalHolder.marginBottom = newHolder.marginBottom;
        } else {
            throw new RuntimeException("参数类型出错,请根据注释提醒进行传参");
        }
    }

    @Override
    public Object getOriginalValues() {
        return mOriginalHolder;
    }

    @Override
    public Object getClone(Object newObj) {
        if (newObj != null && !(newObj.getClass() != this.getClass())) {
            throw new RuntimeException("对象类型错误,无法进行克隆!");
        } else if (newObj == null) {
            newObj = new BaseStageParams();
        }

        boolean isThumbnail = this.isDrawThumbnail();
        this.setIsDrawThumbnail(false, DEFAULT_INT, DEFAULT_INT);

        BaseStageParams newParams = (BaseStageParams) newObj;
        //获取默认原始值
        OriginalValuesHolder holder = (OriginalValuesHolder) this.getOriginalValues();

        newParams.storeOriginalValues(holder);
        //设置当前值
        newParams.setWidth(this.getWidth(), false);
        newParams.setHeight(this.getHeight(), false);
        newParams.setRadius(this.getRadius());
        newParams.setStageMarginTop(this.getStageMarginTop());
        newParams.setStageMarginBottom(this.getStageMarginBottom());

        //设置其它的参数值
        newParams.setIsDraw(this.isDraw());
        newParams.setIsDrawThumbnail(isThumbnail, 0, 0);
        newParams.setThumbnailRate(this.getThumbnailRate());
        //设置图片资源
//        newParams.setImage(this.getImageId());
//        newParams.setImage(this.getImageBitmap());
        newParams.getDrawStyle().imageID = this.getDrawStyle().imageID;
        newParams.getDrawStyle().bitmap = this.getDrawStyle().bitmap;
        //设置绘制方式
        //此部分必须在最后设置,因为一旦设置了图片资源,则会默认将绘制方式修改为图片绘制模式
        newParams.setDrawType(this.getDrawType(true));

        this.setIsDrawThumbnail(isThumbnail, DEFAULT_INT, DEFAULT_INT);
        return newParams;
    }

    /**
     * 原始舞台数据的保存,此处的原始是指<font color="#ff9900"><b>舞台默认设定的宽高或者用户设定的宽高,即第一次运行并显示出来的界面即为原始界面;
     * 当用户对宽高做修改时,也会重新记录此数据</b></font>
     */
    protected class OriginalValuesHolder {
        public float width;
        public float height;
        public float radius;
        public float marginTop;
        public float marginBottom;
    }
}
