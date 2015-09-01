package com.henrytaro.xuhaolin.textjar;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import us.bestapp.henrytaro.draw.interfaces.ISeatInterfaces;
import us.bestapp.henrytaro.view.SeatChooseView;


public class MainActivity extends Activity {
    SeatChooseView view = null;
    int[][] seatMap = {
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//1
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//3
            {1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//3
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 0, 0, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//4
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 0, 0, 3, 1, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//5
            {1, 1, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//6
            {1, 1, 1, 3, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//7
            {1, 3, 3, 1, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//8
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//9
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//10
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//11
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//12
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//13
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (SeatChooseView) findViewById(R.id.view_choose);
        ISeatInterfaces seatInterface = view.getSeatHandleInterface();
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
