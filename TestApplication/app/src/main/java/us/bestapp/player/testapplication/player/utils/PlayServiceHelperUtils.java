package us.bestapp.player.testapplication.player.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ServiceInfo;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import java.util.List;

import us.bestapp.player.testapplication.player.interfaces.IMusicHandleBinder;
import us.bestapp.player.testapplication.player.service.PlayBinder;
import us.bestapp.player.testapplication.player.service.PlaySerive;

/**
 * Created by xuhaolin on 15/10/20.
 * 辅助工具类,用于简化服务及相关操作的一些初始化
 */
public class PlayServiceHelperUtils implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    //保留程序的引用
    private Context mApplicationContext;
    private UpdateSeekBarUtils mUpdateSeekbarUtils;
    private Activity mUiActivity;
    private SeekBar mUpdateSeekBar;

    /**
     * 构造函数,当参数为null时抛出异常,<font color="#ff9900"><b>此类保存的Context为应用的Context,不会持有当前参数context</b></font>
     *
     * @param uiActivity    不可为null
     * @param updateSeekBar 用于更新进度的进度条
     */
    public PlayServiceHelperUtils(Activity uiActivity, SeekBar updateSeekBar) {
        if (uiActivity == null) {
            throw new RuntimeException("context can not be null");
        }
        setNewSeekBar(updateSeekBar, uiActivity);
        //获取应用context
        mApplicationContext = uiActivity.getApplicationContext();
        mUpdateSeekbarUtils = UpdateSeekBarUtils.getInstance();
    }

    /**
     * 初始化操作,启动服务并绑定数据
     *
     * @param connection 服务连接接口,不可为null
     */
    public void initial(ServiceConnection connection) {
        if (connection == null) {
            throw new RuntimeException("service connection can not be null");
        }
        //创建服务
        Intent serviceIntent = new Intent(mApplicationContext, PlaySerive.class);
        //启动服务
        mApplicationContext.startService(serviceIntent);
        //绑定服务
        mApplicationContext.bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 停止服务
     *
     * @param connection
     */
    public void stopService(ServiceConnection connection) {
        //创建服务
        Intent serviceIntent = new Intent(mApplicationContext, PlaySerive.class);
        mApplicationContext.unbindService(connection);
        //启动服务
        mApplicationContext.stopService(serviceIntent);
    }

    /**
     * 设置新的用于更新的进度条
     *
     * @param seekBar    新进度条
     * @param uiActivity 此参数必须与进度条所在的UI界面关联,即进度条必须在此activity中
     */
    public void setNewSeekBar(SeekBar seekBar, Activity uiActivity) {
        if (uiActivity != null && uiActivity != this.mUiActivity) {
            this.mUiActivity = uiActivity;
        }
        if (seekBar != null && seekBar != this.mUpdateSeekBar) {
            this.mUpdateSeekBar = seekBar;
        }
    }

    public Activity getUiActivity() {
        return this.mUiActivity;
    }

    public SeekBar getUpdateSeekBar() {
        return this.mUpdateSeekBar;
    }

    /**
     * 恢复状态
     *
     * @param uiActivity    当前显示的UI界面
     * @param updateSeekbar 用于更新进度的进度条
     */
    public void onResume(Activity uiActivity, SeekBar updateSeekbar) {
        IMusicHandleBinder binder = PlaySerive.getBinder();
        if (binder != null) {
            binder.getOperaHandle().continueUpdateNotify();
        }
        //若当前在进行播放,尝试开始更新进度条
        if (mUpdateSeekbarUtils == null) {
            mUpdateSeekbarUtils = UpdateSeekBarUtils.getInstance();
        }
        mUpdateSeekbarUtils.startUpdate(uiActivity, updateSeekbar);
    }

    /***
     * 停止状态
     */
    public void onStop() {
        IMusicHandleBinder binder = PlaySerive.getBinder();
        if (binder != null) {
            binder.getOperaHandle().pauseUpdateNotify();
        }
    }

    /***
     * 服务连接预设
     *
     * @param binder
     */
    public void onServiceConnect(IMusicHandleBinder binder) {
        if (binder != null) {
            //设置音乐加载初始化监听/播放完毕监听/缓存更新监听
            binder.getMediaPlayerCallback().setOnPreparedListener(this);
            binder.getMediaPlayerCallback().setOnCompletitonListener(this, false);
            binder.getMediaPlayerCallback().setOnBufferUpdateListener(this);
            binder.getMediaPlayerCallback().addOnProgressUpdateListener(mUpdateSeekbarUtils);
        }
    }

    /**
     * 服务断开预设
     */
    public void onServiceDisconnected() {
        if (mUpdateSeekbarUtils != null) {
            mUpdateSeekbarUtils.destory();
        }
        this.mUpdateSeekbarUtils = null;
        this.mApplicationContext = null;
        this.mUiActivity = null;
        this.mUpdateSeekBar = null;
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        //音乐播放完毕停止更新进度条
        mUpdateSeekbarUtils.pauseUpdate();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //音乐加载初始化
        //重置进度条
        mUpdateSeekbarUtils.resetSeekBar();
        //若程序不在前台运行,停止更新进度条
        if (isBackground(mApplicationContext)) {
            mUpdateSeekbarUtils.pauseUpdate();
        } else {
            //尝试更新进度条
            mUpdateSeekbarUtils.startUpdate(mUiActivity, mUpdateSeekBar);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, final int percent) {
        //更新缓冲进度条
        mUpdateSeekbarUtils.updateBufferProgress(percent);
    }
}
