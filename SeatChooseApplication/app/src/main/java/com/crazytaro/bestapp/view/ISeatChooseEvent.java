package com.crazytaro.bestapp.view;

import com.crazytaro.bestapp.draw.params.SeatParams;

/**
 * Created by lenovo on 2015/8/24.
 * <p>选择座位触发的事件,包括当前选中的座位,选中成功/失败的状态等</p>
 */
public interface ISeatChooseEvent {
    /**s
     * 座位选中回调接口，此方法不一定会被调用，当单击在有效区域内时才会回调
     * <p><font color="#ff9900"><b>此方法实际上是对{@link #seatSeleted(int, int, int)}方法的简单处理并返回结果</b></font>，
     * 使用此方法请注意如果所有参数使用默认值则没有问题，如果座位类型有进行了自定义或者是全部替换默认的座位类型，
     * 请注意是否有重新设置座位参数中的默认座位处理类型{@link SeatParams#setSeatTypeConstant(int, int, int)},
     * 此方法的简单处理依赖于上述方法中设置的静态变量{@link SeatParams#SEAT_TYPE_SELETED}用于判断当前座位是否被选中</p>
     *
     * @param rowIndex    座位在列表中的行索引
     * @param columnIndex 列索引
     * @param isChosen    当前座位是否在操作后被选中，true为选中，false为未选中或者不能选中或者不存在座位等任何不可能选中该座位的状态
     */
    public void seatSeleted(int rowIndex, int columnIndex, boolean isChosen);

    /**
     * 座位选中回调接口
     * <p><font color="#ff9900"><b>此方法处理的情况及可选范围较大，如果处理比较复杂的座位类型建议使用此方法,此方法必定会被调用（除非没单击到有效区域） </b></font></p>
     * <p>当选中的位置出错或不存在座位时，返回座位错误类型{@link SeatParams#SEAT_TYPE_ERRO}</p>
     *
     * @param rowIndex    座位在列表中的行索引
     * @param columnIndex 列索引
     * @param seatType    当前座位在操作前的座位类型
     */
    public void seatSeleted(int rowIndex, int columnIndex, int seatType);

    /**
     * 当前选座失败，可能是没有选中座位，可能是选中位置的座位出错
     */
    public void seletedFail();

    /**
     * 当前选中座位数已达到上限
     */
    public void seletedFull();
}
