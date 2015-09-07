package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/7.
 */

import us.bestapp.henrytaro.entity.interfaces.ISeatEntityHandle;
import us.bestapp.henrytaro.params.SeatParams;

/**
 * Created by xuhaolin on 15/9/7.
 */
public abstract class AbsSeat implements ISeatEntityHandle {
    protected int mRowNumber = 0;
    protected int mType = SeatParams.SEAT_TYPE_UNSHOW;
    protected int mColumnNumber = 0;
    protected boolean mIsCouple = false;
    protected String mSeatInfo = null;

    public AbsSeat(int rowNumber, int columnNumber, int type, boolean isCouple, String seatInfo) {
        this.setParams(rowNumber, columnNumber, type, isCouple, seatInfo);
    }

    public void setParams(int rowNumber, int columnNumber, int type, boolean isCouple, String seatInfo) {
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
        this.mIsCouple = isCouple;
        this.mSeatInfo = seatInfo;
    }

    @Override
    public boolean getIsCouple() {
        return this.mIsCouple;
    }

    @Override
    public int getRowNumber() {
        return this.mRowNumber;
    }

    @Override
    public int getColumnNumber() {
        return this.mColumnNumber;
    }

    @Override
    public int getType() {
        return this.mType;
    }

    @Override
    public void updateType(int newType) {
        this.mType = newType;
    }

    public abstract AbsSeat parseToSeat(int rowNumber, String seatInfo, AbsSeat oldSeat);

    /**
     * 重置此数据对象
     */
    public abstract void resetSeat();

    /**
     * 此方法为解析数据生成座位
     *
     * @param rowNumber
     * @param seatInfo
     * @return
     */
}
