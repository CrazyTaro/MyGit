package com.henrytaro.xuhaolin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import us.bestapp.henrytaro.draw.interfaces.IGlobleParamsExport;
import us.bestapp.henrytaro.draw.interfaces.ISeatParamsExport;
import us.bestapp.henrytaro.draw.interfaces.IStageParamsExport;
import us.bestapp.henrytaro.view.ISeatChooseEvent;
import us.bestapp.henrytaro.view.SeatChooseView;


public class MainActivity extends Activity implements ISeatChooseEvent {
    SeatChooseView mChooseview = null;
    private int[][] mSeatMap = {
            //1,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//1
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//2
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//3
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 0, 0, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//4
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 0, 0, 2, 1, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//5
            {1, 1, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//6
            {1, 1, 1, 2, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//7
            {1, 2, 2, 1, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//8
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//9
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//10
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//11
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//12
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//13
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//14
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//15
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 0, 0, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//16
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 0, 0, 2, 1, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//17
            {1, 1, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//18
            {1, 1, 1, 2, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//19
            {1, 2, 2, 1, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//20
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//21
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//22
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//23
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//24
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//25
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//26
            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//27
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 0, 0, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//28
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 0, 0, 2, 1, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//29
            {1, 1, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//30
            {1, 1, 1, 2, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//31
            {1, 2, 2, 1, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//32
            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//33
            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2, 1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//34
            {1, 1, 0, 0, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//35
            {1, 1, 0, 0, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2, 2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//36
//            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//13
//            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//14
//            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//15
//            {0, 0, 2, 1, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//16
//            {0, 0, 2, 1, 2, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//17
//            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//18
//            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//19
//            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//20
//            {1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//21
//            {1, 0, 0, 2, 1, 1, 0, 2, 1, 1, 1, 0, 0, 2, 1, 0, 0, 2,},//22
//            {1, 2, 0, 0, 0, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 0, 0, 1,},//23
//            {2, 1, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 1,},//24
//            {1, 1, 0, 0, 0, 0, 0, 2, 1, 1, 1, 2, 1, 1, 0, 0, 0, 2,},//25
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseview = (SeatChooseView) findViewById(R.id.view_choose);
        mChooseview.setSeatMap(mSeatMap);
        mChooseview.setISeatChooseEvent(this);
        mChooseview.setIsShowLog(true, null);

        IGlobleParamsExport globleParams = mChooseview.getParams().getGlobleParams();
        globleParams.setIsDrawThumbnail(false);
        globleParams.setIsDrawSeletedRowColumnNotification(true);
        globleParams.setIsDrawColumnNumber(true);
        globleParams.setIsDrawRowNumber(true);

        ISeatParamsExport seatParams = mChooseview.getParams().getSeatParams();
        seatParams.setExtraSeatTypeWithColor(new int[]{7, 8, 9}, new int[]{Color.parseColor("#ff9900"), Color.parseColor("#7ed321"), Color.parseColor("#0000ff")}, null, new String[]{"预定", "不售", "情侣"});

        IStageParamsExport stageParams = mChooseview.getParams().getStageParams();
    }

    @Override
    public void seatSeleted(int rowIndex, int columnIndex, boolean isChosen) {

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
//        ISeatParamsExport seatParams = mChooseview.getParams().getSeatParams();
////        seatParams.setImage(new int[]{R.drawable.icon_logo_alpaca, R.drawable.icon_logo_main, R.drawable.icon_logo_pkq});
//        seatParams.setImage(new int[]{R.drawable.icon_logo_main, R.drawable.icon_logo_pkq, R.drawable.icon_logo_alpaca, R.drawable.icon_logo_main, R.drawable.icon_logo_pkq, R.drawable.icon_logo_alpaca});
//
//        IGlobleParamsExport globleParams = mChooseview.getParams().getGlobleParams();
//        globleParams.setCanvasBackgroundColor(Color.parseColor("#ff9900"));
    }
}
