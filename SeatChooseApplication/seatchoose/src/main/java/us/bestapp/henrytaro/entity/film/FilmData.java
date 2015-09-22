package us.bestapp.henrytaro.entity.film;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 来自JSON数据中的对象
 */
public class FilmData {
    @SerializedName("row")
    private List<FilmRow> mRowList;

    private int mMaxColumnCount = 0;

    /**
     * 获取行数据(列数据包括在行中)
     *
     * @return
     */
    public List<AbsRowEntity> getRowList() {
        if (mRowList != null && mRowList.size() > 0) {
            List<AbsRowEntity> newList = new ArrayList<AbsRowEntity>();
            for (FilmRow row : mRowList) {
                newList.add(row);
            }
            return newList;
        } else {
            return null;
        }
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
                FilmRow row = mRowList.get(i);
                row.setX(row.getRowNumber() - 1);
                row.parseData();
                //记录每行中最大的列数,因为不是每一行的列数都相同
                mMaxColumnCount = mMaxColumnCount < row.getColumnCount() ? row.getColumnCount() : mMaxColumnCount;
                //解析第一行数据记录第一行的行号
                //第一行行号必须从1开始,若未从1开始则将之前的几行空行补充完整
                if (i == 0 && row.getRowNumber() == 1) {
                    lastRowNumber = row.getRowNumber();
                } else {
                    //当前行的行号
                    currentRowNumber = row.getRowNumber();
                    //上一行与当前行之间并不连续则补充完整
                    if (lastRowNumber + 1 < currentRowNumber) {
                        int tempIndex = i;
                        //在不连续的两行之间补充空行
                        for (int j = lastRowNumber + 1; j < currentRowNumber; j++) {
                            mRowList.add(tempIndex, new FilmRow(j - 1, j));
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
