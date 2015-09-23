package com.henrytaro.xuhaolin.areachoosesapk;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.henrytaro.xuhaolin.areachoosesapk.entity.AreaListEntity;

import us.bestapp.henrytaro.areachoose.draw.AbsDrawUtils;
import us.bestapp.henrytaro.areachoose.view.AreaChooseView;


public class MainActivity extends Activity {
    private Button btn_reset;
    private AreaChooseView view_choose;

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
                AbsDrawUtils drawUtils = view_choose.getDrawUtils();
                drawUtils.setAreaList(areaListEntity.getAreaList());
                drawUtils.setDrawBitmap(R.drawable.img_test_front, R.drawable.img_test_bg);
                view_choose.postInvalidate();
            }
        });
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
}
