package us.bestapp.henrytaro.player.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.util.TreeMap;

import us.bestapp.henrytaro.R;

/**
 * Created by xuhaolin on 15/10/26.
 * 通知栏创建及更新工具类
 */
public class NotificationUtils {
    /**
     * 更新播放按钮
     */
    public static final int UPDATE_VIEW_PLAY = 0x0;
    /**
     * 更新上一首按钮
     */
    public static final int UPDATE_VIEW_PREVIOUS = 0x1;
    /**
     * 更新下一首按钮
     */
    public static final int UPDATE_VIEW_NEXT = 0x2;
    /**
     * 更新专辑头像
     */
    public static final int UPDATE_VIEW_ALBUMN = 0x3;
    /**
     * 更新收藏按钮
     */
    public static final int UPDATE_VIEW_LIKE = 0x4;
    /**
     * 更新背景色
     */
    public static final int UPDATE_VIEW_BACKGROUND = 0x5;


    /**
     * 唯一固定的通知ID
     */
    private static final int NOTIFICATION_ID = 0x10086;
    private static Context mServiceContext;
    private static RemoteViews mNotificationView;
    private static Notification mTrackNotification;

    /**
     * 初始化数据,此方法必须被调用,否则不可用其它的方法
     *
     * @param context 此参数不可为null
     */
    public static void initial(Context context) {
        if (context == null) {
            throw new RuntimeException("context can't be null");
        }
        mServiceContext = context;
    }

    /**
     * 显示固定的通知栏控制器
     *
     * @param titile         第一次显示时通知显示的标题
     * @param contentText    第一次显示时通知的内容
     * @param trackName      第一次显示时默认替换歌曲名的字符串
     * @param trackArtist    第一次显示时默认替换歌手名的字符串
     * @param activityIntent 单击通知栏启动的activity
     */
    public static final void showNotifcation(String titile, String contentText, String trackName, String trackArtist, Intent activityIntent) {
        //创建默认的intent
        if (activityIntent == null) {
            activityIntent = new Intent();
        }

        //通知栏管理服务
        NotificationManager notifyMgr = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);

//        Notification screenNotification = createScreenNotification(activityIntent, titile, contentText, trackName, trackArtist);
        mTrackNotification = createNormalNotification(activityIntent, titile, contentText, trackName, trackArtist, null);

        //通知显示
        notifyMgr.notify(NOTIFICATION_ID, mTrackNotification);
    }

    private static Notification createScreenNotification(Intent activityIntent, String title, String contentText, String trackName, String trackArtist) {
        if (Build.VERSION.SDK_INT < 21) {
            return null;
        }
        //通知栏管理服务
        NotificationManager notifyMgr = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(mServiceContext, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //创建通知栏
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mServiceContext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(contentText);
        builder.setContentIntent(pendingintent);

        //否则显示小通知视图
        RemoteViews remoteView = new RemoteViews(mServiceContext.getPackageName(), R.layout.notification_screen_lock);
        builder.setContent(remoteView);
        Notification screenNotification = builder.build();
        //设置通知栏显示的内容正在运行且不会被用户删除(常驻)
        screenNotification.flags = Notification.FLAG_NO_CLEAR;

        //初始化第一次显示的所有数据及图片
        remoteView.setTextViewText(R.id.textview_name, trackName);
        remoteView.setTextViewText(R.id.textview_artist, trackArtist);
        remoteView.setImageViewResource(R.id.imageview_ablumn, R.drawable.ic_track);
        remoteView.setImageViewResource(R.id.imageview_play, R.drawable.ic_play);
        remoteView.setImageViewResource(R.id.imageview_like, R.drawable.ic_like);
        remoteView.setImageViewResource(R.id.imageview_next, R.drawable.ic_next);
        remoteView.setImageViewResource(R.id.imageview_pre, R.drawable.ic_previous);
        remoteView.setInt(R.id.lin_container, "setBackgroundColor", Color.WHITE);

        //绑定按钮的单击广播事件
        remoteView.setOnClickPendingIntent(R.id.imageview_play, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY));
        remoteView.setOnClickPendingIntent(R.id.imageview_like, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE));
        remoteView.setOnClickPendingIntent(R.id.imageview_next, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT));
        remoteView.setOnClickPendingIntent(R.id.imageview_pre, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS));

        return screenNotification;
    }

    private static Notification createNormalNotification(Intent activityIntent, String title, String contentText, String trackName, String trackArtist, Notification screenNotification) {
        //通知栏管理服务
        NotificationManager notifyMgr = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(mServiceContext, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //创建通知栏
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mServiceContext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(contentText);
        builder.setContentIntent(pendingintent);
        if (screenNotification != null) {
            builder.setPublicVersion(screenNotification);
        }

        Notification trackNotification = builder.build();
        //设置通知栏显示的内容正在运行且不会被用户删除(常驻)
        trackNotification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        //否则显示小通知视图
        RemoteViews remoteView = getContentView(trackNotification, mServiceContext.getPackageName());
        setupView(remoteView, trackName, trackArtist);

//        //初始化第一次显示的所有数据及图片
//        remoteView.setTextViewText(R.id.textview_name, trackName);
//        remoteView.setTextViewText(R.id.textview_artist, trackArtist);
//        remoteView.setImageViewResource(R.id.imageview_ablumn, R.drawable.ic_track);
//        remoteView.setImageViewResource(R.id.imageview_play, R.drawable.ic_play);
//        remoteView.setImageViewResource(R.id.imageview_next, R.drawable.ic_next);
//        remoteView.setInt(R.id.lin_container, "setBackgroundColor", Color.WHITE);
//
//        //绑定按钮的单击广播事件
//        remoteView.setOnClickPendingIntent(R.id.imageview_play, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY));
//        remoteView.setOnClickPendingIntent(R.id.imageview_next, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT));

        return trackNotification;
    }

    private static boolean isUseBigContentView() {
        if (Build.VERSION.SDK_INT >= 16 && !CommonUtils.isMIUI()) {
            return true;
        } else {
            return false;
        }
    }

    private static RemoteViews getContentView(Notification notification, String packageName) {
        RemoteViews remoteViews = null;
        if (isUseBigContentView()) {
            remoteViews = new RemoteViews(packageName, R.layout.notification_big);
            if (Build.VERSION.SDK_INT >= 16) {
                notification.bigContentView = remoteViews;
                notification.priority = Notification.PRIORITY_HIGH;
            }
        } else {
            remoteViews = new RemoteViews(packageName, R.layout.notification_small);
            notification.contentView = remoteViews;
        }
        return remoteViews;
    }

    private static void setupView(RemoteViews remoteView, String trackName, String trackArtist) {
        //初始化第一次显示的所有数据及图片
        remoteView.setTextViewText(R.id.textview_name, trackName);
        remoteView.setTextViewText(R.id.textview_artist, trackArtist);
        remoteView.setImageViewResource(R.id.imageview_ablumn, R.drawable.ic_track);
        remoteView.setImageViewResource(R.id.imageview_play, R.drawable.ic_play);
        remoteView.setImageViewResource(R.id.imageview_next, R.drawable.ic_next);
        remoteView.setInt(R.id.lin_container, "setBackgroundColor", Color.WHITE);

        //绑定按钮的单击广播事件
        remoteView.setOnClickPendingIntent(R.id.imageview_play, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY));
        remoteView.setOnClickPendingIntent(R.id.imageview_next, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT));
        if (isUseBigContentView()) {
            //初始化其它未显示的所有数据及图片
            remoteView.setTextViewText(R.id.textview_name, trackName);
            remoteView.setTextViewText(R.id.textview_artist, trackArtist);
            remoteView.setImageViewResource(R.id.imageview_like, R.drawable.ic_like);
            remoteView.setImageViewResource(R.id.imageview_pre, R.drawable.ic_previous);
            remoteView.setImageViewResource(R.id.imageview_close, R.drawable.ic_close);

            //绑定其它按钮的单击广播事件
            remoteView.setOnClickPendingIntent(R.id.imageview_like, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE));
            remoteView.setOnClickPendingIntent(R.id.imageview_pre, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS));
            remoteView.setOnClickPendingIntent(R.id.imageview_pre, getBroadcastPendingIntent(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE));
        }
    }

    /**
     * 获取广播使用的Intent
     *
     * @param action 广播动作<br/>
     *               {@link us.bestapp.henrytaro.player.utils.CommonUtils.IntentAction#INTENT_ACTION_NOTIFY_CLOSE}<br/>
     *               {@link us.bestapp.henrytaro.player.utils.CommonUtils.IntentAction#INTENT_ACTION_NOTIFY_PREVIOUS}<br/>
     *               {@link us.bestapp.henrytaro.player.utils.CommonUtils.IntentAction#INTENT_ACTION_NOTIFY_NEXT}<br/>
     *               {@link us.bestapp.henrytaro.player.utils.CommonUtils.IntentAction#INTENT_ACTION_NOTIFY_LIKE}<br/>
     *               {@link us.bestapp.henrytaro.player.utils.CommonUtils.IntentAction#INTENT_ACTION_NOTIFY_PLAY}<br/>
     * @return
     */
    private static PendingIntent getBroadcastPendingIntent(String action) {
        Intent broadCastIntent = new Intent(action);
        return PendingIntent.getBroadcast(mServiceContext, 0, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void updateNotification(String trackName, String trackArtist) {
        startUpdate();
        updateText(trackName, trackArtist);
        submitUpdate();
    }

    public static void updateNotification(Bitmap ablumn) {
        if (ablumn != null && !ablumn.isRecycled()) {
            startUpdate();
            updateView(UPDATE_VIEW_ALBUMN, ablumn);
            submitUpdate();
        }
    }

    /**
     * 完整地更新通知栏状态
     *
     * @param trackName
     * @param trackArtist
     * @param playBtnResID 当前播放按钮显示的图标
     */
    public static void updateNotification(String trackName, String trackArtist, int playBtnResID) {
        startUpdate();
        updateText(trackName, trackArtist);
        updateView(UPDATE_VIEW_PLAY, playBtnResID);
        submitUpdate();
    }

    /***
     * 完整地更新通知栏状态
     *
     * @param trackName
     * @param trackArtist
     * @param playBtnResID 当前播放按钮显示的图标
     * @param ablumnBitmap 当前专辑头像显示的图像
     */
    public static void updateNotification(String trackName, String trackArtist, int playBtnResID, Bitmap ablumnBitmap) {
        startUpdate();
        updateText(trackName, trackArtist);
        updateView(UPDATE_VIEW_PLAY, playBtnResID);
        updateView(UPDATE_VIEW_ALBUMN, ablumnBitmap);
        submitUpdate();
    }

    /**
     * 开始更新准备操作
     */
    private static void startUpdate() {
        //更新需要创建新的remoteviews
        mNotificationView = getContentView(mTrackNotification, mServiceContext.getPackageName());
        updateBackgroundColor(Color.WHITE);
    }

    /**
     * 确认提交更新通知栏
     */
    private static void submitUpdate() {
        NotificationManager notifyMgr = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.notify(NOTIFICATION_ID, mTrackNotification);
    }

    /**
     * 更新view的背景图像显示
     *
     * @param updateView 需要更新view的标识<br/>
     *                   {@link #UPDATE_VIEW_PLAY}<br/>
     *                   {@link #UPDATE_VIEW_PREVIOUS}<br/>
     *                   {@link #UPDATE_VIEW_NEXT}<br/>
     *                   {@link #UPDATE_VIEW_LIKE}<br/>
     *                   {@link #UPDATE_VIEW_ALBUMN}<br/>
     *                   {@link #UPDATE_VIEW_BACKGROUND}此方法中此标识无效,没有与之对应的背景更新方法<br/>
     * @param bitmap     图像
     * @return
     */
    private static boolean updateView(int updateView, Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return false;
        }
        switch (updateView) {
            case UPDATE_VIEW_PLAY:
                mNotificationView.setBitmap(R.id.imageview_play, "setImageBitmap", bitmap);
                break;
            case UPDATE_VIEW_PREVIOUS:
                mNotificationView.setBitmap(R.id.imageview_pre, "setImageBitmap", bitmap);
                break;
            case UPDATE_VIEW_NEXT:
                mNotificationView.setBitmap(R.id.imageview_next, "setImageBitmap", bitmap);
                break;
            case UPDATE_VIEW_ALBUMN:
                mNotificationView.setBitmap(R.id.imageview_ablumn, "setImageBitmap", bitmap);
                break;
            case UPDATE_VIEW_LIKE:
                mNotificationView.setBitmap(R.id.imageview_like, "setImageBitmap", bitmap);
                break;
            default:
                return false;
        }
        return true;
    }

    /**
     * 更新view的背景资源显示
     *
     * @param updateView       需要更新view的标识<br/>
     *                         {@link #UPDATE_VIEW_PLAY}<br/>
     *                         {@link #UPDATE_VIEW_PREVIOUS}<br/>
     *                         {@link #UPDATE_VIEW_NEXT}<br/>
     *                         {@link #UPDATE_VIEW_LIKE}<br/>
     *                         {@link #UPDATE_VIEW_ALBUMN}<br/>
     *                         {@link #UPDATE_VIEW_BACKGROUND}<br/>
     * @param updateResourceID 资源ID
     * @return
     */
    private static boolean updateView(int updateView, int updateResourceID) {
        switch (updateView) {
            case UPDATE_VIEW_PLAY:
                mNotificationView.setImageViewResource(R.id.imageview_play, updateResourceID);
                break;
            case UPDATE_VIEW_PREVIOUS:
                mNotificationView.setImageViewResource(R.id.imageview_pre, updateResourceID);
                break;
            case UPDATE_VIEW_NEXT:
                mNotificationView.setImageViewResource(R.id.imageview_next, updateResourceID);
                break;
            case UPDATE_VIEW_ALBUMN:
                mNotificationView.setImageViewResource(R.id.imageview_ablumn, updateResourceID);
                break;
            case UPDATE_VIEW_LIKE:
                mNotificationView.setImageViewResource(R.id.imageview_like, updateResourceID);
                break;
            case UPDATE_VIEW_BACKGROUND:
                mNotificationView.setInt(R.id.lin_container, "setBackgroundResource", updateResourceID);
                break;
            default:
                return false;
        }
        return true;
    }

    private static void updateBackgroundColor(int color) {
        mNotificationView.setInt(R.id.lin_container, "setBackgroundColor", color);
    }

    /**
     * 更新当前播放音乐的文本显示
     *
     * @param trackName
     * @param trackArtist
     * @return
     */
    private static boolean updateText(String trackName, String trackArtist) {
        if (mServiceContext == null) {
            throw new RuntimeException("notification utils was not initialed");
        }
        if (trackName == null || trackArtist == null) {
            return false;
        }
        mNotificationView.setTextViewText(R.id.textview_name, trackName);
        mNotificationView.setTextViewText(R.id.textview_artist, trackArtist);
        return true;
    }

    /**
     * 回收通知栏资源,取消常驻的通知栏
     */
    public static void recycle() {
        NotificationManager notifyMgr = (NotificationManager) mServiceContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.cancel(NOTIFICATION_ID);
        mTrackNotification = null;
        mNotificationView = null;
        mServiceContext = null;
    }
}
