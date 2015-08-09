package com.example.xuhaolin.seatchoose.cutomview;

import android.graphics.Color;

/**
 * Created by xuhaolin on 2015/8/9.
 * <p>舞台参数，包括舞台绘制需要的所有参数</p>
 */
public class StageParams {
    /**
     * 默认舞台颜色
     */
    public static final int DEFAULT_STAGE_COLOR = Color.GREEN;
    /**
     * 默认舞台宽度
     */
    public static final float DEFAULT_STAGE_WIDTH = 350f;
    /**
     * 默认舞台高度
     */
    public static final float DEFAULT_STAGE_HEIGHT = 50f;
    /**
     * 默认舞台圆角度
     */
    public static final float DEFAULT_STAGE_RADIUS = 10f;
    /**
     * 默认舞台与顶端间距
     */
    public static final float DEFAULT_STAGE_MARGIN_TOP = 15f;
    /**
     * 默认整数值:-1
     */
    public static final int DEFAULT_INT = -1;
    /**
     * 默认浮点值:-1
     */
    public static final float DEFAULT_FLOAT = -1;

    private float mStageWidth = DEFAULT_STAGE_WIDTH;
    private float mStageHeight = DEFAULT_STAGE_HEIGHT;
    private float mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
    private float mStageRadius = DEFAULT_STAGE_RADIUS;
    private int mStageColor = DEFAULT_STAGE_COLOR;

    public float getStageWidth() {
        return mStageWidth;
    }

    public void setStageWidth(float mStageWidth) {
        if (mStageWidth == DEFAULT_FLOAT) {
            this.mStageWidth = DEFAULT_STAGE_WIDTH;
        } else {
            this.mStageWidth = mStageWidth;
        }
    }

    public float getStageHeight() {
        return mStageHeight;
    }

    public void setStageHeight(float mStageHeight) {
        if (mStageHeight == DEFAULT_FLOAT) {
            this.mStageHeight = DEFAULT_STAGE_HEIGHT;
        } else {
            this.mStageHeight = mStageHeight;
        }
    }

    public float getStageMarginTop() {
        return mStageMarginTop;
    }

    public void setStageMarginTop(float mStageMarginTop) {
        if (mStageMarginTop == DEFAULT_FLOAT) {
            this.mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            this.mStageMarginTop = mStageMarginTop;
        }
    }

    public float getStageRadius() {
        return mStageRadius;
    }

    public void setStageRadius(float mStageRadius) {
        if (mStageRadius == DEFAULT_FLOAT) {
            this.mStageRadius = DEFAULT_STAGE_RADIUS;
        } else {
            this.mStageRadius = mStageRadius;
        }
    }

    public int getStageColor() {
        return mStageColor;
    }

    public void setStageColor(int mStageColor) {
        if (mStageColor == DEFAULT_INT) {
            this.mStageColor = DEFAULT_STAGE_COLOR;
        } else {
            this.mStageColor = mStageColor;
        }
    }


}
