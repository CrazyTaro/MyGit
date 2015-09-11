package us.bestapp.henrytaro.params.interfaces;

import android.graphics.Color;

/**
 * Created by xuhaolin on 15/8/25.
 * 全局设置参数接口,此接口内的方法是独立且涉及全局数据的设置的,是独立对外开放的接口
 * <p>此接口与{@link IBaseParams}不同,此接口为设置全局性的参数,而{@link IBaseParams}是设置具体参数类型(座位/舞台)对应的相同的参数(宽/高等)</p>
 */
public interface IGlobleParams {

    public static final String FORMAT_STR = "%s";

    /**
     * 设置是否绘制缩略图
     *
     * @param isDraw
     */
    public void setIsDrawThumbnail(boolean isDraw);

    /**
     * 设置是否保持缩略图的显示，若设为false，则缩略图只在拖动界面或者是缩放时显示，当界面静止是不显示缩略图
     *
     * @param isShowAlways
     */
    public void setIsShowThumbnailAlways(boolean isShowAlways);

    /**
     * 设置缩略图背景色及透明度
     *
     * @param color 颜色值,颜色值不作任何检测(颜色默认值为{@link Color#BLACK})
     * @param alpha 透明度,透明度必须在0-255之间,用默认值请用参数{@link IBaseParams#DEFAULT_INT}
     * @return
     */
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha);


    /**
     * 设置缩略图的相对当前view界面显示的比例,此处只以Width为标准计算比例,height的高度是跟随改变的
     *
     * @param widthRate 宽比例,一般为0-1之间,缩略图宽/view宽,默认值为1/3;
     */
    public void setThumbnailWidthRate(float widthRate);

    /**
     * 设置背景色
     *
     * @param bgColor
     */
    public void setCanvasBackgroundColor(int bgColor);

    /**
     * 获取背景色
     *
     * @return
     */
    public int getCanvasBackgroundColor();

    /**
     * 获取缩略图背景色透明度
     *
     * @return
     */
    public int getThumbnailBgAlpha();

    /**
     * 获取缩略图背景色
     *
     * @return
     */
    public int getThumbnailBackgroundColor();

    /**
     * 获取缩略图显示的宽比
     *
     * @return
     */
    public float getThumbnailWidthRate();

    /**
     * 获取是否绘制缩略图
     *
     * @return
     */
    public boolean isDrawThumbnail();


    /**
     * 获取是否保持显示缩略图
     *
     * @return
     */
    public boolean isShowThumbnailAlways();

    /**
     * 设置座位类型需要自动绘制成几行
     * <p>分行绘制的规则如下:座位类型总数/rowCount,若整除则每行为相同的座位类型个数,
     * 若有余则最后一行比其它行将多一个座位类型</p>
     *
     * @param rowCount 此参数值必须大于0,否则将置为默认值1
     */
    public void setSeatTypeRowCount(int rowCount);

    /**
     * 获取座位类型绘制的行数
     *
     * @return
     */
    public int getSeatTypeRowCount();

    /**
     * 是否绘制行号
     *
     * @param isDrawRowNumber
     */
    public void setIsDrawRowNumber(boolean isDrawRowNumber);

    /**
     * 获取是否绘制行号
     */
    public boolean isDrawRowNumber();

    /**
     * 设置行数绘制的背景色及文本颜色
     *
     * @param bgColor   背景色
     * @param textColor 文本颜色
     */
    public void setRowNumberBgColorWithTextColor(int bgColor, int textColor);

    /**
     * 设置行数绘制的透明度,默认值使用{@link IBaseParams#DEFAULT_INT},<font color="#ff9900">透明度的值只能是0-255</font>
     *
     * @param alpha
     */
    public void setRowNumberAlpha(int alpha);

    /**
     * 获取行数绘制的背景颜色
     *
     * @return
     */
    public int getRowNumberBackgroundColor();

    /**
     * 获取行数绘制的文本颜色
     *
     * @return
     */
    public int getRowNumberTextColor();

    /**
     * 获取行数绘制的透明度
     *
     * @return
     */
    public int getRowNumberAlpha();

    /**
     * 是否绘制列号
     *
     * @param isDrawColumnNumber
     */
    public void setIsDrawColumnNumber(boolean isDrawColumnNumber);

    /**
     * 获取是否绘制列号
     */
    public boolean isDrawColumnNumber();

    /**
     * 设置列数背景颜色及文本颜色
     *
     * @param bgColor
     * @param textColor
     */
    public void setColumnNumberBgColorWithTextColor(int bgColor, int textColor);

    /**
     * 设置列数绘制的透明度,默认值请使用{@link IBaseParams#DEFAULT_INT},<font color="#ff9900">透明度的值只能是0-255</font>
     *
     * @param alpha
     */
    public void setColumnNumberAlpha(int alpha);

    /**
     * 获取列数绘制的背景颜色
     *
     * @return
     */
    public int getColumnNumberBackgroundColor();

    /**
     * 获取列数绘制的文本颜色
     *
     * @return
     */
    public int getColumnNumberTextColor();

    /**
     * 获取列数绘制的透明度
     *
     * @return
     */
    public int getColumnNumberAlpha();

    /**
     * 设置是否显示选中某行某列时的提醒
     *
     * @param isDrawNotification
     * @param notifyFormat       用于格式化的通知文字内容,例:%1$s行/%2$s列,参数一将被替换为行号,参数二将被替换为列号,
     *                           若不知道格式化规则可使用{@link #createNotificationFormat(boolean, String...)}创建格式化字符串
     */
    public void setIsDrawSeletedRowColumnNotification(boolean isDrawNotification, String notifyFormat);

    /**
     * 创建用于通知的格式化字符串,创建规则如下:<br/>
     * 将需要显示的字符串依次传入,在需要显示行/列号时分别使用{@link #FORMAT_STR}代替即可.如:<br/>
     * 创建:createNotification(false,FORMAT_STR,"列/",FORMAT_STR,"行");
     * 显示:2列/3行
     *
     * @param isRowFirst 是否行显示在前
     * @param params     可变String参数
     * @return
     */
    public String createNotificationFormat(boolean isRowFirst, String... params);

    /**
     * 是否行显示在前
     *
     * @return
     */
    public boolean isRowFirst();

    /**
     * 获取通知格式化字符串
     *
     * @return
     */
    public String getNotificationFormat();

    /**
     * 获取是否绘制选中某行某列提醒
     *
     * @return
     */
    public boolean isDrawSeletedRowColumnNotification();

    /**
     * 设置是否允许双击同一地方放大或者缩小界面
     *
     * @param isEnabled
     */
    public void setIsEnabledDoubleClickScale(boolean isEnabled);

    /**
     * 获取是否允许双击同一地方放大或者缩小界面
     *
     * @return
     */
    public boolean isEnabledDoubleClickScale();

    /**
     * 设置中心的虚线颜色
     *
     * @param color
     */
    public void setCenterDotLineColor(int color);

    /**
     * 获取中心虚线颜色
     *
     * @return
     */
    public int getCenterDotLineColor();

//    /**
//     * 获取是否允许绘制缩略图,设置用户参数时请勿使用此方法(此方法为提供给绘制时使用)
//     *
//     * @return
//     */
//    public boolean isAllowDrawThumbnail();
//
//    /**
//     * 是否允许绘制缩略图,设置用户参数时请勿使用此方法(此方法为提供给绘制时使用)
//     *
//     * @param isForceToDraw 是否强制要求一定要绘制
//     * @return
//     */
//    public void setIsAllowDrawThumbnail(boolean isForceToDraw);

}
