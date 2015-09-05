package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

import us.bestapp.henrytaro.entity.interfaces.ISeatDataHandle;
import us.bestapp.henrytaro.params.SeatParams;

/**
 * Author:
 * Description:
 */
public class SeatMap implements ISeatDataHandle {
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

    public boolean getIsSucceed() {
        return this.mIsSucceed;
    }

    public int getErrorCode() {
        return this.mErrorCode;
    }

    public String getMessage() {
        return this.mMessage;
    }

    public Data getData() {
        return this.mSeatData;
    }

    public String getSource() {
        return this.mDataSource;
    }

    public static SeatMap objectFromJSONStr(String jsonStr) {
        try {
            SeatMap map = null;
            Gson gson = new Gson();
            map = gson.fromJson(jsonStr, SeatMap.class);
            map.getData().loadRowList();
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SeatMap objectFromJSONObject(JSONObject jsonObject) {
        Gson gson = new Gson();
        SeatMap map = null;
        try {
            map = gson.fromJson(jsonObject.toString(), SeatMap.class);
            map.getData().loadRowList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public static int[][] getDrawMap(SeatMap dataMap) {
        if (dataMap != null) {
            List<Row> rowList = dataMap.getData().getRowList();
            if (rowList != null) {
                int[][] drawMap = new int[rowList.size()][];
                for (int i = 0; i < rowList.size(); i++) {
                    drawMap[i] = rowList.get(i).getDrawColumnSeat();
                }
                return drawMap;
            }
        }
        return null;
    }

    @Override
    public int getSeatType(int mapRow, int mapColumn) {
        Seat seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getType();
        } else {
            return SeatParams.SEAT_TYPE_ERROR;
        }
    }

    @Override
    public boolean setSeatType(int mapRow, int mapColumn) {
        Seat seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            //TODO
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Seat getSeatInfo(int mapRow, int mapColumn) {
        try {
            return this.getData().getRowList().get(mapRow).getSeatInfo(mapColumn);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int getRowCount() {
        return this.getData().getRowList().size();
    }

    @Override
    public int getColumnCountInRow(int rowIndex) {
        return this.getData().getRowList().get(rowIndex).getRealColumnCount();
    }

    @Override
    public boolean getIsCouple(int mapRow, int mapColumn) {
        Seat seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getIsCouple();
        } else {
            throw new RuntimeException("seat info was not existed");
        }
    }

    @Override
    public int getSeatColumnInRow(int mapRow, int mapColumn) {
        Seat seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getColumnIndex();
        } else {
            throw new RuntimeException("seat info was not existed");
        }
    }
}
