package us.bestapp.henrytaro;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.List;

import us.bestapp.henrytaro.player.interfaces.IPlayListHandle;
import us.bestapp.henrytaro.player.interfaces.ITrackHandleBinder;
import us.bestapp.henrytaro.player.model.AbsTrack;
import us.bestapp.henrytaro.player.model.Song;
import us.bestapp.henrytaro.player.utils.CommonUtils;
import us.bestapp.henrytaro.player.utils.PlayServiceHelperUtils;


public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private Button mBtnPre;
    private Button mBtnNext;
    private Button mBtnPlay;
    private Button mBtnModel;
    private SeekBar mSeekBar;
    private int mModelIndex = 0;
    private ListView mLvPlayList;
    private Handler mHanlder;
    private ProgressDialog mDialog;
    private ITrackHandleBinder mBinder;
    private TrackAdapter mAdapter;
    private PlayServiceHelperUtils mPlayServiceHelperUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnPre = (Button) findViewById(R.id.btn_pre);
        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnPlay = (Button) findViewById(R.id.btn_play);
        mBtnModel = (Button) findViewById(R.id.btn_model);
        mSeekBar = (SeekBar) findViewById(R.id.seekbar_music);
        mLvPlayList = (ListView) findViewById(R.id.listview_music);

        mHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x1:
                        mDialog = ProgressDialog.show(MainActivity.this, "请稍候", "加载列表~~");
                        break;
                    case 0x2:
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        break;
                    case 0x3:
                        mLvPlayList.setAdapter(mAdapter);
                        ((TrackAdapter) mLvPlayList.getAdapter()).notifyDataSetChanged();
                        mHanlder.sendEmptyMessage(0x2);
                        break;
                }
            }
        };

        mBtnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinder.getOperaHandle().previous();
            }
        });
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinder.getOperaHandle().next();
            }
        });
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinder.getOperaHandle().pause();
            }
        });
        mBtnModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModelIndex > 2) {
                    mModelIndex = 0;
                }
                String model;
                switch (mModelIndex) {
                    case 0:
                        model = IPlayListHandle.MODEL_SINGLE;
                        break;
                    case 1:
                        model = IPlayListHandle.MODEL_ORDER;
                        break;
                    case 2:
                        model = IPlayListHandle.MODEL_RANDOM;
                        break;
                    default:
                        model = IPlayListHandle.MODEL_ORDER;
                        break;
                }
                if (mBinder != null) {
                    mBinder.getPlayListHandle().setPlayModel(model);
                }
                Toast.makeText(MainActivity.this, "current model " + model, Toast.LENGTH_SHORT).show();
                mModelIndex++;
            }
        });
        mSeekBar.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mBinder != null) {
                        mBinder.getOperaHandle().seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mLvPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AbsTrack music = (AbsTrack) mAdapter.getItem(position);
                if (mBinder.getPlayListHandle().getPlayModel().equals(IPlayListHandle.MODEL_RANDOM)) {
                    mBinder.setPlayListWithTrackPlayNow(mAdapter.getList(), music, position);
                } else {
                    mBinder.getOperaHandle().play(music, position);
                }
            }
        });

        mHanlder.sendEmptyMessage(0x1);
        mPlayServiceHelperUtils = new PlayServiceHelperUtils(this, mSeekBar);
        mPlayServiceHelperUtils.initial(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayServiceHelperUtils.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayServiceHelperUtils.onResume(this, mSeekBar);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.mBinder = (ITrackHandleBinder) service;
        mPlayServiceHelperUtils.onServiceConnect(mBinder);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Song> list = AudioUtils.getAllSongs(MainActivity.this);
                for (int i = 0; i < CommonUtils.FILE_NAME.length; i++) {
                    Song song = new Song();
                    song.setTitle(CommonUtils.FILE_NAME[i]);
                    song.setAlbum("Unknown");
                    song.setFileName(CommonUtils.FILE_NAME[i]);
                    song.setFileUrl(CommonUtils.URL_MUSICS[i]);
                    list.add(song);
                }
                mAdapter = new TrackAdapter(MainActivity.this);
                mAdapter.setList(list);
                mBinder.getPlayListHandle().setPlayList(list);
                mHanlder.sendEmptyMessage(0x3);
            }
        }).start();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mPlayServiceHelperUtils.onServiceDisconnected();
        mBinder = null;
        mPlayServiceHelperUtils = null;
        mHanlder.sendEmptyMessage(0x2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            mBinder.printPlayList();
        }
        return super.onOptionsItemSelected(item);
    }
}
