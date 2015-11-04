package us.bestapp.henrytaro.player.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.model.AbsTrack;
import us.bestapp.henrytaro.player.service.PlayService;
import us.bestapp.henrytaro.player.service.ScreenLockUpdateBroadcast;
import us.bestapp.henrytaro.player.utils.CommonUtils;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class ScreenLockActivity extends Activity implements View.OnClickListener {

    private ScreenLockUpdateBroadcast mScreenLockUpdateBroadcast;

    public class ViewHolder {
        public ImageView imageViewPlay;
        public ImageView imageViewPrevious;
        public ImageView imageViewNext;
        public ImageView imageViewLike;
        public TextView textViewTime;
        public TextView textViewDate;
        public TextView textViewTrack;
        public TextView textViewArtist;
    }

    private ITrackHandleBinder mBiner;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//关键代码
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        View lock = View.inflate(this, R.layout.activity_screen_lock, null);
        setContentView(lock);

        WindowManager windowMgr = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowMgr.getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.type = WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
        this.getWindow().setAttributes(params);

        mScreenLockUpdateBroadcast = new ScreenLockUpdateBroadcast(this, mViewHolder);
//        params.width=windowSize.x;
//        params.height=windowSize.y;
//        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.FullScreen);
//        builder.setView(lock);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.getWindow().setAttributes(params);
//        alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        alertDialog.show();


        //自定义dialog
        //TODO:魅族可用

        //TODO:三星可用


//        //TODO:魅族可用
//        //home无效,返回无效,状态栏有效
//        Dialog dialog = new Dialog(this, R.style.FullScreen);
//        dialog.setContentView(lock);
//        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        dialog.getWindow().setAttributes(params);
//        dialog.show();
//        //TODO:魅族可用

//        //TODO:三星可用
        //home有效,返回无效,状态栏有效
//        Dialog dialog = new Dialog(this, R.style.FullScreen);
////        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(lock);
//        dialog.getWindow().setAttributes(params);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.show();
//        //TODO:三星可用

        initialView(lock, mViewHolder);
    }

    private void updateView(ITrackHandleBinder binder) {
        mBiner = binder;
        if (mBiner != null) {
            String trackName = "播覇音乐";
            String trackArtist = "暂未播放任何音乐";
            AbsTrack track = mBiner.getPlayListHandle().getCurrentTrack();
            if (track != null) {
                trackName = track.getTrackName();
                trackArtist = track.getTrackArtist();
            }
            mViewHolder.textViewTrack.setText(trackName);
            mViewHolder.textViewArtist.setText(trackArtist);

            if (mBiner.getOperaHandle().isPlaying()) {
                mViewHolder.imageViewPlay.setImageResource(R.drawable.ic_pause);
            } else {
                mViewHolder.imageViewPlay.setImageResource(R.drawable.ic_play);
            }
        }

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis());
        mViewHolder.textViewTime.setText(CommonUtils.getTimeStr(ScreenLockUpdateBroadcast.FORMAT_TIME, date));
        mViewHolder.textViewDate.setText(CommonUtils.getDateStr(ScreenLockUpdateBroadcast.FORMAT_DATE, date));
    }

    private void initialView(View view, ViewHolder holder) {
        holder.imageViewLike = (ImageView) view.findViewById(R.id.imageview_screen_like);
        holder.imageViewPrevious = (ImageView) view.findViewById(R.id.imageview_screen_previous);
        holder.imageViewNext = (ImageView) view.findViewById(R.id.imageview_screen_next);
        holder.imageViewPlay = (ImageView) view.findViewById(R.id.imageview_screen_play);
        holder.textViewTime = (TextView) view.findViewById(R.id.textview_screen_time);
        holder.textViewDate = (TextView) view.findViewById(R.id.textview_screen_date);
        holder.textViewTrack = (TextView) view.findViewById(R.id.textview_screen_track);
        holder.textViewArtist = (TextView) view.findViewById(R.id.textview_screen_artist);

        holder.imageViewLike.setOnClickListener(this);
        holder.imageViewPrevious.setOnClickListener(this);
        holder.imageViewNext.setOnClickListener(this);
        holder.imageViewPlay.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mScreenLockUpdateBroadcast != null) {
            this.registerReceiver(mScreenLockUpdateBroadcast, mScreenLockUpdateBroadcast.createBroadcastFilter());
        }
        updateView(PlayService.getBinder());
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mScreenLockUpdateBroadcast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_screen_like:
                if (mBiner != null) {

                }
                break;
            case R.id.imageview_screen_previous:
                if (mBiner != null) {
                    mBiner.getOperaHandle().previous();
                }
                break;
            case R.id.imageview_screen_next:
                if (mBiner != null) {
                    mBiner.getOperaHandle().next();
                }
                break;
            case R.id.imageview_screen_play:
                if (mBiner != null) {
                    mBiner.getOperaHandle().pause();
                }
                break;
        }
    }
}
