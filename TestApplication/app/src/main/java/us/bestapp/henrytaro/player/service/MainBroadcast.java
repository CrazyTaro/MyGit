package us.bestapp.henrytaro.player.service;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import us.bestapp.henrytaro.player.ui.ScreenDialog;
import us.bestapp.henrytaro.player.ui.ScreenLockActivity;
import us.bestapp.henrytaro.player.utils.CommonUtils;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class MainBroadcast extends BroadcastReceiver {
    String SYSTEM_REASON = "reason";
    String SYSTEM_HOME_KEY = "homekey";
    String SYSTEM_HOME_KEY_LONG = "recentapps";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        KeyguardManager keyguardMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        switch (action) {
            case CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON:
//            case CommonUtils.IntentAction.INTENT_ACTION_SCRREN_OFF:
//                KeyguardManager.KeyguardLock keyguardLock = keyguardMgr.newKeyguardLock("screenLock");
//                //屏蔽手机内置的锁屏
//                keyguardLock.disableKeyguard();
//                //启动该第三方锁屏
//                Intent screenLockIntent = new Intent(context, ScreenLockActivity.class);
//                screenLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(screenLockIntent);
                break;
        }
    }
}