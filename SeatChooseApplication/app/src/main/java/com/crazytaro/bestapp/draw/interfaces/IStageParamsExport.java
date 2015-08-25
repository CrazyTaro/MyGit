package com.crazytaro.bestapp.draw.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by xuhaolin on 2015/8/24.
 */
public interface IStageParamsExport extends IBaseParamsExport {
    public void setImage(int imageID);

    public void setImage(Bitmap imageBitmap);

    public Bitmap getImage(Context context);

    public String getStageDescription();

    public void setStageDescription(String text);

    public float getStageMarginTop();

    public void setStageMarginTop(float mStageMarginTop);

    public float getStageMarginBottom();

    public void setStageMarginBottom(float mStageMarginBottom);
}
