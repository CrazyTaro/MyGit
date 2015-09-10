package us.bestapp.henrytaro.entity.interfaces;

/**
 * Created by lenovo on 2015/9/5.<br/>
 * 座位数据处理,座位处理的某些必须实现的方法,提供绘制时需要的数据接口
 */
public interface IMapEntity {
    /**
     * 获取指定map指定位置中的座位类型,以下类型为默认类型<br/>
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
     * @param type      座位类型
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public boolean updateSeatType(int type, int mapRow, int mapColumn);


    /**
     * 获取map指定位置中的座位信息，<font color="#ff9900"><b>此座位信息包含了该座位所有的信息，此接口中获取座位信息的
     * 接口数据也都是来自于此处返回的座位信息</b></font>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public ISeatEntity getSeatInfo(int mapRow, int mapColumn);

    /**
     * 获取座位行数
     *
     * @return
     */
    public int getRowCount();

    /**
     * 获取map中某行的座位列数,<font color="#ff9900"><b>并不是map中所有行的列数相同,有一些行可能会比其它行短,
     * 请注意(这部分是由实际的数据决定的,实际的数据并不以完整地二维表形式给出所有的数据,只提供了有效的数据)</b></font>
     *
     * @return
     */
    public int getColumnCount(int rowIndex);

    /**
     * 获取所有行中最大列的列数,整个二维表基于最大行数与列数进行绘制
     *
     * @return
     */
    public int getMaxColumnCount();

    /**
     * 获取map指定位置中的座位是否情侣座类型,<font color="#ff9900"><b>当座位不存在时,返回false</b></font>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public boolean isCouple(int mapRow, int mapColumn);

    /**
     * 获取map中指定位置的座位的<font color="#ff9900"><b>真实座位列值,当座位不存在时,返回-1</b></font>
     *
     * @param mapRow    map中的行
     * @param mapColumn map中的列，<font color="#ff9900"><b>此处的列并不一定是实际座位中的列数，只是在此map中的列，
     *                  因为map中的列并不都会显示出来，当不显示时，则该列数就不存在</b></font>
     * @return
     */
    public int getSeatColumnInRow(int mapRow, int mapColumn);

    /**
     * 获取某行的所有座位类型数组形式,包含实际的座位与不显示出来的座位(即该map中的该行所有的列),
     *
     * @param mapRow map中的行
     * @return
     */
    public IRowEntity getSeatRowInMap(int mapRow);
}
