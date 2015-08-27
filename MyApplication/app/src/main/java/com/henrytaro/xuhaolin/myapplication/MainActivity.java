package com.henrytaro.xuhaolin.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import henrytaro.bestapp.com.draw.interfaces.ISeatParamsExport;
import henrytaro.bestapp.com.draw.interfaces.IStageParamsExport;
import henrytaro.bestapp.com.draw.params.ExportParams;
import henrytaro.bestapp.com.view.ISeatChooseEvent;
import henrytaro.bestapp.com.view.SeatChooseView;


public class MainActivity extends ActionBarActivity implements ISeatChooseEvent {
    SeatChooseView mChooseView = null;
    int[][] mSeatMap = {
            {1, 1, 2, 3, 1, 1, 2, 2, 2, 2, 2, 1, 2, 3, 2, 1, 2, 1},
            {1, 0, 0, 0, 1, 1, 0, 2, 2, 2, 0, 1, 2, 0, 2, 1, 0, 1},
            {2, 2, 0, 2, 2, 0, 2, 0, 2, 0, 3, 0, 2, 0, 2, 1, 0, 1},
            {2, 2, 0, 3, 2, 0, 2, 2, 0, 2, 3, 0, 2, 0, 2, 1, 0, 1},
            {1, 1, 0, 3, 1, 1, 0, 2, 2, 2, 0, 1, 2, 0, 2, 1, 0, 1},
            {1, 0, 0, 0, 1, 1, 2, 0, 2, 0, 2, 1, 2, 3, 0, 0, 2, 1},
            {1, 1, 2, 3, 1, 1, 2, 2, 0, 2, 2, 1, 2, 3, 2, 1, 2, 1},

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseView = (SeatChooseView) findViewById(R.id.view_seatchoose);
        mChooseView.setIsDrawThumbnail(true);
        mChooseView.setIsShowThumbnailAlways(false);
        mChooseView.setSeatMap(mSeatMap);

        ExportParams params = mChooseView.getParams();
        params.setCanvasBackgroundColor(0xff9900);

        ISeatParamsExport seatParams = params.getSeatParams();
        IStageParamsExport stageParamsExport = params.getStageParams();
        seatParams.setImage(new int[]{R.drawable.icon_alpaca, R.drawable.icon_logo_pkq, R.drawable.icon_qr_view});
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
    public void seatSeleted(int i, int i1, boolean b) {

    }

    @Override
    public void seatSeleted(int i, int i1, int i2) {

    }

    @Override
    public void seletedFail() {

    }

    @Override
    public void seletedFull() {

    }
}
