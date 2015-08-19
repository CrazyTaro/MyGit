package com.example.xuhaolin.seatchoose.cutomview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.io.InputStream;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 */
public class StageParams {
    public static final int DEFAULT_STAGE_TEXT_COLOR = Color.BLACK;
    /**
     * 默认舞台颜色
     */
    public static final int DEFAULT_STAGE_COLOR = Color.GREEN;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 350f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 55f;
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
    /**
     * 默认整数值:-1
     */
    public static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    public static final float DEFAULT_FLOAT = -1;
    /**
     * 舞台绘制方式,默认方式,使用颜色及图形绘制
     */
    public static final int STAGE_DRAW_TYPE_DEFAULT = -1;
    /**
     * 舞台绘制方式,使用图片填充
     */
    public static final int STAGE_DRAW_TYPE_IMAGE = 0;


    private float mStageWidth = DEFAULT_STAGE_WIDTH;
    private float mStageHeight = DEFAULT_STAGE_HEIGHT;
    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
    private float mStageRadius = DEFAULT_STAGE_RADIUS;
    private int mStageColor = DEFAULT_STAGE_COLOR;
    private int mStageTextColor = DEFAULT_STAGE_TEXT_COLOR;
    private String mStageText = DEFAULT_STAGE_TEXT;

    //用于缩放暂存舞台数据
    private float[] mValueHolder = null;
    //用于检测缩放时是否已暂存数据
    private boolean mIsValueHold = false;
    //是否绘制舞台
    private boolean mIsDrawStage = true;
    private static StageParams mInstance = null;

    private boolean mIsDrawThumbnail = false;
    private float mThumbnailRate = 0f;
    //舞台绘制类型
    private int mStageDrawType = STAGE_DRAW_TYPE_DEFAULT;
    //默认资源ID
    private int mStageImageID = DEFAULT_INT;
    private Bitmap mStageImageBitmap = null;

    private StageParams() {
    }

    public void setStageTextColor(int color) {
        this.mStageTextColor = color;
    }

    public int getStageTextColor() {
        return this.mStageTextColor;
    }

    /**
     * 设置舞台绘制方式
     *
     * @param drawType 舞台绘制方式
     *                 <p>
     *                 <li>{@link #STAGE_DRAW_TYPE_DEFAULT},默认绘制方式,使用图形及颜色绘制</li>
     *                 <li>{@link #STAGE_DRAW_TYPE_IMAGE},图片绘制方式</li>
     *                 </p>
     */
    public void setStageDrawType(int drawType) {
        if (drawType == DEFAULT_INT) {
            this.mStageDrawType = STAGE_DRAW_TYPE_DEFAULT;
        } else {
            this.mStageDrawType = drawType;
        }
    }

    /**
     * 获取舞台绘制的方式
     * <p>
     * <li>{@link #STAGE_DRAW_TYPE_DEFAULT}默认绘制方式,使用图形及颜色绘制</li>
     * <li>{@link #STAGE_DRAW_TYPE_IMAGE}图片绘制方式,使用图片填充</li>
     * </p>
     *
     * @return
     */
    public int getStageDrawType() {
        return mStageDrawType;
    }

    /**
     * 设置是否进行舞台绘制
     *
     * @param isDrawStage
     */
    public void setIsDrawStage(boolean isDrawStage) {
        this.mIsDrawStage = isDrawStage;
    }

    /**
     * 是否绘制舞台
     *
     * @return 返回true绘制, 返回false不绘制
     */
    public boolean isDrawStage() {
        return mIsDrawStage;
    }

    /**
     * 设置图片资源ID,该该法会默认将绘制方式设置为图片绘制方式,并且不检测资源ID的可用性,请尽可能保证ID可用
     *
     * @param imageID
     */
    public void setStageImage(int imageID) {
        this.mStageImageID = imageID;
        this.mStageDrawType = STAGE_DRAW_TYPE_IMAGE;
    }

    /**
     * 设置图片资源,该方法会默认将绘制方式设置为图片绘制方式,参数可为null
     *
     * @param imageBitmap
     */
    public void setStageImage(Bitmap imageBitmap) {
        if (imageBitmap != null) {
            this.mStageImageBitmap = imageBitmap;
            this.mStageDrawType = STAGE_DRAW_TYPE_IMAGE;
        } else {
            this.mStageImageBitmap = null;
        }
    }

    public Bitmap getStageImage() {
        return mStageImageBitmap;
    }

    public void loadStageImage(Context context, boolean isReload) {
        if (this.mStageImageID == DEFAULT_INT && isReload) {
            throw new RuntimeException("资源ID不合法,无法重新加载图片资源");
        }
        if (mStageImageID != DEFAULT_INT) {
            if (!isReload && mStageImageBitmap != null) {
                return;
            }
            Bitmap bitmap = getScaleBitmap(context, mStageImageID, (int) mStageWidth, (int) mStageHeight);
            if (bitmap != null) {
                mStageImageBitmap = bitmap;
            } else {
                throw new RuntimeException("无法通过资源ID加载图片资源");
            }
        } else if (mStageImageBitmap != null) {
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

    /**
     * 是否可以进行缩放,用于检测当前比例是否允许进行缩放,<font color="yellow"><b>且缩放的大小有限制,当缩放的字体超过800时不允许继续缩放.因为此时会造成系统无法缓存文字</b></font>
     *
     * @param scaleRate 新的缩放比例
     * @return 可以缩放返回true, 否则返回false
     */
    public boolean isCanScale(float scaleRate) {
        float newHeight = this.mStageHeight * scaleRate;
        //由于舞台的宽度是决定座位对应的文字
        //文字大小不允许超过800
        //超过800的都取消缩放

        //此处使用默认值的16倍,880
        if (newHeight > DEFAULT_STAGE_HEIGHT * 16 || newHeight < DEFAULT_STAGE_HEIGHT * 0.5) {
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
    public void setScaleRate(float scaleRate, boolean isTrueSet) {
        if (mValueHolder == null) {
            mValueHolder = new float[4];
        }
        if (!mIsValueHold) {
            mValueHolder[0] = this.mStageWidth;
            mValueHolder[1] = this.mStageHeight;
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = true;
        }
        this.mStageWidth = mValueHolder[0] * scaleRate;
        this.mStageHeight = mValueHolder[1] * scaleRate;
        this.mStageMarginTop = mValueHolder[2] * scaleRate;
        this.mStageMarginBottom = mValueHolder[3] * scaleRate;
        if (isTrueSet) {
            mValueHolder[0] = this.mStageWidth;
            mValueHolder[1] = this.mStageHeight;
            mValueHolder[2] = this.mStageMarginTop;
            mValueHolder[3] = this.mStageMarginBottom;
            mIsValueHold = false;
        }
    }

    public void resetStageParams() {
        mInstance = new StageParams();
    }

    public static synchronized StageParams getInstance() {
        if (mInstance == null) {
            mInstance = new StageParams();
        }
        return mInstance;
    }

    public String getStageText() {
        return mStageText;
    }

    public void setStageText(String text) {
        this.mStageText = text;
    }

    public float getStageWidth() {
        if (mIsDrawThumbnail) {
            return mStageWidth * mThumbnailRate;
        } else {
            return mStageWidth;
        }
    }

    public void setStageWidth(float mStageWidth) {
        if (mStageWidth == DEFAULT_FLOAT) {
            this.mStageWidth = DEFAULT_STAGE_WIDTH;
        } else {
            this.mStageWidth = mStageWidth;
        }
    }

    public float getStageHeight() {
        if (mIsDrawThumbnail) {
            return mStageHeight * mThumbnailRate;
        } else {
            return mStageHeight;
        }
    }

    public void setStageHeight(float mStageHeight) {
        if (mStageHeight == DEFAULT_FLOAT) {
            this.mStageHeight = DEFAULT_STAGE_HEIGHT;
        } else {
            this.mStageHeight = mStageHeight;
        }
    }

    public float getStageMarginTop() {
        if (mIsDrawThumbnail) {
            return mStageMarginTop * mThumbnailRate;
        } else {
            return mStageMarginTop;
        }
    }

    /**
     * 设置舞台上方顶端的高度，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginTop
     */
    public void setStageMarginTop(float mStageMarginTop) {
        if (mStageMarginTop == DEFAULT_FLOAT) {
            this.mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            this.mStageMarginTop = mStageMarginTop;
        }
    }

    public float getStageMarginBottom() {
        if (mIsDrawThumbnail) {
            return mStageMarginBottom * mThumbnailRate;
        } else {
            return mStageMarginBottom;
        }
    }

    /**
     * 设置舞台与下方（座位）间隔的高度，，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginBottom
     */
    public void setStageMarginBottom(float mStageMarginBottom) {
        if (mStageMarginBottom == DEFAULT_FLOAT) {
            this.mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
        } else {
            this.mStageMarginBottom = mStageMarginBottom;
        }
    }

    public float getStageRadius() {
        if (mIsDrawThumbnail) {
            return mStageRadius * mThumbnailRate;
        } else {
            return mStageRadius;
        }
    }

    /**
     * 设置舞台的圆角弧度，此处并不是以度数计算的，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageRadius 圆角弧度
     */
    public void setStageRadius(float mStageRadius) {
        if (mStageRadius == DEFAULT_FLOAT) {
            this.mStageRadius = DEFAULT_STAGE_RADIUS;
        } else {
            this.mStageRadius = mStageRadius;
        }
    }

    public int getStageColor() {
        return mStageColor;
    }

    public void setStageColor(int mStageColor) {
        if (mStageColor == DEFAULT_INT) {
            this.mStageColor = DEFAULT_STAGE_COLOR;
        } else {
            this.mStageColor = mStageColor;
        }
    }

    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    public float getStageTotalHeight() {
        return this.getStageHeight() + this.getStageMarginBottom() + this.getStageMarginTop();
    }

    public void autoCalculateParams(float widthPrecent, float viewWidth) {
        if (widthPrecent == DEFAULT_FLOAT || widthPrecent < 0 || widthPrecent > 1) {
            widthPrecent = 0.3f;
        }
        mStageWidth = viewWidth * widthPrecent;
        mStageHeight = mStageWidth * 1 / 5;
    }

    public void setIsDrawThumbnail(boolean isDrawThumbnail, float originalWidth, float targetWidth) {
        this.mIsDrawThumbnail = isDrawThumbnail;
        if (originalWidth != DEFAULT_FLOAT && targetWidth != DEFAULT_FLOAT) {
            this.mThumbnailRate = targetWidth / originalWidth;
        }
    }

}
