package us.bestapp.henrytaro.areachoose.view;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import us.bestapp.henrytaro.areachoose.draw.AreaDrawUtils;
import us.bestapp.henrytaro.areachoose.draw.interfaces.IAreaDrawInterfaces;
import us.bestapp.henrytaro.areachoose.view.Interfaces.IAreaViewInterface;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AreaChooseView extends View implements IAreaViewInterface {
    //区域选择控件
    private IAreaDrawInterfaces mAreaDrawInterfaces = new AreaDrawUtils(getContext(), this);

    public AreaChooseView(Context context) {
        super(context);
    }

    public AreaChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AreaChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mAreaDrawInterfaces.drawCanvas(canvas);
    }

    @Override
    public IAreaDrawInterfaces getAreaDrawIntrefaces() {
        return mAreaDrawInterfaces;
    }
}
