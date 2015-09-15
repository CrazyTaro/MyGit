package us.bestapp.henrytaro.entity.absentity;

import java.util.List;

/**
 * Created by xuhaolin on 15/9/7.<br/>
 * 行信息接口,该接口用于提供绘制行时所需要的数据
 */
public abstract class AbsRowEntity {

    /**
     * 座位列表,用于存放每一行对应的座位列表(包含每一列数据,空座位)
     */
    protected List<AbsSeatEntity> mAbsSeatList = null;
    /**
     * 当前行的行号
     */
    protected int mRowNumber = 0;
    /**
     * 是否需要绘制当前行,默认false
     */
    protected boolean mIsDraw = false;
    /**
     * 当前行是否为空行,默认true
     */
    protected boolean mIsEmpty = true;

    /**
     * 构造函数,创建带默认值的构造函数
     *
     * @param rowNumber 行号
     * @param isDraw    是否绘制当前行
     * @param isEmpty   当前行是否为空行
     */
    public AbsRowEntity(int rowNumber, boolean isDraw, boolean isEmpty) {
        this.mRowNumber = rowNumber;
        this.mIsDraw = isDraw;
        this.mIsEmpty = isEmpty;
    }

    /**
     * 设置/替换默认的座位列表
     *
     * @param seatEntityList
     */
    public void setDefaultSeatEntityList(List<AbsSeatEntity> seatEntityList) {
        this.mAbsSeatList = seatEntityList;
    }

    /**
     * 获取默认的座位列表
     *
     * @return
     */
    public List<AbsSeatEntity> getDefaultSeatEntityList() {
        return this.mAbsSeatList;
    }

    /**
     * 默认解析数据,通过调用{@link AbsSeatEntity#parseData()}方法进行解析
     */
    public void parseData() {
        if (mAbsSeatList != null && mAbsSeatList.size() > 0) {
            for (AbsSeatEntity absSeat : mAbsSeatList) {
                absSeat.parseData();
            }
        }
    }

    /**
     * 获取行中存在座位的列（真实存在的座位列数,不包含空列）
     *
     * @return
     */
    public abstract int getExsitColumnCount();

    /**
     * 获取行中的列数，此处是行中所有的列（包含空列）
     *
     * @return
     */
    public abstract int getColumnCount();


    /**
     * 获取行中某个列位置对应的座位信息,<font color="#ff9900"><b>绘制必须方法</b></font>
     *
     * @param columnIndex 行中的行索引（非真实座位列索引）
     * @return
     */
    public abstract AbsSeatEntity getSeatEntity(int columnIndex);

    /**
     * 获取行数,从1开始
     *
     * @return
     */
    public int getRowNumber() {
        return this.mRowNumber;
    }

    /**
     * 设置是否绘制该行
     *
     * @param isDraw
     */
    public void setIsDraw(boolean isDraw) {
        this.mIsDraw = isDraw;
    }

    /**
     * 获取是否绘制该行,<font color="#ff9900"><b>绘制必须方法</b></font>
     *
     * @return
     */
    public boolean isDraw() {
        return this.mIsDraw;
    }

    /**
     * 设置该行是否存在数据(当需要绘制该行且该行为空时,则空出一行),此情况与该行需要绘制但每个座位都不存在是相同的,
     * 只是省略了一些工作与处理
     *
     * @param isEmpty
     */
    public void setIsEmpty(boolean isEmpty) {
        this.mIsEmpty = isEmpty;
    }

    /**
     * 获取该行是否不存在数据,为空(当需要绘制该行且该行为空时,则空出一行),此情况与该行需要绘制但每个座位都不存在是相同的,
     * 只是省略了一些工作与处理,若不想处理是否为空选项,设置isDraw为true且所有座位类型为错误类型或不需要绘制也可以达到相同的效果<br/>
     * <font color="#ff9900"><b>绘制必须方法</b></font>
     *
     * @return
     */
    public boolean isEmpty() {
        return this.mIsEmpty;
    }

}
