package com.henrytaro.xuhaolin.areachoosesapk.entity;/**
 * Created by xuhaolin on 15/9/23.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.areachoose.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/23.
 */
public class AreaListEntity {
    @SerializedName("area")
    private List<AreaEntity> mAreaList;

    public List<AbsAreaEntity> getAreaList() {
        List<AbsAreaEntity> absList = new ArrayList<>();
        absList.addAll(mAreaList);
        return absList;
    }

    public static AreaListEntity objectFromJsonStr(String jsonStr) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonStr, AreaListEntity.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
