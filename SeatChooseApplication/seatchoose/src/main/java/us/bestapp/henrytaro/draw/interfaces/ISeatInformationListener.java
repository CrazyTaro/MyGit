package us.bestapp.henrytaro.draw.interfaces;

import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;

/**
 * Created by xuhaolin on 15/9/1.座位信息监听接口
 */
public interface ISeatInformationListener {
    /**
     * 当前座位状态,处理座位移动事件
     */
    public static final int STATUS_MOVE = 1;
    /**
     * 当前座位状态,处理座位区域单击事件,<font color="#ff9900"><b>事件为单击事件且在座位区域内,但座位此时不一定被选中单击,可能单击在无效区域</b></font>
     */
    public static final int STATUS_CLICK = 2;
    /**
     * 当前座位状态,处理座位被单击事件,<font color="#ff9900"><b>座位已被选中</b></font>
     */
    public static final int STATUS_CHOOSE_SEAT = 3;
    /**
     * 当前座位状态,处理座位区域被单击事件,<font color="#ff9900"><b>座位空白区域被选中,无任何座位被选中</b></font>
     */
    public static final int STATUS_CHOOSE_NOTHING = 4;

    /**
     * 当前座位区域的状态<br/>
     * <p>
     * {@link #STATUS_MOVE},座位移动中<br/>
     * {@link #STATUS_CLICK},座位区域被单击,此事件被触发后面必定跟着{@link #STATUS_CHOOSE_SEAT}或者是{@link #STATUS_CHOOSE_NOTHING}<br/>
     * {@link #STATUS_CHOOSE_SEAT},座位被选中,此事件被触发后面必定触发{@link #chooseInMapSuccess(int, int, ISeatEntity)}<br/>
     * {@link #STATUS_CHOOSE_NOTHING},座位未被选中,单击在空白区域<br/>
     * </p>
     *
     * @param status 座位状态
     */
    public void seatStatus(int status);

    /**
     * 单击选中map中某个位置,此处的选中并不是真实的选中意思,指的是成功单击到map中的有效区域,
     * 此时座位可能是被选中状态,也可能是未被选中状态,此处返回的仅仅是该单击有效区域的行列索引则,并不作任何的处理<br/>
     *
     * @param rowIndexInMap    座位在列表中的行索引,从0开始
     * @param columnIndexInMap 座位在列表中的列索引,从0开始
     * @param seatEntity       座位接口,可能为null
     */
    public void chooseInMapSuccess(int rowIndexInMap, int columnIndexInMap, ISeatEntity seatEntity);

//    /**
//     * 单击选中座位,此方法回调是在单击到有效的实际座位时回调,座位的状态是不确定的,可能被选中了,也可能未被选中,可根据座位接口中的类型进行处理.
//     * <font color="#ff9900"><b>此处指的是map中真正的座位,参数rowNumber/columnNubmer为该座位的实际行列值(从1开始)</b></font>,
//     * 当此方法有效时,{@link #chooseInMapSuccess(int, int, ISeatEntity)}必定也有返回,且两个方法返回的数据是不同的,行值是接近的(一个从0开始,一个从1开始),
//     * 但列值是不一定相同的,此方法中的是实际座位中的列数,而chooseInMapSuccess中的是有效区域中map的列值<br/>
//     * 如:<br/>
//     * chooseInMapSuccess(0,4);第一行,第五列
//     * chooseSeatSuccess(1,2,0,4);座位实际位置:第一行,第二列,位于map中第一行,第五列
//     *
//     * @param rowNumber    座位的实际行数,从1开始
//     * @param columnNumber 座位的实际列数,从1开始
//     * @param seatEntity   座位接口,此处必定为实际座位,不可能为null
//     */
//    public void chooseSeatSuccess(int rowNumber, int columnNumber, ISeatEntity seatEntity);

    /**
     * 选择座位失败,未单击到有效的座位区域(可能单击在空白区等)
     */
    public void chosseInMapFail();


    public void chooseSeatSuccess(int rowIndexInMap,int columnIndexInMap,int rowNumber,int columnNumber,ISeatEntity seatEntity);

    public void chooseSeatFail();

    /**
     * 缩放达到最大限度(已达到最小值或者最大值)
     */
    public void scaleMaximum();
}
