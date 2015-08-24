package com.crazytaro.bestapp.draw.interfaces;

/**
 * Created by lenovo on 2015/8/24.
 */
public interface IBaseParamsExport {
    public boolean setThumbnailBackgroundColorWithAlpha(int color, int alpha);

    public void setCanvasBackgroundColor(int bgColor);

    public int getCanvasBackgroundColor();

    public boolean setLargeScaleRate(int large);

    public boolean setSmallScaleRate(float small);

    public void setDescriptionSize(float mSeatTextSize);

    public void setDescriptionColor(int color);

    public void setDrawType(int drawType);

    public int getDrawType(boolean isGetOriginalDrawType);

    public void setIsDraw(boolean isDraw);

    public boolean isDraw();

    public float getWidth();

    public void setWidth(float width);

    public float getHeight();

    public void setHeight(float height);

    public float getRadius();

    public void setRadius(float radius);

    public int getColor();

    public void setColor(int color);
}
