package us.bestapp.henrytaro.seatchoose.utils;/**
 * Created by xuhaolin on 15/9/2.
 */

/**
 * Author:
 * Description:
 */
public class StringUtils {
    /**
     * 判断字符串是否为null或者空字串
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
