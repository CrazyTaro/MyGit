package us.bestapp.henrytaro.player.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.service.PlaySerive;

/**
 * Created by xuhaolin on 15/10/29.
 */
public class ScreenDialog extends Dialog implements View.OnClickListener {
    private static ScreenDialog mInstance;

    ITrackHandleBinder mBiner;
    ImageView imageViewPlay;
    ImageView imageViewPrevious;
    ImageView imageViewNext;
    ImageView imageViewLike;
    TextView textViewTime;
    TextView textViewDate;
    TextView textViewTrack;
    TextView textViewArtist;

    public ScreenDialog(Context context, int themeResId) {
        super(context, R.style.FullScreen);
        initial(context);
    }

    public ScreenDialog(Context context) {
        super(context);
        initial(context);
    }


    private void initial(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View lockView = inflater.inflate(R.layout.activity_screen_lock, null);
        initialView(lockView);

        WindowManager windowMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowMgr.getDefaultDisplay();
        Point windowSize = new Point();
        display.getSize(windowSize);

        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setAttributes(params);
        this.getWindow().setContentView(lockView);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mBiner = PlaySerive.getBinder();
    }

    public static void createDialog(Context context) {
        if (mInstance == null) {
            mInstance = new ScreenDialog(context, 0);
        }
    }

    public static ScreenDialog getInstance() {
        return mInstance;
    }

    public static void showDialog() {
        if (mInstance != null) {
            mInstance.show();
        }
    }

    public static void dismissDialog() {
        if (mInstance != null) {
            mInstance.dismiss();
        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
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
