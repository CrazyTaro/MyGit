package us.bestapp.henrytaro.params;/**
 * Created by xuhaolin on 15/8/27.
 */

import android.graphics.Color;

import us.bestapp.henrytaro.params.interfaces.IBaseParams;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;

/**
 * created by xuhaolin at 2015/08/27
 * <p>实现了全局变量设置的接口{@link IGlobleParams},提供对外的参数设置接口</p>
 * <p><font color="#ff9900"><b>此类很重要!!!此类是绘制工具{@link us.bestapp.henrytaro.draw.utils.SeatDrawUtils}基本所必须的,请必须保证此类为有效引用</b></font>,
 * 在所有接口中并不提供此类的替换接口或者是重新设置的接口,但可以通过方法获取到此类的引用,
 * 该方法是用于设置全局参数,但可能会被设置为null引用,此时会出错</p>
 */
public class GlobleParams implements IGlobleParams {
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
    //是否允许双击放大缩小界面
    private boolean mIsEnabledDoubleClickScale = true;

    //选中行列的提醒
    private boolean mIsDrawSeletedRowColumnNotification = false;
    //提醒格式化字符串
    private String mNotificationFormat = "%1$行/%2$列";
    //格式化字符串时是否行显示在前
    private boolean mIsRowFirst = true;
    //绘制列数
    private boolean mIsDrawColumnNumber = false;
    //绘制行数
    private boolean mIsDrawRowNumber = false;
    //列数绘制的背景色
    private int mColumnNumberBgColor = Color.BLACK;
    //列数绘制的文本颜色
    private int mColumnNumberTextColor = Color.WHITE;
    //列数绘制的背景透明度
    private int mColumnNumberAlpha = 100;
    //行数绘制的背景色
    private int mRowNumberBgColor = Color.BLACK;
    //行数绘制的文本颜色
    private int mRowNumberTextColor = Color.WHITE;
    //行数绘制的背景透明色
    private int mRowNumberAlpha = 100;


    //默认绘制的座位类型行数为1
    private int mSeatTypeDrawRowCount = 1;

    @Override
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha) {
        if ((alpha < 0 || alpha > 255) && alpha != IBaseParams.DEFAULT_INT) {
            return false;
        } else {
            mThumbnailColor = color;
            if (alpha == IBaseParams.DEFAULT_INT) {
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

    @Override
    public void setSeatTypeRowCount(int rowCount) {
        if (rowCount > 0) {
            this.mSeatTypeDrawRowCount = rowCount;
        } else {
            this.mSeatTypeDrawRowCount = 1;
        }
    }

    @Override
    public int getSeatTypeRowCount() {
        return this.mSeatTypeDrawRowCount;
    }

    @Override
    public void setIsDrawRowNumber(boolean isDrawRowNumber) {
        this.mIsDrawRowNumber = isDrawRowNumber;
    }

    @Override
    public boolean getIsDrawRowNumber() {
        return this.mIsDrawRowNumber;
    }

    @Override
    public void setRowNumberBgColorWithTextColor(int bgColor, int textColor) {
        this.mRowNumberBgColor = bgColor;
        this.mRowNumberTextColor = textColor;
    }

    @Override
    public void setRowNumberAlpha(int alpha) {
        if (alpha == IBaseParams.DEFAULT_INT) {
            this.mRowNumberAlpha = 100;
        } else if (alpha >= 0 && alpha <= 255) {
            this.mRowNumberAlpha = alpha;
        }
    }

    @Override
    public int getRowNumberBackgroundColor() {
        return this.mRowNumberBgColor;
    }

    @Override
    public int getRowNumberTextColor() {
        return this.mRowNumberTextColor;
    }

    @Override
    public int getRowNumberAlpha() {
        return this.mRowNumberAlpha;
    }

    @Override
    public void setIsDrawColumnNumber(boolean isDrawColumnNumber) {
        this.mIsDrawColumnNumber = isDrawColumnNumber;
    }

    @Override
    public boolean getIsDrawColumnNumber() {
        return this.mIsDrawColumnNumber;
    }

    @Override
    public void setColumnNumberBgColorWithTextColor(int bgColor, int textColor) {
        this.mColumnNumberBgColor = bgColor;
        this.mColumnNumberTextColor = textColor;
    }

    @Override
    public void setColumnNumberAlpha(int alpha) {
        if (alpha == IBaseParams.DEFAULT_INT) {
            this.mColumnNumberAlpha = 100;
        } else if (alpha >= 0 && alpha <= 255) {
            this.mColumnNumberAlpha = alpha;
        }
    }

    @Override
    public int getColumnNumberBackgroundColor() {
        return this.mColumnNumberBgColor;
    }

    @Override
    public int getColumnNumberTextColor() {
        return this.mColumnNumberTextColor;
    }

    @Override
    public int getColumnNumberAlpha() {
        return this.mColumnNumberAlpha;
    }

    @Override
    public void setIsDrawSeletedRowColumnNotification(boolean isDrawNotification, String notifyFormat) {
        this.mIsDrawSeletedRowColumnNotification = isDrawNotification;
        this.mNotificationFormat = notifyFormat;
    }

    @Override
    public String createNotificationFormat(boolean isRowFirst, String... params) {
        if (params != null) {
            this.mIsRowFirst = isRowFirst;
            String format = "";
            for (String str : params) {
                format += str;
            }
            return format;
        } else {
            return null;
        }
    }

    @Override
    public boolean getIsRowFirst() {
        return this.mIsRowFirst;
    }

    @Override
    public String getNotificationFormat() {
        return this.mNotificationFormat;
    }

    @Override
    public boolean getIsDrawSeletedRowColumnNotification() {
        return this.mIsDrawSeletedRowColumnNotification;
    }

    @Override
    public void setIsEnabledDoubleClickScale(boolean isEnabled) {
        this.mIsEnabledDoubleClickScale = isEnabled;
    }

    @Override
    public boolean getIsEnabledDoubleClickScale() {
        return this.mIsEnabledDoubleClickScale;
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
