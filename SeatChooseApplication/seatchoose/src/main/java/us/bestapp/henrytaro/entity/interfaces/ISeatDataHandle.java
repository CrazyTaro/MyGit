package us.bestapp.henrytaro.entity.interfaces;

import us.bestapp.henrytaro.entity.Seat;

/**
 * Created by lenovo on 2015/9/5.
 */
public interface ISeatDataHandle {
    /**
     * 获取指定map指定位置中的座位类型<br/>
     * {@link us.bestapp.henrytaro.params.SeatParams#SEAT_TYPE_UNSHOW}，不显示<br/>
     * {@link us.bestapp.henrytaro.params.SeatParams#SEAT_TYPE_UNSELETED}，可选<br/>
     * {@link us.bestapp.henrytaro.params.SeatParams#SEAT_TYPE_SELETED}，已选<br/>
     * {@link us.bestapp.henrytaro.params.SeatParams#SEAT_TYPE_DISABLE_SELETED}，不可选<br/>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public int getSeatType(int mapRow, int mapColumn);

    /**
     * 设置map指定位置中的座位类型，此方法用于修改数据
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public boolean setSeatType(int mapRow, int mapColumn);

    /**
     * 获取map指定位置中的座位信息，<font color="#ff9900"><b>此座位信息包含了该座位所有的信息，此接口中获取座位信息的
     * 接口数据也都是来自于此处返回的座位信息</b></font>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public Seat getSeatInfo(int mapRow, int mapColumn);

    /**
     * 获取座位行数
     *
     * @return
     */
    public int getRowCount();

    /**
     * 获取某行中的座位列数，<font color="#ff9900"><b>此处的列数是指真正存在的座位列数，不同于map中的column</b></font>
     *
     * @param rowIndex map中的行
     * @return
     */
    public int getColumnCountInRow(int rowIndex);

    /**
     * 获取map指定位置中的座位是否情侣座类型
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public boolean getIsCouple(int mapRow, int mapColumn);

    /**
     * 获取map中指定位置的座位的<font color="#ff9900"><b>真实座位列值</b></font>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public int getSeatColumnInRow(int mapRow, int mapColumn);
}
