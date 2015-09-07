package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.entity.interfaces.ISeatEntityHandle;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 来自JSON数据中的对象
 */
public class Row {

    @SerializedName("rownum")
    private int mRowNum;
    @SerializedName("rowid")
    private int mRowId;
    @SerializedName("columns")
    private String mColumns;

    private int mColumnCount = 0;

    private AbsSeat[] mColumnData = null;

    /**
     * 获取行数
     *
     * @return
     */
    public int getRowNum() {
        return this.mRowNum;
    }

    /**
     * 获取行ID
     *
     * @return
     */
    public int getRowId() {
        return this.mRowId;
    }

    /**
     * 获取行中的列座位数据
     *
     * @return
     */
    public AbsSeat[] getDataColumnSeat() {
        return this.mColumnData;
    }

    /**
     * 获取行中的列数，此处是行中所有的列（不管有没有座位的列）
     *
     * @return
     */
    public int getColumnCount() {
        return this.mColumnData.length;
    }

    /**
     * 获取行中存在座位的列（真实的座位列数）
     *
     * @return
     */
    public int getRealColumnCount() {
        return this.mColumnCount;
    }

    /**
     * 获取行中某个列位置对应的座位信息
     *
     * @param columnIndex 行中的行索引（非真实座位列索引）
     * @return
     */
    public ISeatEntityHandle getSeatInfo(int columnIndex) {
        try {
            return this.mColumnData[columnIndex];
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解析已经加载的Json数据
     *
     * @return
     */
    public boolean parseJson() {
        if (StringUtils.isNullOrEmpty(mColumns)) {
            return false;
        } else {
            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            //1级分离数据类型
            String[] columInfo = mColumns.split(",");
            if (mColumns != null) {
                mColumnData = new Seat[columInfo.length];
                for (int i = 0; i < columInfo.length; i++) {
                    AbsSeat newSeat = Seat.getNewInstance(mRowNum, columInfo[i]);
                    //座位解析
                    mColumnData[i] = newSeat;
                    if (newSeat != null && newSeat.getType() != SeatParams.SEAT_TYPE_UNSHOW) {
                        mColumnCount++;
                    }
                }
                return true;
            } else {
                throw new RuntimeException("column info is unable");
            }
        }
    }
}
