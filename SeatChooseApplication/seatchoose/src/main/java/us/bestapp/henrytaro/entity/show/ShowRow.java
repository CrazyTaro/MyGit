package us.bestapp.henrytaro.entity.show;/**
 * Created by xuhaolin on 15/9/14.
 */

import java.util.ArrayList;

import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by xuhaolin on 15/9/14.
 * 此对象继承自{@link AbsRowEntity},JSON数据本身并不需要此对象,此对象是用于绘制时所需要的,
 * 是虚似出来的行(列表数据中原本不存在行的区分)
 */
public class ShowRow extends AbsRowEntity {

    public ShowRow(int x, int rowNumber) {
        super(x, rowNumber, true, false);
        this.mRowNumber = rowNumber;
    }

    /**
     * 将座位数据添加到当前行中
     *
     * @param newData
     */
    public void addNewData(AbsSeatEntity newData) {
        if (mAbsSeatList == null) {
            mAbsSeatList = new ArrayList<AbsSeatEntity>();
        }
        mAbsSeatList.add(newData);
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
