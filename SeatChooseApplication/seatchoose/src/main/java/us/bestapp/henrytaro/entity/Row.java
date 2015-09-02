package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Author:
 * Description:
 */
public class Row {
    public static int TYPE_UNABLE = 0;
    public static int TYPE_UNLOKE = 1;
    public static int TYPE_LOKE = 2;

    @SerializedName("rownum")
    public int mRowNum;
    @SerializedName("rowid")
    public int mRowId;
    @SerializedName("columns")
    public String mColumns;

    private Seat[] mColumnData = null;

    public Seat[] getDataColumnSeat() {
        return this.mColumnData;
    }

    public int[] getDrawColumnSeat() {
        if (mColumnData != null) {
            int[] drawColumn = new int[mColumnData.length];
            for (int i = 0; i < mColumnData.length; i++) {
                drawColumn[i] = mColumnData[i].mType;
            }
            return drawColumn;
        } else {
            return null;
        }
    }

    public boolean parseJson() {
        if (StringUtils.isNullOrEmpty(mColumns)) {
            return false;
        } else {
            String[] columInfo = mColumns.split(",");
            if (mColumns != null) {
                mColumnData = new Seat[columInfo.length];
                for (int i = 0; i < columInfo.length; i++) {
                    Seat newSeat = new Seat();
                    if (newSeat.parseToSeat(columInfo[i])) {
                        mColumnData[i] = newSeat;
                    } else {
                        throw new RuntimeException("something erro,can not parse seat info");
                    }
                }
                return true;
            } else {
                throw new RuntimeException("column info is unable");
            }
        }
    }
}
