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
    public static final float DEFAULT_STAGE_HEIGHT = 55f;
    /**
     * 默认舞台圆角度
     */
    public static final float DEFAULT_STAGE_RADIUS = 10f;
    /**
     * 默认舞台与顶端间距
     */
    public static final float DEFAULT_STAGE_MARGIN_TOP = 30f;
    /**
     * 默认舞台下方空白高度（与座位的间隔）
     */
    public static final float DEFAULT_STAGE_MARGIN_BOTTOM = 30f;
    /**
     * 默认舞台文字
     */
    public static final String DEFAULT_STAGE_TEXT = "舞台";
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
    private float mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
    private float mStageRadius = DEFAULT_STAGE_RADIUS;
    private int mStageColor = DEFAULT_STAGE_COLOR;
    private String mStageText = DEFAULT_STAGE_TEXT;

    private static StageParams mInstance = null;

    private StageParams() {
    }

    public void resetStageParams() {
        mInstance = new StageParams();
    }

    public static synchronized StageParams getInstance() {
        if (mInstance == null) {
            mInstance = new StageParams();
        }
        return mInstance;
    }

    public String getStageText() {
        return mStageText;
    }

    public void setStageText(String text) {
        this.mStageText = text;
    }

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

    /**
     * 设置舞台上方顶端的高度，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginTop
     */
    public void setStageMarginTop(float mStageMarginTop) {
        if (mStageMarginTop == DEFAULT_FLOAT) {
            this.mStageMarginTop = DEFAULT_STAGE_MARGIN_TOP;
        } else {
            this.mStageMarginTop = mStageMarginTop;
        }
    }

    public float getStageMarginBottom() {
        return mStageMarginBottom;
    }

    /**
     * 设置舞台与下方（座位）间隔的高度，，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageMarginBottom
     */
    public void setStageMarginBottom(float mStageMarginBottom) {
        if (mStageMarginBottom == DEFAULT_FLOAT) {
            this.mStageMarginBottom = DEFAULT_STAGE_MARGIN_BOTTOM;
        } else {
            this.mStageMarginBottom = mStageMarginBottom;
        }
    }

    public float getStageRadius() {
        return mStageRadius;
    }

    /**
     * 设置舞台的圆角弧度，此处并不是以度数计算的，使用默认值请用{@link #DEFAULT_FLOAT}
     *
     * @param mStageRadius 圆角弧度
     */
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

    /**
     * 获取舞台占用的高度，包括舞台距顶端的高度+舞台实际高度+舞台与下方（座位）间隔高度
     *
     * @return 返回舞台占用的高度
     */
    public float getStageTotalHeight() {
        return mStageHeight + mStageMarginBottom + mStageMarginTop;
    }

    public void autoCalculateParams(float widthPrecent, float viewWidth) {
        if (widthPrecent == DEFAULT_FLOAT || widthPrecent < 0 || widthPrecent > 1) {
            widthPrecent = 0.3f;
        }
        mStageWidth = viewWidth * widthPrecent;
        mStageHeight = mStageColor * 1 / 5;
    }

}
