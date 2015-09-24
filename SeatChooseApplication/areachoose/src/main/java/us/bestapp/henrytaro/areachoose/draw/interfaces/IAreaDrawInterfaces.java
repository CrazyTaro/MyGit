package us.bestapp.henrytaro.areachoose.draw.interfaces;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/24.
 */
public interface IAreaDrawInterfaces {

    /**
     * 设置数据并初始化
     *
     * @param areaList   区域列表
     * @param frontBmpID 前景图像
     * @param backBmpID  背景图像
     * @param event      区域处理事件,包括蒙板图层的创建
     */
    public void initial(List<AbsAreaEntity> areaList, int frontBmpID, int backBmpID, IAreaEventHandle event);


    /**
     * 设置数据并初始化
     *
     * @param areaList 区域列表
     * @param frontBmp 前景图像
     * @param backBmp  背景图像
     * @param event    区域处理事件,包括蒙板图层的创建
     */
    public void initial(List<AbsAreaEntity> areaList, Bitmap frontBmp, Bitmap backBmp, IAreaEventHandle event);

    /**
     * 绘制界面
     *
     * @param canvas
     */
    public void drawCanvas(Canvas canvas);
}
