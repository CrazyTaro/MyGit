package us.bestapp.henrytaro.view.interfaces;

import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;

/**
 * Created by lenovo on 2015/8/24.
 * <p>选择座位触发的事件,包括当前选中的座位,选中成功/失败的状态等</p>
 */
public interface ISeatChooseEvent {
    /**
     * 座位选中成功回调,<font color="@ff9900"><b>此方法只有在实际座位被单击选中的情况下才会被调用</b></font>,
     * 此方法被调用时{@link #selectedSeatSuccess(int, int, int, int, ISeatEntity)}必定会被回调<br/>
     *
     * @param rowNumber    选中座位的行号
     * @param columnNumber 选中座位的列号
     * @param isChoosen    <font color="@ff9900"><b>座位被选中后的状态改变,</b></font>true为该座位当前已被选中,false为该座位当前未被选中(或被取消选中)
     */
    public void selectedSeatSuccess(int rowNumber, int columnNumber, boolean isChoosen);

    /**
     * 座位选中成功回调,<font color="@ff9900"><b>此方法只有在实际座位被单击选中的情况下才会被调用</b></font>
     *
     * @param rowInMap    座位在列表中的行索引,<font color="@ff9900"><b>此处的行列值为map中的索引,不是实际该座位的行列号</b></font>
     * @param columnInMap 座位在列表中的列索引,<font color="@ff9900"><b>此处的行列值为map中的索引,不是实际该座位的行列号</b></font>
     * @param rowNumber   座位<font color="@ff9900"><b>实际行号</b></font>
     * @param rowColumn   座位<font color="@ff9900"><b>实际列行号</b></font>
     * @param seatEntity  座位接口
     */
    public void selectedSeatSuccess(int rowInMap, int columnInMap, int rowNumber, int rowColumn, ISeatEntity seatEntity);

    /**
     * 当前选座失败，可能是没有选中座位，可能是选中位置的座位出错,或者单击的位置在空白区域,不能选中有效的实际座位
     */
    public void seletedFail();

    /**
     * 当前选中座位数已达到上限,设置选座上限请通过{@link ISeatViewInterface#setMostSeletedCount(int)}
     */
    public void seletedFull();
}
