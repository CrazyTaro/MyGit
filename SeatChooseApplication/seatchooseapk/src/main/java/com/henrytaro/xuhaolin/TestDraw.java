package com.henrytaro.xuhaolin;/**
 * Created by xuhaolin on 15/9/25.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import us.bestapp.henrytaro.seatchoose.draw.utils.AbsTouchEventHandle;
import us.bestapp.henrytaro.seatchoose.utils.TouchUtils;


/**
 * Created by xuhaolin on 15/9/25.
 */
public class TestDraw extends AbsTouchEventHandle implements TouchUtils.IScaleEvent, TouchUtils.IMoveEvent {
    TouchUtils mTouch = new TouchUtils();
    View mDrawView = null;
    Context mContext = null;
    Paint mPaint = new Paint();
    float mRadius = 200;
    float mTempRadius = mRadius;

    public TestDraw(View drawView, Context context) {
        this.mDrawView = drawView;
        this.mContext = context;
        mTouch.setMoveEvent(this);
        mTouch.setScaleEvent(this);
        this.mDrawView.setOnTouchListener(this);
    }

    public void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mTouch.getOffsetX() + 300, mTouch.getOffsetY() + 300, mRadius, mPaint);
    }

    @Override
    public void onSingleTouchEventHandle(MotionEvent event, int extraMotionEvent) {
        mTouch.singleTouchEvent(event, extraMotionEvent);
    }

    @Override
    public void onMultiTouchEventHandle(MotionEvent event, int extraMotionEvent) {
        mTouch.multiTouchEvent(event, extraMotionEvent);
    }

    @Override
    public void onSingleClickByTime(MotionEvent event) {

    }

    @Override
    public void onSingleClickByDistance(MotionEvent event) {

    }

    @Override
    public void onDoubleClickByTime() {

    }

    @Override
    public void onDoubleClickByDistance() {

    }

    @Override
    public boolean isCanMovedOnX(float moveDistanceX, float newOffsetX) {
        return true;
    }

    @Override
    public boolean isCanMovedOnY(float moveDistacneY, float newOffsetY) {
        return true;
    }

    @Override
    public void onMove(int suggestEventAction) {
        mDrawView.postInvalidate();
    }

    @Override
    public void onMoveFail(int suggetEventAction) {

    }

    @Override
    public boolean isCanScale(float newScaleRate) {
        return true;
    }

    @Override
    public void setScaleRate(float newScaleRate, boolean isNeedStoreValue) {
        mRadius = mTempRadius * newScaleRate;
        if (isNeedStoreValue) {
            mTempRadius = mRadius;
        }
    }

    @Override
    public void onScale(int suggestEventAction) {
        mDrawView.postInvalidate();
    }

    @Override
    public void onScaleFail(int suggetEventAction) {

    }
}
