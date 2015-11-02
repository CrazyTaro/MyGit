package us.bestapp.henrytaro.player.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Calendar;
import java.util.Date;

import us.bestapp.henrytaro.player.ui.ScreenLockActivity;
import us.bestapp.henrytaro.player.utils.CommonUtils;
import us.bestapp.henrytaro.player.utils.NotificationUtils;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class TrackStatusBroadcast extends BroadcastReceiver {

    public static final int ACTION_TARGET_SCREENT_ACTIVITY = 0;
    public static final int ACTION_TARGET_NOTIFICATION = 1;

    public static final String FORMAT_DATE = "%1$4d/%2$02d/%3$02d %4$s";
    public static final String FORMAT_TIME = "%1$02d : %2$02d";

    private int mActionTarget = ACTION_TARGET_NOTIFICATION;
    private Activity mUiActivity = null;
    private Calendar mDate = null;
    private ScreenLockActivity.ViewHolder mViewHolder = null;

    public TrackStatusBroadcast() {
        mActionTarget = ACTION_TARGET_NOTIFICATION;
    }

    public TrackStatusBroadcast(Activity uiActivity, ScreenLockActivity.ViewHolder holder) {
        if (uiActivity == null || holder == null) {
            throw new RuntimeException("params can not be null");
        }
        mUiActivity = uiActivity;
        mViewHolder = holder;
        mActionTarget = ACTION_TARGET_SCREENT_ACTIVITY;

        mDate = Calendar.getInstance();
    }

    public IntentFilter getDefaultBroadcastFilter() {
        IntentFilter filter = new IntentFilter();
        switch (mActionTarget) {
            case ACTION_TARGET_NOTIFICATION:
                filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE);
                break;
            case ACTION_TARGET_SCREENT_ACTIVITY:
                filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE);
                filter.addAction(Intent.ACTION_TIME_TICK);
                break;
        }
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE:
                if (mActionTarget == ACTION_TARGET_SCREENT_ACTIVITY) {
                    handleActionInScreenActivity(intent);
                } else if (mActionTarget == ACTION_TARGET_NOTIFICATION) {
                    handleActionInNotification(intent);
                }
                break;
            case Intent.ACTION_TIME_TICK:
                updateTimeAndDate();
                break;
        }
    }

    public void updateTimeAndDate() {
        long nowTime = System.currentTimeMillis();
        mDate.setTimeInMillis(nowTime);
        final String time = CommonUtils.getTimeStr(FORMAT_TIME, mDate);
        final String date = CommonUtils.getDateStr(FORMAT_DATE, mDate);
        if (mUiActivity != null && mViewHolder != null) {
            mUiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.textViewTime.setText(time);
                    mViewHolder.textViewDate.setText(date);
                }
            });
        }
    }

    public void handleActionInScreenActivity(Intent intent) {
        final String trackName = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_NAME);
        final String trackArtist = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ARTITST);
        final int playResID = intent.getIntExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_PLAY_RESID, 0);

        if (mUiActivity != null && mViewHolder != null) {
            mUiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mViewHolder.textViewTrack.setText(trackName);
                    mViewHolder.textViewArtist.setText(trackArtist);
                    if (playResID != 0) {
                        mViewHolder.imageViewPlay.setImageResource(playResID);
                    }
                }
            });
        }
    }

    public void handleActionInNotification(Intent intent) {
        String trackName = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_NAME);
        String trackArtist = intent.getStringExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ARTITST);
        int playResID = intent.getIntExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_PLAY_RESID, 0);

        if (trackName != null && trackArtist != null) {
            if (playResID == 0) {
                NotificationUtils.updateNotification(trackName, trackArtist);
            } else {
                NotificationUtils.updateNotification(trackName, trackArtist, playResID);
            }
        }
    }
}
