package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/2.
 */

import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Author:
 * Description:
 */
public class Seat {
    private int mType = SeatParams.SEAT_TYPE_UNSHOW;
    private int mColumnIndex = 0;
    private boolean mIsCouple = false;

    private Seat(int type, int columnIndex, boolean isCouple) {
        this.mType = type;
        this.mColumnIndex = columnIndex;
        this.mIsCouple = isCouple;
    }

    public int getType() {
        return this.mType;
    }

    public int getColumnIndex() {
        return this.mColumnIndex;
    }

    public boolean getIsCouple() {
        return this.mIsCouple;
    }

    /**
     * 此方法为解析数据生成座位
     *
     * @param seatInfo
     * @return
     */
    public static Seat parseToSeat(String seatInfo) {
        if (StringUtils.isNullOrEmpty(seatInfo)) {
            return null;
        } else {
            int type = SeatParams.SEAT_TYPE_UNSHOW;
            int columnIndex = 0;
            boolean isCouple = false;

            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            if (!seatInfo.equals("ZL")) {
                String[] infos = seatInfo.split("@");
                if (infos != null) {
                    columnIndex = Integer.parseInt(infos[0]);
                    isCouple = Integer.parseInt(infos[2]) != 0 ? true : false;
                    switch (infos[1]) {
                        case "A":
                            //可选座位
                            type = SeatParams.SEAT_TYPE_UNSELETED;
                            break;
                        case "LK":
                            //锁定座位
                            type = SeatParams.SEAT_TYPE_DISABLE_SELETED;
                            break;
                    }
                }
            }
            //设置座位信息
            return new Seat(type, columnIndex, isCouple);
        }
    }
}
