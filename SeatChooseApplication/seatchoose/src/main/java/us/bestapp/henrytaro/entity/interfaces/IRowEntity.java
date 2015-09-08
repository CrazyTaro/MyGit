package us.bestapp.henrytaro.entity.interfaces;

/**
 * Created by xuhaolin on 15/9/7.<br/>
 * 行信息接口,该接口用于提供绘制行时所需要的数据
 */
public interface IRowEntity {

    /**
     * 获取行中存在座位的列（真实的座位列数,不包含空列）
     *
     * @return
     */
    public int getRealColumnCount();

    /**
     * 获取行中的列数，此处是行中所有的列（包含空列）
     *
     * @return
     */
    public int getColumnCount();

    /**
     * 获取行数,从1开始
     *
     * @return
     */
    public int getRowNumber();

    /**
     * 设置是否绘制该行
     *
     * @param isDraw
     */
    public void setIsDraw(boolean isDraw);

    /**
     * 获取是否绘制该行
     *
     * @return
     */
    public boolean getIsDraw();

    /**
     * 获取该行是否不存在数据,为空(当需要绘制该行且该行为空时,则空出一行)
     *
     * @return
     */
    public boolean getIsEmpty();

    /**
     * 获取行中某个列位置对应的座位信息
     *
     * @param columnIndex 行中的行索引（非真实座位列索引）
     * @return
     */
    public ISeatEntity getSeat(int columnIndex);
}
