package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

import java.util.ArrayList;

/**
 * Created by xuhaolin on 15/9/10.
 * 虚拟座位列表,使用数字代码座位
 */
public class EgSeatMap extends AbsMapEntity {
    private String[] mRowIDs = null;

    public EgSeatMap(int[][] arr) {
        if (arr == null) {
            throw new RuntimeException("arr can not be null");
        } else {
            mRowIDs = new String[arr.length];
            mAbsRowList = new ArrayList<AbsRowEntity>();
            for (int i = 0; i < arr.length; i++) {
                EgSeatRow row = new EgSeatRow(i, arr[i]);
                mAbsRowList.add(row);
                mRowIDs[i] = row.getRowNumber() + "";
            }
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
        return this.getColumnCount(0);
    }

    @Override
    public String[] getRowIDs() {
        return mRowIDs;
    }


}
