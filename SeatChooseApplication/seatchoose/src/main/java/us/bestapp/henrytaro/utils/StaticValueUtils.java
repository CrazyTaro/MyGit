package us.bestapp.henrytaro.utils;/**
 * Created by xuhaolin on 15/9/2.
 */

import us.bestapp.henrytaro.params.interfaces.IBaseParamsExport;

/**
 * Author:
 * Description:
 */
public class StaticValueUtils {


    /**
     * 座位默认基本类型,不可选(可见),<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_DISABLE_SELETED = 3;
    /**
     * 座位默认基本类型,已选,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_SELETED = 2;
    /**
     * 座位默认基本类型,未选,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>
     */
    public static int SEAT_TYPE_UNSELETED = 1;
    /**
     * 座位默认基本类型,不可见,<font color="#ff9900"><b>此方法与座位的类型并没有直接关系,该静态变量(非常量)仅是方便用于处理数据而已,可修改</b></font>,其值与{@link IBaseParamsExport#DRAW_TYPE_NO}保持一致
     */
    public static int SEAT_TYPE_UNSHOW = IBaseParamsExport.DRAW_TYPE_NO;


}
