package us.bestapp.henrytaro.params;/**
 * Created by xuhaolin on 15/8/7.
 */

import android.graphics.RectF;

import java.util.Map;

import us.bestapp.henrytaro.params.baseparams.BaseDrawStyle;
import us.bestapp.henrytaro.params.baseparams.BaseSeatParams;
import us.bestapp.henrytaro.params.interfaces.ISeatParams;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>座位参数，包括座位绘制需要的各种参数;座位类型及其描述相关参数与在此类设置</p>
 * <p>所有{@code protected}方法及部分{@code public}都是绘制时需要的,对外公开可以进行设置的方法只允许从接口中进行设置,详见{@link ISeatParams}</p>
 */
public class SeatParams extends BaseSeatParams {
    /**
     * 默认主座位高度
     */
    public static final float DEFAULT_SEAT_MAIN_HEIGHT = DEFAULT_SEAT_HEIGHT * 0.75f;
    /**
     * 默认次座位高度
     */
    public static final float DEFAULT_SEAT_MINOR_HEIGHT = DEFAULT_SEAT_HEIGHT * 0.2f;
    /**
     * 默认主次座位间隔高度
     */
    public static final float DEFAULT_SEAT_HEIGHT_INTERVAL = DEFAULT_SEAT_HEIGHT * 0.05f;

    //主座位高度, 与次座位一起绘制显示为一个座位,显得好看一点,此参数不对外公开
    protected float mMainSeatHeight = DEFAULT_SEAT_MAIN_HEIGHT;
    //次座位高度
    protected float mMinorSeatHeight = DEFAULT_SEAT_MINOR_HEIGHT;
    //主次座位之间的间隔
    protected float mSeatHeightInterval = DEFAULT_SEAT_HEIGHT_INTERVAL;

    /**
     * 根据座位高度自动计算座位绘制的主座位与次座位高度
     *
     * @param seatHeight
     */
    protected void autoCalculateSeatShapeHeight(float seatHeight) {
        float newRadius = seatHeight * 0.1f;
        this.setRadius(newRadius > 20f ? 20f : newRadius);
        this.mMainSeatHeight = seatHeight * 0.75f;
        this.mMinorSeatHeight = seatHeight * 0.2f;
        this.mSeatHeightInterval = seatHeight * 0.05f;

        this.setSeatHorizontalInterval(seatHeight * 0.2f);
        this.setSeatVerticalInterval(seatHeight * 0.8f);
    }

    @Override
    protected void updateWidthAndHeightWhenSet(float width, float height) {
        super.updateWidthAndHeightWhenSet(width, height);
        if (height != -1) {
            this.autoCalculateSeatShapeHeight(height);
        }
    }

    /**
     * 获取默认的座位绘制区域,自定义座位使用
     *
     * @param seatRectf     座位区域
     * @param drawPositionX 座位绘制的中心X
     * @param drawPositionY 座位绘制的中心Y
     * @param isMainSeat    是否主座位(不是主座位即为次座位)
     * @return
     */
    public RectF getSeatDrawDefaultRectf(RectF seatRectf, float drawPositionX, float drawPositionY, boolean isMainSeat) {
        if (seatRectf == null) {
            seatRectf = new RectF();
        }
        //创建主座位
        seatRectf.left = drawPositionX - this.getWidth() / 2;
        seatRectf.right = seatRectf.left + this.getWidth();

        seatRectf.top = drawPositionY - this.getHeight() / 2;
        seatRectf.bottom = seatRectf.top + this.getHeight() * 0.75f;

        if (!isMainSeat) {
            //若不是主座位,则更改上下座位坐标(左右是一致的)
            seatRectf.top = seatRectf.bottom + this.getHeight() * 0.05f + this.getHeight() * 0.2f / 2;
            seatRectf.bottom = seatRectf.top + this.getHeight() * 0.2f;
        }

        return seatRectf;
    }


    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        this.autoCalculateSeatShapeHeight(this.getHeight());
    }

    @Override
    public void setScaleRate(float scaleRate, boolean isTrueSetValue) {
        super.setScaleRate(scaleRate, isTrueSetValue);
        this.autoCalculateSeatShapeHeight(this.getHeight());
    }

    @Override
    public float setOriginalValuesToReplaceCurrents(float fixScaleRate) {
        float scaleRate = super.setOriginalValuesToReplaceCurrents(fixScaleRate);
        this.autoCalculateSeatShapeHeight(this.getHeight());
        return scaleRate;
    }

    @Override
    protected BaseSeatParams getSelectableClone(BaseSeatParams newParams, Map<String, BaseDrawStyle> styleMap) {
        SeatParams newObj = (SeatParams) super.getSelectableClone(newParams, styleMap);
        newObj.autoCalculateSeatShapeHeight(newObj.getHeight());
        return newObj;
    }
}
