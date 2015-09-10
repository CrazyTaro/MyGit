package us.bestapp.henrytaro.entity;/**
 * Created by xuhaolin on 15/9/7.
 */

import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.params.SeatParams;

/**
 * Created by xuhaolin on 15/9/7.<br/>
 * 抽象座位类,用于实现绘制使用的座位接口及初始化部分必定需要的基本数据,需要子类实现其抽象方法完善此类
 */
public abstract class AbsSeat implements ISeatEntity {
    protected int mRowNumber = 0;
    protected int mType = SeatParams.seat_type_unshow;
    protected int mColumnNumber = 0;
    protected boolean mIsCouple = false;
    protected String mSeatInfo = null;

    /**
     * 构造函数,用于初始化数据
     *
     * @param rowNumber    座位行号,从1开始
     * @param columnNumber 座位列号,从1开始
     * @param type         座位类型,用整数表示
     * @param isCouple     是否情侣座位,此参数暂时未使用
     * @param seatInfo     座位信息,来自网络的JSON数据,用于解析当前座位的数据
     */
    public AbsSeat(int rowNumber, int columnNumber, int type, boolean isCouple, String seatInfo) {
        this.setParams(rowNumber, columnNumber, type, isCouple, seatInfo);
        //加载数据
        this.parseToSeat(rowNumber, 0, seatInfo, this);
    }

    /**
     * 设置参数
     *
     * @param rowNumber    座位行号,从1开始
     * @param columnNumber 座位列号,从1开始
     * @param type         座位类型,用整数表示
     * @param isCouple     是否情侣座位,此参数暂时未使用
     * @param seatInfo     座位信息,来自网络的JSON数据,用于解析当前座位的数据
     */
    public void setParams(int rowNumber, int columnNumber, int type, boolean isCouple, String seatInfo) {
        this.mRowNumber = rowNumber;
        this.mColumnNumber = columnNumber;
        this.mType = type;
        this.mIsCouple = isCouple;
        this.mSeatInfo = seatInfo;
    }

    @Override
    public boolean isCouple() {
        return this.mIsCouple;
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

    /**
     * 抽象方法,用于解析数据,由于传输的数据格式可能不同,所以解析数据部分需要重写该方法并实现其实际功能,
     * <font color="#ff9900"><b>此方法为重要方法,必须实现</b></font><br/>
     * 此方法的功能为,通过解析seatInfo得到座位相关的信息,参数1/2,行号与列号并不是必须的,
     * 如果座位信息里有包含则可以直接通过解析得到,此二参数仅仅作为一个备用参数;<font color="#ff9900"><b>当参数oldSeat不为null时,
     * 所有的数据应该覆盖到oldSeat中,同时返回的对象也是oldSeat本身;当参数oldSeat为null时,应该创建一个新的对象同时返回对象为新对象
     * </b></font>
     *
     * @param rowNumber    座位所在行号
     * @param columnNumber 座位所在的列号
     * @param seatInfo     座位解析信息
     * @param oldSeat      旧座位对象(可用于替换)   @return
     */
    public abstract AbsSeat parseToSeat(int rowNumber, int columnNumber, String seatInfo, AbsSeat oldSeat);

    /**
     * 重置此数据对象
     */
    public abstract void resetSeat();

}
