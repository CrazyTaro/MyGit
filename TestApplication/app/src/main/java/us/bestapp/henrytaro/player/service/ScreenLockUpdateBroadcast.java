package us.bestapp.henrytaro.player.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Calendar;

import us.bestapp.henrytaro.player.ui.ScreenLockActivity;
import us.bestapp.henrytaro.player.utils.CommonUtils;

/**
 * Created by xuhaolin on 15/10/29.
 * 锁屏广播,用于监听音乐状态更新并更新锁屏界面/更新系统时间
 */
public class ScreenLockUpdateBroadcast extends BroadcastReceiver {
    /**
     * 日期格式化,格式:2015/10/01 星期一
     */
    public static final String FORMAT_DATE = "%1$4d/%2$02d/%3$02d %4$s";
    /**
     * 时间格式化,格式:12 : 00
     */
    public static final String FORMAT_TIME = "%1$02d : %2$02d";

    //锁屏界面
    private Activity mUiActivity = null;
    //时间处理类
    private Calendar mDate = null;
    //锁屏界面关联的view
    private ScreenLockActivity.ViewHolder mViewHolder = null;

    /**
     * 创建广播(应该在锁屏界面中设置该广播的监听)
     *
     * @param uiActivity 锁屏界面(也用于后期在UI线程更新)
     * @param holder     锁屏界面相关view
     */
    public ScreenLockUpdateBroadcast(Activity uiActivity, ScreenLockActivity.ViewHolder holder) {
        if (uiActivity == null || holder == null) {
            throw new RuntimeException("params can not be null");
        }
        mUiActivity = uiActivity;
        mViewHolder = holder;

        mDate = Calendar.getInstance();
    }

    /***
     * 创建该广播需要监听的事件
     *
     * @return
     */
    public static IntentFilter createBroadcastFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE);
        filter.addAction(Intent.ACTION_TIME_TICK);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            //音乐状态更新
            case CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE:
                handleActionFromUpdateTrackState(intent);
                break;
            //时间更新
            case Intent.ACTION_TIME_TICK:
                handleActionFromUpdatDateTime();
                break;
        }
    }

    /**
     * 更新时间
     */
    public void handleActionFromUpdatDateTime() {
        long nowTime = System.currentTimeMillis();
        mDate.setTimeInMillis(nowTime);
        //时间
        final String time = CommonUtils.getTimeStr(FORMAT_TIME, mDate);
        //日期
        final String date = CommonUtils.getDateStr(FORMAT_DATE, mDate);
        if (mUiActivity != null && mViewHolder != null) {
            mUiActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //线程中更新
                    mViewHolder.textViewTime.setText(time);
                    mViewHolder.textViewDate.setText(date);
                }
            });
        }
    }

    /**
     * 更新锁屏界面的音乐状态及信息
     *
     * @param intent
     */
    public void handleActionFromUpdateTrackState(Intent intent) {
        final String trackName = intent.getStringExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_NAME);
        final String trackArtist = intent.getStringExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_ARTITST);
        final int playResID = intent.getIntExtra(CommonUtils.IntentExtra.INTENT_EXTRA_TRACK_PLAY_RESID, 0);

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

}
