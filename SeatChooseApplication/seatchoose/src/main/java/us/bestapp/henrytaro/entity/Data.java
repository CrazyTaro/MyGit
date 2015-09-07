package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.entity.interfaces.IRowEntity;

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
    public List<IRowEntity> getRowList() {
        List<IRowEntity> newList = new ArrayList<IRowEntity>();
        for (Row row : mRowList) {
            newList.add(row);
        }
        return newList;
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
            int lastRowNumber = 0;
            int currentRowNumber = 0;
            for (int i = 0; i < mRowList.size(); i++) {
                Row row = mRowList.get(i);
                row.parseJson();
                //记录每行中最大的列数,因为不是每一行的列数都相同
                mMaxColumnCount = mMaxColumnCount < row.getColumnCount() ? row.getColumnCount() : mMaxColumnCount;
                //解析第一行数据记录第一行的行号
                if (i == 0) {
                    lastRowNumber = row.getRowNumber();
                } else {
                    //当前行的行号
                    currentRowNumber = row.getRowNumber();
                    //上一行与当前行之间并不连续则补充完整
                    if (lastRowNumber + 1 < currentRowNumber) {
                        int tempIndex = i;
                        //在不连续的两行之间补充空行
                        for (int j = lastRowNumber + 1; j < currentRowNumber; j++) {
                            mRowList.add(tempIndex, new Row(j, null, true, true));
                        }
                        //重新计算补充后的索引
                        i += currentRowNumber - lastRowNumber - 1;
                    }
                    //将当前行的行号作为新的上一行的行号
                    //继续解析
                    lastRowNumber = row.getRowNumber();
                }
            }
        }
    }
}
