package com.example.xuhaolin.seatchoose.cutomview;/**
 * Created by xuhaolin on 15/8/14.
 */

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 抽像类,处理触摸事件,区分单击及多点触摸事件
 */
public abstract class AbsTouchEvent implements View.OnTouchListener {
    /**
     * 额外分配的触摸事件,用于建议优先处理的触摸事件
     */
    public static final int MOTION_EVENT_NOTHING = -1;

    private boolean mIsShowLog = false;
    //多点触摸按下
    private boolean mIsMultiDown = false;
    //多点触摸事件开始处理
    private boolean mIsMultiPoint = false;
    //是否进入单击移动事件
    private boolean mIsSingleMove = false;
    //是否进入移动事件
    private boolean mIsInMotionMove = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //进入单点单击处理
                singleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                showMsg("单击 down ");
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //若在确认进入多点单击之前没有任何移动操作
                //则认为是触发多点单击事件
                if (!mIsInMotionMove) {
                    //开始多点单击事件
                    mIsMultiPoint = true;
                    showMsg("多点触控 down");
                    doubleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                }
                mIsMultiDown = true;
                break;
            case MotionEvent.ACTION_UP:
                //不行进入多点触发事件,同时单点移动也不允许任何的多点触摸事件
                //这种情况是为了避免有可能有用户单击移动之后再进行多点触控,这种情况无法处理为用户需要移动还是需要缩放

                //在处理单击事件up中,任何时候只要在结束up之前产生任何的多点触控,都不将此次的事件处理为单击up
                //因为这时候单击事件已经不完整了,混合了其它的事件,也无法分辨是否需要处理单击或者多点触控
                if (!mIsMultiPoint && !mIsMultiDown) {
                    if (mIsSingleMove) {
                        showMsg("单击 up");
                        singleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                    } else {
                        showMsg("单击 up 不处理");
                    }
                }
                //取消移动状态的记录
                mIsInMotionMove = false;
                //多点单击的标志必须在此处才可以被重置
                //因为多点单击的抬起事件优先处理于单击的抬起事件
                //如果在多点单击的抬起事件时重置该变量则会导致上面的判断百分百是成立的
                mIsMultiPoint = false;
                mIsMultiDown = false;
                mIsSingleMove = false;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //当确认进入多点单击状态,则执行多点单击抬起事件
                if (mIsMultiPoint) {
                    showMsg("多点触控 up");
                    doubleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                }
                //此处不重置mIsMultiDown变量是因为后面检测单击事件的up与多点触控的up需要
                //而且此处不重置并不会对其它的部分造成影响
                break;
            case MotionEvent.ACTION_MOVE:
                //进入移动状态
                mIsInMotionMove = true;
                //当前不是多点单击状态,则进行移动操作
                //不行进入多点触发事件,同时单点移动也不允许任何的多点触摸事件
                //这种情况是为了避免有可能有用户单击移动之后再进行多点触控,这种情况无法处理为用户需要移动还是需要缩放
                if (!mIsMultiPoint && !mIsMultiDown) {
                    showMsg("单击 move");
                    singleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                    mIsSingleMove = true;
                } else if (mIsSingleMove && mIsMultiDown) {
                    //当前用户已经移动了界面并且又增加了触控点
                    //此时按当前的位置处理移动完的界面(即设为在此时结束移动操作),且不进行后续任何操作
                    showMsg("单击 move 结束");
                    singleTouchEventHandle(event, MotionEvent.ACTION_UP);
                    mIsSingleMove = false;
                } else if (mIsMultiPoint && mIsMultiDown) {
                    showMsg("多点触控 move");
                    doubleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                }
                break;
        }
        return true;
    }

    public void setIsShowLog(boolean isShowLog) {
        this.mIsShowLog = isShowLog;
    }

    private void showMsg(String msg) {
        if (mIsShowLog) {
            Log.i("touch event ", msg);
        }
    }

    /**
     * 单点触摸事件处理
     *
     * @param event            单点触摸事件
     * @param extraMotionEvent 建议处理的额外事件,如果不需要进行额外处理则该参数值为{@link #MOTION_EVENT_NOTHING}
     *                         <p>存在此参数是因为可能用户进行单点触摸并移动之后,会再进行多点触摸(此时并没有松开触摸),在这种情况下是无法分辨需要处理的是单点触摸事件还是多点触摸事件.
     *                         <font color="yellow"><b>此时会传递此参数值为单点触摸的{@link MotionEvent#ACTION_UP},建议按抬起事件处理并结束事件</b></font></p>
     */
    public abstract void singleTouchEventHandle(MotionEvent event, int extraMotionEvent);

    /**
     * 多点触摸事件处理(两点触摸,暂没有做其它任何多点触摸)
     *
     * @param event            多点触摸事件
     * @param extraMotionEvent 建议处理的额外事件,如果不需要进行额外处理则该参数值为{@link #MOTION_EVENT_NOTHING}
     */
    public abstract void doubleTouchEventHandle(MotionEvent event, int extraMotionEvent);
}
