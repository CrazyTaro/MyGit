package com.henrytaro.xuhaolin;/**
 * Created by xuhaolin on 15/9/25.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xuhaolin on 15/9/25.
 */
public class TestView extends View {
    TestDraw mTestDraw = new TestDraw(this, getContext());
    ;

    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        mTestDraw.onDraw(canvas);
    }
}
