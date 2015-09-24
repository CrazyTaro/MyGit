package com.henrytaro.xuhaolin.areachoosesapk;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import us.bestapp.henrytaro.areachoose.draw.interfaces.IAreaDrawInterfaces;
import us.bestapp.henrytaro.areachoose.draw.interfaces.IAreaEventHandle;
import us.bestapp.henrytaro.areachoose.entity.AreaListEntity;
import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;
import us.bestapp.henrytaro.areachoose.utils.AreaMaskColorUtils;
import us.bestapp.henrytaro.areachoose.view.AreaChooseView;


public class MainActivity extends Activity implements IAreaEventHandle {
    private Button btn_reset;
    private AreaChooseView view_choose;
    private ProgressDialog dialog;
    private Handler mHanlder = null;

    private String jsonStr = "{area:[" +
            "{area_name: 1, color: \"#d21616\", is_sold_out: true},\n" +
            "{area_name: 2, color: \"#16d292\", is_sold_out: true},\n" +
            "{area_name: 3, color: \"#1689d2\", is_sold_out: false},\n" +
            "{area_name: 4, color: \"#2cd216\", is_sold_out: false},\n" +
            "{area_name: 5, color: \"#8516d2\", is_sold_out: false},\n" +
            "{area_name: 6, color: \"#f9d71d\", is_sold_out: false},\n" +
            "{area_name: 7, color: \"#1df9f6\", is_sold_out: false},\n" +
            "{area_name: 8, color: \"#2a00a3\", is_sold_out: false},\n" +
            "{area_name: 9, color: \"#ffba00\", is_sold_out: false}]}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view_choose = (AreaChooseView) this.findViewById(R.id.view_areachoose);
        btn_reset = (Button) this.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AreaListEntity areaListEntity = AreaListEntity.objectFromJsonStr(jsonStr);
                IAreaDrawInterfaces drawUtils = view_choose.getAreaDrawIntrefaces();
                drawUtils.setAreaMaskColorUtils(new AreaMaskColorUtils(areaListEntity.getAreaList()));
                drawUtils.initial(areaListEntity.getAreaList(), R.drawable.img_test_small_front, R.drawable.img_test_small_bg, MainActivity.this);
            }
        });

        mHanlder = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x1:
                        dialog = ProgressDialog.show(MainActivity.this, null, "正在加载数据,请稍等!");
                        break;
                    case 0x2:
                        dialog.dismiss();
                        view_choose.postInvalidate();
                        break;
                }
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartLoadMaskBitmap() {
        mHanlder.sendEmptyMessage(0x1);
    }

    @Override
    public void onFinishLoadMaskBitmap(boolean isSuccess) {
        if (isSuccess && dialog != null) {
            mHanlder.sendEmptyMessage(0x2);
        }
    }

    @Override
    public void onAreaChoose(AbsAreaEntity areaEntity) {
        Toast.makeText(this, "选中颜色为:" + areaEntity.getAreaColorHXStr() + "/选中区为:" + areaEntity.getAreaName(), Toast.LENGTH_SHORT).show();
    }
}
