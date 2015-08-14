package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.example.xuhaolin.seatchoose.R;

/**
 * Author:
 * Description:
 */
public class SeatChooseView extends View {
    private SeatDrawUtils mSeatDrawUtils = null;
    private int[][] mSeatMap = {
            {3, 1, 0, 2, 1, 3, 0, 2, 1, 1, 2, 0, 3, 2, 1, 3, 0, 2,},
            {3, 1, 1, 2, 0, 3, 0, 2, 1, 1, 3, 0, 2, 2, 1, 3, 0, 2,},
            {3, 0, 2, 1, 1, 3, 1, 0, 2, 1, 2, 2, 1, 3, 0, 2, 0, 3,},
            {2, 3, 1, 2, 0, 3, 0, 2, 1, 1, 2, 0, 2, 1, 3, 0, 2, 3,},
            {3, 1, 0, 2, 1, 3, 0, 2, 1, 1, 3, 2, 1, 3, 0, 2, 0, 2,}
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
//        stage.setStageDrawType(StageParams.STAGE_DRAW_TYPE_NO);
        mSeatDrawUtils.setStageParams(stage);

        SeatParams seat = SeatParams.getInstance();
        seat.setSeatHeight(50f);
        seat.setSeatWidth(50f);
//        seat.setIsDrawSeatType(false);
        seat.setSeatImage(new int[]{R.drawable.icon_logo_main, R.drawable.icon_logo_pkq, R.drawable.icon_logo_alpaca});
        mSeatDrawUtils.setSeatParams(seat);

        mSeatDrawUtils.setSeatDrawMap(mSeatMap);
        mSeatDrawUtils.setIsShowLog(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSeatDrawUtils.onDraw(canvas);
    }

}
