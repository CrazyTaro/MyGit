package us.bestapp.henrytaro.draw.interfaces;

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;

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
     * {@link #STATUS_CHOOSE_SEAT},座位被选中,此事件被触发后面必定触发{@link #chooseInMapSuccess(AbsSeatEntity)}<br/>
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
     * @param seatEntity       座位接口,可能为null
     */
    public void chooseInMapSuccess(AbsSeatEntity seatEntity);

    /**
     * 选择座位失败,未单击到有效的座位区域(可能单击在空白区等)
     */
    public void chosseInMapFail();

    /**
     * 选中座位成功,此处指实际的座位(不包括map中不显示的座位或者是空白区域)
     *
     * @param seatEntity       座位数据接口
     */
    public void chooseSeatSuccess(AbsSeatEntity seatEntity);

    /**
     * 选中座位失败，此处可能是选中的座位无效(座位类型错误或不能显示等...)
     */
    public void chooseSeatFail();

    /**
     * 缩放达到最大限度(已达到最小值或者最大值)
     */
    public void scaleMaximum();
}
