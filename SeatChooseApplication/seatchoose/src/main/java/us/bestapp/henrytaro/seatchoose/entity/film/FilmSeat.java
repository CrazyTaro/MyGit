package us.bestapp.henrytaro.seatchoose.entity.film;/**
 * Created by xuhaolin on 15/9/2.
 */

import us.bestapp.henrytaro.seatchoose.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.seatchoose.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.seatchoose.utils.StringUtils;

/**
 * Created by xuhaolin on 15/9/2.<br/>
 * 自定义创建的对象,用于实现通用的座位接口与数据处理传递
 */
public class FilmSeat extends AbsSeatEntity {
    /**
     * 错误类型,默认的类型
     */
    public static final String TAG_ERROR_SEAT = BaseSeatParams.TAG_ERROR_SEAT;
    /**
     * 情侣座位可选的类型(普通座与情侣座选中类型是一样的)
     */
    public static final String TAG_COUPLE_OPTIONAL_SEAT = BaseSeatParams.TAG_COUPLE_OPTIONAL_SEAT;
    /**
     * 普通座可选类型
     */
    public static final String TAG_OPTIONAL_SEAT = BaseSeatParams.TAG_OPTIONAL_SEAT;
    /**
     * 已选类型
     */
    public static final String TAG_TAG_SELECTE_SEAT = BaseSeatParams.TAG_SELECTE_SEAT;
    /**
     * 未显示座位类型
     */
    public static final String TAG_UNSHOW_SEAT = BaseSeatParams.TAG_UNSHOW_SEAT;
    /**
     * 锁定座位类型
     */
    public static final String TAG_LOCK_SEAT = BaseSeatParams.TAG_LOCK_SEAT;

    private int mRowNumber = 0;
    private int mColumnNumber = 0;
    private int mType = 0;
    private String mTypeStr = null;
    private String mSeatInfo = null;


    /**
     * 获取新的实例,此方法会同时解析座位信息
     *
     * @param rowNumber 行号
     * @param seatInfo  座位信息
     * @return
     */
    public FilmSeat(int x, int y, int rowNumber, String seatInfo) {
        super(x, y);
        this.mRowNumber = rowNumber;
        this.mSeatInfo = seatInfo;
    }


    public void setParams(int rowNumber, int columnNumber, int type, String seatInfo) {
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
        this.mSeatInfo = seatInfo;
    }


    @Override
    public void parseData() {
        if (!StringUtils.isNullOrEmpty(mSeatInfo)) {

            //原始数据大致格式：ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0
            //ZL表示走廊，无座位
            //01@A0，表示第1行，A表示可选座位，LK表示锁定座位（不可选）
            //0表示座位类型，普通座位，1/2组成情侣座，情侣座不可单选，必须两个座位一起选
            if (!mSeatInfo.startsWith("ZL")) {
                String[] infos = mSeatInfo.split("@");
                if (infos != null) {
                    //解析列号
                    mColumnNumber = Integer.parseInt(infos[0]);
                    //解析情侣座
                    mType = Integer.parseInt(infos[2]);
                    mTypeStr = infos[1];
                }
            } else {
                mTypeStr = mSeatInfo;
            }
        }
    }

    @Override
    public String getDrawStyleTag() {
        if (mTypeStr == null) {
            return BaseSeatParams.TAG_ERROR_SEAT;
        } else {
            switch (mTypeStr) {
                case "A":
                    if (this.isCouple()) {
                        return BaseSeatParams.TAG_COUPLE_OPTIONAL_SEAT;
                    } else {
                        return BaseSeatParams.TAG_OPTIONAL_SEAT;
                    }
                case "LK":
                    return BaseSeatParams.TAG_LOCK_SEAT;
                case "SL":
                    return BaseSeatParams.TAG_SELECTE_SEAT;
                default:
                    return BaseSeatParams.TAG_UNSHOW_SEAT;
            }
        }
    }

    @Override
    public boolean isCouple() {
        if (mType == 1 || mType == 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isCoupleLeftToRight() {
        if (mType == 1) {
            return true;
        } else {
            return false;
        }
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
        if (mTypeStr.equals("ZL")) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void updateData(int updateTag) {
        if (!this.mTypeStr.equals("LK")) {
            if (updateTag > 0) {
                this.mTypeStr = "SL";
            } else {
                this.mTypeStr = "A";
            }
        }
    }

    @Override
    public int isChosen() {
        if (this.mTypeStr.equals("SL")) {
            return 1;
        } else if (this.mTypeStr.equals("A")) {
            return -1;
        } else {
            return 0;
        }
    }

}
