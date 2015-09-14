package us.bestapp.henrytaro.entity.show;/**
 * Created by xuhaolin on 15/9/14.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by xuhaolin on 15/9/14.
 */
public class ShowSeatMap extends AbsMapEntity {
    @SerializedName("success")
    private boolean mSuccess;
    @SerializedName("error_code")
    private int mErrorCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("data")
    private List<ShowData> mData;

    private int mMaxColumnCount = 0;

    public void parseJson() {
        if (mData != null) {
            if (mAbsRowList == null) {
                mAbsRowList = new ArrayList<AbsRowEntity>();
            } else {
                mAbsRowList.clear();
            }

            ShowRow currentRow = null;
            int currentRowNumber = -1;
            for (ShowData data : mData) {
                if (currentRowNumber != data.getRowNumber()) {
                    currentRowNumber = data.getRowNumber();
                    currentRow = new ShowRow(currentRowNumber);
                    mAbsRowList.add(currentRow);
                }
                data.parseData();
                currentRow.addNewData(data);
            }
        } else {
            mAbsRowList = null;
        }
    }


    public static ShowSeatMap objectFromJsonStr(String jsonStr) {
        try {
            ShowSeatMap map = null;
            Gson gson = new Gson();
            map = gson.fromJson(jsonStr, ShowSeatMap.class);
            //加载并解析数据
            map.parseJson();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public AbsSeatEntity getSeatEntity(int mapRow, int mapColumn) {
        try {
            return this.mAbsRowList.get(mapRow).getSeatEntity(mapColumn);
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public int getMaxColumnCount() {
        if (mAbsRowList != null && mAbsRowList.size() > 0) {
            return mAbsRowList.get(0).getColumnCount();
        } else {
            return 0;
        }
    }

}
