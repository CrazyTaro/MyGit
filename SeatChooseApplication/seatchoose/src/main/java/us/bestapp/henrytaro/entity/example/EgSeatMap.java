package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.entity.interfaces.IMapEntity;
import us.bestapp.henrytaro.entity.interfaces.IRowEntity;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;

/**
 * Created by xuhaolin on 15/9/10.
 */
public class EgSeatMap implements IMapEntity {
    private List<IRowEntity> mRowList = null;

    public EgSeatMap(int[][] arr) {
        if (arr == null) {
            throw new RuntimeException("arr can not be null");
        } else {
            mRowList = new ArrayList<IRowEntity>();
            for (int i = 0; i < arr.length; i++) {
                mRowList.add(new EgSeatRow(i, arr[i]));
            }
        }
    }

    @Override
    public int getSeatType(int mapRow, int mapColumn) {
        ISeatEntity egSeat = this.getSeatInfo(mapRow, mapColumn);
        if (egSeat != null) {
            return egSeat.getType();
        } else {
            return IBaseParams.TYPE_ERROR;
        }
    }

    @Override
    public boolean updateSeatType(int type, int mapRow, int mapColumn) {
        ISeatEntity egSeat = this.getSeatInfo(mapRow, mapColumn);
        if (egSeat != null) {
            egSeat.updateType(type);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ISeatEntity getSeatInfo(int mapRow, int mapColumn) {
        try {
            return this.mRowList.get(mapRow).getSeat(mapColumn);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getRowCount() {
        if (this.mRowList != null) {
            return mRowList.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getColumnCount(int rowIndex) {
        if (this.mRowList != null && this.mRowList.size() > rowIndex) {
            return mRowList.get(rowIndex).getColumnCount();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxColumnCount() {
        return this.getColumnCount(0);
    }

    @Override
    public boolean isCouple(int mapRow, int mapColumn) {
        return false;
    }

    @Override
    public int getSeatColumnInRow(int mapRow, int mapColumn) {
        ISeatEntity egSeat = this.getSeatInfo(mapRow, mapColumn);
        if (egSeat != null) {
            return egSeat.getColumnNumber();
        } else {
            return -1;
        }
    }

    @Override
    public IRowEntity getSeatRowInMap(int mapRow) {
        if (this.mRowList != null && mRowList.size() > mapRow) {
            return this.mRowList.get(mapRow);
        } else {
            return null;
        }
    }
}
