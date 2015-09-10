package us.bestapp.henrytaro.params.absparams;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;

import java.io.InputStream;

import us.bestapp.henrytaro.params.interfaces.IBaseParams;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;


/**
 * Created by xuhaolin on 2015/8/24.
 * <p>创建参数类型对应的基本类,此类包括子类参数需要的共同的参数(开放设置接口为{@link IBaseParams})及全局性的参数(开放设置接口为{@link IGlobleParams})</p>
 * <br/><font color="#ff9900"><b>继承绘制类{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}自定义绘制时,若需要使用自定义的参数,
 * 请继承此类</b></font>
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
    private int mLargeScaleRate = 10;
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
     * 按指定宽高加载资源ID指定的图片到内存中
     *
     * @param context      上下文对象,用于获取资源对象
     * @param imageID      资源ID
     * @param targetWidth  目标图片的宽度(此处一般为座位宽度)
     * @param targetHeight 目标图片的高度(此处一般为座位的高度)
     * @return
     */
    public static Bitmap getScaleBitmap(Context context, int imageID, int targetWidth, int targetHeight) {
        try {
            //以流的形式加载比直接使用ID加载到消耗内存会少一些,并且可以指定宽高进行加载
            //加载资源文件到流
            InputStream in = context.getResources().openRawResource(imageID);
            //设置加载选项
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            //设置目标宽高
            options.outWidth = targetWidth;
            options.outHeight = targetHeight;
            //加载图片
            return BitmapFactory.decodeStream(in, null, options);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
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

    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT && isDrawThumbnail) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

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


    public boolean setLargeScaleRate(int large) {
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


    public float getLargestHeight() {
        return this.mHeight * mLargeScaleRate;
    }


    public float getSmallestHeight() {
        return this.mHeight * mSmallScaleRate;
    }


    public int getDescriptionColor() {
        return this.mDescriptionColor;
    }


    public void setDescriptionColor(int color) {
        this.mDescriptionColor = color;
    }


    public float getDescriptionSize() {
        return this.getHeight() * 0.8f;
    }


    public void setDrawType(int drawType) {
        if (drawType == DEFAULT_INT) {
            this.mDrawType = DRAW_TYPE_DEFAULT;
        } else {
            this.mDrawType = drawType;
        }
    }


    public int getDrawType(boolean isGetOriginalDrawType) {
        if (mIsDrawThumbnail && !isGetOriginalDrawType) {
            return DRAW_TYPE_THUMBNAIL;
        } else {
            return mDrawType;
        }
    }


    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }


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
     * 加载座位图片
     *
     * @param context      上下文对象,用于加载图片
     * @param imageID      用于加载的资源ID，可为null，<font color="#ff9900"><b>但是两个资源参数必须一个不为null，且isReload为true时，此参数不可为null，否则抛出异常</b></font>
     * @param imageBitmap  用于加载的资源图片数组，可为null，<font color="#ff9900"><b>但是两个资源参数必须一个不为null</b></font>
     * @param targetWidth  加载图片的预期宽度
     * @param targetHeight 加载图片的预期高度
     * @param isReload     是否重新加载,若为true则以imageID为准,重新加载所有的bitmap,若为false则根据bitmap是否存在,若不存在则加载imageID的图片,存在则直接使用bitmap
     *                     <p><font color="#ff9900"><b>此参数为true时，参数imageID不可为null，否则抛出异常</b></font></p>
     * @return
     */
    protected Bitmap[] loadSeatImage(Context context, int[] imageID, Bitmap[] imageBitmap, int targetWidth, int targetHeight, boolean isReload) {
        if (imageID == null && isReload) {
            throw new RuntimeException("资源ID不存在,无法重新加载图片资源");
        }
        //检测imageID是否存在
        if (imageID != null) {
            //不需要重新加载且bitmap不为null
            //只要设置了新的图片资源,第一次加载图片时必然使用新的图片资源进行加载
            if (!isReload && imageBitmap != null && !mIsSetNewImage) {
                boolean isNullObjeact = false;
                //检测是否bitmap数组为空数组
                for (Bitmap bitmap : imageBitmap) {
                    if (bitmap == null) {
                        isNullObjeact = true;
                        break;
                    }
                }
                if (!isNullObjeact) {
                    //不存在空元素直直接使用该bitmap
                    return imageBitmap;
                }
            }
            //存在空元素则重新加载数据
            imageBitmap = new Bitmap[imageID.length];

            for (int i = 0; i < imageID.length; i++) {
                //按预期宽度比例加载图片
                //用于防止原图片太大加载的内存过大
                Bitmap bitmap = getScaleBitmap(context, imageID[i], targetWidth, targetHeight);
                imageBitmap[i] = bitmap;
            }

            mIsSetNewImage = false;
            return imageBitmap;
        } else if (imageBitmap != null) {
            return imageBitmap;
        } else {
            //即不存在资源ID,也不存在图片文件
            throw new RuntimeException("不存在可加载的图片资源或者已经加载的图片资源!");
        }
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
    public float getHeight() {
        if (mIsDrawThumbnail) {
            return mHeight * mThumbnailRate;
        } else {
            return mHeight;
        }
    }


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
    public float getRadius() {
        if (mIsDrawThumbnail) {
            return mRadius * mThumbnailRate;
        } else {
            return mRadius;
        }
    }

    public void setRadius(float radius) {
        if (radius == DEFAULT_FLOAT) {
            this.mRadius = DEFAULT_RADIUS;
        } else {
            this.mRadius = radius;
        }
    }


    public int getColor() {
        return mColor;
    }


    public void setColor(int color) {
        this.mColor = color;
    }


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

    public RectF getDrawRecf(RectF imageRecft, float drawPositionX, float drawPositionY) {
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
