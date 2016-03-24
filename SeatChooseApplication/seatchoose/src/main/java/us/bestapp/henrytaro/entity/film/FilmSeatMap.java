package us.bestapp.henrytaro.entity.film;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 座位列表对象,用于实现座位处理接口及处理JSON数据
 */
public class FilmSeatMap extends AbsMapEntity {
    @SerializedName("success")
    private boolean mIsSucceed;
    @SerializedName("error_code")
    private int mErrorCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("data")
    private FilmData mSeatData;
    @SerializedName("source")
    private String mDataSource;

    /**
     * 加载JSON数据创建对象
     *
     * @param jsonObject
     * @return
     */
    public static FilmSeatMap objectFromJsonObject(JSONObject jsonObject) {
        if (jsonObject != null) {
            return FilmSeatMap.objectFromJsonStr(jsonObject.toString());
        } else {
            return null;
        }
    }

    /**
     * 解析JSON数据
     *
     * @param jsonStr
     * @return
     */
    public static FilmSeatMap objectFromJsonStr(String jsonStr) {
        try {
            FilmSeatMap map = new Gson().fromJson(jsonStr, new TypeToken<FilmSeatMap>() {
            }.getType());
            //加载并解析数据
            map.getData().loadRowList();
            map.setDefaultRowEntityList(map.getData().getRowList());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    public FilmData getData() {
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

    @Override
    public AbsSeatEntity getSeatEntity(int mapRow, int mapColumn) {
        try {
            return this.getData().getRowList().get(mapRow).getSeatEntity(mapColumn);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getMaxColumnCount() {
        return this.getData().getMaxColumnCount();
    }

    @Override
    public String[] getRowIDs() {
        return this.getData().mRowIDs.toArray(new String[this.getData().mRowIDs.size()]);
    }

    public void setIsSucceed(boolean isSucceed) {
        this.mIsSucceed = isSucceed;
    }

    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setSeatData(FilmData seatData) {
        this.mSeatData = seatData;
    }

    public void setDataSource(String dataSource) {
        this.mDataSource = dataSource;
    }

    @Override
    public String toString() {
        return "FilmSeatMap{" +
                "mIsSucceed=" + mIsSucceed +
                ", mErrorCode=" + mErrorCode +
                ", mMessage='" + mMessage + '\'' +
                ", mSeatData=" + mSeatData +
                ", mDataSource='" + mDataSource + '\'' +
                '}';
    }
}
