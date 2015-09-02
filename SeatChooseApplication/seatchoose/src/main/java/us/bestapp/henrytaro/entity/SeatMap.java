package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

/**
 * Author:
 * Description:
 */
public class SeatMap {
    public boolean success;
    @SerializedName("error_code")
    public int errorCode;
    public String message;
    public Data data;
    public String source;

    private SeatMap() {
    }

    public static SeatMap objectFromJSONStr(String jsonStr) {
        try {
            SeatMap map = null;
            Gson gson = new Gson();
            map = gson.fromJson(jsonStr, SeatMap.class);
            map.data.loadRowList();
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
            map.data.loadRowList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public static int[][] getDrawMap(SeatMap dataMap) {
        if (dataMap != null) {
            List<Row> rowList = dataMap.data.getRowList();
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
}
