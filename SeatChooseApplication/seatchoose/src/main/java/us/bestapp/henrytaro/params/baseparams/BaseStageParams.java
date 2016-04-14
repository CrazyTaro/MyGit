package us.bestapp.henrytaro.params.baseparams;/**
 * Created by xuhaolin on 15/9/10.
 */

import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import us.bestapp.henrytaro.params.interfaces.IStageParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/10.<br/>
 * 舞台绘制参数基本类,该类继承{@link AbsBaseParams}及对外公开接口{@link IStageParams},此接口所有的方法用于对外公开提供给用户调用;
 * 其余此类中的 {@code public} 方法均是用于提供给绘制时使用的方法,需要自定义参数类时请继承此方法重写部分方法即可
 */
public class BaseStageParams extends AbsBaseParams implements IStageParams, Cloneable {
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
    //默认资源
    private BaseDrawStyle mStageStyle = null;


    public BaseStageParams() {
        this(DEFAULT_STAGE_WIDTH, DEFAULT_STAGE_HEIGHT, DEFAULT_STAGE_RADIUS, DEFAULT_STAGE_COLOR, 3f, 0.5f);
    }

    public BaseStageParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor, float largeScale, float smallScale) {
        super(defaultWidth, defaultHeight, defaultRadius, defaultColor, largeScale, smallScale);
        initial();
    }

    protected void initial() {
        this.setLargeScaleRate(this.getDrawWidth() * 3f);
        this.setSmallScaleRate(this.getDrawWidth() * 0.1f);
        this.storeDefaultValues(null);

        mStageStyle = new BaseDrawStyle(null, true, DEFAULT_STAGE_COLOR, DEFAULT_STAGE_COLOR, Color.BLACK, "舞台", DEFAULT_INT, null);
    }


    @Override
    public boolean isCanScale(float scaleRate) {
        //计算新的文字大小,若其值大于880,则不允许进行缩放
        float textSize = this.getDrawHeight() * 0.8f;
        //图形不可大于此值
        if (textSize > 880 || !super.isCanScale(scaleRate)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void updateWidthAndHeightWhenSet(float newWidth, float newHeight) {

    }

    @Override
    public float getDescriptionSize(float textRate) {
        if (StringUtils.isNullOrEmpty(this.getStageDescription())) {
            return 0;
        } else {
            //计算理论字体大小,以舞台宽度为标准
            float theoryTextLength = this.getDrawWidth() - this.getDrawWidth() * 0.2f;
            float textSize = theoryTextLength / this.getStageDescription().length();
            //理论值大于舞台高度时,以舞台高度为标准
            if (textSize > this.getDrawHeight()) {
                return this.getDrawHeight() * 0.8f;
            } else {
                return textSize;
            }
        }
    }

    @Override
    public void setScaleRate(float scaleRate, boolean isTrueSet) {
        super.setScaleRate(scaleRate, isTrueSet);
        if (mValueHolder == null) {
            mValueHolder = new float[2];
            mValueHolder[0] = this.mStageMarginTop;
            mValueHolder[1] = this.mStageMarginBottom;
        }
        this.mStageMarginTop = mValueHolder[0] * scaleRate;
        this.mStageMarginBottom = mValueHolder[1] * scaleRate;
        if (isTrueSet) {
            mValueHolder[0] = this.mStageMarginTop;
            mValueHolder[1] = this.mStageMarginBottom;
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
        return this.getDrawHeight() + this.getStageMarginBottom() + this.getStageMarginTop();
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
        points[0].x = drawCenterX - this.getDrawWidth() / 2;
        points[0].y = drawCenterY - this.getDrawHeight() / 2;

        //左下角
        points[1].x = points[0].x;
        points[1].y = points[0].y + this.getDrawHeight();

        //右下角
        points[2].x = drawCenterX + this.getDrawWidth() / 2;
        points[2].y = points[1].y;

        //右上角
        points[3].x = points[2].x + this.getDrawHeight();
        points[3].y = points[2].y - this.getDrawHeight();

        return points;
    }

    @Override
    public float setScaleDefaultValuesToReplaceCurrents(float fixScaleRate) {
        float oldScaleRate = super.setScaleDefaultValuesToReplaceCurrents(fixScaleRate);
        this.mStageMarginTop = mOriginalHolder.DEFAULT_MARGIN_TOP * fixScaleRate;
        this.mStageMarginBottom = mOriginalHolder.DEFAULT_MARGIN_BOTTOM * fixScaleRate;

        return oldScaleRate;
    }

    @Override
    public void storeDefaultValues(DefaultValuesHolder holder) {
        if (mOriginalHolder == null) {
            mOriginalHolder = new OriginalValuesHolder();
        }
        mOriginalHolder.updateValues(holder);
        mOriginalHolder.DEFAULT_MARGIN_TOP = this.getStageMarginTop();
        mOriginalHolder.DEFAULT_MARGIN_BOTTOM = this.getStageMarginBottom();
    }

    @Override
    public OriginalValuesHolder getDefaultValues() {
        return mOriginalHolder;
    }

    @Override
    public BaseStageParams clone() {
        BaseStageParams params = (BaseStageParams) super.clone();
        if (mStageStyle != null) {
            params.mStageStyle = mStageStyle.clone();
        }
        if (mOriginalHolder != null) {
            params.mOriginalHolder = mOriginalHolder.clone();
        }
        if (mValueHolder != null) {
            params.mValueHolder = mValueHolder.clone();
        }
        return params;
    }

    /**
     * 原始舞台数据的保存,此处的原始是指<font color="#ff9900"><b>舞台默认设定的宽高或者用户设定的宽高,即第一次运行并显示出来的界面即为原始界面;
     * 当用户对宽高做修改时,也会重新记录此数据</b></font>
     */
    protected class OriginalValuesHolder extends DefaultValuesHolder implements Cloneable {
        public float DEFAULT_MARGIN_TOP;
        public float DEFAULT_MARGIN_BOTTOM;

        public OriginalValuesHolder() {
        }

        public OriginalValuesHolder(OriginalValuesHolder holder) {
            this.updateValues(holder.DEFAULT_WIDTH, holder.DEFAULT_HEIGHT, holder.DEFAULT_RADIUS, holder.DEFAULT_COLOR,
                    holder.DEFAULT_MARGIN_TOP, holder.DEFAULT_MARGIN_BOTTOM);
        }

        public void updateValues(float width, float height, float radius, int color,
                                 float marginTop, float marginBottom) {
            super.updateValues(width, height, radius, color);
            this.DEFAULT_MARGIN_TOP = marginTop;
            this.DEFAULT_MARGIN_BOTTOM = marginBottom;
        }

        @Override
        public OriginalValuesHolder clone() {
            return (OriginalValuesHolder) super.clone();
        }
    }
}
