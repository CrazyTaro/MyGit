package us.bestapp.henrytaro.player.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import us.bestapp.henrytaro.player.utils.CommonUtils;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class TrackStatusBroadcast extends BroadcastReceiver {

    public static final int ACTION_TARGET_SCREENT_ACTIVITY = 0;
    public static final int ACTION_TARGET_NOTIFICATION = 1;
    private int mActionTarget = ACTION_TARGET_NOTIFICATION;

    public TrackStatusBroadcast(int actionTarget) {
        if (actionTarget != ACTION_TARGET_NOTIFICATION && actionTarget != ACTION_TARGET_SCREENT_ACTIVITY) {
            throw new RuntimeException("params error!");
        }
        mActionTarget = actionTarget;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON:
                if (mActionTarget == ACTION_TARGET_SCREENT_ACTIVITY) {
                    handleActionInScreenActivity(intent);
                } else if (mActionTarget == ACTION_TARGET_NOTIFICATION) {
                    handleActionInNotification(intent);
                }
                break;
            case CommonUtils.IntentAction.INTENT_ACTION_SCRREN_OFF:
                if (mActionTarget == ACTION_TARGET_SCREENT_ACTIVITY) {
                    handleActionInScreenActivity(intent);
                } else if (mActionTarget == ACTION_TARGET_NOTIFICATION) {
                    handleActionInNotification(intent);
                }
                break;
        }
    }

    public void handleActionInScreenActivity(Intent intent) {
        String trackName = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_NAME);
        String trackArtist = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ARTITST);
    }

    public void handleActionInNotification(Intent intent) {
        String trackName = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_NAME);
        String trackArtist = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ARTITST);
    }
}
