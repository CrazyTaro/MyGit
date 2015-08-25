package com.crazytaro.bestapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.crazytaro.bestapp.R;
import com.crazytaro.bestapp.view.ISeatChooseEvent;
import com.crazytaro.bestapp.view.SeatChooseView;


public class MainActivity extends Activity implements ISeatChooseEvent {
    private int[][] mSeatMap = {
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//1
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//2
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//3
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//4
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//5
            {1, 1, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//6
            {1, 1, 1, 2, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//7
            {1, 2, 2, 1, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//8
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//9
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//10
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//11
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//12
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//13
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//14
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//15
            {0, 0, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//16
            {0, 0, 2, 1, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//17
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//18
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//19
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//20
            {1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//21
            {1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//22
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//23
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//24
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//25
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//1
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//2
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//3
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//4
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//5
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//6
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//7
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//8
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//9
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//10
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//11
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//12
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//13
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//14
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//15
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//16
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//17
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//18
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//19
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//20
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//21
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//22
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//23
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//24
//            {2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1,},//25
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SeatChooseView chooseView = (SeatChooseView) findViewById(R.id.view_seatchoose);
        chooseView.setSeatMap(mSeatMap);
        chooseView.setISeatChooseEvent(this);
        chooseView.setIsDrawThumbnail(true);
        chooseView.getParams().setCanvasBackgroundColor(0x7ed321);
        chooseView.setMostSeletedCount(3);
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
    public void seatSeleted(int rowIndex, int columnIndex, boolean isChosen) {
        if (isChosen) {
            Toast.makeText(this, "选中了座位x=" + rowIndex + "/y=" + columnIndex, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void seatSeleted(int rowIndex, int columnIndex, int seatType) {

    }

    @Override
    public void seletedFail() {
    }

    @Override
    public void seletedFull() {
        Toast.makeText(this, "选座已满", Toast.LENGTH_SHORT).show();
    }
}
