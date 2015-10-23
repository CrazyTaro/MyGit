package us.bestapp.player.testapplication.player.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

import us.bestapp.player.testapplication.player.interfaces.IMediaPlayerCallback;
import us.bestapp.player.testapplication.player.interfaces.IMusicHandleBinder;
import us.bestapp.player.testapplication.player.interfaces.IPlayCallback;
import us.bestapp.player.testapplication.player.interfaces.IPlayerOperaHandle;
import us.bestapp.player.testapplication.player.model.AbsMusic;

/**
 * Created by xuhaolin on 15/10/18.
 * 播放后台服务
 */
public class PlaySerive extends Service implements IPlayerOperaHandle, IMediaPlayerCallback, MediaPlayer.OnCompletionListener, Runnable {
    public static final int THREAD_PLAY = 0x1;
    public static final int THREAD_PAUSE = 0x2;
    public static final int THREAD_PREVIOUS = 0x3;
    public static final int THREAD_NEXT = 0x4;
    public static final int THREAD_SEEK_TO = 0x5;
    public static final int THREAD_PLAY_MUSIC = 0x6;
    public static final int THREAD_UPDATE_PROGRESS = 0x7;

    //绑定服务的间接操作
    private static PlayBinder mBinder;
    //播放器
    private MediaPlayer mPlayer;
    //是否第一次播放
    private boolean mIsFirstPlay = true;
    //最近一次切换上一首的时间
    private long mLastPreviousTime = 0;
    private volatile boolean mIsStopUpdate = false;

    //回调事件
    private MediaPlayer.OnCompletionListener mUserComletionListener;
    private List<IPlayCallback.onProgressUpdateListener> mProgressUpdateCallbackList = new LinkedList<>();
    private IPlayCallback.onPlayerStatusChangedListener mPlayerStatusChangedListener;

    //后台处理播放线程及进度更新线程
    private Thread mBackgroundThread;
    private Thread mUpdateProgressThread;
    private Runnable mUpdateProgressRunnable;
    private Handler mHandler;

    /**
     * 返回服务绑定的对象(服务只存在一个,绑定的对象也只存在一个)
     */
    public static synchronized IMusicHandleBinder getBinder() {
        return mBinder;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBinder = PlayBinder.getInstance(this);
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initial();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBinder.getPlayCallback() != null) {
            mBinder.getPlayCallback().onServiceDestory();
        }
        if (mPlayer != null) {
            try {
                mPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPlayer = null;
            }
        }
        if (mBinder != null) {
            mBinder.destory();
            mBinder = null;
        }
    }

    //初始化
    private void initial() {
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
    }

    /**
     * 尝试播放指定音乐
     *
     * @param errorTag 出错时的标签<br/>
     *                 {@link IPlayCallback#IN_NEXT}<br/>
     *                 {@link IPlayCallback#IN_PLAY}<br/>
     *                 {@link IPlayCallback#IN_PREVIOUS}<br/>
     *                 {@link IPlayCallback#IN_SEEK_TO}<br/>
     * @param music    指定播放的音乐, 可为null,若为null尝试通知音乐回调
     */
    private void tryPlayMusic(int errorTag, AbsMusic music) {
        if (music == null) {
            //通知音乐为null回调
            if (mBinder.getPlayCallback() != null) {
                mBinder.getPlayCallback().onMusicNotExsit();
                return;
            }
        } else {
            //设置该音乐为当前音乐
            //尝试播放当前音乐
            if (mBinder.setCurrentMusic(music)) {
                tryPlayCurrentMusic(errorTag);
            }
        }
    }

    /**
     * 尝试播放当前音乐,设置当前音乐通过{@link PlayBinder#setCurrentMusic(AbsMusic)}
     *
     * @param errorTag 出错时的标签<br/>
     *                 {@link IPlayCallback#IN_NEXT}<br/>
     *                 {@link IPlayCallback#IN_PLAY}<br/>
     *                 {@link IPlayCallback#IN_PREVIOUS}<br/>
     *                 {@link IPlayCallback#IN_SEEK_TO}<br/>
     */
    private void tryPlayCurrentMusic(int errorTag) {
        //获取当前播放音乐
        AbsMusic current = mBinder.getCurrentMusic();
        if (current != null) {
            try {
                //重置
                mPlayer.reset();
                mPlayer.setDataSource(this, Uri.parse(current.url));
                //必须进行加载
                mPlayer.prepare();
                mPlayer.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                //通知播放失败
                if (mBinder.getPlayCallback() != null) {
                    mBinder.getPlayCallback().onFailPlay(ex.getMessage(), errorTag);
                }
            }
        }
    }

    private void threadPlay() {
        if (mIsFirstPlay) {
            //若当前播放的音乐为null且为第一次播放,则对尝试根据当前的播放模式生成音乐进行播放
            AbsMusic firstMusic = mBinder.getNextMusic();
            tryPlayMusic(IPlayCallback.IN_PLAY, firstMusic);
        } else {
            try {
                //尝试播放,此处用于处理当前音乐被暂停的情况
                mPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
                //尝试播放当前的音乐,此处为第一次播放时情况(此前未有任何音乐进行播放过并暂停的情况)
                tryPlayCurrentMusic(IPlayCallback.IN_PLAY);
            }
        }
        onStatusChanged(IPlayCallback.IN_PLAY);
    }

    private void threadPause() {
        //暂停事件
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            onStatusChanged(IPlayCallback.IN_PAUSE);
        } else {
            play();
        }
    }

    private void threadPrevious() {
        //获取历史播放列表的上一首歌曲
        AbsMusic pre = mBinder.getPreviousMusic();
        tryPlayMusic(IPlayCallback.IN_PREVIOUS, pre);
        onStatusChanged(IPlayCallback.IN_PREVIOUS);
    }

    private void threadNext() {
        //将当前播放的音乐添加到历史播放列表中
        mBinder.putHistoryMusic(null);
        //获取下一首歌曲
        AbsMusic next = mBinder.getNextMusic();
        //尝试播放指定歌曲
        tryPlayMusic(IPlayCallback.IN_NEXT, next);
        onStatusChanged(IPlayCallback.IN_NEXT);
    }

    private void threadPlay(AbsMusic source, int index) {
        mBinder.putHistoryMusic(null);
        mBinder.setCurrentPlayIndex(index);
        tryPlayMusic(IPlayCallback.IN_PLAY, source);
        onStatusChanged(IPlayCallback.IN_PLAY);
    }

    private void threadSeekTo(int position) {
        try {
            //移动到指定的播放位置
            mPlayer.seekTo(position);
        } catch (Exception e) {
            e.printStackTrace();
            onStatusChanged(IPlayCallback.IN_SEEK_TO);
        }
    }

    private void notifyProgressUpdate() {
        for (IPlayCallback.onProgressUpdateListener listener : mProgressUpdateCallbackList) {
            listener.onProgressUpdate(getCurrentPosition(), getDuration());
        }
    }

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
    public void previous() {
        mHandler.sendEmptyMessage(THREAD_PREVIOUS);
    }

    @Override
    public void next() {
        mHandler.sendEmptyMessage(THREAD_NEXT);
    }

    @Override
    public void play(AbsMusic source, int index) {
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
    public void destoryUpdateThread() {
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
                        threadPlay((AbsMusic) msg.obj, msg.arg1);
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
}
