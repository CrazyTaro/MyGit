package us.bestapp.henrytaro.draw.params;/**
 * Created by xuhaolin on 15/8/27.
 */

import android.graphics.Color;

import us.bestapp.henrytaro.draw.interfaces.IBaseParamsExport;
import us.bestapp.henrytaro.draw.interfaces.IGlobleParamsExport;

/**
 * created by xuhaolin at 2015/08/27
 * <p>实现了全局变量设置的接口{@link IGlobleParamsExport},提供对外的参数设置接口</p>
 * <p><font color="#ff9900"><b>此类很重要!!!此类是绘制工具{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}基本所必须的,请必须保证此类为有效引用</b></font>,
 * 在所有接口中并不提供此类的替换接口或者是重新设置的接口,但可以通过方法获取到此类的引用,
 * 该方法是用于设置全局参数,但可能会被设置为null引用,此时会出错</p>
 */
public class GlobleParams implements IGlobleParamsExport {
    //画布背景颜色
    private int mCanvasBackgroundColor = Color.LTGRAY;
    //缩略图背景色
    private int mThumbnailColor = Color.BLACK;
    //缩略图背景透明度
    private int mThumbnailAlpha = 100;
    //缩略图显示宽比
    private float mThumbnailWidthRate = 1 / 3f;
    //是否绘制缩略图
    private boolean mIsDrawThumbnail = true;
    //是否保持显示缩略图
    private boolean mIsShowThumbnailAlways = false;
    //是否允许绘制缩略图
    private boolean mIsAllowDrawThumbnail = true;

    @Override
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha) {
        if ((alpha < 0 || alpha > 255) && alpha != IBaseParamsExport.DEFAULT_INT) {
            return false;
        } else {
            mThumbnailColor = color;
            if (alpha == IBaseParamsExport.DEFAULT_INT) {
                mThumbnailAlpha = 100;
            } else {
                mThumbnailAlpha = alpha;
            }
            return true;
        }
    }

    @Override
    public int getCanvasBackgroundColor() {
        return this.mCanvasBackgroundColor;
    }

    @Override
    public void setCanvasBackgroundColor(int bgColor) {
        this.mCanvasBackgroundColor = bgColor;
    }

    @Override
    public int getThumbnailBgAlpha() {
        return this.mThumbnailAlpha;
    }

    @Override
    public int getThumbnailBackgroundColor() {
        return this.mThumbnailColor;
    }

    @Override
    public float getThumbnailWidthRate() {
        return this.mThumbnailWidthRate;
    }

    @Override
    public void setThumbnailWidthRate(float widthRate) {
        if (widthRate < 1 && widthRate > 0) {
            this.mThumbnailWidthRate = widthRate;
        } else {
            throw new RuntimeException("设置缩略图显示的宽比时只能在0-1之间");
        }
    }

    @Override
    public boolean getIsDrawThumbnail() {
        return this.mIsDrawThumbnail;
    }

    @Override
    public void setIsDrawThumbnail(boolean isDraw) {
        this.mIsDrawThumbnail = isDraw;
    }

    @Override
    public boolean getIsShowThumbnailAlways() {
        return this.mIsShowThumbnailAlways;
    }

    @Override
    public void setIsShowThumbnailAlways(boolean isShowAlways) {
        this.mIsShowThumbnailAlways = isShowAlways;
        this.mIsAllowDrawThumbnail = isShowAlways && mIsDrawThumbnail;
    }

    /**
     * 获取是否允许绘制缩略图,此方法不对外公开
     *
     * @return
     */
    public boolean getIsAllowDrawThumbnail() {
        return this.mIsAllowDrawThumbnail;
    }


    /**
     * 是否允许绘制缩略图,此方法不对外公开
     *
     * @param isForceToDraw 是否强制要求一定要绘制
     * @return
     */
    public void setIsAllowDrawThumbnail(boolean isForceToDraw) {
        if (isForceToDraw) {
            this.mIsAllowDrawThumbnail = true;
        } else {
            this.mIsAllowDrawThumbnail = mIsDrawThumbnail && mIsShowThumbnailAlways;
        }
    }
}
