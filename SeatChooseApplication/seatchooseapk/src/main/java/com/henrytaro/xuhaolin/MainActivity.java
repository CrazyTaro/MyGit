package com.henrytaro.xuhaolin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import us.bestapp.henrytaro.draw.interfaces.ISeatDrawHandle;
import us.bestapp.henrytaro.entity.SeatMap;
import us.bestapp.henrytaro.entity.example.EgSeatMap;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;
import us.bestapp.henrytaro.view.SeatChooseView;
import us.bestapp.henrytaro.view.interfaces.ISeatChooseEvent;


public class MainActivity extends Activity implements ISeatChooseEvent {
    SeatChooseView mChooseview = null;
    private int[][] mSeatMap = {
            //1,02,03,04,05,06,01,08,03,10,11,12,13,14,15,16,11,18,13,20,21,22,23,24,25,26,21,28,23,30,31,32,33,34,35,36
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//1
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//3
            {1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//3
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 0, 0, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//4
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 0, 0, 3, 1, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//5
            {1, 1, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//6
            {1, 1, 1, 3, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//1
            {1, 3, 3, 1, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//8
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//3
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//10
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//11
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//12
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//13
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//14
            {1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//15
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 0, 0, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//16
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 0, 0, 3, 1, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//11
            {1, 1, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//18
            {1, 1, 1, 3, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//13
            {1, 3, 3, 1, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//20
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//21
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//22
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//23
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//24
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//25
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//26
            {1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//21
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 0, 0, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//28
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 0, 0, 3, 1, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//23
            {1, 1, 3, 1, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//30
            {1, 1, 1, 3, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//31
            {1, 3, 3, 1, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3,},//32
            {3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3,},//33
            {1, 1, 0, 0, 0, 0, 0, 3, 1, 1, 1, 3, 1, 1, 0, 0, 0, 3, 1, 0, 0, 3, 1, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3,},//34
            {1, 1, 0, 0, 1, 1, 0, 3, 1, 1, 3, 0, 0, 3, 1, 0, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 1, 3, 3, 1, 1, 0, 0, 0, 1,},//35
            {1, 1, 0, 0, 3, 1, 0, 3, 1, 1, 1, 0, 0, 3, 1, 0, 0, 3, 3, 1, 0, 0, 0, 0, 0, 3, 1, 1, 3, 1, 3, 1, 0, 0, 0, 1,},//36
    };

    String jsonStr = "{\n" +
            "    \"success\": true, \n" +
            "    \"error_code\": \"0\", \n" +
            "    \"message\": \"请求成功\", \n" +
            "    \"data\": {\n" +
            "        \"row\": [\n" +
            "            {\n" +
            "                \"rownum\": \"1\", \n" +
            "                \"rowid\": \"1\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"2\", \n" +
            "                \"rowid\": \"2\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"3\", \n" +
            "                \"rowid\": \"3\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"4\", \n" +
            "                \"rowid\": \"4\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"5\", \n" +
            "                \"rowid\": \"5\", \n" +
            "                \"columns\": \"ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"6\", \n" +
            "                \"rowid\": \"6\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@LK@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"7\", \n" +
            "                \"rowid\": \"7\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"8\", \n" +
            "                \"rowid\": \"8\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@LK@0,03@LK@0,04@A@0,05@A@0,06@LK@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"9\", \n" +
            "                \"rowid\": \"9\", \n" +
            "                \"columns\": \"ZL,ZL,ZL,01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"10\", \n" +
            "                \"rowid\": \"10\", \n" +
            "                \"columns\": \"01@A@0,02@A@0,ZL,03@A@0,04@A@0,05@A@0,06@A@0,07@LK@0,08@LK@0,09@LK@0,10@A@0,11@A@0,12@A@0\"\n" +
            "            }, \n" +
            "            {\n" +
            "                \"rownum\": \"11\", \n" +
            "                \"rowid\": \"11\", \n" +
            "                \"columns\": \"01@A@0,02@A@0,03@A@0,04@A@0,05@A@0,06@A@0,07@A@0,08@A@0,09@A@0,10@A@0,11@A@0,12@A@0,13@A@0\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }, \n" +
            "    \"source\": \"gewala\"\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeatMap dataMap = SeatMap.objectFromJSONStr(jsonStr);

        mChooseview = (SeatChooseView) findViewById(R.id.view_choose);
        mChooseview.setISeatChooseEvent(this);
        ISeatDrawHandle seatDataHandle = mChooseview.getSeatDrawInterface();
        seatDataHandle.setIsShowLog(false, null);
        seatDataHandle.setSeatDrawMap(dataMap);
//        seatDataHandle.setSeatDrawMap(new EgSeatMap(mSeatMap));

        IGlobleParams globleParams = seatDataHandle.getGlobleParams();
        globleParams.setIsDrawThumbnail(true);
//        globleParams.setIsDrawSeletedRowColumnNotification(true, globleParams.createNotificationFormat(true, "第", IGlobleParams.FORMAT_STR, "行/第", IGlobleParams.FORMAT_STR, "列"));
        globleParams.setIsDrawRowNumber(true);
//        globleParams.setIsDrawColumnNumber(true);

        ISeatParams seatParams = seatDataHandle.getSeatParams();
        seatParams.setIsDrawSeatType(false);
//        seatParams.setAllSeatTypeWithColor(new int[]{1, 8, 3}, seatParams.getSeatColorArrary(), seatParams.getThumbnailColorArrary(), seatParams.getSeatTypeDescription());
//        seatParams.setSeatTypeConstant(8, 1, 0, 3);
//        seatParams.setImage(new int[]{R.drawable.icon_bg, R.drawable.zuowei_selected, R.drawable.zuowei_disabled});

        IStageParams stageParams = seatDataHandle.getStageParams();
        stageParams.setStageDescription("深圳橙天嘉禾影城万象城店 6号厅");
    }

    @Override
    public void selectedSeatSuccess(int rowIndexInMap, int columnIndexInMap, boolean isChosen) {

    }

    @Override
    public void selectedSeatSuccess(int rowInMap, int columnInMap, int rowNumber, int rowColumn, ISeatEntity seatEntity) {

    }

    @Override
    public void seletedFail() {

    }

    @Override
    public void seletedFull() {
        Toast.makeText(this, "选座已满", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scaleMaximum() {

    }
}
