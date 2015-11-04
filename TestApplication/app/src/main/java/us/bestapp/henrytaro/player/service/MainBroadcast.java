package us.bestapp.henrytaro.player.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.ui.ScreenLockActivity;
import us.bestapp.henrytaro.player.utils.CommonUtils;
import us.bestapp.henrytaro.player.utils.NotificationUtils;

/**
 * Created by xuhaolin on 15/10/29.
 * 主广播,启动于服务中,用于处理屏幕点亮锁屏/通知栏音乐状态更新/响应通知栏音乐操作/耳机拔插监听
 */
public class MainBroadcast extends BroadcastReceiver {
    public static final int HEADSET_IN = 1;
    public static final int HEADSET_OUT = 0;
    public static final int MICROPHONE_YES = 1;
    public static final int MICROPHONE_NO = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON:
//            case CommonUtils.IntentAction.INTENT_ACTION_SCRREN_OFF:
//                KeyguardManager keyguardMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//                KeyguardManager.KeyguardLock keyguardLock = keyguardMgr.newKeyguardLock("screenLock");
//                //屏蔽手机内置的锁屏
//                keyguardLock.disableKeyguard();
                handleActionFromScreenOn(context);
                break;
            //更新通知栏状态
            case CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE:
                handleActionFromUpdateTrackState(intent);
                break;
            case CommonUtils.IntentAction.INTENT_ACTION_HEADSET_PLUG:
                handleActionFromHeadset(intent);
                break;
            //播放/暂停/下一首/上一首/收藏/关闭
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY:
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT:
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS:
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE:
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE:
                handleActionFromNotification(action);
                break;
        }
    }

    /***
     * 创建此广播需要监听的事件
     *
     * @return
     */
    public static IntentFilter createBroadcastFilter() {
        IntentFilter filter = new IntentFilter();
        //点亮屏幕
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON);
        //更新音乐状态
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE);
        //更新音乐操作
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY);
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT);
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS);
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE);
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE);
        //耳机监听
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_HEADSET_PLUG);
        return filter;
    }

    /**
     * 处理来自耳机事件的动作
     *
     * @param intent
     */
    private void handleActionFromHeadset(Intent intent) {
        int headsetState = intent.getIntExtra(CommonUtils.IntentExtra.INTENT_EXTRA_HEADSET, -1);
        //耳机拔出
        if (headsetState == HEADSET_OUT) {
            ITrackHandleBinder binder = PlaySerive.getBinder();
            if (binder != null) {
                //停止播放
                binder.getOperaHandle().stop();
            }
        }
    }

    /**
     * 处理来自屏幕点亮事件的动作
     *
     * @param context
     */
    private void handleActionFromScreenOn(Context context) {
        ITrackHandleBinder binder = PlaySerive.getBinder();
        //当前歌曲正在播放则启动锁屏,否则不使用锁屏
        if (binder != null && binder.getOperaHandle().isPlaying()) {
            //启动该第三方锁屏
            Intent screenLockIntent = new Intent(context, ScreenLockActivity.class);
            screenLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //启动锁屏界面
            context.startActivity(screenLockIntent);
        }
    }

    /**
     * 处理来自通知栏更新播放操作事件的动作
     *
     * @param action
     */
    private void handleActionFromNotification(String action) {
        //获取当前服务绑定的binder
        //binder不存在时,不进行任何操作
        ITrackHandleBinder binder = PlaySerive.getBinder();
        if (binder == null) {
            return;
        }
        switch (action) {
            //更新播放
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY:
                binder.getOperaHandle().pause();
                break;
            //更新上一首
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS:
                binder.getOperaHandle().previous();
                break;
            //更新下一首
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT:
                binder.getOperaHandle().next();
                break;
            //收藏
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE:
                break;
            //关闭程序
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE:
                binder.destroy();
                break;
            default:
                break;
        }
    }

    /**
     * 处理来自更新播放状态事件的动作
     *
     * @param intent
     */
    private void handleActionFromUpdateTrackState(Intent intent) {
        //获取当前播放歌曲的名称/艺人及播放按钮的资源ID
        String trackName = intent.getStringExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_NAME);
        String trackArtist = intent.getStringExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_ARTITST);
        int playResID = intent.getIntExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_PLAY_RESID, 0);

        if (trackName != null && trackArtist != null) {
            if (playResID == 0) {
                //不存在按钮ID,只更新歌曲的名称与艺人
                NotificationUtils.updateNotification(trackName, trackArtist);
            } else {
                //更新所有数据
                NotificationUtils.updateNotification(trackName, trackArtist, playResID);
            }
        }
    }
}
