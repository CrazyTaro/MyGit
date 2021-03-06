package us.bestapp.henrytaro.params;

import android.graphics.PointF;

import us.bestapp.henrytaro.params.baseparams.BaseStageParams;
import us.bestapp.henrytaro.params.interfaces.IStageParams;
import us.bestapp.henrytaro.utils.StringUtils;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 * <p>所有{@code protected}方法及部分{@code public}都是绘制时需要的,对外公开可以进行设置的方法只允许从接口中进行设置,详见{@link IStageParams}</p>
 */
public class StageParams extends BaseStageParams {

    @Override
    public float getDescriptionSize(float textRate) {
        if (StringUtils.isNullOrEmpty(this.getStageDescription())) {
            return 0;
        } else {
            //计算理论字体大小,以舞台宽度为标准
            //此处由于舞台是不规则的,以舞台最短的宽设为文字的最大长度(底边<顶边)
            float theoryTextLength = this.getDrawWidth() - 2 * this.getDrawHeight();
            float textSize = theoryTextLength / this.getStageDescription().length();
            //理论值大于舞台高度时,以舞台高度为标准
            if (textSize > this.getDrawHeight()) {
                return this.getDrawHeight() * 0.8f;
            } else {
                return textSize;
            }
        }
    }

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
        points[0].x = drawCenterX - this.getDrawWidth() / 2;
        points[0].y = drawCenterY - this.getDrawHeight() / 2;

        //左下角
        points[1].x = points[0].x + this.getDrawHeight();
        points[1].y = points[0].y + this.getDrawHeight();

        //右下角
        points[2].x = drawCenterX + this.getDrawWidth() / 2 - this.getDrawHeight();
        points[2].y = points[1].y;

        //右上角
        points[3].x = points[2].x + this.getDrawHeight();
        points[3].y = points[2].y - this.getDrawHeight();

        return points;
    }
}
