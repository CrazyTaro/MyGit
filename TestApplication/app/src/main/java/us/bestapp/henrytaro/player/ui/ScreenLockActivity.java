package us.bestapp.henrytaro.player.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.service.PlaySerive;
import us.bestapp.henrytaro.player.utils.CommonUtils;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class ScreenLockActivity extends Activity implements View.OnClickListener {

    ITrackHandleBinder mBiner;
    ImageView imageViewPlay;
    ImageView imageViewPrevious;
    ImageView imageViewNext;
    ImageView imageViewLike;
    TextView textViewTime;
    TextView textViewDate;
    TextView textViewTrack;
    TextView textViewArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//关键代码
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);


        View lock = View.inflate(this, R.layout.activity_screen_lock, null);
        initialView(lock);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        Dialog dialog = new Dialog(this, R.style.FullScreen);
//        dialog.setContentView(lock, params);
//        dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        dialog.show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.FullScreen);
//        builder.setView(lock);
//        AlertDialog dialog = builder.create();
        Dialog dialog=new Dialog(this,R.style.FullScreen);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setAttributes(params);
//        dialog.getWindow().setContentView(lock);
        dialog.setContentView(lock);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.show();

        mBiner = PlaySerive.getBinder();
    }

    private void initialView(View view) {
        imageViewLike = (ImageView) view.findViewById(R.id.imageview_screen_like);
        imageViewPrevious = (ImageView) view.findViewById(R.id.imageview_screen_previous);
        imageViewNext = (ImageView) view.findViewById(R.id.imageview_screen_next);
        imageViewPlay = (ImageView) view.findViewById(R.id.imageview_screen_play);
        textViewTime = (TextView) view.findViewById(R.id.textview_screen_time);
        textViewDate = (TextView) view.findViewById(R.id.textview_screen_date);
        textViewTrack = (TextView) view.findViewById(R.id.textview_screen_track);
        textViewArtist = (TextView) view.findViewById(R.id.textview_screen_artist);

        imageViewLike.setOnClickListener(this);
        imageViewPrevious.setOnClickListener(this);
        imageViewNext.setOnClickListener(this);
        imageViewPlay.setOnClickListener(this);
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
