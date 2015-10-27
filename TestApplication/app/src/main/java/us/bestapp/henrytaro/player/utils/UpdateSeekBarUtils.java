package us.bestapp.henrytaro.player.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.SeekBar;

import us.bestapp.henrytaro.player.interfaces.IPlayCallback;
import us.bestapp.henrytaro.player.service.PlayBinder;

/**
 * Created by xuhaolin on 15/10/22.
 * 自动更新进度条的工具类
 */
public class UpdateSeekBarUtils implements IPlayCallback.onProgressUpdateListener {
    //唯一对象
    private static us.bestapp.henrytaro.player.utils.UpdateSeekBarUtils mInstance;
    //用于更新显示的进度条
    private SeekBar mSeekbar;
    //绑定的数据来源
    private PlayBinder mBinder;
    //更新线程
    private Runnable mUpdateProgressRunnable;
    private Runnable mUpdateBufferRunnable;
    //seekBar所在UI界面,用于UI线程更新使用
    private Activity mUiActivity;
    //同步处理,当前缓冲的百分比
    private volatile int mBufferPercent;
    private volatile int mProgress;
    private volatile int mDuration;
    //同步处理,是否更新
    private volatile boolean mIsStart = false;
    //同步处理,是否退出更新线程
    private volatile boolean mIsBreak = false;

    /**
     * 获取更新进度条工具的唯一实例
     */
    public static synchronized us.bestapp.henrytaro.player.utils.UpdateSeekBarUtils getInstance() {
        if (mInstance == null) {
            mInstance = new us.bestapp.henrytaro.player.utils.UpdateSeekBarUtils();
        }
        return mInstance;
    }

    /**
     * 创建更新对象
     */
    private UpdateSeekBarUtils() {
        mUpdateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSeekbar != null) {
                    if (mSeekbar.getMax() <= 0) {
                        mSeekbar.setMax(mDuration);
                    }
                    Log.i("thread","progress update");
                    mSeekbar.setProgress(mProgress);
                }
            }
        };
        mUpdateBufferRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSeekbar != null) {
                    int secondProgress = mSeekbar.getMax() * mBufferPercent / 100;
                    mSeekbar.setSecondaryProgress(secondProgress);
                }
            }
        };
    }

    /**更新进度条*/
    private void updateProgress(int progress, int duration) {
        mProgress = progress;
        mDuration = duration;
        if (mUiActivity != null && mSeekbar != null) {
            mUiActivity.runOnUiThread(mUpdateProgressRunnable);
        }
    }


    /**
     * 更新进度条缓存的数据
     */
    public void updateBufferProgress(int percent) {
        mBufferPercent = percent;
        if (mUiActivity != null && mSeekbar != null) {
            mUiActivity.runOnUiThread(mUpdateBufferRunnable);
        }
    }


    /**
     * 开始更新,此方法在原更新线程存在的时候使用原更新线程,若更新线程不存在时,创建新线程进行更新,<font color="#ff9900"><b>参数尽可能不为null</b></font>
     *
     * @param uiActivity seekBar所在的UI界面
     * @param seekBar    用于显示进度的进度条
     * @return
     */
    public boolean startUpdate(Activity uiActivity, SeekBar seekBar) {
        //任何一个参数理论上都不可以为null
        if (uiActivity == null || seekBar == null) {
            return false;
            //throw new RuntimeException("binder, activity, seekbar can not be null or ui can't be update");
        }

        //保存新的参数
        this.mUiActivity = uiActivity;
        this.mSeekbar = seekBar;
        mIsStart = true;
        mIsBreak = false;
        return true;
    }

    /**
     * 继续更新
     */
    private void continueUpdate() {
        this.mIsStart = true;
    }

    /**
     * 暂停更新
     */
    public void pauseUpdate() {
        this.mIsStart = false;
    }

    /**
     * 销毁更新线程及重置所有的数据
     */
    public void destory() {
        this.mUiActivity = null;
        this.mSeekbar = null;
    }

    /**
     * 重置进度条,将当前进度条时长生置为-1,缓存进度条清除
     */
    public void resetSeekBar() {
        if (mSeekbar != null) {
            mSeekbar.setMax(-1);
            mSeekbar.setSecondaryProgress(0);
        }
    }

    @Override
    public void onProgressUpdate(int progress, int duration) {
        updateProgress(progress, duration);
    }
}
