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
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON:
            case CommonUtils.IntentAction.INTENT_ACTION_SCRREN_OFF:
                KeyguardManager keyguardMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock keyguardLock = keyguardMgr.newKeyguardLock("screenLock");
                //屏蔽手机内置的锁屏
                keyguardLock.disableKeyguard();
                //启动该第三方锁屏
                Intent screenLockIntent = new Intent(context, ScreenLockActivity.class);
                screenLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(screenLockIntent);
//                ScreenDialog.createDialog(context);
//                ScreenDialog.showDialog();
                break;
        }
    }
}
