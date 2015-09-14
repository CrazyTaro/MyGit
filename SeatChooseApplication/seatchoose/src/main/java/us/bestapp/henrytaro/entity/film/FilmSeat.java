package us.bestapp.henrytaro.entity.film;/**
 * Created by xuhaolin on 15/9/2.
 */

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 自定义创建的对象,用于实现通用的座位接口与数据处理传递
 */
public class FilmSeat extends AbsSeatEntity {
    private int mRowNumber = 0;
    private int mColumnNumber = 0;
    private int mType = IBaseParams.TYPE_ERROR;
    private boolean mIsCouple = false;
    private String mSeatInfo = null;

    /**
     * 获取新的实例,此方法会同时解析座位信息
     *
     * @param rowNumber 行号
     * @param seatInfo  座位信息
     * @return
     */
    public FilmSeat(int rowNumber, String seatInfo) {
        this.mRowNumber = rowNumber;
        this.mSeatInfo = seatInfo;
    }

//    @Override
//    public AbsSeat parseToSeat(int rowNumber, int columnNumber, String seatInfo, AbsSeat oldSeat) {
//        if (StringUtils.isNullOrEmpty(seatInfo)) {
//            return null;
//        } else {
//            int type = IBaseParams.TYPE_ERROR;
//            boolean isCouple = false;
//
//            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
//            //ZL表示走廊，无座位
//            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
//            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
//            if (!seatInfo.startsWith("ZL")) {
//                String[] infos = seatInfo.split("@");
//                if (infos != null) {
//                    //解析列号
//                    columnNumber = Integer.parseInt(infos[0]);
//                    //解析情侣座
//                    isCouple = Integer.parseInt(infos[2]) != 0 ? true : false;
//                    switch (infos[1]) {
//                        case "A":
//                            //可选座位
//                            type = SeatParams.seat_type_unselected;
//                            break;
//                        case "LK":
//                            //锁定座位
//                            type = SeatParams.seat_type_disable_selected;
//                            break;
//                    }
//                }
//            }
//            //设置座位信息
//            if (oldSeat == null) {
//                return new FilmSeat(rowNumber, columnNumber, type, isCouple, seatInfo);
//            } else {
//                //使用原对象
//                oldSeat.setParams(rowNumber, columnNumber, type, isCouple, seatInfo);
//                return oldSeat;
//            }
//        }
//    }

    public void setParams(int rowNumber, int columnNumber, int type, boolean isCouple, String seatInfo) {
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
        this.mIsCouple = isCouple;
        this.mSeatInfo = seatInfo;
    }


    @Override
    public void parseData() {
        if (!StringUtils.isNullOrEmpty(mSeatInfo)) {
            int type = IBaseParams.TYPE_ERROR;
            boolean isCouple = false;

            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            if (!mSeatInfo.startsWith("ZL")) {
                String[] infos = mSeatInfo.split("@");
                if (infos != null) {
                    //解析列号
                    mColumnNumber = Integer.parseInt(infos[0]);
                    //解析情侣座
                    isCouple = Integer.parseInt(infos[2]) != 0 ? true : false;
                    switch (infos[1]) {
                        case "A":
                            //可选座位
                            type = SeatParams.seat_type_unselected;
                            break;
                        case "LK":
                            //锁定座位
                            type = SeatParams.seat_type_disable_selected;
                            break;
                    }
                }
            }
            this.mType = type;
            this.mIsCouple = isCouple;
        }
    }

    @Override
    public boolean isCouple() {
        return mIsCouple;
    }

    @Override
    public int getRowNumber() {
        return mRowNumber;
    }

    @Override
    public int getColumnNumber() {
        return mColumnNumber;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public void updateType(int newType) {
        this.mType = newType;
    }
}
