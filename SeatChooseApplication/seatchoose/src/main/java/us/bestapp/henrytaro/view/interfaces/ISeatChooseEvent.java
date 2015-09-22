package us.bestapp.henrytaro.view.interfaces;

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

/**
 * Created by lenovo on 2015/8/24.
 * <p>选择座位触发的事件,包括当前选中的座位,选中成功/失败的状态等</p>
 */
public interface ISeatChooseEvent {
    /**
     * 座位选中成功回调,<font color="#ff9900"><b>座位状态改变前</b></font><br/>
     * 座位信息包含了该座位在列表中的行索引,此处的行列值为map中的索引,不是实际该座位的行列号,从0开始.
     * 及座位在列表中的列索引,此处的行列值为map中的索引,不是实际该座位的行列号,从0开始,同时也包含了座位实际的行列号<br/>
     * <br/>
     * {@link AbsSeatEntity#getX()},获取座位的行索引,位置信息<br/>
     * {@link AbsSeatEntity#getY()},获取座位的列索引,位置信息<br/>
     * {@link AbsSeatEntity#getRowNumber()},获取座位的行号,具体信息<br/>
     * {@link AbsSeatEntity#getColumnNumber()},获取座位的列号,具体信息<br/>
     *
     * @param seatEntity 座位接口
     */
    public void clickSeatSuccess(AbsSeatEntity seatEntity);

    /**
     * 被选中座位状态改变时回调,<font color="#ff9900"><b>此处只有座位可以被选中且座位的状态改变才会回调,特别是情侣座情况,只有当选中的情侣座可以一起改变时才会返回</b></font>
     *
     * @param seatEntities 选中座位
     * @param isChosen     状态改变之后是否被选中
     */
    public void seatStatusChanged(AbsSeatEntity[] seatEntities, boolean isChosen);

    /**
     * 当前选座失败，可能是没有选中座位，可能是选中位置的座位出错,或者单击的位置在空白区域,不能选中有效的实际座位
     *
     * @param isCoupleSeat 是否情侣座选座失败
     */
    public void seletedFail(boolean isCoupleSeat);

    /**
     * 当前选中座位数已达到上限,设置选座上限请通过{@link ISeatViewInterface#setMostSeletedCount(int)}
     *
     * @param isCoupleSeat 是否选中情侣座时座位已满
     */
    public void seletedFull(boolean isCoupleSeat);

    /**
     * 缩放到极限值(最大或最小值)
     */
    public void scaleMaximum();
}
