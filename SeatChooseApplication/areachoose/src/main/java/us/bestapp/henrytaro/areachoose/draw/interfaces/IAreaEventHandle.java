package us.bestapp.henrytaro.areachoose.draw.interfaces;

import us.bestapp.henrytaro.areachoose.entity.absentity.AbsAreaEntity;

/**
 * Created by xuhaolin on 15/9/24.
 */
public interface IAreaEventHandle {

    /**
     * 开始加载蒙板图层
     */
    public void onStartLoadMaskBitmap();

    /**
     * 加载蒙板图层结束,并返回是否成功加载的标志
     *
     * @param isSuccess
     */
    public void onFinishLoadMaskBitmap(boolean isSuccess);

    /**
     * 区域选中事件
     *
     * @param areaEntity
     */
    public void onAreaChoose(AbsAreaEntity areaEntity);
}
