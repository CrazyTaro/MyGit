package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import us.bestapp.henrytaro.entity.interfaces.ISeatEntityHandle;
import us.bestapp.henrytaro.entity.interfaces.ISeatMapHandle;
import us.bestapp.henrytaro.params.SeatParams;

/**
 * Author:
 * Description:
 */
public class SeatMap implements ISeatMapHandle {
    @SerializedName("success")
    private boolean mIsSucceed;
    @SerializedName("error_code")
    private int mErrorCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("data")
    private Data mSeatData;
    @SerializedName("source")
    private String mDataSource;

    private SeatMap() {
    }

    /**
     * 获取成功状态
     *
     * @return
     */
    public boolean getIsSucceed() {
        return this.mIsSucceed;
    }

    /**
     * 获取错误码
     *
     * @return
     */
    public int getErrorCode() {
        return this.mErrorCode;
    }

    /**
     * 获取状态信息
     *
     * @return
     */
    public String getMessage() {
        return this.mMessage;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public Data getData() {
        return this.mSeatData;
    }

    /**
     * 获取数据来源对象
     *
     * @return
     */
    public String getSource() {
        return this.mDataSource;
    }

    /**
     * 加载JSON数据创建数据对象
     *
     * @param jsonStr
     * @return
     */
    public static SeatMap objectFromJSONStr(String jsonStr) {
        try {
            SeatMap map = null;
            Gson gson = new Gson();
            map = gson.fromJson(jsonStr, SeatMap.class);
            //加载并解析数据
            map.getData().loadRowList();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载JSON数据创建对象
     *
     * @param jsonObject
     * @return
     */
    public static SeatMap objectFromJSONObject(JSONObject jsonObject) {
        Gson gson = new Gson();
        SeatMap map = null;
        try {
            map = gson.fromJson(jsonObject.toString(), SeatMap.class);
            //加载并解析数据
            map.getData().loadRowList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    @Override
    public int getSeatType(int mapRow, int mapColumn) {
        ISeatEntityHandle seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getType();
        } else {
            return SeatParams.SEAT_TYPE_ERROR;
        }
    }

    @Override
    public boolean updateSeatType(int type, int mapRow, int mapColumn) {
        ISeatEntityHandle seat = this.getSeatInfo(mapRow, mapColumn);
        seat.updateType(type);
        return true;
    }

    @Override
    public void resetSeat(int mapRow, int mapColumn, boolean isResetAll) {

    }

    @Override
    public ISeatEntityHandle getSeatInfo(int mapRow, int mapColumn) {
        try {
            return this.getData().getRowList().get(mapRow).getSeatInfo(mapColumn);
        } catch (Exception e) {
            return null;
//            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int getRowCount() {
        if (this.getData() != null && this.getData().getRowList() != null) {
            return this.getData().getRowList().size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount(int rowIndex) {
        if (this.getRowCount() > rowIndex) {
            return this.getData().getRowList().get(rowIndex).getColumnCount();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxColumnCount() {
        return this.getData().getMaxColumnCount();
    }

    @Override
    public boolean getIsCouple(int mapRow, int mapColumn) {
        ISeatEntityHandle seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getIsCouple();
        } else {
            throw new RuntimeException("seat info was not existed");
        }
    }

    @Override
    public int getSeatColumnInRow(int mapRow, int mapColumn) {
        ISeatEntityHandle seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getColumnNumber();
        } else {
            throw new RuntimeException("seat info was not existed");
        }
    }

    @Override
    public int[] getSeatListInRow(int mapRow) {
        if (this.getRowCount() > mapRow) {
            int columnCount = this.getColumnCount(mapRow);
            int[] seatList = new int[columnCount];
            Row row = this.getData().getRowList().get(mapRow);
            for (int i = 0; i < columnCount; i++) {
                seatList[i] = row.getSeatInfo(i).getType();
            }
            return seatList;
        } else {
            return null;
        }
    }
}
