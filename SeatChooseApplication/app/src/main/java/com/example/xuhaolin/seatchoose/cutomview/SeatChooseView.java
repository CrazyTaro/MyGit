package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.example.xuhaolin.seatchoose.R;

/**
 * Author:
 * Description:
 */
public class SeatChooseView extends View implements SeatDrawUtils.ISeatInformationListener {
    private SeatDrawUtils mSeatDrawUtils = null;
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
    };

    public SeatChooseView(Context context) {
        super(context);
        initial();
    }

    public SeatChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public void initial() {
        mSeatDrawUtils = SeatDrawUtils.getInstance(getContext(), this);
        StageParams stage = StageParams.getInstance();
        stage.setStageImage(R.drawable.icon_bg);
        stage.setStageTextColor(Color.WHITE);
        stage.setStageDrawType(StageParams.STAGE_DRAW_TYPE_DEFAULT);
        stage.setIsDrawStage(false);
        mSeatDrawUtils.setStageParams(stage);

        SeatParams seat = SeatParams.getInstance();
        seat.setSeatHeight(50f);
        seat.setSeatWidth(50f);
        seat.setSeatImage(new int[]{R.drawable.icon_logo_main, R.drawable.icon_logo_pkq, R.drawable.icon_logo_alpaca});
        seat.setSeatDrawType(SeatParams.SEAT_DRAW_TYPE_DEFAULT);
//        seat.setIsDrawSeatType(false);
        mSeatDrawUtils.setSeatParams(seat);

//        mSeatDrawUtils.setIsShowThumbnailAlways(false);
        mSeatDrawUtils.setSeatDrawMap(mSeatMap);
        mSeatDrawUtils.setIsShowLog(true);
        mSeatDrawUtils.setSeatInformationListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSeatDrawUtils.onDraw(canvas);
    }


    @Override
    public void seatStatus(int status) {
    }

    @Override
    public void chooseSeatSuccess(int rowIndex, int columnIndex) {
        int seatType = mSeatDrawUtils.getSeatTypeInSeatMap(rowIndex, columnIndex);
        if (seatType == SeatParams.SEAT_TYPE_UNSHOW || seatType == SeatParams.SEAT_TYPE_ERRO) {
            return;
        } else if (seatType == SeatParams.SEAT_TYPE_SELETED) {
            mSeatDrawUtils.updateSeatTypeInMap(SeatParams.SEAT_TYPE_UNSELETED, rowIndex, columnIndex);
        } else if (seatType == SeatParams.SEAT_TYPE_UNSELETED) {
            mSeatDrawUtils.updateSeatTypeInMap(SeatParams.SEAT_TYPE_SELETED, rowIndex, columnIndex);
        }
    }

    @Override
    public void chosseSeatFail() {
        Toast.makeText(getContext(), "没有选中座位", Toast.LENGTH_SHORT).show();
    }
}
