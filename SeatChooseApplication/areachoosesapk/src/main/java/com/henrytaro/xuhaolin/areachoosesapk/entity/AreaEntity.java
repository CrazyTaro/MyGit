package com.henrytaro.xuhaolin.areachoosesapk.entity;/**
 * Created by xuhaolin on 15/9/23.
 */

import android.graphics.Color;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.areachoose.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/23.
 */
public class AreaEntity extends AbsAreaEntity {
    @SerializedName("area_name")
    public String areaName;
    @SerializedName("color")
    public String colorName;
    @SerializedName("is_sold_out")
    public boolean isSoldOut;

    private int colorValue = 0;

    @Override
    public String getAreaName() {
        return this.areaName;
    }

    @Override
    public String getAreaColorName() {
        return this.colorName;
    }

    @Override
    public int getAreaColor() {
        if (colorValue == 0) {
            colorValue = Color.parseColor(colorName);
            Log.i("color", this.colorName + "/" + colorValue);
        }
        return this.colorValue;
    }

    @Override
    public boolean isSoldOut() {
        return isSoldOut;
    }
}
