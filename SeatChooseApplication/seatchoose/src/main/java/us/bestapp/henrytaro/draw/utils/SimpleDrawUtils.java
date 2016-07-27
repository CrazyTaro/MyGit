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
import us.bestapp.henrytaro.params.GlobalParams;
import us.bestapp.henrytaro.params.SeatParams;
import us.bestapp.henrytaro.params.StageParams;
import us.bestapp.henrytaro.params.baseparams.BaseDrawStyle;
import us.bestapp.henrytaro.params.baseparams.BaseGlobalParams;
import us.bestapp.henrytaro.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.params.baseparams.BaseStageParams;
import us.bestapp.henrytaro.params.interfaces.IBaseParams;

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
    protected void initial(BaseSeatParams seatParams, BaseStageParams stage, BaseGlobalParams global) {
        mSeatParams = new SeatParams();
        mStageParams = new StageParams();

        this.setParams(mSeatParams, mStageParams, global);
        //创建主座位与次要座位
        //此部分仅仅是让座位看起来比较好看而已....
        mMainSeatRectf = new RectF();
        mMinorSeatRectf = new RectF();
    }

    @Override
    protected void drawNormalSingleSeat(Canvas canvas, Paint paint, BaseSeatParams seatParams, float drawPositionX, float drawPositionY, BaseDrawStyle drawStyle) {
        SeatParams extendSeatParams = (SeatParams) seatParams;
        //设置绘制座位的颜色
        seatParams.setColor(drawStyle.drawColor);

        //座位的占拒的总高度(两部分座位)
        mMainSeatRectf = extendSeatParams.getSeatDrawDefaultRectf(mMainSeatRectf, drawPositionX, drawPositionY, true);
        mMinorSeatRectf = extendSeatParams.getSeatDrawDefaultRectf(mMinorSeatRectf, drawPositionX, drawPositionY, false);

        //绘制情侣座
        if (drawStyle.tag.equals(BaseSeatParams.DRAW_STYLE_COUPLE_OPTIONAL_SEAT)) {
            //绘制单个座位的情侣座位
            //只绘制边
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(seatParams.getDrawHeight() * 0.05f);
            paint.setColor(drawStyle.drawColor);
            //默认的绘制方式
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMainSeatRectf, seatParams.getDrawRadius(), seatParams.getDrawRadius(), paint);
            }
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMinorSeatRectf, seatParams.getDrawRadius(), seatParams.getDrawRadius(), paint);
            }
        } else {
            //绘制普通座位
            //填充座位并绘制
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawStyle.drawColor);


            //默认的绘制方式
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMainSeatRectf, seatParams.getDrawRadius(), seatParams.getDrawRadius(), paint);
            }
            //当该绘制图像有显示在画布上的部分时才进行绘制
            //所有的坐标都不在画布的有效显示范围则不进行绘制
            if (isRectfCanSeen(mMainSeatRectf)) {
                canvas.drawRoundRect(mMinorSeatRectf, seatParams.getDrawRadius(), seatParams.getDrawRadius(), paint);
            }
        }
    }

    @Override
    protected void drawThumbnailSeat(Canvas canvas, Paint paint, BaseSeatParams seatParams, float drawPositionX, float drawPositionY, BaseDrawStyle drawStyle) {
        super.drawThumbnailSeat(canvas, paint, seatParams, drawPositionX, drawPositionY, drawStyle);
        //绘制缩略图,默认使用正式绘制方式进行绘制,如需要可以自定义缩略图的绘制方式
        //比如简单化座位的表现形式
    }

    @Override
    protected void drawNormalStage(Canvas canvas, Paint paint, BaseStageParams stageParams, float drawPositionX, float drawPositionY) {
        float textBeginDrawX = 0f;
        float textBeginDrawY = 0f;
        float textLength = 0f;
        //设置文字大小为舞台高度小一点(保证文字可以显示在舞台范围内)
        float textSize = stageParams.getDescriptionSize(IBaseParams.DEFAULT_FLOAT);


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
            paint.setColor(stageParams.getDrawStyle().descColor);
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
        this.setParams(new SeatParams(), new StageParams(), new GlobalParams());
    }
}
