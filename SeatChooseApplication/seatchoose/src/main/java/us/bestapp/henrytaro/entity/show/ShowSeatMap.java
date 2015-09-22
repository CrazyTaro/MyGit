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
 * 演出座位列表
 */
public class ShowSeatMap extends AbsMapEntity {
    @SerializedName("success")
    private boolean mIsSucceed;
    @SerializedName("error_code")
    private int mErrorCode;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("data")
    private List<ShowData> mData;

    private int mMaxColumnCount = 0;

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
    public List<ShowData> getData() {
        return this.mData;
    }


    /**
     * 解析数据,<font color="#ff9900"><b>此处依赖于数据是先行后列的顺序排序好的,否则解析可能出错</b></font>
     */
    public void parseJson() {
        //解析座位信息的同时,将座位分配到不同的行对象中
        if (mData != null) {
            if (mAbsRowList == null) {
                mAbsRowList = new ArrayList<AbsRowEntity>();
            } else {
                mAbsRowList.clear();
            }
            //当前行
            ShowRow currentRow = null;
            int currentRowNumber = 0;
            int currentColumnIndex = 0;
            for (int i = 0; i < mData.size(); i++) {
                ShowData data = mData.get(i);
                //当前行与遍历的数据行号不同,则创建新的一行
                //此处依赖于数据是先行后列的顺序排序好的,否则解析可能出错
                if (currentRowNumber != data.getRowNumber()) {
                    currentRowNumber = data.getRowNumber();
                    //创建新行,重置座位列号
                    currentColumnIndex = 0;
                    //创建并设置新行的行号
                    currentRow = new ShowRow(data.getX(), currentRowNumber);
                    mAbsRowList.add(currentRow);
                }
                //解析座位数据
                data.parseData();
                data.setXY(currentRowNumber - 1, currentColumnIndex);
                //添加座位到新行
                currentRow.addNewData(data);
                currentColumnIndex++;
            }
        } else {
            mAbsRowList = null;
        }
    }

    /**
     * 解析JSON数据
     *
     * @param jsonStr
     * @return
     */
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
