package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author:
 * Description:
 */
public class Data {
    @SerializedName("row")
    private List<Row> mRowList;

    public List<Row> getRowList() {
        return this.mRowList;
    }

    public void loadRowList() {
        if (mRowList != null) {
            for (Row row : mRowList) {
                row.parseJson();
            }
        }
    }
}
