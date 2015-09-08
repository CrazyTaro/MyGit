package us.bestapp.henrytaro.params.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by xuhaolin on 2015/8/24.
 * 舞台参数设置接口,此接口用于公开可进行设置的参数方法,不允许访问和设置的方法没有在此接口公开(此接口仅仅相当于一个中间转换而已)
 */
public interface IStageParams extends IBaseParams {
    /**
     * 设置图片资源ID,该该法会默认将绘制方式设置为图片绘制方式,并且不检测资源ID的可用性,请尽可能保证ID可用
     *
     * @param imageID
     */
    public void setImage(int imageID);

    /**
     * 设置图片资源,该方法会默认将绘制方式设置为图片绘制方式,参数可为null
     *
     * @param imageBitmap
     */
    public void setImage(Bitmap imageBitmap);

    /**
     * stage中的imageID与bitmap（即图片资源ID与图片资源）是两个相互独立的部分，其中以资源ID为主（存在资源ID的情况下）。
     * 当设置了资源ID获取的图片资源一般是由该ID生成的图片资源，但是如果资源ID还未被加载，此时获取的图片资源可能为null（在绘制舞台时如果资源ID未被加载会自动加载）
     *
     * @return
     */
    public Bitmap getImage(Context context);

    /**
     * 获取舞台描述文字
     *
     * @return
     */
    public String getStageDescription();

    /**
     * 设置舞台描述文字
     *
     * @param text
     */
    public void setStageDescription(String text);

    public float getStageMarginTop();

    /**
     * 设置舞台上方顶端的高度，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginTop
     */
    public void setStageMarginTop(float mStageMarginTop);

    public float getStageMarginBottom();

    /**
     * 设置舞台与下方（座位）间隔的高度，，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginBottom
     */
    public void setStageMarginBottom(float mStageMarginBottom);
}
