package us.bestapp.henrytaro.entity.show;/**
 * Created by xuhaolin on 15/9/14.
 */

import java.util.ArrayList;

import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by xuhaolin on 15/9/14.
 */
public class ShowRow extends AbsRowEntity {

    public ShowRow(int rowNumber) {
        super(rowNumber, true, false);
        this.mRowNumber = rowNumber;
    }

    public void addNewData(AbsSeatEntity newData) {
        if (mAbsSeatList == null) {
            mAbsSeatList = new ArrayList<AbsSeatEntity>();
        }
        mAbsSeatList.add(newData);
    }

    @Override
    public int getExsitColumnCount() {
        return mAbsSeatList.size();
    }

    @Override
    public int getColumnCount() {
        return mAbsSeatList.size();
    }


    @Override
    public AbsSeatEntity getSeatEntity(int columnIndex) {
        if (columnIndex < mAbsSeatList.size()) {
            return mAbsSeatList.get(columnIndex);
        } else {
            return null;
        }
    }
}
