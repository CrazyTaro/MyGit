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
    public int mType = SeatParams.SEAT_TYPE_UNSHOW;
    public int mColumnIndex = 0;
    public boolean mIsCouple = false;


    private void setSeat(int type, int columnIndex, boolean isCouple) {
        this.mType = type;
        this.mColumnIndex = columnIndex;
        this.mIsCouple = isCouple;
    }

    public boolean parseToSeat(String seatInfo) {
        if (StringUtils.isNullOrEmpty(seatInfo)) {
            return false;
        } else {
            int type = SeatParams.SEAT_TYPE_UNSHOW;
            int columnIndex = 0;
            boolean isCouple = false;

            if (!seatInfo.equals("ZL")) {
                String[] infos = seatInfo.split("@");
                if (infos != null) {
                    columnIndex = Integer.parseInt(infos[0]);
                    isCouple = Integer.parseInt(infos[2]) != 0 ? true : false;
                    switch (infos[1]) {
                        case "A":
                            type = SeatParams.SEAT_TYPE_UNSELETED;
                            break;
                        case "LK":
                            type = SeatParams.SEAT_TYPE_DISABLE_SELETED;
                            break;
                    }
                }
            }

            this.setSeat(type, columnIndex, isCouple);
            return true;
        }
    }
}
