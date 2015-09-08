package us.bestapp.henrytaro.entity.interfaces;

/**
 * Created by xuhaolin on 15/9/6.<br/>
 * 座位相关的必须实现或存在的一些对外的接口方法,该接口用于绘制中提供数据
 */
public interface ISeatEntity {

    /**
     * 是否情侣座位,暂时未使用到
     *
     * @return
     */
    public boolean getIsCouple();

    /**
     * 获取当前座位所在的实际行号,从1开始
     *
     * @return
     */
    public int getRowNumber();

    /**
     * 获取当前座位所在的实际列号,从1开始
     *
     * @return
     */
    public int getColumnNumber();

    /**
     * 获取当前座位的座位类型
     *
     * @return
     */
    public int getType();

    /**
     * 更新当前座位的座位类型
     *
     * @param newType
     */
    public void updateType(int newType);
}
