package us.bestapp.henrytaro.player.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.utils.CommonUtils;
import us.bestapp.henrytaro.player.utils.NotificationUtils;

/**
 * Created by xuhaolin on 15/10/26.
 * 操作通知栏控制器通知服务更新操作广播
 */
public class NotificationBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //获取当前服务绑定的binder
        //binder不存在时,不进行任何操作
        ITrackHandleBinder binder = PlaySerive.getBinder();
        if (binder == null) {
            return;
        }
        //获取当前的更新动作
        String action = intent.getAction();
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
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE:
                break;
            //关闭程序
            case CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE:
                binder.destory();
                break;
            default:
                break;
        }
    }
}
