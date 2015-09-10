package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.entity.interfaces.IRowEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;

/**
 * Created by xuhaolin on 15/9/10.
 */
public class EgSeatRow implements IRowEntity {
    private int mRowIndex = 0;
    private int mColumnCount = 0;
    private boolean mIsDraw = true;
    private boolean mIsEmpty = false;
    List<ISeatEntity> mSeatList = null;

    public EgSeatRow(int rowIndex, int[] rowArr) {
        if (rowArr == null) {
            throw new RuntimeException("rowarr can not be null");
        } else {
            this.mRowIndex = rowIndex;
            this.mColumnCount = rowArr.length;
            mSeatList = new ArrayList<ISeatEntity>();
            for (int i = 0; i < rowArr.length; i++) {
                mSeatList.add(new EgSeat(rowIndex + 1, i + 1, rowArr[i]));
            }
        }
    }

    @Override
    public int getRealColumnCount() {
        return this.mColumnCount;
    }

    @Override
    public int getColumnCount() {
        return this.mColumnCount;
    }

    @Override
    public int getRowNumber() {
        return this.mRowIndex + 1;
    }

    @Override
    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }

    @Override
    public boolean isDraw() {
        return this.mIsDraw;
    }

    @Override
    public boolean isEmpty() {
        return this.mIsEmpty;
    }

    @Override
    public ISeatEntity getSeat(int columnIndex) {
        if (mSeatList != null && mSeatList.size() > columnIndex) {
            return this.mSeatList.get(columnIndex);
        } else {
            return null;
        }
    }
}
