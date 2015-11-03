package us.bestapp.henrytaro.player.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import us.bestapp.henrytaro.MainActivity;
import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.player.interfaces.IMediaPlayerCallback;
import us.bestapp.henrytaro.player.interfaces.IPlayCallback;
import us.bestapp.henrytaro.player.interfaces.IPlayerOperaHandle;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.model.AbsTrack;
import us.bestapp.henrytaro.player.utils.CommonUtils;
import us.bestapp.henrytaro.player.utils.NotificationUtils;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放后台服务
 */
public class PlaySerive extends Service implements IPlayerOperaHandle, IMediaPlayerCallback, MediaPlayer.OnCompletionListener, Runnable, AudioManager.OnAudioFocusChangeListener {
    public static final int THREAD_PLAY = 0x1;
    public static final int THREAD_PAUSE = 0x2;
    public static final int THREAD_PREVIOUS = 0x3;
    public static final int THREAD_NEXT = 0x4;
    public static final int THREAD_SEEK_TO = 0x5;
    public static final int THREAD_PLAY_MUSIC = 0x6;
    public static final int THREAD_UPDATE_PROGRESS = 0x7;
    public static final int THREAD_STOP = 0x8;
    public static final int THREAD_CONTINUE = 0x9;

    public static int RES_ID_PLAY = R.drawable.ic_play;
    public static int RES_ID_PAUSE = R.drawable.ic_pause;
    public static Class NOTIFICATION_ACTIVITY = MainActivity.class;

    //绑定服务的间接操作
    private static PlayBinder mBinder;
    //播放器
    private MediaPlayer mPlayer;
    //是否第一次播放
    private boolean mIsFirstPlay = true;
    //更新线程是否需要停止更新通知
    private volatile boolean mIsStopUpdate = false;
    //通知栏常驻播放控制器的广播响应
    private NotificationBroadcast mNotificationBroadCast;
    private MainBroadcast mMainBroadcast;
    private TrackStatusBroadcast mTrackStatusBroadcast;

    //回调事件
    private MediaPlayer.OnCompletionListener mUserComletionListener;
    private List<IPlayCallback.onProgressUpdateListener> mProgressUpdateCallbackList = new LinkedList<>();
    private IPlayCallback.onPlayerStatusChangedListener mPlayerStatusChangedListener;

    //后台处理播放线程及进度更新线程
    private Thread mBackgroundThread;
    private Thread mUpdateProgressThread;
    private Runnable mUpdateProgressRunnable;
    private Handler mHandler;

    private PowerManager.WakeLock mWakeLock;

    /**
     * 返回服务绑定的对象(服务只存在一个,绑定的对象也只存在一个)
     */
    public static synchronized ITrackHandleBinder getBinder() {
        return mBinder;
    }

    public static synchronized void setResID(int playResID, int pauseResID) {
        RES_ID_PLAY = playResID;
        RES_ID_PAUSE = pauseResID;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(CommonUtils.IntentAction.INTENT_ACTION_SERVICE_START);
        sendBroadcast(broadcastIntent);

//        PendingIntent pendingIntent = PendingIntent.getService(this, 0, new Intent(this, PlaySerive.class), 0);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentTitle("service");
//        builder.setContentText("on going");
//        builder.setSmallIcon(R.drawable.ic_track);
//        builder.setContentIntent(pendingIntent);
//        Notification notification = builder.build();
//        startForeground(startId, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initial();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, PlaySerive.class.getName());
        mWakeLock.acquire();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("bug", "recycle service");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(CommonUtils.IntentAction.INTENT_ACTION_SERVICE_DESTROY);
        sendBroadcast(broadcastIntent);

        if(mWakeLock!=null){
            mWakeLock.release();
            mWakeLock=null;
        }

//        stopForeground(true);
        //回收通知栏相关的资源,取消通知栏常驻
        NotificationUtils.recycle();

        unRegisterReceivers();
        destroyUpdateThread();
        destroyBackgroundThread();

        //通知服务销毁
        if (mBinder.getPlayCallback() != null) {
            mBinder.getPlayCallback().onServiceDestory();
        }

        //binder销毁
        if (mBinder != null) {
            mBinder.recycle();
            mBinder = null;
        }
        //释放播放器资源
        if (mPlayer != null) {
            try {
                mPlayer.reset();
                mPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPlayer = null;
            }
        }
    }

    //初始化
    private void initial() {
        mBinder = us.bestapp.henrytaro.player.service.PlayBinder.getInstance(this);


        if (mPlayer == null) {
            //创建播放器
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            //设置默认播放完的处理事件(自动播放下一首)
            mPlayer.setOnCompletionListener(this);
        }
        mUpdateProgressRunnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!mIsStopUpdate) {
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mHandler.sendEmptyMessage(THREAD_UPDATE_PROGRESS);
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        mBackgroundThread = new Thread(this);
        mBackgroundThread.setDaemon(true);
        mBackgroundThread.start();

        mUpdateProgressThread = new Thread(mUpdateProgressRunnable);
        mUpdateProgressThread.setDaemon(true);
        mUpdateProgressThread.start();


        //初始化通知栏播放控制器
        NotificationUtils.initial(this);
        Intent activityIntent = null;
        if (NOTIFICATION_ACTIVITY != null) {
            activityIntent = new Intent(this.getApplicationContext(), NOTIFICATION_ACTIVITY);
        }
        NotificationUtils.showNotifcation("播覇音乐", "音乐播放~", "播覇音乐", "音乐播放~", activityIntent);
        registerReceivers();

        requestAudioFocus(false, null);
    }

    //销毁后台控制播放的线程(销毁后播放功能不可再用)
    private void destroyBackgroundThread() {
        if (mBackgroundThread != null && mBackgroundThread.isAlive()) {
            mBackgroundThread.interrupt();
            mBackgroundThread = null;
        }
    }

    //注册广播
    private void registerReceivers() {
        //注册广播
        //通知栏控制条通知操作广播
        if (mNotificationBroadCast == null) {
            mNotificationBroadCast = new us.bestapp.henrytaro.player.service.NotificationBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PLAY);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_PREVIOUS);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_NEXT);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_LIKE);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_NOTIFY_CLOSE);
            this.registerReceiver(mNotificationBroadCast, filter);
        }

        //主广播,锁屏使用
        if (mMainBroadcast == null) {
            mMainBroadcast = new MainBroadcast();
            IntentFilter filter = new IntentFilter();
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ON);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_SCRREN_OFF);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ACTIVITY_CREATE);
            filter.addAction(CommonUtils.IntentAction.INTENT_ACTION_SCREEN_ACTIVITY_DESTROY);
            this.registerReceiver(mMainBroadcast, filter);
        }

        //歌曲状态切换广播
        //同步通知栏与播放状态
        if (mTrackStatusBroadcast == null) {
            mTrackStatusBroadcast = new TrackStatusBroadcast();
            this.registerReceiver(mTrackStatusBroadcast, mTrackStatusBroadcast.getDefaultBroadcastFilter());
        }
    }

    //取消广播注册
    private void unRegisterReceivers() {
        if (mNotificationBroadCast != null) {
            this.unregisterReceiver(mNotificationBroadCast);
        }

        if (mMainBroadcast != null) {
            this.unregisterReceiver(mMainBroadcast);
        }

        if (mTrackStatusBroadcast != null) {
            this.unregisterReceiver(mTrackStatusBroadcast);
        }
    }

    //请求媒体音乐的焦点
    private void requestAudioFocus(boolean isPlay, Bitmap ablumn) {
        AudioManager audioMgr = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        audioMgr.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        updateTrackStatus(mBinder.getCurrentTrack(), isPlay, ablumn);
    }

    //更新歌曲状态
    private void updateTrackStatus(AbsTrack currentPlay, boolean isPlay, Bitmap ablumn) {
        String trackName = "播覇音乐";
        String trackArtist = "暂未播放任何音乐";
        int resID = 0;
        //设置当前播放歌曲的信息
        if (currentPlay != null) {
            trackName = currentPlay.getTrackName();
            trackArtist = currentPlay.getTrackArtist();
        }
        //根据当前播放状态确定显示使用的图像
        if (isPlay) {
            resID = RES_ID_PAUSE;
        } else {
            resID = RES_ID_PLAY;
        }
        //通知更新当前播放歌曲
        Intent updateBroadcast = new Intent();
        updateBroadcast.setAction(CommonUtils.IntentAction.INTENT_ACTION_TRACK_UPDATE);
        updateBroadcast.putExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_NAME, trackName);
        updateBroadcast.putExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ARTITST, trackArtist);
        updateBroadcast.putExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_PLAY_RESID, resID);
        if (ablumn != null) {
            updateBroadcast.putExtra(CommonUtils.IntentAction.INTENT_EXTRA_TRACK_ABLUMN_BITMAP, ablumn);
        }
        sendBroadcast(updateBroadcast);
    }


    /**
     * 尝试播放指定音乐
     *
     * @param errorTag 出错时的标签<br/>
     *                 {@link IPlayCallback#IN_NEXT}<br/>
     *                 {@link IPlayCallback#IN_PLAY}<br/>
     *                 {@link IPlayCallback#IN_PREVIOUS}<br/>
     *                 {@link IPlayCallback#IN_SEEK_TO}<br/>
     * @param track    指定播放的音乐, 可为null,若为null尝试通知音乐回调
     */
    private void tryPlayTrack(int errorTag, AbsTrack track) {
        if (track == null) {
            //通知音乐为null回调
            if (mBinder.getPlayCallback() != null) {
                mBinder.getPlayCallback().onTrackNotExsit();
                return;
            }
        } else {
            //设置该音乐为当前音乐
            //尝试播放当前音乐
            if (track.equals(mBinder.getCurrentTrack())) {
                threadSeekTo(0);
                return;
            }
            if (mBinder.setCurrentTrack(track)) {
                tryPlayCurrentTrack(errorTag);
            }
        }
    }

    /**
     * 尝试播放当前音乐,设置当前音乐通过{@link us.bestapp.henrytaro.player.service.PlayBinder#setCurrentTrack(AbsTrack)}
     *
     * @param errorTag 出错时的标签<br/>
     *                 {@link IPlayCallback#IN_NEXT}<br/>
     *                 {@link IPlayCallback#IN_PLAY}<br/>
     *                 {@link IPlayCallback#IN_PREVIOUS}<br/>
     *                 {@link IPlayCallback#IN_SEEK_TO}<br/>
     */
    private void tryPlayCurrentTrack(int errorTag) {
        //获取当前播放音乐
        AbsTrack current = mBinder.getCurrentTrack();
        if (current != null) {
            try {
                //重置
                mPlayer.reset();
                mPlayer.setDataSource(this, Uri.parse(current.getUrlOrFilePath()));
                //必须进行加载
                mPlayer.prepare();
                mPlayer.start();

                //更新通知栏
                requestAudioFocus(true, null);
            } catch (Exception ex) {
                ex.printStackTrace();
                //通知播放失败
                if (mBinder.getPlayCallback() != null) {
                    mBinder.getPlayCallback().onFailPlay(ex.getMessage(), errorTag, current);
                }
            }
        }
    }

    //播放当前歌曲
    private void threadPlay() {
        //尝试播放当前歌曲
        tryPlayCurrentTrack(IPlayCallback.IN_PLAY);
        onStatusChanged(IPlayCallback.IN_PLAY);
    }

    //尝试继续播放歌曲(若为第一次播放,则直接播放歌曲,如果尝试继续播放歌曲失败,则播放当前歌曲)
    private void threadContinue() {
        if (mIsFirstPlay) {
            //若当前播放的音乐为null且为第一次播放,则对尝试根据当前的播放模式生成音乐进行播放
            AbsTrack firstTrack = mBinder.getNextTrack();
            tryPlayTrack(IPlayCallback.IN_PLAY, firstTrack);
            mIsFirstPlay = false;
        } else {
            try {
                //尝试播放,此处用于处理当前音乐被暂停的情况
                mPlayer.start();

                requestAudioFocus(true, null);
            } catch (Exception e) {
                e.printStackTrace();
                //尝试播放当前的音乐,此处为第一次播放时情况(此前未有任何音乐进行播放过并暂停的情况)
                tryPlayCurrentTrack(IPlayCallback.IN_PLAY);
            }
        }
        onStatusChanged(IPlayCallback.IN_PLAY);
    }

    //暂停(停止状态尝试播放,播放状态尝试停止 )
    private void threadPause() {
        //暂停事件
        if (!threadStop()) {
            threadContinue();
        }
    }

    //停止
    private boolean threadStop() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();

            updateTrackStatus(mBinder.getCurrentTrack(), false, null);
            onStatusChanged(IPlayCallback.IN_STOP);

            return true;
        } else {
            return false;
        }
    }

    //播放上一首
    private void threadPrevious() {
        //获取当前的时间
        int intervalTime = getCurrentPosition() / 1000;
        //判断当前时间是否为播放后的10秒,在10秒内切换为上一首,10秒后切换为重新播放当前歌曲
        if (intervalTime > 10) {
            threadSeekTo(0);
            return;
        }
        //获取历史播放列表的上一首歌曲
        AbsTrack pre = mBinder.getPreviousTrack();
        tryPlayTrack(IPlayCallback.IN_PREVIOUS, pre);
        onStatusChanged(IPlayCallback.IN_PREVIOUS);
    }

    //播放下一首
    private void threadNext() {
        //获取下一首歌曲
        AbsTrack next = mBinder.getNextTrack();
        //尝试播放指定歌曲
        tryPlayTrack(IPlayCallback.IN_NEXT, next);
        onStatusChanged(IPlayCallback.IN_NEXT);
    }

    //播放指定歌曲
    private void threadPlay(AbsTrack source, int index) {
        mBinder.setCurrentPlayIndex(index);
        tryPlayTrack(IPlayCallback.IN_PLAY, source);
        onStatusChanged(IPlayCallback.IN_PLAY);
        mIsFirstPlay = false;
    }

    //设置播放进度
    private void threadSeekTo(int position) {
        try {
            //移动到指定的播放位置
            mPlayer.seekTo(position);
        } catch (Exception e) {
            e.printStackTrace();
            onStatusChanged(IPlayCallback.IN_SEEK_TO);
        }
    }

    /**
     * 通知进度条更新
     */
    private void notifyProgressUpdate() {
        for (IPlayCallback.onProgressUpdateListener listener : mProgressUpdateCallbackList) {
            listener.onProgressUpdate(getCurrentPosition(), getDuration());
        }
    }

    /**
     * 播放器当前状态切换时(来自用户操作的切换)
     */
    private void onStatusChanged(int status) {
        if (mPlayerStatusChangedListener != null) {
            mPlayerStatusChangedListener.onPlayerStatusChanged(status);
        }
    }

    //meida player opera handle
    @Override
    public void play() {
        mHandler.sendEmptyMessage(THREAD_PLAY);
    }

    @Override
    public void pause() {
        mHandler.sendEmptyMessage(THREAD_PAUSE);
    }

    @Override
    public void continues() {
        mHandler.sendEmptyMessage(THREAD_CONTINUE);
    }

    @Override
    public void stop() {
        mHandler.sendEmptyMessage(THREAD_STOP);
    }

    @Override
    public void previous() {
        mHandler.sendEmptyMessage(THREAD_PREVIOUS);
    }

    @Override
    public void next() {
        mHandler.sendEmptyMessage(THREAD_NEXT);
    }

    @Override
    public void play(AbsTrack source, int index) {
        Message msg = Message.obtain();
        msg.obj = source;
        msg.arg1 = index;
        msg.what = THREAD_PLAY_MUSIC;
        mHandler.sendMessage(msg);
    }

    @Override
    public void seekTo(int position) {
        Message msg = Message.obtain();
        msg.arg1 = position;
        msg.what = THREAD_SEEK_TO;
        mHandler.sendMessage(msg);
    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mPlayer.getDuration();
    }

    @Override
    public void destroyUpdateThread() {
        if (mUpdateProgressThread != null && mUpdateProgressThread.isAlive()) {
            mUpdateProgressThread.interrupt();
            mUpdateProgressThread = null;
        }
    }

    @Override
    public void createUpdateThread() {
        if (mUpdateProgressThread == null) {
            mUpdateProgressThread = new Thread(mUpdateProgressRunnable);
            mUpdateProgressThread.setDaemon(true);
            mUpdateProgressThread.start();
        } else if (!mUpdateProgressThread.isAlive()) {
            mUpdateProgressThread.interrupt();
            mUpdateProgressThread = new Thread(mUpdateProgressRunnable);
            mUpdateProgressThread.setDaemon(true);
            mUpdateProgressThread.start();
        }
    }

    @Override
    public boolean pauseUpdateNotify() {
        if (mUpdateProgressThread != null && mUpdateProgressThread.isAlive()) {
            mIsStopUpdate = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean continueUpdateNotify() {
        if (mUpdateProgressThread != null && mUpdateProgressThread.isAlive()) {
            mIsStopUpdate = false;
            return true;
        } else {
            return false;
        }
    }

    //Mediaplayer listener
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
        if (mUserComletionListener != null) {
            mUserComletionListener.onCompletion(mp);
        }
    }

    //Media Player call back
    @Override
    public void setOnCompletitonListener(MediaPlayer.OnCompletionListener listener, boolean isRemoveOriginal) {
        if (isRemoveOriginal) {
            mPlayer.setOnCompletionListener(listener);
        } else {
            mUserComletionListener = listener;
        }
    }

    @Override
    public void setOnBufferUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) {
        mPlayer.setOnBufferingUpdateListener(listener);
    }

    @Override
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        mPlayer.setOnErrorListener(listener);
    }

    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        mPlayer.setOnPreparedListener(listener);
    }

    @Override
    public void setOnInfoListener(MediaPlayer.OnInfoListener listener) {
        mPlayer.setOnInfoListener(listener);
    }

    @Override
    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener) {
        mPlayer.setOnSeekCompleteListener(listener);
    }

    @Override
    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        mPlayer.setOnVideoSizeChangedListener(listener);
    }

    @Override
    public void setOnPlayerStatusChangedListener(IPlayCallback.onPlayerStatusChangedListener listener) {
        this.mPlayerStatusChangedListener = listener;
    }

    @Override
    public boolean addOnProgressUpdateListener(IPlayCallback.onProgressUpdateListener listener) {
        if (listener != null) {
            mProgressUpdateCallbackList.add(listener);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeOnProgressUpdateListener(IPlayCallback.onProgressUpdateListener listener) {
        if (listener != null) {
            return mProgressUpdateCallbackList.remove(listener);
        } else {
            return false;
        }
    }


    @Override
    public void run() {
        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case THREAD_PLAY:
                        threadPlay();
                        break;
                    case THREAD_PAUSE:
                        threadPause();
                        break;
                    case THREAD_STOP:
                        threadStop();
                        break;
                    case THREAD_CONTINUE:
                        threadContinue();
                        break;
                    case THREAD_PREVIOUS:
                        threadPrevious();
                        break;
                    case THREAD_NEXT:
                        threadNext();
                        break;
                    case THREAD_SEEK_TO:
                        threadSeekTo(msg.arg1);
                        break;
                    case THREAD_PLAY_MUSIC:
                        threadPlay((AbsTrack) msg.obj, msg.arg1);
                        break;
                    case THREAD_UPDATE_PROGRESS:
                        notifyProgressUpdate();
                        break;
                    default:
                        break;
                }
            }
        };
        Looper.loop();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                if (!mIsFirstPlay && !isPlaying()) {
                    continues();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                stop();
                break;
        }
    }
}
