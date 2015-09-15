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


    public boolean isCoupleToRight() {
        return true;
    }

    /**
     * 获取当前座位所在的实际行号,从1开始,类似{@link #getColumnNumber()},<font color="#ff9900"><b>绘制必须方法</b></font>
     *
     * @return
     */
    public abstract int getRowNumber();

    /**
     * 获取当前座位所在的实际列号,从1开始,该方法仅提供一个更加方便的数据返回接口,用于处理座位行列号与其所在列表中位置不统一的情况,
     * 若不需要处理此情况或者座位行列号与其位置统一,可按实际情况覆盖此方法;<font color="#ff9900"><b>绘制必须方法</b></font>
     *
     * @return
     */
    public abstract int getColumnNumber();

    /**
     * 获取当前座位的座位类型,<font color="#ff9900"><b>绘制必须方法</b></font>
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
