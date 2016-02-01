package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

import java.util.ArrayList;

/**
 * Created by xuhaolin on 15/9/10.
 */
public class EgSeatMap extends AbsMapEntity {

    public EgSeatMap(int[][] arr) {
        if (arr == null) {
            throw new RuntimeException("arr can not be null");
        } else {
            mAbsRowList = new ArrayList<AbsRowEntity>();
            for (int i = 0; i < arr.length; i++) {
                mAbsRowList.add(new EgSeatRow(i, arr[i]));
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
        return new String[0];
    }


}
