package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import java.util.ArrayList;

import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by xuhaolin on 15/9/10.
 */
public class EgSeatRow extends AbsRowEntity {
    private int mColumnCount = 0;

    public EgSeatRow(int rowIndex, int[] rowArr) {
        super(rowIndex + 1, true, false);
        if (rowArr == null) {
            throw new RuntimeException("rowarr can not be null");
        } else {
            this.mColumnCount = rowArr.length;
            mAbsSeatList = new ArrayList<AbsSeatEntity>();
            for (int i = 0; i < rowArr.length; i++) {
                mAbsSeatList.add(new EgSeat(rowIndex + 1, i + 1, rowArr[i]));
            }
        }
    }

    @Override
    public int getExsitColumnCount() {
        return this.mColumnCount;
    }

    @Override
    public int getColumnCount() {
        return this.mColumnCount;
    }

    @Override
    public AbsSeatEntity getSeatEntity(int columnIndex) {
        if (mAbsSeatList != null && mAbsSeatList.size() > columnIndex) {
            return this.mAbsSeatList.get(columnIndex);
        } else {
            return null;
        }
    }
}
