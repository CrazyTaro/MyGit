package us.bestapp.henrytaro.seatchoose.entity.absentity;

/**
 * Created by xuhaolin on 15/9/6.<br/>
 * 座位相关的必须实现或存在的一些对外的接口方法,该接口用于绘制中提供数据,
 * <font color="#ff9900"><b>座位是否需要被绘制由座位本身决定,包括座位是否情侣座等情况,
 * 这是由于座位的情况可能很复杂,解析方式也不一样,那么由座位自身处理是最好的</b></font>
 */
public abstract class AbsSeatEntity {
    private int mRowX = 0;
    private int mColumnY = 0;

    public AbsSeatEntity(int x, int y) {
        this.mRowX = x;
        this.mColumnY = y;
    }

    /**
     * 解析数据
     */
    public abstract void parseData();

    /**
     * 获取绘制样式的标签
     *
     * @return
     */
    public abstract String getDrawStyleTag();

    /**
     * 是否情侣座位
     *
     * @return
     */
    public abstract boolean isCouple();


    /**
     * 是否左情侣座,即当前座位的右边为与之配对的另一个情侣位;若返回false则相反,左边为与之配对的情侣座;
     * 此方法仅在{@link #isCouple()}为true时有效
     *
     * @return
     */
    public abstract boolean isCoupleLeftToRight();

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
     * 获取当前座位所在的位置,行索引(在map列表中的位置)
     *
     * @return
     */
    public int getX() {
        return this.mRowX;
    }

    /**
     * 获取当前座位所在的位置,列索引(在map列表中的位置)
     *
     * @return
     */
    public int getY() {
        return this.mColumnY;
    }

    /**
     * 设置座位的位置XY,设置的值只有>=0才有效,否则保留原值
     *
     * @param x 行索引,位置
     * @param y 列索引,位置
     */
    public void setXY(int x, int y) {
        if (x >= 0) {
            this.mRowX = x;
        }
        if (y >= 0) {
            this.mColumnY = y;
        }
    }


    /**
     * 当前位置座位是否存在,不存在则不绘制,此方法是绘制的一个判断标准
     *
     * @return
     */
    public abstract boolean isExsit();

    /**
     * 更新当前座位的座位信息,当前使用是当updateTag大于0时,当前座位更新为被选中;
     * 当updateTag小于0时,当前座位更新为未被选中
     *
     * @param updateTag 用于更新的标志,此处并不作任何限制,<font color="ff9900"><b>updateTag仅仅作为一个标量提供一个更新的目标,
     *                  实际上可以使用updateTag去处理更多的情况</b></font>
     */
    public abstract void updateData(int updateTag);

    /**
     * 当前座位是否被选中,返回值为整型,大于0为被选中,小于0为未被选中,其它情况为0(可能座位类型不对或该座位不可能更改状态)
     *
     * @return
     */
    public abstract int isChosen();

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (o == this) {
            return true;
        } else if (o instanceof AbsSeatEntity) {
            AbsSeatEntity seatEntity = (AbsSeatEntity) o;
            //位置要相同
            if (seatEntity.getX() == this.getX() && seatEntity.getY() == this.getY() &&
                    //行列号要相同
                    seatEntity.getRowNumber() == this.getRowNumber() && seatEntity.getColumnNumber() == this.getColumnNumber()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
