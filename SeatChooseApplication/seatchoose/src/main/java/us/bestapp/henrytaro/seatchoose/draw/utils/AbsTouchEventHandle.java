package us.bestapp.henrytaro.seatchoose.draw.utils;/**
 * Created by xuhaolin on 15/8/14.
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xuhaolin in 2015-08-14
 * <p>抽像类,处理触摸事件,区分单击及多点触摸事件</p>
 * <p>此类中使用到handler,请确保使用在UI线程或者是自定义looper的线程中(一般也没有人会把触摸事件放在非UI线程吧 =_=)</p>
 */
public abstract class AbsTouchEventHandle implements View.OnTouchListener {
    /**
     * 距离双击事件
     */
    public static final int EVENT_DOUBLE_CLICK_DISTANCE = 0;


    /**
     * 额外分配的触摸事件,用于建议优先处理的触摸事件
     */
    public static final int MOTION_EVENT_NOTHING = 0;
    /**
     * 处理距离单击事件
     */
    private static final int HANDLE_SINGLE_CLICK_AT_DISTANCE = 1;
    /**
     * 处理时间单击事件
     */
    private static final int HANDLE_SINGLE_CLICK_AT_TIME = -1;
    /**
     * 处理时间按下事件
     */
    private static final int HANDLE_SINGLE_DOWN_AT_TIME = -2;
    private static String TAG = "touch_event";

    //    private ITouchEventListener mItouchEventListener = null;
    //已经触发单击事件的情况下,是否触发单点触摸事件
    private boolean mIsTriggerSingleTouchEvent = true;
    private boolean mIsShowLog = false;
    //多点触摸按下
    private boolean mIsMultiDown = false;
    //多点触摸事件开始处理
    private boolean mIsMultiPoint = false;
    //是否进入单击移动事件
    private boolean mIsSingleMove = false;
    //是否进入移动事件
    private boolean mIsInMotionMove = false;
    //是否单击按下
    private boolean mIsSingleDownAtTime = false;
    //是否完成单击(一次)
    private boolean mIsSingleClickAtTime = false;
    private boolean mIsSingleClickAtDistance = false;

    private float mDownX = 0f;
    private float mDownY = 0f;
    private float mUpX = 0f;
    private float mUpY = 0f;

    private Handler mHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLE_SINGLE_CLICK_AT_TIME:
                    //取消双击事件的标志
                    mIsSingleClickAtTime = false;
                    break;
                case HANDLE_SINGLE_CLICK_AT_DISTANCE:
                    mIsSingleClickAtDistance = false;
                    break;
                case HANDLE_SINGLE_DOWN_AT_TIME:
                    //取消单击事件的标志
                    mIsSingleDownAtTime = false;
                    break;
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                //进入单点单击处理
                showMsg("单击 down ");
                mIsSingleDownAtTime = true;
                mHandle.sendEmptyMessageDelayed(HANDLE_SINGLE_DOWN_AT_TIME, 250);

                mDownX = event.getX();
                mDownY = event.getY();
                this.onSingleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                //开始多点单击事件
                mIsMultiPoint = true;
                showMsg("多点触控 down");
                mIsMultiDown = true;
                this.onMultiTouchEventHandle(event, MOTION_EVENT_NOTHING);
                break;
            case MotionEvent.ACTION_UP:
                //移动距离单击处理事件
                //触摸点down事件的坐标与up事件的坐标距离不超过10像素时,认为一次单击事件(与时间无关)
                //两次单击事件之间的时间间隔在500ms内则认为是一次双击事件
                if (!mIsMultiDown) {
                    mUpX = event.getX();
                    mUpY = event.getY();
                    float moveDistanceX = mUpX - mDownX;
                    float moveDistanceY = mUpY - mDownY;
                    //根据触摸点up与down事件的坐标差判断是否为单击事件(不由时间决定)
                    if (Math.abs(moveDistanceX) < 10 && Math.abs(moveDistanceY) < 10) {
                        if (mIsSingleClickAtDistance) {
                            mHandle.removeMessages(HANDLE_SINGLE_CLICK_AT_DISTANCE);
                            showMsg("双击事件(距离) double");
                            this.onDoubleClickByDistance();
                            break;
                        } else {
                            showMsg("单击事件(距离) single");
                            mIsSingleClickAtDistance = true;
                            mHandle.sendEmptyMessageDelayed(HANDLE_SINGLE_CLICK_AT_DISTANCE, 250);
                            mHandle.removeMessages(HANDLE_SINGLE_DOWN_AT_TIME);
                            this.onSingleClickByDistance(event);
                        }
                    }
                }


                //根据时间处理单击事件
                //触摸点down之后500ms内触摸点抬起则认为是一次单击事件
                //两次单击事件之间的时间间隔在500ms内即为一次双击事件
                if (!mIsMultiDown) {
                    if (mIsSingleDownAtTime) {
                        //单击事件处理
                        if (mIsSingleClickAtTime) {
                            //双击事件处理完直接退出,不进行下面的任何事件处理
                            mHandle.removeMessages(HANDLE_SINGLE_CLICK_AT_TIME);
                            showMsg("双击事件 double");
                            this.onDoubleClickByTime();
                        } else {
                            showMsg("单击事件 single");
                            mIsSingleClickAtTime = true;
                            mHandle.sendEmptyMessageDelayed(HANDLE_SINGLE_CLICK_AT_TIME, 250);
                            mHandle.removeMessages(HANDLE_SINGLE_DOWN_AT_TIME);
                            this.onSingleClickByTime(event);
                        }

                    }
                }

                //不触发单点触摸事件,则处理为单击事件
                if (!mIsTriggerSingleTouchEvent) {
                    //取消移动状态的记录
                    mIsInMotionMove = false;
                    //多点单击的标志必须在此处才可以被重置
                    //因为多点单击的抬起事件优先处理于单击的抬起事件
                    //如果在多点单击的抬起事件时重置该变量则会导致上面的判断百分百是成立的
                    mIsMultiPoint = false;
                    mIsMultiDown = false;
                    mIsSingleMove = false;
                    break;
                }

                //不进入多点触发事件,同时单点移动也不允许任何的多点触摸事件
                //这种情况是为了避免有可能有用户单击移动之后再进行多点触控,这种情况无法处理为用户需要移动还是需要缩放

                //在处理单击事件up中,任何时候只要在结束up之前产生任何的多点触控,都不将此次的事件处理为单击up
                //因为这时候单击事件已经不完整了,混合了其它的事件,也无法分辨是否需要处理单击或者多点触控
                if (!mIsMultiPoint && !mIsMultiDown) {
                    //此处分为两种情况
                    //一种是未进行任何多点触摸状态的,那么必定为单点触摸,事件必须响应
                    //在事件响应处两个判断条件是:1.用户快速单击,不产生move事件;
                    if (!mIsInMotionMove
                            //2. 用户慢速单击, 产生了move事件但仍没有造成多点触摸事件
                            || (mIsInMotionMove && mIsSingleMove)) {
                        showMsg("单击 up");
                        this.onSingleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                    } else {
                        //一种是进行了多点触摸,且在多点触摸之间保证着单点触摸的状态,此时以多点触摸按下的时刻处理掉单点触摸事件(即在move中已经按up处理掉事件了)
                        //则在完成所有事件之后的up中将不再处理该事件,即下面的"不处理"
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
                    this.onMultiTouchEventHandle(event, MOTION_EVENT_NOTHING);
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
                    this.onSingleTouchEventHandle(event, MOTION_EVENT_NOTHING);
                    mIsSingleMove = true;
                } else if (mIsSingleMove && mIsMultiDown) {
                    //当前用户已经移动了界面并且又增加了触控点
                    //此时按当前的位置处理移动完的界面(即设为在此时结束移动操作)
                    showMsg("单击 move 结束");
                    this.onSingleTouchEventHandle(event, MotionEvent.ACTION_UP);
                    showMsg("多点触控 move 尝试进行");
                    //同时处理多点操作
                    this.onMultiTouchEventHandle(event, MOTION_EVENT_NOTHING);
                    mIsSingleMove = false;
                } else if (mIsMultiPoint && mIsMultiDown) {
                    //正常直接多点操作
                    showMsg("多点触控 move");
                    this.onMultiTouchEventHandle(event, MOTION_EVENT_NOTHING);
                }


                float moveDistanceX = event.getX() - mUpX;
                float moveDistanceY = event.getY() - mUpY;
                if (Math.abs(moveDistanceX) > 20 || Math.abs(moveDistanceY) > 20) {
                    mIsSingleClickAtDistance = false;
                }
                break;
        }
        return true;
    }

    /**
     * 设置在触发单击事件时是否同时触发单点触摸事件;默认触发
     * <p>单击事件本身属于单点触摸事件之中的一种,只是触摸时间在500ms以内则认为是单击事件,但同时是满足触发单点触摸事件的(此处仅指up事件)</p>
     *
     * @param isTrigger true为同时触发,false为忽略单点触摸事件
     */
    public void setIsTriggerSingleTouchEvent(boolean isTrigger) {
        this.mIsTriggerSingleTouchEvent = isTrigger;
    }

    /**
     * 设置是否显示log
     *
     * @param isShowLog
     * @param tag       tag为显示log的标志,可为null,tag为null时使用默认标志"touch_event"
     */
    public void setIsShowLog(boolean isShowLog, String tag) {
        if (tag != null) {
            TAG = tag;
        } else {
            TAG = "touch_event";
        }
        this.mIsShowLog = isShowLog;
    }

    /**
     * 打印默认的log,默认标志为:touch_event
     *
     * @param msg 打印消息
     */
    public void showMsg(String msg) {
        if (mIsShowLog) {
            Log.i(TAG, msg);
        }
    }

    /**
     * 打印log
     *
     * @param tag 标志tag
     * @param msg 打印信息
     */
    public void showMsg(String tag, String msg) {
        if (mIsShowLog) {
            Log.i(tag, msg);
        }
    }

    /**
     * <font color="#ff9900">取消事件有效性,目前仅对距离双击事件有效{@link #onDoubleClickByDistance()}</font><br/>
     * <p>这是由于距离双击事件触发双击的本质是在同一个地方(坐标差距不超过10像素)单击两次即可.<br/>
     * 除了单击本身有时间限制外(500ms内触发down事件并触发up事件才会被判定为一次单击),两次单击之间并没有时间限制,<br/>
     * 只要保证前后两次单击的距离在触发范围内即可触发事件,因此在触发事件后需要通过取消事件从而达到结束双击事件的触发</p>
     *
     * @param event 需要取消的事件
     */
    public void cancelEvent(int event) {
        switch (event) {
            case EVENT_DOUBLE_CLICK_DISTANCE:
                this.mIsSingleClickAtDistance = false;
                break;
        }
    }


    /**
     * 单点触摸事件处理
     *
     * @param event            单点触摸事件
     * @param extraMotionEvent 建议处理的额外事件,如果不需要进行额外处理则该参数值为{@link #MOTION_EVENT_NOTHING}
     *                         <p>存在此参数是因为可能用户进行单点触摸并移动之后,会再进行多点触摸(此时并没有松开触摸),在这种情况下是无法分辨需要处理的是单点触摸事件还是多点触摸事件.
     *                         <font color="#ff9900"><b>此时会传递此参数值为单点触摸的{@link MotionEvent#ACTION_UP},建议按抬起事件处理并结束事件</b></font></p>
     */
    public abstract void onSingleTouchEventHandle(MotionEvent event, int extraMotionEvent);

    /**
     * 多点触摸事件处理(两点触摸,暂没有做其它任何多点触摸)
     *
     * @param event            多点触摸事件
     * @param extraMotionEvent 建议处理的额外事件,如果不需要进行额外处理则该参数值为{@link #MOTION_EVENT_NOTHING}
     */
    public abstract void onMultiTouchEventHandle(MotionEvent event, int extraMotionEvent);

    /**
     * 单击事件处理,由于只要触摸到屏幕且时间足够长,就可以产生move事件,并不一定需要移动触摸才能产生move事件,
     * <font color="#ff9900"><b>所以产生单击事件的同时也会触发up事件{@link #onSingleTouchEventHandle(MotionEvent, int)}</b></font>,
     * <p>单击事件仅仅只能控制触摸时间<font color="#ff9900"><b>少于500ms</b></font>的触摸事件,超过500ms将不会触摸单击事件</p>
     *
     * @param event 单击触摸事件
     */
    public abstract void onSingleClickByTime(MotionEvent event);

    /**
     * 单击事件处理,触摸点down的坐标与up坐标距离差不大于10像素则认为是一次单击,<font color="#ff9900"><b>与时间无关</b></font>
     *
     * @param event 单击触摸事件
     */
    public abstract void onSingleClickByDistance(MotionEvent event);

    /**
     * 双击事件处理,每次单击判断由时间决定,参考{@link #onSingleClickByTime(MotionEvent)}
     */
    public abstract void onDoubleClickByTime();

    /**
     * 双击事件处理,每两次单击事件之间的间隔小于500ms则认为是一次双击事件,单击事件由距离决定而不是时间,参考{@link #onSingleClickByDistance(MotionEvent)}<br/>
     * <font color="#ff9900">处理双击事件后建议调用{@link AbsTouchEventHandle#cancelEvent(int)}来取消双击事件</font>
     */
    public abstract void onDoubleClickByDistance();

}
