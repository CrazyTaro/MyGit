package com.crazytaro.bestapp.draw.interfaces;

import android.graphics.Bitmap;

/**
 * Created by xuhaolin on 2015/8/24.
 */
public interface ISeatParamsExport extends IBaseParamsExport {
    public Bitmap[] getImageBitmap();

    public int[] getImageID();

    public void setSeatTypeConstant(int seleted, int unSeleted, int unShow);

    public void resetSeatTypeConstant();

    public void resetDefaultSeatParams();

    public void resetSeatTypeWithColor();

    public void setDefaultSeatDrawNoType(int seatDrawNo);

    public void setIsDrawSeatType(boolean isDrawSeatType);

    public void setIsDraw(int seatType);

    public boolean isDrawSeatType();

    public float getSeatHorizontalInterval();

    public void setSeatHorizontalInterval(float mSeatHorizontalInterval);

    public float getSeatVerticalInterval();

    public void setSeatVerticalInterval(float mSeatVerticalInterval);

    public float getSeatTypeDescInterval();

    public void setSeatTextInterval(float mSeatTextInterval);

    public float getSeatTypeInterval();

    public void setSeatTypeInterval(float mSeatTypeInterval);

    public int[] getSeatTypeArrary();

    public int[] getSeatColorArrary();

    public String[] getSeatTypeDescription();

    public int getSeatTypeLength();

    public void setDefaultSeatType(int firstSeatType, int secondSeatType, int thirdSeatType);

    public void setDefaultSeatColor(int firstColor, int secondColor, int thirdColor);

    public void setDefaultSeatTypeDescription(String firstDesc, String secondDesc, String thirdDesc);

    public void setExtraSeatTypeWithColor(int[] seatExtraTypeArr, int[] colorExtraArr, String[] seatTypeExtraDesc);

    public void setSeatTypeWithImage(int[] seatTypeArr, int[] imageID);

    public void setSeatTypeWithImage(int[] seatTypeArr, Bitmap[] imageBitmap);

    public void setAllSeatTypeWithColor(int[] seatTypeArr, int[] colorArr, String[] seatTypeDesc);

    public void setImage(int[] imageID);

    public void setImage(Bitmap[] imageBitmap);
}
