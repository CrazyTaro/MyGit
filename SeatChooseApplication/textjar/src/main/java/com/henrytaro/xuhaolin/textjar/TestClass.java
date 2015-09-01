package com.henrytaro.xuhaolin.textjar;/**
 * Created by xuhaolin on 15/9/1.
 */

import android.content.Context;
import android.view.View;

import us.bestapp.henrytaro.draw.params.GlobleParams;
import us.bestapp.henrytaro.draw.params.SeatParams;
import us.bestapp.henrytaro.draw.params.StageParams;
import us.bestapp.henrytaro.draw.utils.AbsTouchEventHandle.ITouchEventListener;
import us.bestapp.henrytaro.draw.utils.SeatDrawUtils;

/**
 * Author:
 * Description:
 */
public class TestClass extends SeatDrawUtils implements ITouchEventListener{

    public TestClass(Context context, View drawView, SeatParams seat, StageParams stage, GlobleParams globle) {
        super(context, drawView, seat, stage, globle);
    }

    public TestClass(Context context, View drawView) {
        super(context, drawView);
    }
}
