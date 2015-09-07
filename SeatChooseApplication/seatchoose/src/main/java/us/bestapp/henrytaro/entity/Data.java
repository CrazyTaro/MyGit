package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 来自JSON数据中的对象
 */
public class Data {
    @SerializedName("row")
    private List<Row> mRowList;

    private int mMaxColumnCount = 0;

    /**
     * 获取行数据(列数据包括在行中)
     *
     * @return
     */
    public List<Row> getRowList() {
        return this.mRowList;
    }

    /**
     * 获取所有行中最大的列数
     *
     * @return
     */
    public int getMaxColumnCount() {
        return this.mMaxColumnCount;
    }

    /**
     * 加载行数据,同时解析数据到每个单位
     */
    public void loadRowList() {
        if (mRowList != null) {
            for (Row row : mRowList) {
                row.parseJson();
                mMaxColumnCount = mMaxColumnCount < row.getColumnCount() ? row.getColumnCount() : mMaxColumnCount;
            }
        }
    }
}
