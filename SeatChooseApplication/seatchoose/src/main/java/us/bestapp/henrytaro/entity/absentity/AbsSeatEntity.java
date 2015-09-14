package us.bestapp.henrytaro.entity.absentity;

/**
 * Created by xuhaolin on 15/9/6.<br/>
 * 座位相关的必须实现或存在的一些对外的接口方法,该接口用于绘制中提供数据
 */
public abstract class AbsSeatEntity {

    /**
     * 解析数据
     */
    public abstract void parseData();

    /**
     * 是否情侣座位,暂时未使用到
     *
     * @return
     */
    public abstract boolean isCouple();

    /**
     * 获取当前座位所在的实际行号,从1开始
     *
     * @return
     */
    public abstract int getRowNumber();

    /**
     * 获取当前座位所在的实际列号,从1开始
     *
     * @return
     */
    public abstract int getColumnNumber();

    /**
     * 获取当前座位的座位类型
     *
     * @return
     */
    public abstract int getType();

    /**
     * 更新当前座位的座位类型
     *
     * @param newType
     */
    public abstract void updateType(int newType);
}
