package us.bestapp.henrytaro.player.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.DeadObjectException;
import android.text.TextUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by xuhaolin on 15/10/23.
 */
public class CommonUtils {
    /**
     * 192比特率
     */
    public static final String KBPS_192 = "_ab192";
    /**
     * 320比特率
     */
    public static final String KBPS_320 = "_ab320";


    public static final String URL_李白 = "http://m1.music.126.net/kAhFZHim0uZ2hx6C32-tqg==/7762552092593411.mp3";
    public static final String URL_十年 = "http://m1.music.126.net/V6G5ER-inEZ_Oufxq9tmfA==/1153387697546241.mp3";
    public static final String URL_匆匆那年 = "http://m1.music.126.net/Eo_-MW6N0NfylSz5x3c7sg==/6657542907290639.mp3";
    public static final String URL_夜空中最亮的星 = "http://m1.music.126.net/LA3YLd7pwbB3ahas69xp0g==/5815316999394446.mp3";
    public static final String URL_平凡之路 = "http://m1.music.126.net/at0wmUoxoCMqDJbJt1QFeQ==/5935163767130356.mp3";
    public static final String URL_Five_Hundred_Miles = "http://m1.music.126.net/25XnV5dWFisfNiHKS83lrw==/1284229581289947.mp3";
    public static final String URL_DJ = "http://mp3.dj527.com:97/dj527/d/2015-03-28/142736484941343.mp3";
    public static final String URL_Try = "http://m1.music.126.net/XJqqn29T4Tz1gad8qwFVuw==/6011030069576904.mp3";
    public static final String URL_如果有来生 = "http://m1.music.126.net/TEg0oTYxKQMXgMv0cVzPCQ==/1191870604517277.mp3";
    public static final String URL_I_Just_Wanna_Run = "http://m1.music.126.net/swBzXZJMeuqq753jV1a3qA==/2018703348607974.mp3";
    public static final String URL_192 = "http://hoishow-file.b0.upaiyun.com/uploads/boom_track/file/110/c89fb9a926699864c2efaa4e258deee1_ab192.mp3";
    public static final String URL_320 = "http://hoishow-file.b0.upaiyun.com/uploads/boom_track/file/110/c89fb9a926699864c2efaa4e258deee1_ab320.mp3";
    public static final String URL_ = "";
//    public static final String URL_ = "";

    public static final String[] FILE_NAME = {"192bit", "320bit", "李白", "十年", "勿勿那年", "夜空中最亮的星", "平凡之路", "Five Hundred Miles", "DJ", "Try", "如果有来生", "I Just Wanna Run"};
    public static final String[] URL_MUSICS = {URL_192, URL_320, URL_李白, URL_十年, URL_匆匆那年, URL_夜空中最亮的星, URL_平凡之路, URL_Five_Hundred_Miles, URL_DJ, URL_Try, URL_如果有来生, URL_I_Just_Wanna_Run};

    /**
     * 获取指定Kbps的链接
     *
     * @param url  原始链接
     * @param kbps 指定Kbps<br/>
     *             {@link #KBPS_192}<br/>
     *             {@link #KBPS_320}<br/>
     * @return
     */
    public static String getKbpsUrl(String url, String kbps) {
        if (url != null && kbps != null && (kbps.equals(KBPS_192) || kbps.equals(KBPS_320))) {
            return url.substring(0, url.length() - 4) + kbps + ".mp3";
        } else {
            return null;
        }
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;
    }

    /***
     * 检测当前应用是否在前台运行
     *
     * @param context 当前应用Context
     * @return
     */
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public static String getDateStr(String format, Calendar date) {
        String dayWeek = getWeekStr(date);
        return String.format(format, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE), dayWeek);
    }

    public static String getTimeStr(String format, Calendar date) {
        return String.format(format, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
    }

    public static String getWeekStr(Calendar date) {
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:
                return "星期日";
            case Calendar.MONDAY:
                return "星期一";
            case Calendar.TUESDAY:
                return "星期二";
            case Calendar.WEDNESDAY:
                return "星期三";
            case Calendar.THURSDAY:
                return "星期四";
            case Calendar.FRIDAY:
                return "星期五";
            case Calendar.SATURDAY:
                return "星期六";
            default:
                return "未知";
        }
    }

    public static final class IntentAction {
        public static final String INTENT_ACTION_SERVICE_START="us.bestapp.player.service.start";
        public static final String INTENT_ACTION_SERVICE_DESTROY="us.bestapp.player.service.destroy";

        public static final String INTENT_ACTION_SCREEN_ACTIVITY_DESTROY = "us.bestapp.player.screen_activity_destroy";
        public static final String INTENT_ACTION_SCREEN_ACTIVITY_CREATE = "us.bestapp.player.screen_activity_create";

        public static final String INTENT_ACTION_TRACK_UPDATE = "us.bestapp.player.track_action_update";

        public static final String INTENT_ACTION_SCREEN_ON = "android.intent.action.SCREEN_ON";
        public static final String INTENT_ACTION_SCRREN_OFF = "android.intent.action.SCREEN_OFF";

        /**
         * 通知单击关闭按钮广播
         */
        public static final String INTENT_ACTION_NOTIFY_CLOSE = "us.bestapp.player.action_close";
        /**
         * 通知单击播放按钮广播
         */
        public static final String INTENT_ACTION_NOTIFY_PLAY = "us.bestapp.player.action_play";
        /**
         * 通知单击上一首按钮广播
         */
        public static final String INTENT_ACTION_NOTIFY_PREVIOUS = "us.bestapp.player.action_previous";
        /**
         * 通知单击下一首按钮广播
         */
        public static final String INTENT_ACTION_NOTIFY_NEXT = "us.bestapp.player.action_next";
        /**
         * 通知单击收藏按钮广播
         */
        public static final String INTENT_ACTION_NOTIFY_LIKE = "us.bestapp.player.action_like";


        public static final String INTENT_EXTRA_TRACK_NAME = "track_name";
        public static final String INTENT_EXTRA_TRACK_ARTITST = "track_artist";
        public static final String INTENT_EXTRA_TRACK_PLAY_RESID = "track_play_resid";
        public static final String INTENT_EXTRA_TRACK_ABLUMN_RESID = "track_ablumn_resid";
        public static final String INTENT_EXTRA_TRACK_ABLUMN_BITMAP = "track_ablumn_bitmap";
        public static final String INTENT_EXTRA_TRACK_BACKGROUND_BITMAP = "track_bg_bitmap";
    }
}
