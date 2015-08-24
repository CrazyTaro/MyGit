package com.crazytaro.bestapp.draw.interfaces;

import android.graphics.Bitmap;

/**
 * Created by lenovo on 2015/8/24.
 */
public interface IStageParamsExport extends IBaseParamsExport{
    public void setImage(int imageID);
    public void setImage(Bitmap imageBitmap);
    public Bitmap getImage();
    public String getStageText();
    public void setStageText(String text);
    public float getStageMarginTop();
    public void setStageMarginTop(float mStageMarginTop);
    public float getStageMarginBottom();
    public void setStageMarginBottom(float mStageMarginBottom);
}
