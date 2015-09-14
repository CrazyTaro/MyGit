package us.bestapp.henrytaro.view;/**
 * Created by xuhaolin on 15/8/13.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import us.bestapp.henrytaro.draw.interfaces.ISeatDrawInterface;
import us.bestapp.henrytaro.draw.interfaces.ISeatInformationListener;
import us.bestapp.henrytaro.draw.utils.SimpleDrawUtils;
import us.bestapp.henrytaro.entity.interfaces.ISeatEntity;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;
import us.bestapp.henrytaro.view.interfaces.ISeatChooseEvent;
import us.bestapp.henrytaro.view.interfaces.ISeatViewInterface;

/**
 * @author xuhaolin
 * @version 5.0
 *          <p/>
 *          created by xuhaolin at 2015/08/10
 *          <p>可以自定义view并使用已有的绘制参数类进行处理,会更加灵活</p>
 *          <p>使用自定义View时需要创建内部对象{@link SimpleDrawUtils},此类是处理所有绘制方法的重要类,必须使用该类才能完成绘制功能,
 *          同时需要重写view的onDraw事件,通过调用seatDrawUtil.drawCanvas()完成绘制.</p>
 *          <p>如果需要处理选座事件,请实现接口{@link ISeatInformationListener},
 *          并为seatDrawUtil设置该接口对应的监听事件</p>
 *          <br/>
 *          <p>不需要自定义view实现,仅使用此控件的话,请实现{@link ISeatChooseEvent}接口,以处理此控件事件处理后的回调</p>
 *          <p>
 *          <br/>
 *          <br/>
 *          此view初始化情况下使用的参数值全部都是默认值及默认设置<br/>
 *          1.默认不绘制行数/列数<br/>
 *          2.默认绘制缩略图并不保持显示<br/>
 *          3.默认绘制图形界面而不使用图片<br/>
 *          4.默认座位类型只有三种,已选,可选,已售<br/>
 *          5.默认缩略图座位颜色跟随界面座位颜色<br/>
 *          6.默认不提醒当前选中座位行列值<br/>
 *          7.默认可选座位最大值为5<br/>
 */
public class SeatChooseView extends View implements ISeatInformationListener, ISeatViewInterface {
    private ISeatDrawInterface mSeatDrawHandle = null;
    private ISeatChooseEvent mSeatChooseEvent = null;
    private ISeatParams mSeatParams = null;
    private int mMostSeletedCount = 5;
    private List<Point> mCurrentSeletedSeats = null;
    private Context mContext = null;

    public SeatChooseView(Context context) {
        super(context);
        initial(context);
    }

    public SeatChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initial(context);
    }

    //初始化
    private void initial(Context context) {
        mContext = context.getApplicationContext();
        //创建绘制对象
        mSeatDrawHandle = new SimpleDrawUtils(this.getContext(), this);
        mSeatParams = mSeatDrawHandle.getSeatParams();

        //不显示log
        mSeatDrawHandle.setIsShowLog(false, null);
        //设置监听事件
        mSeatDrawHandle.setSeatInformationListener(this);
        //创建选择座位的存储列表
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
        mSeatDrawHandle.drawCanvas(canvas);
    }


    @Override
    public void seatStatus(int status) {
    }

    @Override
    public void chooseInMapSuccess(int rowIndexInMap, int columnIndexInMap, ISeatEntity seatEntity) {
    }


    @Override
    public void chosseInMapFail() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.seletedFail();
        }
    }

    @Override
    public void chooseSeatSuccess(int rowIndexInMap, int columnIndexInMap, int rowNumber, int columnNumber, ISeatEntity seatEntity) {
        //通知选中事件
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.selectedSeatSuccess(rowIndexInMap, columnIndexInMap, rowNumber, columnNumber, seatEntity);
        }

        //是否被选中状态
        boolean isChoosen = false;
        int selectedType = mSeatParams.getSeletedType();
        int unselectedType = mSeatParams.getUnseletedType();

        int seatType = seatEntity.getType();
        if (seatType == selectedType) {
            //选座成功，当前座位为选中状态，将状态改为未选中，且从选中座位列表中移除该座位
            mSeatDrawHandle.updateSeatIMap(unselectedType, rowIndexInMap, columnIndexInMap);
            removeSeat(rowIndexInMap, columnIndexInMap);
            isChoosen = false;
        } else if (seatType == unselectedType) {
            if (mCurrentSeletedSeats.size() < mMostSeletedCount) {
                //选座成功，当前座位为未选中状态，选中该座位并将座位加入选中座位列表中
                mSeatDrawHandle.updateSeatIMap(selectedType, rowIndexInMap, columnIndexInMap);
                addSeat(rowIndexInMap, columnIndexInMap);
                isChoosen = true;
            } else {
                //当前选中座位数已达上限，回调接口，不将座位加入选中列表中
                if (mSeatChooseEvent != null) {
                    mSeatChooseEvent.seletedFull();
                    return;
                }
            }
        }

        if (mSeatChooseEvent != null) {
            //若座位有效且未满，则回调选中座位结果
            mSeatChooseEvent.selectedSeatSuccess(seatEntity.getRowNumber(), seatEntity.getColumnNumber(), isChoosen);
        }
    }

    @Override
    public void chooseSeatFail() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.seletedFail();
        }
    }

    @Override
    public void scaleMaximum() {
        if (mSeatChooseEvent != null) {
            mSeatChooseEvent.scaleMaximum();
        }
    }

    @Override
    public ISeatDrawInterface getSeatDrawInterface() {
        return mSeatDrawHandle;
    }

    /**
     * 默认最多选择的个数为5，当参数不合法时该重置为默认值
     */
    @Override
    public void setMostSeletedCount(int mostCount) {
        mMostSeletedCount = mostCount > 0 ? mostCount : 5;
    }

    @Override
    public List<Point> getSeletedSeats() {
        if (mCurrentSeletedSeats == null || mCurrentSeletedSeats.size() <= 0) {
            return null;
        } else {
            List<Point> seletedSeatsList = new ArrayList<Point>(mCurrentSeletedSeats.size());
            seletedSeatsList.addAll(mCurrentSeletedSeats);
            return seletedSeatsList;
        }
    }

    @Override
    public void setISeatChooseEvent(ISeatChooseEvent eventListener) {
        mSeatChooseEvent = eventListener;
    }
}
