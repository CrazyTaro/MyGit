package us.bestapp.henrytaro.entity.show;/**
 * Created by xuhaolin on 15/9/14.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;

/**
 * Created by xuhaolin on 15/9/14.
 */
public class ShowData extends AbsSeatEntity {
    @SerializedName("row")
    private int mRowNumber;
    @SerializedName("column")
    private int mColumnNumber;
    @SerializedName("name")
    private String mSeatName;
    @SerializedName("price")
    private float mPrice;
    @SerializedName("status")
    private String mStatus;

    private int mType = IBaseParams.TYPE_ERROR;

    @Override
    public void parseData() {
        if (mStatus != null) {
            switch (mStatus) {
                case "avaliable":
                    mType = SeatParams.seat_type_unselected;
                    break;
                case "locked":
                    mType = SeatParams.seat_type_disable_selected;
                    break;
                case "unused":
                    mType = IBaseParams.TYPE_ERROR;
                    break;
                default:
                    mType = IBaseParams.TYPE_ERROR;
                    break;
            }
        }
    }

    @Override
    public boolean isCouple() {
        return false;
    }

    @Override
    public int getRowNumber() {
        return this.mRowNumber;
    }

    @Override
    public int getColumnNumber() {
        return this.mColumnNumber;
    }

    @Override
    public int getType() {
        return this.mType;
    }

    @Override
    public void updateType(int newType) {
        this.mType = newType;
    }
}
