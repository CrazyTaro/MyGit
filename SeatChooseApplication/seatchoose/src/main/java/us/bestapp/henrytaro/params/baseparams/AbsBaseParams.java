package us.bestapp.henrytaro.params.baseparams;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;

import us.bestapp.henrytaro.params.interfaces.IBaseParams;


/**
 * Created by xuhaolin on 2015/8/24.
 * <p>创建参数类型对应的基本类,此类包括子类参数需要的共同的参数(开放设置接口为{@link IBaseParams})</p>
 * <br/><font color="#ff9900"><b>此类是舞台与座位参数类的基类,自定义参数时请尽量继承{@link BaseStageParams}/{@link BaseSeatParams}</b></font>
 */
public abstract class AbsBaseParams implements IBaseParams {

    /**
     * 默认文字大小
     */
    public static final float DEFAULT_DESCRIPTION_SIZE = 28f;
    /**
     * 默认文字颜色
     */
    public static final int DEFAULT_DESCRIPTION_COLOR = Color.BLACK;
    /**
     * 默认的宽度，此值不为定值，由子类实现具体默认宽度
     */
    protected float DEFAULT_WIDTH = 0f;
    /**
     * 默认的高度，此值不为定值，由子类实现具体的默认高宽
     */
    protected float DEFAULT_HEIGHT = 0f;
    /**
     * 默认圆角度，此值不为定值，由子类实现具体的默认值
     */
    protected float DEFAULT_RADIUS = 0f;
    /**
     * 默认颜色，此值不为定值，由子类实现具体的默认值
     */

    private int DEFAULT_COLOR = Color.GRAY;
    private float mWidth = DEFAULT_WIDTH;
    private float mHeight = DEFAULT_HEIGHT;
    private float mRadius = DEFAULT_RADIUS;
    private int mColor = DEFAULT_COLOR;
    private int mDescriptionColor = DEFAULT_DESCRIPTION_COLOR;


    private boolean mIsSetNewImage = false;
    //是否绘制
    private boolean mIsDraw = true;
    //是否绘制缩略图
    private boolean mIsDrawThumbnail = false;
    //缩略图绘制的比例
    private float mThumbnailRate = 0.1f;
    //绘制类型
    private int mDrawType = DRAW_TYPE_DEFAULT;
    //最大缩放倍数
    private float mLargeScaleRate = 10f;
    //最小绽放比例
    private float mSmallScaleRate = 0.2f;

    /**
     * 构造参数
     *
     * @param defaultWidth  默认宽度
     * @param defaultHeight 默认高度
     * @param defaultRadius 默认圆角弧度
     * @param defaultColor  默认颜色
     */
    public AbsBaseParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor) {
        this.setDefault(defaultWidth, defaultHeight, defaultRadius, defaultColor);
    }

    /**
     * 设置默认值，当默认值需要改变时，必须调用此方法
     *
     * @param defaultWidth  默认宽度
     * @param defaultHeight 默认高度
     * @param defaultRadius 默认圆角弧度
     * @param defaultColor  默认颜色
     */
    protected void setDefault(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor) {
        this.DEFAULT_WIDTH = defaultWidth;
        this.DEFAULT_HEIGHT = defaultHeight;
        this.DEFAULT_RADIUS = defaultRadius;
        this.DEFAULT_COLOR = defaultColor;

        this.mWidth = defaultWidth;
        this.mHeight = defaultHeight;
        this.mRadius = defaultRadius;
        this.mColor = defaultColor;

        this.storeOriginalValues(null);
    }

    /**
     * 设置是否使用绘制缩略图的参数,缩略图的缩放比例只由宽度决定,高度是可变的<br/>
     * <font color="#ff9900"><b>只有当设置为true时,后面两个参数才有效,否则无效</b></font>
     *
     * @param isDrawThumbnail 是否绘制缩略图,<font color="#ff9900"><b>此参数为true,则所有的座位相关的绘制数据返回时将计算为缩略图的大小返回</b></font>
     * @param originalWidth   实际绘制界面的宽度
     * @param targetWidth     目标缩略图的宽度
     */
    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT && isDrawThumbnail) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

    /**
     * 获取是否绘制缩略图
     *
     * @return
     */
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

    /**
     * 设置当前缩略图缩放比例,此方法不对外公开,设置缩放比时也请尽量不使用此方法,请使用{@link #setIsDrawThumbnail(boolean, float, float)}
     *
     * @param thumbnailRate
     */
    protected void setThumbnailRate(float thumbnailRate) {
        this.mThumbnailRate = thumbnailRate;
    }


    @Override
    public boolean setLargeScaleRate(float large) {
        if (large == DEFAULT_INT) {
            this.mLargeScaleRate = 24;
            return true;
        } else if (large > 0 && large * this.mHeight <= 880) {
            this.mLargeScaleRate = large;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setSmallScaleRate(float small) {
        if (small == DEFAULT_INT) {
            this.mSmallScaleRate = 0.5f;
            return true;
        } else if (small > 0 && small * this.mHeight <= 880) {
            this.mSmallScaleRate = small;
            return true;
        } else {
            return false;
        }
    }

//    /**
//     * 获取最大的高度
//     *
//     * @return
//     */
//    public float getLargestHeight() {
//        return this.mHeight * mLargeScaleRate;
//    }
//
//    /**
//     * 获取最小的高度
//     *
//     * @return
//     */
//    public float getSmallestHeight() {
//        return this.mHeight * mSmallScaleRate;
//    }
//
//
//    public int getDescriptionColor() {
//        return this.mDescriptionColor;
//    }
//
//
//    public void setDescriptionColor(int color) {
//        this.mDescriptionColor = color;
//    }


    public float getDescriptionSize() {
        return this.getHeight() * 0.8f;
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

    /**
     * 设置是否进行对象绘制
     *
     * @param isDraw
     */
    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }

    /**
     * 是否绘制对象
     *
     * @return 返回true绘制, 返回false不绘制
     */
    public boolean isDraw() {
        return mIsDraw;
    }

    /**
     * 设置图片资源ID,<font color="#ff9900"><b>该图片资源ID数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)})
     * <p><font color="#ff9900"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),当重新加载数据或者不存在图片资源时以资源ID数据为准</b></font></p>
     * 资源ID设置通过拷贝的方式设置，防止内部数据受到外部数据的影响
     *
     * @param imageID   设置的资源ID
     * @param typeCount 类型个数（即每一种类型对应一个资源ID，此处参数个数是为了确定imageID是否提供正确数据）
     * @param restoreID 用于存储设置的资源ID数组
     * @return
     */
    protected int[] setImage(int[] imageID, int typeCount, int[] restoreID) {
        if (imageID != null && imageID.length != typeCount) {
            throw new RuntimeException("设置图片IDlength与类型数量typeCount不符合");
        }
        mDrawType = DRAW_TYPE_IMAGE;
        //通过拷贝保存引用对象数据,而不是直接保存引用
        if (imageID != null) {
            restoreID = new int[imageID.length];
            System.arraycopy(imageID, 0, restoreID, 0, imageID.length);

            mIsSetNewImage = true;
        } else {
            restoreID = null;
        }
        return restoreID;
    }

    /**
     * <p><font color="#ff9900"><b>建议设置图片通过{@link #setImage(int[], int, int[])}设置资源图片,非必要情况下尽量不使用此方法</b></font>,
     * 使用此方法设置图片无法自动压缩并按适当的宽高加载图片,可能会占用大量内存</p>
     * <br/>
     * 设置图片资源,<font color="#ff9900"><b>该图片资源数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)} )
     * <p><font color="#ff9900"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),若需要使用当前的图像数据同时防止被其它数据影响,请将imageID设置为null(详见,{@link AbsBaseParams#setImage(int[], int, int[])})</b></font></p>
     * 资源设置通过拷贝的方式设置，防止内部数据受到外部数据的影响
     *
     * @param imageBitmap   设置资源的图片对象数组
     * @param typeCount     类型个数（即每一种类型对应一个资源图片，此处参数个数是为了确定imageBitmap是否提供正确数据）
     * @param restoreBitmap 用于存储设置的资源图片数组
     * @return
     */
    protected Bitmap[] setImage(Bitmap[] imageBitmap, int typeCount, Bitmap[] restoreBitmap) {
        if (imageBitmap != null && imageBitmap.length != typeCount) {
            throw new RuntimeException("设置图片length与类型数量typeCount不符合");
        }
        mDrawType = DRAW_TYPE_IMAGE;
        //通过拷贝保存引用对象数据,而不是直接保存引用
        if (imageBitmap != null) {
            restoreBitmap = new Bitmap[imageBitmap.length];
            System.arraycopy(imageBitmap, 0, restoreBitmap, 0, imageBitmap.length);
        } else {
            restoreBitmap = null;
        }
        return restoreBitmap;
    }

    /**
     * 获取宽度
     * <p>基类{@link AbsBaseParams}中的宽高值都为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int)}进行设置</p>
     *
     * @return
     */
    public float getWidth() {
        if (mIsDrawThumbnail) {
            return mWidth * mThumbnailRate;
        } else {
            return mWidth;
        }
    }

    @Override
    public void setWidth(float width) {
        this.setWidth(width, true);
    }

    /**
     * 设置宽度,同时指定是否将此次值作为原始值存储,详见{@link #storeOriginalValues(Object)}
     * <p>此方法为内部开放使用,外部公开设置请用{@link #setWidth(float)} ,且该方法必定会存储设置值</p>
     *
     * @param width
     * @param isStoreValue true为存储此次值,false为不存储
     */
    protected void setWidth(float width, boolean isStoreValue) {
        if (width == DEFAULT_FLOAT) {
            this.mWidth = DEFAULT_WIDTH;
        } else {
            this.mWidth = width;
        }
        if (isStoreValue) {
            this.storeOriginalValues(null);
        }
    }


    /**
     * 获取高度
     * <p>基类{@link AbsBaseParams}中的宽高值都为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int)}进行设置</p>
     *
     * @return
     */
    @Override
    public float getHeight() {
        if (mIsDrawThumbnail) {
            return mHeight * mThumbnailRate;
        } else {
            return mHeight;
        }
    }


    @Override
    public void setHeight(float height) {
        this.setHeight(height, true);
    }


    /**
     * 设置高度,同时指定是否将此次值作为原始值存储,详见{@link #storeOriginalValues(Object)}
     * <p>此方法为内部开放使用,外部公开设置请用{@link #setHeight(float)},且该方法必定会存储设置值</p>
     *
     * @param height
     * @param isStoreValue true为存储此次值,false为不存储
     */
    protected void setHeight(float height, boolean isStoreValue) {
        if (height == DEFAULT_FLOAT) {
            this.mHeight = DEFAULT_HEIGHT;
        } else {
            this.mHeight = height;
        }
        if (isStoreValue) {
            this.storeOriginalValues(null);
        }
    }

    /**
     * 获取圆角弧度
     * <p>基类{@link AbsBaseParams}中的弧度值为0,必须是子类的值覆盖了基类中的值,或者通过{@link #setDefault(float, float, float, int)}进行设置</p>
     *
     * @return
     */
    @Override
    public float getRadius() {
        if (mIsDrawThumbnail) {
            return mRadius * mThumbnailRate;
        } else {
            return mRadius;
        }
    }

    @Override
    public void setRadius(float radius) {
        if (radius == DEFAULT_FLOAT) {
            this.mRadius = DEFAULT_RADIUS;
        } else {
            this.mRadius = radius;
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
        float newHeight = this.mHeight * scaleRate;
        //由于座位的宽度是决定座位对应的文字
        //文字大小不允许超过800
        //超过800的都取消缩放
        //设置最大最小缩放值

        //此处使用默认值的24倍,840
        if (newHeight > DEFAULT_HEIGHT * mLargeScaleRate || newHeight < DEFAULT_HEIGHT * mSmallScaleRate) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取当前对象绘制的区域,区域的宽高以对象本身的宽高为准,且必须为长方形
     *
     * @param imageRecft    绘制区域
     * @param drawPositionX 对象绘制的中心X
     * @param drawPositionY 对象绘制的中心Y
     * @return
     */
    public RectF getNormalDrawRecf(RectF imageRecft, float drawPositionX, float drawPositionY) {
        if (imageRecft == null) {
            imageRecft = new RectF();
        }
        float imageWidth = this.getWidth();
        float imageHeight = this.getHeight();
        imageRecft.left = drawPositionX - imageWidth / 2;
        imageRecft.right = imageRecft.left + imageWidth;
        imageRecft.top = drawPositionY - imageHeight / 2;
        imageRecft.bottom = imageRecft.top + imageHeight;
        return imageRecft;
    }

    /**
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="#ff9900"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比
     * @param isTrueSet 是否将此次缩放结果记录为永久结果
     */
    public abstract void setScaleRate(float scaleRate, boolean isTrueSet);

    /**
     * 获取当前的绘制界面与原始(最初设定的界面)的比例,当前界面/原始界面
     *
     * @return
     */
    public abstract float getScaleRateCompareToOriginal();

    /**
     * 设置预设的缩放比为当前值(只用于双击缩放)
     *
     * @param isSetEnlarge 是否使用最大比例值替换当前值,true使用最大预设值替换当前值,false使用最小值替换当前值
     * @return
     */
    public abstract float setOriginalValuesToReplaceCurrents(boolean isSetEnlarge);

    /**
     * 存储预设的缩放比,保证不管界面用户如何缩放在双击时都可以正常缩放到某个预设的比例
     *
     * @param copyObj 用于拷贝的存储对象,此参数可为null,若不为null则以此参数为源拷贝值
     */
    public abstract void storeOriginalValues(Object copyObj);

    /**
     * 获取存储的原始座位数据,返回的数据类型由子类自身确定(子类通过创建内部类用于存储的方式进行数据存储)
     *
     * @return
     */
    public abstract Object getOriginalValues();
}
