package us.bestapp.henrytaro.player.utils;

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

import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.player.model.AbsTrack;

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
     * 通知单击关闭按钮广播
     */
    public static final String NOTIFY_ACTION_CLOSE = "action_close";
    /**
     * 通知单击播放按钮广播
     */
    public static final String NOTIFY_ACTION_PLAY = "action_play";
    /**
     * 通知单击上一首按钮广播
     */
    public static final String NOTIFY_ACTION_PREVIOUS = "action_previous";
    /**
     * 通知单击下一首按钮广播
     */
    public static final String NOTIFY_ACTION_NEXT = "action_next";
    /**
     * 通知单击收藏按钮广播
     */
    public static final String NOTIFY_ACTION_LIKE = "action_like";

    /**
     * 唯一固定的通知ID
     */
    private static final int NOTIFICATION_ID = 0x10086;
    private static Context mApplicationContext;
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
        mApplicationContext = context.getApplicationContext();
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
        NotificationManager notifyMgr = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingintent = PendingIntent.getActivity(mApplicationContext, 0, activityIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        //创建通知栏
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mApplicationContext);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(titile);
        builder.setContentText(contentText);
        builder.setContentIntent(pendingintent);
        mTrackNotification = builder.build();
        //设置通知栏显示的内容正在运行且不会被用户删除(常驻)
        mTrackNotification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;

        //当前SDK版本在16以上,显示大通知视图
        if (Build.VERSION.SDK_INT >= 16) {
            mNotificationView = new RemoteViews(mApplicationContext.getPackageName(), R.layout.notification_big);
            mTrackNotification.bigContentView = mNotificationView;
        } else {
            //否则显示小通知视图
            mNotificationView = new RemoteViews(mApplicationContext.getPackageName(), R.layout.notification_small);
            mTrackNotification.contentView = mNotificationView;
        }

        //初始化第一次显示的所有数据及图片
        mNotificationView.setTextViewText(R.id.textview_name, trackName);
        mNotificationView.setTextViewText(R.id.textview_artist, trackArtist);
        mNotificationView.setImageViewResource(R.id.imageview_close, R.drawable.ic_close);
        mNotificationView.setImageViewResource(R.id.imageview_ablumn, R.drawable.ic_track);
        mNotificationView.setImageViewResource(R.id.imageview_play, R.drawable.ic_play);
        mNotificationView.setImageViewResource(R.id.imageview_like, R.drawable.ic_like);
        mNotificationView.setImageViewResource(R.id.imageview_next, R.drawable.ic_next);
        mNotificationView.setImageViewResource(R.id.imageview_pre, R.drawable.ic_previous);
        mNotificationView.setInt(R.id.lin_container, "setBackgroundColor", Color.WHITE);

        //绑定按钮的单击广播事件
        mNotificationView.setOnClickPendingIntent(R.id.imageview_close, getBroadcastPendingIntent(NOTIFY_ACTION_CLOSE));
        mNotificationView.setOnClickPendingIntent(R.id.imageview_play, getBroadcastPendingIntent(NOTIFY_ACTION_PLAY));
        mNotificationView.setOnClickPendingIntent(R.id.imageview_like, getBroadcastPendingIntent(NOTIFY_ACTION_PLAY));
        mNotificationView.setOnClickPendingIntent(R.id.imageview_next, getBroadcastPendingIntent(NOTIFY_ACTION_NEXT));
        mNotificationView.setOnClickPendingIntent(R.id.imageview_pre, getBroadcastPendingIntent(NOTIFY_ACTION_PREVIOUS));
        //通知显示
        notifyMgr.notify(NOTIFICATION_ID, mTrackNotification);
    }

    /**
     * 获取广播使用的Intent
     *
     * @param action 广播动作<br/>
     *               {@link #NOTIFY_ACTION_PLAY}<br/>
     *               {@link #NOTIFY_ACTION_PREVIOUS}<br/>
     *               {@link #NOTIFY_ACTION_NEXT}<br/>
     *               {@link #NOTIFY_ACTION_LIKE}<br/>
     *               {@link #NOTIFY_ACTION_CLOSE}<br/>
     * @return
     */
    public static PendingIntent getBroadcastPendingIntent(String action) {
        Intent broadCastIntent = new Intent(action);
        return PendingIntent.getBroadcast(mApplicationContext, 0, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 完整地更新通知栏状态
     *
     * @param track        当前播放的音乐
     * @param playBtnResID 当前播放按钮显示的图标
     */
    public static void updateNotification(AbsTrack track, int playBtnResID) {
        startUpdate();
        updateText(track);
        updateView(UPDATE_VIEW_PLAY, playBtnResID);
        submitUpdate();
    }

    /***
     * 完整地更新通知栏状态
     *
     * @param track        当前播放的音乐
     * @param playBtnResID 当前播放按钮显示的图标
     * @param ablumnBitmap 当前专辑头像显示的图像
     */
    public static void updateNotification(AbsTrack track, int playBtnResID, Bitmap ablumnBitmap) {
        startUpdate();
        updateText(track);
        updateView(UPDATE_VIEW_PLAY, playBtnResID);
        updateView(UPDATE_VIEW_ALBUMN, ablumnBitmap);
        submitUpdate();
    }

    /**
     * 开始更新准备操作
     */
    public static void startUpdate() {
        //更新需要创建新的remoteviews
        if (Build.VERSION.SDK_INT >= 16) {
            mNotificationView = new RemoteViews(mApplicationContext.getPackageName(), R.layout.notification_big);
            mTrackNotification.bigContentView = mNotificationView;
        } else {
            mNotificationView = new RemoteViews(mApplicationContext.getPackageName(), R.layout.notification_small);
            mTrackNotification.contentView = mNotificationView;
        }
    }

    /**
     * 确认提交更新通知栏
     */
    public static void submitUpdate() {
        NotificationManager notifyMgr = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
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
    public static boolean updateView(int updateView, Bitmap bitmap) {
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
    public static boolean updateView(int updateView, int updateResourceID) {
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

    /**
     * 更新当前播放音乐的文本显示
     *
     * @param track 当前播放的音乐
     * @return
     */
    public static boolean updateText(AbsTrack track) {
        if (mApplicationContext == null) {
            throw new RuntimeException("notification utils was not initialed");
        }
        if (track == null) {
            return false;
        }
        mNotificationView.setTextViewText(R.id.textview_name, track.getTrackName());
        mNotificationView.setTextViewText(R.id.textview_artist, track.getTrackArtist());
        return true;
    }

    /**
     * 回收通知栏资源,取消常驻的通知栏
     */
    public static void recycle() {
        NotificationManager notifyMgr = (NotificationManager) mApplicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyMgr.cancel(NOTIFICATION_ID);
        mTrackNotification = null;
        mNotificationView = null;
        mApplicationContext = null;
    }
}
