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
            {3, 1, 0, 0, 1, 3, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//1
            {3, 1, 0, 0, 2, 3, 0, 2, 1, 1, 3, 0, 0, 2, 1, 0, 0, 2,},//2
            {3, 2, 0, 0, 0, 0, 0, 3, 2, 1, 2, 2, 1, 3, 0, 0, 0, 3,},//3
            {2, 3, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 3,},//4
            {3, 1, 0, 0, 0, 0, 0, 2, 1, 1, 3, 2, 1, 3, 0, 0, 0, 2,},//5
            {3, 1, 2, 1, 1, 3, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//6
            {3, 1, 1, 2, 2, 3, 0, 2, 1, 1, 3, 0, 0, 2, 1, 0, 0, 2,},//7
            {3, 2, 2, 1, 0, 0, 0, 3, 2, 1, 2, 2, 1, 3, 0, 0, 0, 3,},//8
            {2, 3, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 3,},//9
            {3, 1, 0, 0, 0, 0, 0, 2, 1, 1, 3, 2, 1, 3, 0, 0, 0, 2,},//10
            {3, 1, 0, 0, 1, 3, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//11
            {3, 1, 0, 0, 2, 3, 0, 2, 1, 1, 3, 0, 0, 2, 1, 0, 0, 2,},//12
            {3, 2, 0, 0, 0, 0, 0, 3, 2, 1, 2, 2, 1, 3, 0, 0, 0, 3,},//13
            {2, 3, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 3,},//14
            {3, 1, 0, 0, 0, 0, 0, 2, 1, 1, 3, 2, 1, 3, 0, 0, 0, 2,},//15
            {0, 0, 2, 1, 1, 3, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//16
            {0, 0, 2, 1, 2, 3, 0, 2, 1, 1, 3, 0, 0, 2, 1, 0, 0, 2,},//17
            {3, 2, 0, 0, 0, 0, 0, 3, 2, 1, 2, 2, 1, 3, 0, 0, 0, 3,},//18
            {2, 3, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 3,},//19
            {3, 1, 0, 0, 0, 0, 0, 2, 1, 1, 3, 2, 1, 3, 0, 0, 0, 2,},//20
            {3, 0, 0, 2, 1, 3, 0, 2, 1, 1, 2, 0, 0, 2, 1, 0, 0, 2,},//21
            {3, 0, 0, 2, 1, 3, 0, 2, 1, 1, 3, 0, 0, 2, 1, 0, 0, 2,},//22
            {3, 2, 0, 0, 0, 0, 0, 3, 2, 1, 2, 2, 1, 3, 0, 0, 0, 3,},//23
            {2, 3, 0, 0, 0, 0, 0, 2, 1, 1, 2, 1, 2, 1, 0, 0, 0, 3,},//24
            {3, 1, 0, 0, 0, 0, 0, 2, 1, 1, 3, 2, 1, 3, 0, 0, 0, 2,},//25
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
        mSeatDrawUtils.setStageParams(stage);

        SeatParams seat = SeatParams.getInstance();
        seat.setSeatHeight(50f);
        seat.setSeatWidth(50f);
        seat.setSeatImage(new int[]{R.drawable.icon_logo_main, R.drawable.icon_logo_pkq, R.drawable.icon_logo_alpaca});
        seat.setSeatDrawType(SeatParams.SEAT_DRAW_TYPE_DEFAULT);
        mSeatDrawUtils.setSeatParams(seat);

//        mSeatDrawUtils.setOriginalOffset(-500,-500);
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
