package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:
 * Description:
 */
public class SeatChooseView extends View {
    private SeatDrawUtils mSeatDrawUtils = null;

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
        SeatParams params = SeatParams.getInstance();
        params.setSeatHeight(50f);
        params.setSeatWidth(60f);
        mSeatDrawUtils.setSeatParams(params);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mSeatDrawUtils.onDraw(canvas);
    }

}
