package us.bestapp.henrytaro.params;

import android.graphics.PointF;

import us.bestapp.henrytaro.params.absparams.BaseStageParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 * <p>所有{@code protected}方法及部分{@code public}都是绘制时需要的,对外公开可以进行设置的方法只允许从接口中进行设置,详见{@link IStageParams}</p>
 */
public class StageParams extends BaseStageParams {

    /**
     * 更改舞台默认形状
     *
     * @param drawCenterX 舞台绘制位置的中心X轴坐标
     * @param drawCenterY 舞台绘制位置的中心Y轴坐标
     * @return
     */
    @Override
    protected PointF[] getStagePathPoint(float drawCenterX, float drawCenterY) {
        PointF[] points = new PointF[4];

        for (int i = 0; i < 4; i++) {
            points[i] = new PointF();
        }

        //左上角
        points[0].x = drawCenterX - this.getWidth() / 2;
        points[0].y = drawCenterY - this.getHeight() / 2;

        //左下角
        points[1].x = points[0].x + this.getHeight();
        points[1].y = points[0].y + this.getHeight();

        //右下角
        points[2].x = drawCenterX + this.getWidth() / 2 - this.getHeight();
        points[2].y = points[1].y;

        //右上角
        points[3].x = points[2].x + this.getHeight();
        points[3].y = points[2].y - this.getHeight();

        return points;
    }
}
