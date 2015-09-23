package us.bestapp.henrytaro.areachoose.view;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import us.bestapp.henrytaro.areachoose.draw.AbsDrawUtils;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AreaChooseView extends View {
    private AbsDrawUtils mDrawUtils = new AbsDrawUtils(getContext(), this);

    public AreaChooseView(Context context) {
        super(context);
    }

    public AreaChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AreaChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AbsDrawUtils getDrawUtils() {
        return this.mDrawUtils;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mDrawUtils.drawCanvas(canvas);
    }
}
