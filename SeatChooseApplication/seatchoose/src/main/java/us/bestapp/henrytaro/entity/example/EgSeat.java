package us.bestapp.henrytaro.entity.example;/**
 * Created by xuhaolin on 15/9/10.
 */

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;

/**
 * Created by xuhaolin on 15/9/10.
 *4/5 表示情侣座
 * 1表示座位可选
 * 2表示已选座位
 * 3表示锁定座位
 * -4/-5 表示已选了的情侣座位
 */
public class EgSeat extends AbsSeatEntity {
    private int mType = IBaseParams.TYPE_ERROR;
    private int mRowNumber = 0;
    private int mColumnNumber = 0;

    public EgSeat(int x, int y, int rowNumber, int columnNumber, int type) {
        super(x, y);
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
    }

    @Override
    public void parseData() {

    }

    @Override
    public String getDrawStyleTag() {
        switch (mType) {
            case 1:
                return BaseSeatParams.DRAW_STYLE_OPTIONAL_SEAT;
            case -4:
            case -5:
            case 2:
                return BaseSeatParams.DRAW_STYLE_SELECTED_SEAT;
            case 3:
                return BaseSeatParams.DRAW_STYLE_LOCK_SEAT;
            case 4:
            case 5:
                return BaseSeatParams.DRAW_STYLE_COUPLE_OPTIONAL_SEAT;
            default:
                return BaseSeatParams.DRAW_STYLE_ERROR_SEAT;
        }
    }

    @Override
    public boolean isCouple() {
        return mType == 4 || mType == 5 || mType == -4 || mType == -5;
    }

    @Override
    public boolean isCoupleLeftToRight() {
        return mType == 4 || mType == -4;
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
    public boolean isExsit() {
        return mType != 0;
    }

    @Override
    public void updateData(int updateTag) {
        if (updateTag > 0) {
            switch (mType) {
                case 1:
                    this.mType = 2;
                    break;
                case 4:
                    this.mType = -4;
                    break;
                case 5:
                    this.mType = -5;
                    break;
            }
        } else {
            switch (mType) {
                case 2:
                    this.mType = 1;
                    break;
                case -4:
                    this.mType = 4;
                    break;
                case -5:
                    this.mType = 5;
                    break;
            }
        }
    }

    @Override
    public int isChosen() {
        switch (mType) {
            case 1:
            case 4:
            case 5:
                return -1;
            case 2:
            case -4:
            case -5:
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public String getRow() {
        return null;
    }

    @Override
    public String getColumn() {
        return null;
    }

}
