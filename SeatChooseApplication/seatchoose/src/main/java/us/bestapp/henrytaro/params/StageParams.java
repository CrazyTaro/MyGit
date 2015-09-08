package us.bestapp.henrytaro.params;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import us.bestapp.henrytaro.params.interfaces.IBaseParams;
import us.bestapp.henrytaro.params.interfaces.IDrawStageParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 * <p>所有{@code protected}方法及部分{@code public}都是绘制时需要的,对外公开可以进行设置的方法只允许从接口中进行设置,详见{@link IStageParams}</p>
 */
public class StageParams extends BaseParams implements IDrawStageParams {
    /**
     * 默认舞台颜色
     */
    public static final int DEFAULT_STAGE_COLOR = Color.GREEN;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 500f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 80f;
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
    public static final float DEFAULT_STAGE_MARGIN_BOTTOM = 30f;
    /**
     * 默认舞台文字
     */
    public static final String DEFAULT_STAGE_TEXT = "舞台";
    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
    private String mStageDescription = DEFAULT_STAGE_TEXT;

    private OriginalValuesHolder mOriginalHolder = null;
    //用于缩放暂存舞台数据
    private float[] mValueHolder = null;
    //用于检测缩放时是否已暂存数据
    private boolean mIsValueHold = false;
    //默认资源ID
    private int mStageImageID = DEFAULT_INT;
    private Bitmap mStageImageBitmap = null;

    public StageParams() {
        super(DEFAULT_STAGE_WIDTH, DEFAULT_STAGE_HEIGHT, DEFAULT_STAGE_RADIUS, DEFAULT_STAGE_COLOR);
        this.storeOriginalValues(null);
    }

    @Override
    public void setImage(int imageID) {
        this.mStageImageID = imageID;
        super.setDrawType(DRAW_TYPE_IMAGE);
    }

    @Override
    public void setImage(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            this.mStageImageBitmap = imageBitmap;
            super.setDrawType(DRAW_TYPE_IMAGE);
        } else {
            this.mStageImageBitmap = null;
        }
    }

    /**
     * 获取图片资源ID,默认值为{@link IBaseParams#DEFAULT_INT}
     *
     * @return
     */
    public int getImageId() {
        return this.mStageImageID;
    }

    /**
     * 获取图片资源,可能为null
     *
     * @return
     */
    public Bitmap getImageBitmap() {
        return this.mStageImageBitmap;
    }

    @Override
    public Bitmap getImage(Context context) {
        this.loadStageImage(context, false);
        return mStageImageBitmap;
    }

    /**
     * 加载资源图片，当isReload为true时，即重新加载，以资源ID为主重新加载资源，此时不管bitmap是存为null都会重新加载；
     * 当isReload为false时，则存在bitmap优先返回bitmap，否则加载资源ID，两者都不存在抛出异常
     *
     * @param context
     * @param isReload 是否以资源ID重新加载数据
     */
    protected void loadStageImage(Context context, boolean isReload) {
        int[] imageID = new int[]{this.mStageImageID};
        Bitmap[] imageBitmap = new Bitmap[]{this.mStageImageBitmap};
        super.loadSeatImage(context, imageID, imageBitmap, (int) this.getWidth(), (int) this.getHeight(), isReload);
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
    public String getStageDescription() {
        return mStageDescription;
    }

    @Override
    public void setStageDescription(String text) {
        this.mStageDescription = text;
    }

    @Override
    public float getStageMarginTop() {
        if (super.getIsDrawThumbnail()) {
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
        if (super.getIsDrawThumbnail()) {
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

    @Override
    public float getStageTotalHeight() {
        return this.getHeight() + this.getStageMarginBottom() + this.getStageMarginTop();
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
    public float setOriginalValuesToReplaceCurrents(boolean isSetEnlarge) {
        float oldScaleRate = 0f;
        float targetScaleRate = 0f;
        if (isSetEnlarge) {
            targetScaleRate = 3f;
        } else {
            targetScaleRate = 1f;
        }
        oldScaleRate = mOriginalHolder.width / this.getWidth();

        this.setWidth(mOriginalHolder.width * targetScaleRate, false);
        this.setHeight(mOriginalHolder.height * targetScaleRate, false);
        this.setRadius(mOriginalHolder.radius * targetScaleRate);
        this.mStageMarginTop = mOriginalHolder.marginTop * targetScaleRate;
        this.mStageMarginBottom = mOriginalHolder.marginBottom * targetScaleRate;

        return oldScaleRate;
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
        } else if (copyObj instanceof StageParams.OriginalValuesHolder) {
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
    public Object getClone() {
        boolean isThumbnail = this.getIsDrawThumbnail();
        this.setIsDrawThumbnail(false, DEFAULT_INT, DEFAULT_INT);

        StageParams newObj = new StageParams();
        //获取默认原始值
        OriginalValuesHolder holder = (OriginalValuesHolder) this.getOriginalValues();

        newObj.storeOriginalValues(holder);
        //设置当前值
        newObj.setWidth(this.getWidth(), false);
        newObj.setHeight(this.getHeight(), false);
        newObj.setRadius(this.getRadius());
        newObj.setStageMarginTop(this.getStageMarginTop());
        newObj.setStageMarginBottom(this.getStageMarginBottom());

        //设置其它的参数值
        newObj.setIsDraw(this.getIsDraw());
        newObj.setIsDrawThumbnail(isThumbnail, 0, 0);
        newObj.setThumbnailRate(this.getThumbnailRate());
        //设置图片资源
        newObj.setImage(this.getImageId());
        newObj.setImage(this.getImageBitmap());
        //设置绘制方式
        //此部分必须在最后设置,因为一旦设置了图片资源,则会默认将绘制方式修改为图片绘制模式
        newObj.setDrawType(this.getDrawType(true));

        this.setIsDrawThumbnail(isThumbnail, DEFAULT_INT, DEFAULT_INT);
        return newObj;
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
