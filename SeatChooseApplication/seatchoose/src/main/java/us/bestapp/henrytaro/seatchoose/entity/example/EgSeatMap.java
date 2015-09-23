package us.bestapp.henrytaro.seatchoose.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import java.util.ArrayList;

import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsRowEntity;
import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsSeatEntity;

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

}
