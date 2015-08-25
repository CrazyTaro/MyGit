package com.crazytaro.bestapp.draw.params;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.crazytaro.bestapp.draw.interfaces.IBaseParamsExport;

import java.io.InputStream;


/**
 * Created by xuhaolin on 2015/8/24.
 * 创建参数类型对应的基本类
 */
public abstract class BaseParams implements IBaseParamsExport {

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
    protected int DEFAULT_COLOR = Color.GRAY;
    protected float mWidth = DEFAULT_WIDTH;
    protected float mHeight = DEFAULT_HEIGHT;
    protected float mRadius = DEFAULT_RADIUS;
    protected int mColor = DEFAULT_COLOR;
    protected int mDescriptionColor = DEFAULT_DESCRIPTION_COLOR;

    //静态变量,全局通用的
    //画布背景颜色
    private static int mCanvasBackgroundColor = Color.LTGRAY;
    //缩略图背景色
    private static int mThumbnailColor = Color.BLACK;
    //缩略图背景透明度
    private static int mThumbnailAlpha = 100;


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
    public BaseParams(float defaultWidth, float defaultHeight, float defaultRadius, int defaultColor) {
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
    }

    /**
     * 设置是否使用绘制缩略图的参数,缩略图的缩放比例只由宽度决定,高度是可变的
     *
     * @param isDrawThumbnail 是否绘制缩略图,<font color="yellow"><b>此参数为true,则所有的座位相关的绘制数据返回时将计算为缩略图的大小返回</b></font>
     * @param originalWidth   实际绘制界面的宽度
     * @param targetWidth     目标缩略图的宽度
     */
    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

    /**
     * 获取是否绘制缩略图
     *
     * @return
     */
    public boolean getIsDrawThumbnail() {
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
     * 设置缩略图背景色及透明度
     *
     * @param color 颜色值,颜色值不作任何检测(颜色默认值为{@link Color#BLACK})
     * @param alpha 透明度,透明度必须在0-255之间,用默认值请用参数{@link #DEFAULT_INT}
     * @return
     */
    public static boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha) {
        if ((alpha < 0 || alpha > 255) && alpha != DEFAULT_INT) {
            return false;
        } else {
            mThumbnailColor = color;
            if (alpha == DEFAULT_INT) {
                mThumbnailAlpha = 100;
            } else {
                mThumbnailAlpha = alpha;
            }
            return true;
        }
    }

    /**
     * 获取缩略图背景色
     *
     * @return
     */
    public static int getThumbnailBackgroundColor() {
        return mThumbnailColor;
    }

    /**
     * 获取缩略图背景色透明度
     *
     * @return
     */
    public static int getThumbnailBgAlpha() {
        return mThumbnailAlpha;
    }

    /**
     * 设置背景色
     *
     * @param bgColor
     */
    public static void setCanvasBackgroundColor(int bgColor) {
        mCanvasBackgroundColor = bgColor;
    }

    /**
     * 获取背景色
     *
     * @return
     */
    public static int getCanvasBackgroundColor() {
        return mCanvasBackgroundColor;
    }

    /**
     * 设置缩放最大值比,缩放最大倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="yellow"><b>使用默认参数{@link #DEFAULT_INT}可设置为原始默认值</b></font>,一般该参数大于1
     * <p>该缩放倍数是以默认高度为基数{@link #DEFAULT_HEIGHT}</p>
     *
     * @param large 放大倍数
     * @return 设置成功返回true, 否则返回false, 不改变原值
     */
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

    /**
     * 设置缩放最小值比,缩放最小倍数后应该座位高度应该小于880(为了文字可以进行处理),<font color="yellow"><b>使用默认参数{@link #DEFAULT_FLOAT}可设置为原始默认值</b></font>,一般该参数在0-1之间
     * <p>该缩放倍数是以默认高度为基数{@link #DEFAULT_HEIGHT}</p>
     *
     * @param small 缩小比例
     * @return 设置成功返回true, 否则返回false
     */
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

    /**
     * 获取最大的高度
     *
     * @return
     */
    public float getLargestHeight() {
        return this.mHeight * mLargeScaleRate;
    }

    /**
     * 获取最小的高度
     *
     * @return
     */
    public float getSmallestHeight() {
        return this.mHeight * mSmallScaleRate;
    }

    /**
     * 设置描述文字的颜色,此颜色是用于绘制时使用的(即当前绘制的文字必定使用此颜色,所以此颜色值是动态可变的)
     * <p>设置座位描述文字时请勿使用此方法</p>
     *
     * @param color
     */
    public void setDescriptionColor(int color) {
        this.mDescriptionColor = color;
    }

    /**
     * 获取描述文字的颜色(即当前绘制的文字必定使用此颜色,所以此颜色值是动态可变的)
     */
    public int getDescriptionColor() {
        return this.mDescriptionColor;
    }

    /**
     * 获取自动计算的描述文字字体大小，此值由当前参数height所决定，使字体的大小心保证与参数高度统一
     *
     * @return
     */
    public float getDescriptionSize() {
        return this.getHeight() * 0.8f;
    }

    /**
     * 设置绘制方式
     *
     * @param drawType 绘制方式
     *                 <p>
     *                 <li>{@link #DRAW_TYPE_DEFAULT},默认绘制方式,使用图形及颜色绘制</li>
     *                 <li>{@link #DRAW_TYPE_IMAGE},图片绘制方式</li>
     *                 </p>
     */
    public void setDrawType(int drawType) {
        if (drawType == DEFAULT_INT) {
            this.mDrawType = DRAW_TYPE_DEFAULT;
        } else {
            this.mDrawType = drawType;
        }
    }

    /**
     * 获取绘制的方式
     * <p>
     * <li>{@link #DRAW_TYPE_DEFAULT}默认绘制方式,使用图形及颜色绘制</li>
     * <li>{@link #DRAW_TYPE_IMAGE}图片绘制方式,使用图片填充</li>
     * <li>{@link #DRAW_TYPE_THUMBNAIL}缩略图绘制模式</li>
     * </p>
     *
     * @param isGetOriginalDrawType 是否获取实际的绘制类型(存在缩略图的情况下,缩略图不属于实际的绘制方式中的任何一种),true返回实际绘制类型,false返回缩略图绘制模式(如果允许绘制缩略图的话)
     * @return
     */
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
     * 设置图片资源ID,<font color="yellow"><b>该图片资源ID数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)})
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),当重新加载数据或者不存在图片资源时以资源ID数据为准</b></font></p>
     * 资源ID设置通过拷贝的方式设置，防止内部数据受到外部数据的影响
     *
     * @param imageID   设置的资源ID
     * @param typeCount 类型个数（即每一种类型对应一个资源ID，此处参数个数是为了确定imageID是否提供正确数据）
     * @param restoreID 用于存储设置的资源ID数组
     */
    protected void setImage(int[] imageID, int typeCount, int[] restoreID) {
        if (imageID != null && imageID.length != typeCount) {
            throw new RuntimeException("设置图片IDlength与类型数量typeCount不符合");
        }
        mDrawType = DRAW_TYPE_IMAGE;
        //通过拷贝保存引用对象数据,而不是直接保存引用
        if (imageID != null) {
            restoreID = new int[imageID.length];
            System.arraycopy(imageID, 0, restoreID, 0, imageID.length);
        } else {
            restoreID = null;
        }
    }

    /**
     * 设置图片资源,<font color="yellow"><b>该图片资源数组length必须与当前的座位类型length相同,否则抛出异常</b></font>,此方法会自动将绘制方式设置成图片绘制方式(详见{@link #setDrawType(int)} )
     * <p><font color="yellow"><b>加载图片时资源ID(imageID)优先于图片资源(bitmap),若需要使用当前的图像数据同时防止被其它数据影响,请将imageID设置为null(详见,{@link #setImage(int[], int, int[])})</b></font></p>
     * 资源设置通过拷贝的方式设置，防止内部数据受到外部数据的影响
     *
     * @param imageBitmap   设置资源的图片对象数组
     * @param typeCount     类型个数（即每一种类型对应一个资源图片，此处参数个数是为了确定imageBitmap是否提供正确数据）
     * @param restoreBitmap 用于存储设置的资源图片数组
     */
    protected void setImage(Bitmap[] imageBitmap, int typeCount, Bitmap[] restoreBitmap) {
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
    }

    /**
     * 加载座位图片
     *
     * @param context      上下文对象,用于加载图片
     * @param imageID      用于加载的资源ID，可为null，<font color="yellow"><b>但是两个资源参数必须一个不为null，且isReload为true时，此参数不可为null，否则抛出异常</b></font>
     * @param imageBitmap  用于加载的资源图片数组，可为null，<font color="yellow"><b>但是两个资源参数必须一个不为null</b></font>
     * @param targetWidth  加载图片的预期宽度
     * @param targetHeight 加载图片的预期高度
     * @param isReload     是否重新加载,若为true则以imageID为准,重新加载所有的bitmap,若为false则根据bitmap是否存在,若不存在则加载imageID的图片,存在则直接使用bitmap
     *                     <p><font color="yellow"><b>此参数为true时，参数imageID不可为null，否则抛出异常</b></font></p>
     */
    protected void loadSeatImage(Context context, int[] imageID, Bitmap[] imageBitmap, int targetWidth, int targetHeight, boolean isReload) {
        if (imageID == null && isReload) {
            throw new RuntimeException("资源ID不存在,无法重新加载图片资源");
        }
        //检测imageID是否存在
        if (imageID != null) {
            //不需要重新加载且bitmap不为null
            if (!isReload && imageBitmap != null) {
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
                    return;
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
        } else if (imageBitmap != null) {
            return;
        } else {
            //即不存在资源ID,也不存在图片文件
            throw new RuntimeException("不存在可加载的图片资源或者已经加载的图片资源!");
        }
    }

    /**
     * 按指定宽高比例加载资源ID指定的图片到内存中
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

//    /**
//     * 获取描述文字
//     *
//     * @return
//     */
//    public String getDescription() {
//        return mDescription;
//    }

//    /**
//     * 设置描述文字
//     *
//     * @param text
//     */
//    public void setDescription(String text) {
//        this.mDescription = text;
//    }

    public float getWidth() {
        if (mIsDrawThumbnail) {
            return mWidth * mThumbnailRate;
        } else {
            return mWidth;
        }
    }

    /**
     * 设置宽度
     *
     * @param width
     */
    public void setWidth(float width) {
        if (width == DEFAULT_FLOAT) {
            this.mWidth = DEFAULT_WIDTH;
        } else {
            this.mWidth = width;
        }
        this.storeDefaultScaleValue();
    }


    public float getHeight() {
        if (mIsDrawThumbnail) {
            return mHeight * mThumbnailRate;
        } else {
            return mHeight;
        }
    }

    /**
     * 设置高度
     *
     * @param height
     * @return
     */
    public void setHeight(float height) {
        if (height == DEFAULT_FLOAT) {
            this.mHeight = DEFAULT_HEIGHT;
        } else {
            this.mHeight = height;
        }
        this.storeDefaultScaleValue();
    }

    public float getRadius() {
        if (mIsDrawThumbnail) {
            return mRadius * mThumbnailRate;
        } else {
            return mRadius;
        }
    }

    /**
     * 设置圆角弧度，此处并不是以度数计算的，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param radius 圆角弧度
     */
    public void setRadius(float radius) {
        if (radius == DEFAULT_FLOAT) {
            this.mRadius = DEFAULT_RADIUS;
        } else {
            this.mRadius = radius;
        }
    }

    /**
     * 获取绘制的颜色值
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

    protected void storeDefaultScaleValue() {
    }

    /**
     * 是否可以进行缩放,用于检测当前比例是否允许进行缩放,<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
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
     * 设置缩放比例,缩放比是相对开始缩放前数据的缩放;<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比
     * @param isTrueSet 是否将此次缩放结果记录为永久结果
     */
    public abstract void setScaleRate(float scaleRate, boolean isTrueSet);
}
