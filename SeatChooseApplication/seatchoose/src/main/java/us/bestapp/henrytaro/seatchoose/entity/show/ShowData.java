package us.bestapp.henrytaro.seatchoose.entity.show;/**
 * Created by xuhaolin on 15/9/14.
 */

import com.google.gson.annotations.SerializedName;

import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.seatchoose.params.baseparams.BaseSeatParams;

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

    /**
     * 构造函数
     *
     * @param x 行索引,位置
     * @param y 列索引,位置
     */
    public ShowData(int x, int y) {
        super(x, y);
    }

    @Override
    public void parseData() {
    }

    @Override
    public String getDrawStyleTag() {
        if (mStatus != null) {
            switch (mStatus) {
                case "avaliable":
                    return BaseSeatParams.TAG_OPTIONAL_SEAT;
                case "locked":
                    return BaseSeatParams.TAG_LOCK_SEAT;
                case "unused":
                    return BaseSeatParams.TAG_UNSHOW_SEAT;
                case "selected":
                    return BaseSeatParams.TAG_SELECTE_SEAT;
                default:
                    return BaseSeatParams.TAG_ERROR_SEAT;
            }
        } else {
            return BaseSeatParams.TAG_ERROR_SEAT;
        }
    }

    @Override
    public boolean isCouple() {
        return false;
    }

    @Override
    public boolean isCoupleLeftToRight() {
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
    public boolean isExsit() {
        if (mStatus.equals("unused")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void updateData(int updateTag) {
        if (updateTag > 0) {
            this.mStatus = "selected";
        } else {
            this.mStatus = "avaliable";
        }
    }

    @Override
    public int isChosen() {
        if (this.mStatus.equals("avaliable")) {
            return -1;
        } else if (this.mStatus.equals("selected")) {
            return 1;
        } else {
            return 0;
        }
    }
}
