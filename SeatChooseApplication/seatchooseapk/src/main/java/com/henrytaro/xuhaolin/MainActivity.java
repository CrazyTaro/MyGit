package com.henrytaro.xuhaolin;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import us.bestapp.henrytaro.draw.interfaces.ISeatDrawInterface;
import us.bestapp.henrytaro.entity.absentity.AbsMapEntity;
import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.entity.example.EgSeatMap;
import us.bestapp.henrytaro.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.params.interfaces.IGlobleParams;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;
import us.bestapp.henrytaro.utils.SeatCheckRuleUtils;
import us.bestapp.henrytaro.view.SeatChooseView;
import us.bestapp.henrytaro.view.interfaces.ISeatChooseEvent;


public class MainActivity extends Activity implements ISeatChooseEvent {
    SeatChooseView mChooseview = null;
    Button mBtnSubmit = null;
    ISeatDrawInterface mSeatDataHandle = null;
    AbsMapEntity mMap = null;
    //虚拟座位表
    //4/5 表示情侣座
    //1表示座位可选
    //2表示已选座位
    //3表示锁定座位
    private int[][] mSeatMap = {
            //1,02,03,04,05,06,0708,09,10,11,12,13,14,15,16,11,18,13,20,21,22,23,24,25,26,21,28,23,30,31,32,33,34,35,36
            {4, 5,1, 4, 5, 0, 0, 1, 1, 1, 4, 5, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1, 1, 3, 4, 5,},//1
            {1, 1,1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1,},//3
            {1, 3,1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 1, 1, 1, 0, 0, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//3
            {3, 1,1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//4
            {1, 1,1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 5,},//5
            {1, 1,1, 1, 1, 0, 0, 4, 5, 1, 1, 1, 3, 4, 5, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//6
            {1, 1,1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 3, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//1
            {1, 3,1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 3, 1, 1, 0, 0, 1, 4, 5, 3, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//8
            {3, 1,1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1,},//3
            {1, 1,1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 1, 3, 4, 5, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//11
            {1, 1,1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 1, 0, 0, 0, 0, 0, 1, 1, 3, 1, 1,},//11
            {1, 1,1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 1, 1, 3, 1, 1,},//12
            {1, 1,1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 1, 3, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//13
            {1, 1,1, 1, 1, 0, 0, 3, 1, 1, 1, 1, 1, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1,},//14
            {1, 3,1, 1, 3, 0, 0, 1, 1, 1, 1, 3, 1, 1, 1, 0, 0, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1,},//15
            {3, 1,1, 3, 1, 0, 0, 1, 1, 1, 3, 1, 1, 1, 1, 0, 0, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//16
            {1, 1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1,},//11
            {1, 1,1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1,},//18
//            {1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 4, 5, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//13
//            {1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//21
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3,},//21
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//22
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 4, 5, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//23
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//24
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//25
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 4, 5, 3, 1, 3, 1, 1, 1, 1, 1,},//26
//            {1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//21
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 3, 4, 5, 3, 1, 1, 3, 1, 1, 1, 3,},//28
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//23
//            {1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 3, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//31
//            {1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//31
//            {1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3,},//32
//            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3,},//33
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3,},//34
//            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 3, 1, 1, 1, 3, 4, 5, 1, 1, 1, 1, 1, 1, 3, 1, 3, 3, 1, 1, 1, 1, 1, 1,},//35
//            {1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 1, 1, 1, 1, 3, 1, 1, 1, 3, 3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 3, 1, 3, 1, 1, 1, 1, 1,},//36
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mChooseview = (SeatChooseView) findViewById(R.id.view_choose);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);

        EgSeatMap egMap = new EgSeatMap(mSeatMap);
        mChooseview.setISeatChooseEvent(this);
        mSeatDataHandle = mChooseview.getSeatDrawInterface();
        mSeatDataHandle.setIsShowLog(true, null);


        IGlobleParams globleParams = mSeatDataHandle.getGlobleParams();
        //绘制缩略图
        globleParams.setIsDrawThumbnail(true);
        //选中位置时绘制提醒条
//        globleParams.setIsDrawSeletedRowColumnNotification(true, globleParams.createNotificationFormat(true, "第", IGlobleParams.FORMAT_STR, "行/第", IGlobleParams.FORMAT_STR, "列"));
        //绘制行号
        globleParams.setIsDrawRowNumber(true);
        //设置样本类型绘制行数
        globleParams.setSeatTypeRowCount(2);
        //设置是否总是显示缩略图(即使在没有任何操作的情况下)
        globleParams.setIsShowThumbnailAlways(true);
        //设置是否可以通过单击缩略图快速定位到对应的位置
        globleParams.setIsEnabledQuickShowByClickOnThumbnail(true);
        //设置是否绘制列号
        globleParams.setIsDrawColumnNumber(true);

        ISeatParams seatParams = mSeatDataHandle.getSeatParams();
        //设置是否绘制样本类型
        seatParams.setIsDrawSampleStyle(true);
        //设置单个座位宽度
        seatParams.setWidth(40f);
        //设置单个座位高度
        seatParams.setHeight(40f);

        IStageParams stageParams = mSeatDataHandle.getStageParams();
        stageParams.setStageDescription("CrazyTaro的舞台 6号厅");

        mMap=egMap;
        mSeatDataHandle.setSeatDrawMap(mMap);

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              List<AbsSeatEntity> selectSeats = mChooseview.getSeletedSeats();
                                              String[] edgeTags = new String[]{BaseSeatParams.TAG_UNSHOW_SEAT, BaseSeatParams.TAG_ERROR_SEAT, BaseSeatParams.TAG_LOCK_SEAT};
                                              String[] enabledTags = new String[]{BaseSeatParams.TAG_OPTIONAL_SEAT, BaseSeatParams.TAG_COUPLE_OPTIONAL_SEAT};
                                              if (SeatCheckRuleUtils.isIllegalSeatList(selectSeats, mMap, edgeTags, enabledTags)) {
                                                  Toast.makeText(MainActivity.this, "不能留下单个座位!", Toast.LENGTH_SHORT).show();
                                              } else {
                                                  Toast.makeText(MainActivity.this, "所有座位有效~~~", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      }

        );
    }

    private void setImage(ISeatParams params) {
        params.getDrawStyle(BaseSeatParams.TAG_LOCK_SEAT).imageID = R.drawable.zuowei_disabled;
        params.getDrawStyle(BaseSeatParams.TAG_COUPLE_OPTIONAL_SEAT).imageID = R.drawable.zuowei_lovers;
        params.getDrawStyle(BaseSeatParams.TAG_OPTIONAL_SEAT).imageID = R.drawable.zuowei_optional;
        params.getDrawStyle(BaseSeatParams.TAG_SELECTE_SEAT).imageID = R.drawable.zuowei_selected;

        params.setDrawType(ISeatParams.DRAW_TYPE_IMAGE);
    }


    @Override
    public void clickSeatSuccess(AbsSeatEntity seatEntity) {
    }

    @Override
    public void seatStatusChanged(AbsSeatEntity[] seatEntities, boolean isChosen) {
        if (isChosen && seatEntities != null) {

        }
    }

    @Override
    public void seletedFail(boolean isCoupleSeat) {
        if (isCoupleSeat) {
            Toast.makeText(this, "情侣选座失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "选座失败", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void seletedFull(boolean isCoupleSeat) {
        if (isCoupleSeat) {
            Toast.makeText(this, "情侣座满", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "普通座满", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void scaleMaximum() {

    }
}
