package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;

/**
 * Created by xuhaolin on 15/9/10.
 */
public class EgSeat extends AbsSeatEntity {
    private int mType = IBaseParams.TYPE_ERROR;
    private int mRowNumber = 0;
    private int mColumnNumber = 0;

    public EgSeat(int rowNumber, int columnNumber, int type) {
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
    }

    @Override
    public void parseData() {

    }

    @Override
    public boolean isCouple() {
        return false;
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
