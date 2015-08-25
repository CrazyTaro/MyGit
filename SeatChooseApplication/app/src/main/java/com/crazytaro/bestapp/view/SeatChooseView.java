package com.crazytaro.bestapp.view;/**
 * Created by xuhaolin on 15/8/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.crazytaro.bestapp.draw.params.ExportParams;
import com.crazytaro.bestapp.draw.params.SeatParams;
import com.crazytaro.bestapp.draw.params.StageParams;
import com.crazytaro.bestapp.draw.utils.SeatDrawUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * created by xuhaolin at 2015/08/10
 * <p>可以自定义view并使用已有的绘制参数类进行处理,会更加灵活</p>
 * <p>使用自定义View时需要创建内部对象{@link SeatDrawUtils},此类是处理所有绘制方法的重要类,必须使用该类才能完成绘制功能,
 * 同时需要重写view的onDraw事件,通过调用seatDrawUtil.onDraw()完成绘制.</p>
 * <p>如果需要处理选座事件,请实现接口{@link com.crazytaro.bestapp.draw.utils.SeatDrawUtils.ISeatInformationListener},
 * 并为seatDrawUtil设置该接口对应的监听事件</p>
 * <br/>
 * <p>不需要自定义view实现,仅使用此控件的话,请实现{@link ISeatChooseEvent}接口,以处理此控件事件处理后的回调</p>
 */
public class SeatChooseView extends View implements SeatDrawUtils.ISeatInformationListener, ISeatChoose {
    private SeatDrawUtils mSeatDrawUtils = null;
    private ISeatChooseEvent mSeatChooseEvent = null;
    private int mMostSeletedCount = 5;
    private List<Point> mCurrentSeletedSeats = null;

    public SeatChooseView(Context context) {
        super(context);
        initial();
    }

    public SeatChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial();
    }

    public void initial() {
        mSeatDrawUtils = new SeatDrawUtils(this.getContext(), this);
        ExportParams params = mSeatDrawUtils.getExportParams();
        params.getSeatParams().setDrawType(SeatParams.DRAW_TYPE_DEFAULT);
        params.getStageParams().setDrawType(StageParams.DRAW_TYPE_DEFAULT);

        mSeatDrawUtils.setIsShowLog(false, null);
        mSeatDrawUtils.setSeatInformationListener(this);

        mCurrentSeletedSeats = new ArrayList<Point>(mMostSeletedCount);
    }

    /**
     * 从被选中的座位中将某个座位移除
     *
     * @param rowIndex    行索引
     * @param columnIndex 列索引
     */
    private void removeSeat(int rowIndex, int columnIndex) {
        for (Point seat : mCurrentSeletedSeats) {
            if (seat.x == rowIndex && seat.y == columnIndex) {
                mCurrentSeletedSeats.remove(seat);
                return;
            }
        }
    }

    /**
     * 将选中的座位加入到选中座位列表中
     *
     * @param rowIndex
     * @param columnIndex
     */
    private void addSeat(int rowIndex, int columnIndex) {
        Point seletedSeat = new Point();
        seletedSeat.x = rowIndex;
        seletedSeat.y = columnIndex;
        mCurrentSeletedSeats.add(seletedSeat);
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
        boolean isChoosen = false;
        int seatType = mSeatDrawUtils.getSeatTypeInSeatMap(rowIndex, columnIndex);
        //回调选座结果
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.seatSeleted(rowIndex, columnIndex, seatType);
        }

        if (seatType == SeatParams.SEAT_TYPE_UNSHOW || seatType == SeatParams.SEAT_TYPE_ERRO) {
            //选座出错或者未选中有效区域，选座位失败
            if (mSeatChooseEvent != null) {
                mSeatChooseEvent.seletedFail();
            }
        } else if (seatType == SeatParams.SEAT_TYPE_SELETED) {
            //选座成功，当前座位为选中状态，将状态改为未选中，且从选中座位列表中移除该座位
            mSeatDrawUtils.updateSeatTypeInMap(SeatParams.SEAT_TYPE_UNSELETED, rowIndex, columnIndex);
            removeSeat(rowIndex, columnIndex);
            isChoosen = false;
        } else if (seatType == SeatParams.SEAT_TYPE_UNSELETED) {
            if (mCurrentSeletedSeats.size() < mMostSeletedCount) {
                //选座成功，当前座位为未选中状态，选中该座位并将座位加入选中座位列表中
                mSeatDrawUtils.updateSeatTypeInMap(SeatParams.SEAT_TYPE_SELETED, rowIndex, columnIndex);
                addSeat(rowIndex, columnIndex);
                isChoosen = true;
            } else {
                //当前选中座位数已达上限，回调接口，不将座位加入选中列表中
                if (mSeatChooseEvent != null) {
                    mSeatChooseEvent.seletedFull();
                }
                return;
            }
        }
        if (mSeatChooseEvent != null) {
            //若座位有效且未满，则回调选中座位结果
            mSeatChooseEvent.seatSeleted(rowIndex, columnIndex, isChoosen);
        }
    }

    @Override
    public void chosseSeatFail() {
        Toast.makeText(getContext(), "没有选中座位", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scaleMaximum() {
        Toast.makeText(getContext(), "已缩放到极限值", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resetParams() {
        mSeatDrawUtils.resetParams(null);
    }

    @Override
    public void setSeatMap(int[][] seatMap) {
        mSeatDrawUtils.setSeatDrawMap(seatMap);
    }

    @Override
    public boolean updateSeatTypeInMap(int seatType, int rowIndex, int columnIndex) {
        return mSeatDrawUtils.updateSeatTypeInMap(seatType, rowIndex, columnIndex);
    }

    @Override
    public void setParams(ExportParams params) {
        mSeatDrawUtils.resetParams(params);
    }

    @Override
    public ExportParams getParams() {
        return mSeatDrawUtils.getExportParams();
    }

    @Override
    /**
     * 默认最多选择的个数为5，当参数不合法时该重置为默认值
     */
    public void setMostSeletedCount(int mostCount) {
        mMostSeletedCount = mostCount > 0 ? mostCount : 5;
    }

    @Override
    public List<Point> getSeletedSeats() {
        List<Point> seletedSeatsList = new ArrayList<Point>(mCurrentSeletedSeats.size());
        seletedSeatsList.addAll(mCurrentSeletedSeats);
        return seletedSeatsList;
    }

    @Override
    public void setIsShowThumbnailAlways(boolean isShowAlways) {
        mSeatDrawUtils.setIsShowThumbnailAlways(isShowAlways);
    }

    @Override
    public void setIsDrawThumbnail(boolean isDraw) {
        mSeatDrawUtils.setIsDrawThumbnail(isDraw);
    }

    @Override
    public void setISeatChooseEvent(ISeatChooseEvent eventListener) {
        mSeatChooseEvent = eventListener;
    }

    @Override
    public void setIsShowLog(boolean isShow, String tag) {
        mSeatDrawUtils.setIsShowLog(isShow, tag);
    }
}
