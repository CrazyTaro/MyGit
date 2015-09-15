package us.bestapp.henrytaro.draw.utils;/**
 * Created by xuhaolin on 15/8/6.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.View;

import us.bestapp.henrytaro.entity.absentity.AbsSeatEntity;
import us.bestapp.henrytaro.params.GlobleParams;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.StageParams;
import us.bestapp.henrytaro.params.baseparams.BaseGlobleParams;
import us.bestapp.henrytaro.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.params.baseparams.BaseStageParams;

/**
 * Created by xuhaolin on 15/8/6.<br/>
 * 默认实现的简单绘制工具类,可直接从此类扩展,若需要自定义则从基类扩展{@link AbsDrawUtils}
 */
public class SimpleDrawUtils extends AbsDrawUtils {
    private SeatParams mSeatParams = null;
    private StageParams mStageParams = null;
    //主座位,两者结合让座位看起来比较好看而已...
    private RectF mMainSeatRectf = null;
    //次座位,两者结合让座位看起来比较好看而已...
    private RectF mMinorSeatRectf = null;

    public SimpleDrawUtils(Context context, View drawView) {
        super(context, drawView);
    }


    @Override
    protected void initial(BaseSeatParams seat, BaseStageParams stage, BaseGlobleParams globle) {
        super.initial(seat, stage, globle);
        mSeatParams = new SeatParams();
        mStageParams = new StageParams();

        this.setParams(mSeatParams, mStageParams, globle);
        //创建主座位与次要座位
        //此部分仅仅是让座位看起来比较好看而已....
        mMainSeatRectf = new RectF();
        mMinorSeatRectf = new RectF();
    }

    @Override
    protected void drawNormalSingleSeat(Canvas canvas, Paint paint, BaseSeatParams seatParams, float drawPositionX, float drawPositionY, int seatType) {
        SeatParams extendSeatParams = (SeatParams) seatParams;

        int seatColor = seatParams.getSeatColorByType(seatType);
        //设置绘制座位的颜色
        seatParams.setColor(seatColor);

        //座位的占拒的总高度(两部分座位)
        mMainSeatRectf = extendSeatParams.getSeatDrawDefaultRectf(mMainSeatRectf, drawPositionX, drawPositionY, true);
        mMinorSeatRectf = extendSeatParams.getSeatDrawDefaultRectf(mMinorSeatRectf, drawPositionX, drawPositionY, false);

        //填充座位并绘制
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(seatParams.getColor());


        //默认的绘制方式
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (isRectfCanSeen(mMainSeatRectf)) {
            canvas.drawRoundRect(mMainSeatRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
        //当该绘制图像有显示在画布上的部分时才进行绘制
        //所有的坐标都不在画布的有效显示范围则不进行绘制
        if (isRectfCanSeen(mMainSeatRectf)) {
            canvas.drawRoundRect(mMinorSeatRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
    }

    @Override
    protected void drawThumbnailSeat(Canvas canvas, Paint paint, BaseSeatParams seatParams, float drawPositionX, float drawPositionY, int seatType) {
//        super.drawThumbnailSeat(canvas, paint, seatParams, drawPositionX, drawPositionY, seatType);
        //绘制单个座位
        //若当前的绘制类型是缩略图,则只绘制区域内的小方块作为座位显示(由于缩略图很小,没必要绘制很复杂,反正也看不清楚...)
        //获取座位类型对应的颜色
        int seatColor = seatParams.getSeatColorByType(seatType);
        //设置绘制座位的颜色
        seatParams.setColor(seatColor);

        //获取绘制的区域
        mImageRectf = seatParams.getNormalDrawRecf(mImageRectf, drawPositionX, drawPositionY);
        paint.setStyle(Paint.Style.FILL);
        //填充颜色
        paint.setColor(seatParams.getColor());
        if (isRectfCanSeen(mImageRectf)) {
            //绘制圆角矩形
            canvas.drawRoundRect(mImageRectf, seatParams.getRadius(), seatParams.getRadius(), paint);
        }
    }

    @Override
    protected void drawNormalStage(Canvas canvas, Paint paint, BaseStageParams stageParams, float drawPositionX, float drawPositionY) {
        float textBeginDrawX = 0f;
        float textBeginDrawY = 0f;
        float textLength = 0f;
        //设置文字大小为舞台高度小一点(保证文字可以显示在舞台范围内)
        float textSize = stageParams.getDescriptionSize();


        //缩略图的绘制也是使用默认的绘制方式,图片绘制到很小的的时候完全看不清楚而已很难分辨
        //没有绘制过图片舞台,尝试默认绘制方式
        //绘制默认舞台
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(stageParams.getColor());
        //不规则舞台绘制
        Path stagePath = stageParams.getStageDrawPath(drawPositionX, drawPositionY, true);
        canvas.drawPath(stagePath, paint);

        //绘制舞台文字
        String stageText = stageParams.getStageDescription();
        if (stageText != null) {
            paint.setTextSize(textSize);
            paint.setColor(stageParams.getDescriptionColor());
            paint.setStyle(Paint.Style.FILL);

            textLength = paint.measureText(stageText);
            textBeginDrawX = drawPositionX - textLength / 2;
            textBeginDrawY = drawPositionY + textSize / 3;
            //文字绘制不做限制
            //文字本身绘制范围小,另外需要重新计算两个参数才能进行判断
            canvas.drawText(stageText, textBeginDrawX, textBeginDrawY, paint);
        }
    }

    @Override
    protected Point updateNotifyRowWithColumn(Point notifyPoint, AbsSeatEntity seatEntity) {
        notifyPoint.x = seatEntity.getRowNumber();
        notifyPoint.y = seatEntity.getColumnNumber();
        return notifyPoint;
    }

    @Override
    public void resetParams() {
        this.setParams(new SeatParams(), new StageParams(), new GlobleParams());
    }
}
