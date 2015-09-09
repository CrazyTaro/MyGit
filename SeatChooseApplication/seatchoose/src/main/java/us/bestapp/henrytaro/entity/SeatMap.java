package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import us.bestapp.henrytaro.entity.interfaces.IRowEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatMapEntity;
import us.bestapp.henrytaro.params.SeatParams;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 座位列表对象,用于实现座位处理接口及处理JSON数据
 */
public class SeatMap implements ISeatMapEntity {
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
    public boolean isSucceed() {
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
        ISeatEntity seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getType();
        } else {
            return SeatParams.SEAT_TYPE_ERROR;
        }
    }

    @Override
    public boolean updateSeatType(int type, int mapRow, int mapColumn) {
        ISeatEntity seat = this.getSeatInfo(mapRow, mapColumn);
        seat.updateType(type);
        return true;
    }

    @Override
    public ISeatEntity getSeatInfo(int mapRow, int mapColumn) {
        try {
            return this.getData().getRowList().get(mapRow).getSeat(mapColumn);
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
    public boolean isCouple(int mapRow, int mapColumn) {
        ISeatEntity seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.isCouple();
        } else {
            return false;
//            throw new RuntimeException("seat info was not existed");
        }
    }

    @Override
    public int getSeatColumnInRow(int mapRow, int mapColumn) {
        ISeatEntity seat = this.getSeatInfo(mapRow, mapColumn);
        if (seat != null) {
            return seat.getColumnNumber();
        } else {
            return -1;
//            throw new RuntimeException("seat info was not existed");
        }
    }

    @Override
    public IRowEntity getSeatRowInMap(int mapRow) {
        if (this.getRowCount() > mapRow) {
            return this.getData().getRowList().get(mapRow);
        } else {
            return null;
        }
    }
}
