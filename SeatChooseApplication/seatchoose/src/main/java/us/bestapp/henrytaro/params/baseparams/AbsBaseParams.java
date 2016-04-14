package us.bestapp.henrytaro.params.baseparams;

import android.database.DefaultDatabaseErrorHandler;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;


/**
 * Created by xuhaolin on 2015/8/24.
 * <p>创建参数类型对应的基本类,此类包括子类参数需要的共同的参数(开放设置接口为{@link IBaseParams})</p>
 * <br/><font color="#ff9900"><b>此类是舞台与座位参数类的基类,自定义参数时请尽量继承{@link BaseStageParams}/{@link BaseSeatParams}</b></font>
 */
public abstract class AbsBaseParams implements IBaseParams, Cloneable {

    /**
     * 默认文字大小
     */
    public static final float DEFAULT_DESCRIPTION_SIZE = 28f;
    /**
     * 默认文字颜色
     */
    public static final int DEFAULT_DESCRIPTION_COLOR = Color.BLACK;
//    /**
//     * 默认的宽度，此值不为定值，由子类实现具体默认宽度
//     */
//    private float DEFAULT_WIDTH = 0f;
//    /**
//     * 默认的高度，此值不为定值，由子类实现具体的默认高宽
//     */
//    private float DEFAULT_HEIGHT = 0f;
//    /**
//     * 默认圆角度，此值不为定值，由子类实现具体的默认值
//     */
//    private float DEFAULT_RADIUS = 0f;
//    /**
//     * 默认颜色，此值不为定值，由子类实现具体的默认值
//     */
//    private int DEFAULT_COLOR = Color.GRAY;

    private DefaultValuesHolder mDefaulHolder = null;

    //暂存每一次缩放前的width
    private float mTempWidth;
    private float mTempHeight;
    private float mTempRadius;
    //绘制时使用的width
    private float mDrawWidth;
    private float mDrawHeight;
    private float mDrawRadius;
    private int mColor;

    //是否绘制
    private boolean mIsDraw = true;
    //是否绘制缩略图
    private boolean mIsDrawThumbnail = false;
    //缩略图绘制的比例
    private float mThumbnailRate = 0.1f;
    //绘制类型
    private int mDrawType = DRAW_TYPE_DEFAULT;
    //缩放最大宽度
    private float mLargeScaleRate = 0;
    //缩放最小宽度
    private float mSmallScaleRate = 0;
    private PointF mEdgeScaleWidth = null;
    private PointF mEdgeScaleHeight = null;

    /**
     * 构造参数
     *
     * @param defaultWidth  默认宽度
     * @param defaultHeight 默认高度
     * @param defaultRadius 默认圆角弧度
     * @param defaultColor  默认颜色
     */
    public AbsBaseParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor, float largeScale, float smallScale) {
        mEdgeScaleWidth = new PointF();
        mEdgeScaleHeight = new PointF();
        mDefaulHolder = new DefaultValuesHolder();
        this.setDefault(defaultWidth, defaultHeight, defaultRadius, defaultColor, largeScale, smallScale);
    }

    @Override
    public void setDefault(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor, float largeScaleRate, float smallScaleRate) {
        this.mDefaulHolder.updateValues(defaultWidth, defaultHeight, defaultRadius, defaultColor);

        this.mDrawWidth = defaultWidth;
        this.mDrawHeight = defaultHeight;
        this.mDrawRadius = defaultRadius;
        this.mTempWidth = defaultWidth;
        this.mTempHeight = defaultHeight;
        this.mTempRadius = defaultRadius;
        this.mColor = defaultColor;

        this.setLargeScaleRate(largeScaleRate);
        this.setSmallScaleRate(smallScaleRate);
        this.storeDefaultValues(this.mDefaulHolder);
    }

    @Override
    public PointF getDefaultWidthAndHeight() {
        return new PointF(this.mDefaulHolder.DEFAULT_WIDTH, this.mDefaulHolder.DEFAULT_HEIGHT);
    }

    @Override
    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT && isDrawThumbnail) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

    @Override
    public boolean isDrawThumbnail() {
        return this.mIsDrawThumbnail;
    }

    /**
     * 获取当前缩略图的缩放比例，此参数不可单独设置，通过调用方法将自己计算并设置{@link #setIsDrawThumbnail(boolean, float, float)}
     *
     * @return
     */
    protected float getThumbnailRate() {
        return this.mThumbnailRate;
    }


    @Override
    public boolean setLargeScaleRate(float large) {
        float newWidth = mDefaulHolder.DEFAULT_WIDTH * large;
        float newHeight = mDefaulHolder.DEFAULT_HEIGHT * large;
        if ((newWidth <= 0 || newWidth > 4096) || (newHeight <= 0 || newHeight > 4096)) {
            return false;
        } else {
            mLargeScaleRate = large;
            mEdgeScaleHeight.x = newHeight;
            mEdgeScaleWidth.x = newWidth;
            return true;
        }
    }

    @Override
    public boolean setSmallScaleRate(float small) {
        float newWidth = mDefaulHolder.DEFAULT_WIDTH * small;
        float newHeight = mDefaulHolder.DEFAULT_HEIGHT * small;
        if ((newWidth <= 0 || newWidth > 4096) || (newHeight <= 0 || newHeight > 4096)) {
            return false;
        } else {
            mSmallScaleRate = small;
            mEdgeScaleHeight.y = newHeight;
            mEdgeScaleWidth.y = newWidth;
            return true;
        }
    }

    @Override
    public float getLargeScaleRate() {
        return this.mLargeScaleRate;
    }

    @Override
    public float getSmallScaleRate() {
        return this.mSmallScaleRate;
    }

    @Override
    public PointF getEdgeScaleWidth() {
        return new PointF(mEdgeScaleWidth.x, mEdgeScaleWidth.y);
    }

    @Override
    public PointF getEdgeScaleHeight() {
        return new PointF(mEdgeScaleHeight.x, mEdgeScaleHeight.y);
    }

    @Override
    public float getDescriptionSize(float textRate) {
        if (textRate <= 0 || textRate > 1) {
            textRate = 0.8f;
        }
        float smallSize = this.getDrawWidth();
        smallSize = smallSize <= this.getDrawHeight() ? smallSize :
                this.getDrawHeight();
        return smallSize * textRate;
    }

    @Override
    public void setDrawType(int drawType) {
        this.mDrawType = drawType;
    }

    @Override
    public int getDrawType(boolean isGetOriginalDrawType) {
        if (mIsDrawThumbnail && !isGetOriginalDrawType) {
            return DRAW_TYPE_THUMBNAIL;
        } else {
            return mDrawType;
        }
    }

    @Override
    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }

    @Override
    public boolean isDraw() {
        return mIsDraw;
    }

    /**
     * 获取宽度
     * <p>基类{@link AbsBaseParams}中的宽高值都为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int, float, float)}进行设置</p>
     *
     * @return
     */
    public float getDrawWidth() {
        if (mIsDrawThumbnail) {
            return mDrawWidth * mThumbnailRate;
        } else {
            return mDrawWidth;
        }
    }

    @Override
    public float getWidthNotInThumbnail() {
        return mDrawWidth;
    }

    @Override
    public boolean seDrawWidth(float width) {
        if (width < mEdgeScaleWidth.y || width > mEdgeScaleWidth.x) {
            return false;
        } else {
            this.mTempWidth = width;
            this.mDrawWidth = width;
            return true;
        }
    }

    /**
     * 获取高度
     * <p>基类{@link AbsBaseParams}中的宽高值都为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int, float, float)}进行设置</p>
     *
     * @return
     */
    @Override
    public float getDrawHeight() {
        if (mIsDrawThumbnail) {
            return mDrawHeight * mThumbnailRate;
        } else {
            return mDrawHeight;
        }
    }

    @Override
    public float getHeightNotInThumbnail() {
        return mDrawHeight;
    }

    @Override
    public boolean seDrawHeight(float height) {
        if (height < mEdgeScaleHeight.y || height > mEdgeScaleHeight.x) {
            return false;
        } else {
            this.mTempHeight = height;
            this.mDrawHeight = height;
            return true;
        }
    }

    /**
     * 获取圆角弧度
     * <p>基类{@link AbsBaseParams}中的弧度值为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int, float, float)}进行设置</p>
     *
     * @return
     */
    @Override
    public float getDrawRadius() {
        if (mIsDrawThumbnail) {
            return mDrawRadius * mThumbnailRate;
        } else {
            return mDrawRadius;
        }
    }

    @Override
    public void setDrawRadius(float radius) {
        if (radius == DEFAULT_FLOAT) {
            this.mDrawRadius = mDefaulHolder.DEFAULT_RADIUS;
        } else {
            this.mDrawRadius = radius;
        }
    }

    /**
     * 获取当前绘制的颜色
     *
     * @return
     */
    public int getColor() {
        return mColor;
    }

    /**
     * 设置当前的绘制颜色值
     *
     * @param color
     */
    public void setColor(int color) {
        this.mColor = color;
    }

    /**
     * 是否可以进行缩放,用于检测当前比例是否允许进行缩放,<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比例
     * @return 可以缩放返回true, 否则返回false
     */
    public boolean isCanScale(float scaleRate) {
        float newWidth = this.mTempWidth * scaleRate;
        float newHeight = this.mTempHeight * scaleRate;
        //由于座位的宽度是决定座位对应的文字
        //文字大小不允许超过800
        //超过800的都取消缩放
        //设置最大最小缩放值

        //此处使用默认值的24倍,840
        if ((newWidth > mEdgeScaleWidth.x || newWidth < mEdgeScaleWidth.y) || (newHeight > mEdgeScaleHeight.x || newWidth < mEdgeScaleHeight.y)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比
     * @param isTrueSet 是否将此次缩放结果记录为永久结果
     */
    public void setScaleRate(float scaleRate, boolean isTrueSet) {
        this.mDrawWidth = mTempWidth * scaleRate;
        this.mDrawHeight = mTempHeight * scaleRate;
        this.mDrawRadius = mTempRadius * scaleRate;
        if (isTrueSet) {
            this.mTempWidth = mDrawWidth;
            this.mTempHeight = mDrawHeight;
            this.mTempRadius = mDrawRadius;
        }
    }

    /**
     * 获取当前的绘制界面与原始(默认的界面)的比例,当前界面/原始界面
     *
     * @return 返回当前相对于默认值的缩放比例.当计算失败时(如默认值不存在等), 返回 {@link #DEFAULT_FLOAT}
     */
    public float getScaleRateCompareToDefault() {
        if (mDefaulHolder != null) {
            //计算当前值与原始值的缩放比
            return this.getDrawWidth() / mDefaulHolder.DEFAULT_WIDTH;
        } else {
            return DEFAULT_FLOAT;
        }
    }

    /**
     * 按给定缩放比例参数基于默认值的缩放后,替换为当前的参数值.即使用默认值进行指定比例的缩放(可用于固定值缩放)
     *
     * @param fixScaleRate 基于默认值固定缩放的比例
     * @return 返回默认值缩放后界面与当前界面值的比例, default * fixScaleRate / current; 计算失败时返回 {@link #DEFAULT_FLOAT}
     */
    public float setScaleDefaultValuesToReplaceCurrents(float fixScaleRate) {
        if (mDefaulHolder == null) {
            return DEFAULT_FLOAT;
        }
        float oldScaleRate = 0f;
        float targetScaleRate = fixScaleRate;
        //计算缩放后的值与当前值的比例,作为当前缩放比
        oldScaleRate =
                //计算缩放后的宽值(放大原始的3倍或者还原为原始的大小)
                mDefaulHolder.DEFAULT_WIDTH * targetScaleRate
                        //当前界面值
                        / this.getDrawWidth();

        this.seDrawWidth(mDefaulHolder.DEFAULT_WIDTH * targetScaleRate);
        this.seDrawHeight(mDefaulHolder.DEFAULT_HEIGHT * targetScaleRate);
        this.setDrawRadius(mDefaulHolder.DEFAULT_RADIUS * targetScaleRate);
        return oldScaleRate;
    }

    /**
     * 获取存储的原始座位数据,返回的数据类型由子类自身确定(子类通过创建内部类用于存储的方式进行数据存储)
     *
     * @return
     */
    public DefaultValuesHolder getDefaultValues() {
        return new DefaultValuesHolder(mDefaulHolder);
    }

    /**
     * 获取当前对象绘制的区域,区域的宽高以对象本身的宽高为准,且必须为长方形
     *
     * @param imageRecft    绘制区域,此参数不为null时返回修改值后的此参数,否则创建一个新的对象返回
     * @param drawPositionX 对象绘制的中心X
     * @param drawPositionY 对象绘制的中心Y
     * @return
     */
    public RectF getNormalDrawRecf(RectF imageRecft, float drawPositionX, float drawPositionY) {
        if (imageRecft == null) {
            imageRecft = new RectF();
        }
        float imageWidth = this.getDrawWidth();
        float imageHeight = this.getDrawHeight();
        imageRecft.left = drawPositionX - imageWidth / 2;
        imageRecft.right = imageRecft.left + imageWidth;
        imageRecft.top = drawPositionY - imageHeight / 2;
        imageRecft.bottom = imageRecft.top + imageHeight;
        return imageRecft;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "params\t\t|values\t\t\n" +
                "width\t\t|" + mDrawWidth + "\t\t\n" +
                "height\t\t|" + mDrawHeight + "\t\t\n" +
                "radius\t\t|" + mDrawRadius + "\t\t\n";
    }

    /**
     * 更新width与height在其值被设置的时候
     *
     * @param newWidth  非设置此值时,值为-1或{@link #DEFAULT_FLOAT}
     * @param newHeight 非设置此值时,值为-1或{@link #DEFAULT_FLOAT}
     */
    protected abstract void updateWidthAndHeightWhenSet(float newWidth, float newHeight);

    /**
     * 存储默认的值,基本默认值已经存储,可以仅存储自定义的默认参数值
     */
    public abstract void storeDefaultValues(DefaultValuesHolder holder);

    /**
     * 默认值存储对象
     */
    public static class DefaultValuesHolder implements Cloneable {
        public float DEFAULT_WIDTH = 0;
        public float DEFAULT_HEIGHT = 0;
        public float DEFAULT_RADIUS = 0;
        public int DEFAULT_COLOR = Color.GRAY;

        /**
         * 创建一个空的存储对象
         */
        public DefaultValuesHolder() {
        }

        /**
         * 从旧的存储对象中创建新的对象
         *
         * @param oldHolder
         */
        public DefaultValuesHolder(DefaultValuesHolder oldHolder) {
            this.updateValues(oldHolder);
        }

        /**
         * 创建默认值存储对象
         *
         * @param width  默认宽
         * @param height 默认高
         * @param radius 默认角度
         * @param color  默认颜色
         */
        public DefaultValuesHolder(float width, float height, float radius, int color) {
            this.updateValues(width, height, radius, color);
        }

        /**
         * 更新默认值
         *
         * @param width  默认宽
         * @param height 默认高
         * @param radius 默认角度
         * @param color  默认颜色
         */
        public void updateValues(float width, float height, float radius, int color) {
            DEFAULT_WIDTH = width;
            DEFAULT_HEIGHT = height;
            DEFAULT_RADIUS = radius;
            DEFAULT_COLOR = color;
        }

        /**
         * 从旧存储对象中更新当前默认值
         *
         * @param oldHolder
         */
        public void updateValues(DefaultValuesHolder oldHolder) {
            if (oldHolder != null) {
                this.updateValues(oldHolder.DEFAULT_WIDTH, oldHolder.DEFAULT_HEIGHT, oldHolder.DEFAULT_RADIUS, oldHolder.DEFAULT_COLOR);
            }
        }

        @Override
        public DefaultValuesHolder clone() {
            try {
                return (DefaultValuesHolder) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }
    }
}
