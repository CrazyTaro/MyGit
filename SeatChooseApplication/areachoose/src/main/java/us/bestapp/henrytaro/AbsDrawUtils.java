package us.bestapp.henrytaro;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AbsDrawUtils implements View.OnTouchListener {
    private Context mContext = null;
    private View mDrawView = null;
    private Bitmap mBitmap = null;
    private Rect mSrcRectf = null;
    private RectF mDstRectf = null;
    private Paint mPaint = null;
    private List<PointF> mPathPoint = null;
    private Path mPath = null;
    private float scaleRate = 0;
    private float dstWidth = 0;
    private float dstHeight = 0;
    private float viewWidth = 0;
    private float viewHeight = 0;

    public AbsDrawUtils(Context context, View drawView) {
        this.mContext = context;
        this.mDrawView = drawView;

        mDrawView.setOnTouchListener(this);
        mBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_place_2);
        mSrcRectf = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        mDstRectf = new RectF();

        mPaint = new Paint();
        mPathPoint = new ArrayList<>();
        mPath = getPath();
    }

    public boolean isPointInArea(PointF clickPoint, List<PointF> points) {
        if (clickPoint == null || points == null || points.size() <= 0) {
            return false;
        } else {
            List<PointF> intersectPoints = new ArrayList<>();
            for (int i = 0; i < points.size(); i++) {
                if (i + 1 < points.size()) {
                    PointF p = isIntersectToLine(clickPoint, points.get(i), points.get(i + 1));
                    if (p != null) {
                        intersectPoints.add(p);
                    }
                } else {
                    PointF p = isIntersectToLine(clickPoint, points.get(i), points.get(0));
                    if (p != null) {
                        intersectPoints.add(p);
                    }
                }
            }

            Log.i("test", "intersectCount=" + intersectPoints.size());
            if (intersectPoints.size() > 0 && (intersectPoints.size() & 1) == 0) {
                PointF firstPoint = intersectPoints.get(0);
                PointF secondPoint = intersectPoints.get(1);

                boolean checkInX = isPointInLineX(clickPoint, firstPoint, secondPoint);
                boolean checkInY = isPointInLineY(clickPoint, firstPoint, secondPoint);

                if (checkInX && checkInY) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }


    public PointF isIntersectToLine(PointF clickPoint, PointF startPoint, PointF endPoint) {
        if (clickPoint == null || startPoint == null || endPoint == null) {
            return null;
        } else {
            float tanClick = clickPoint.y / clickPoint.x;
            float tanLine = (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
            float intercept = endPoint.y - tanLine * endPoint.x;

            PointF intersectPoint = new PointF();
            intersectPoint.x = intercept / (tanClick - tanLine);
            intersectPoint.y = intersectPoint.x * tanClick;
            float testY = intersectPoint.x * tanLine + intercept;
            Log.i("test", "tanY=" + intersectPoint.y + "/testY=" + testY);

            boolean checkInX = isPointInLineX(intersectPoint, startPoint, endPoint);
            boolean checkInY = isPointInLineY(intersectPoint, startPoint, endPoint);

            if (checkInX && checkInY) {
                return intersectPoint;
            } else {
                return null;
            }
        }
    }

    public boolean isPointInLineY(PointF intersectPoint, PointF startPoint, PointF endPoint) {
        if (intersectPoint == null || startPoint == null || endPoint == null) {
            return false;
        } else {
            float smallYInLine = 0f;
            float largeYInLine = 0f;
            if (startPoint.y < endPoint.y) {
                smallYInLine = startPoint.y;
                largeYInLine = endPoint.y;
            } else {
                smallYInLine = endPoint.y;
                largeYInLine = startPoint.y;
            }

            if (intersectPoint.y > smallYInLine && intersectPoint.y < largeYInLine) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isPointInLineX(PointF intersectPoint, PointF startPoint, PointF endPoint) {
        if (intersectPoint == null || startPoint == null || endPoint == null) {
            return false;
        } else {
            float smallXInLine = 0f;
            float largeXInLine = 0f;
            if (startPoint.x < endPoint.x) {
                smallXInLine = startPoint.x;
                largeXInLine = endPoint.x;
            } else {
                smallXInLine = endPoint.x;
                largeXInLine = startPoint.x;
            }

            if (intersectPoint.x > smallXInLine && intersectPoint.x < largeXInLine) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void drawCanvas(Canvas canvas) {

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPath, mPaint);

//        viewWidth = mDrawView.getWidth();
//        viewHeight = mDrawView.getHeight();
//
//        scaleRate = viewWidth / mBitmap.getWidth();
//        dstWidth = mBitmap.getWidth() * scaleRate;
//        dstHeight = mBitmap.getHeight() * scaleRate;
//
//        mDstRectf.left = viewWidth / 2 - dstWidth / 2;
//        mDstRectf.right = mDstRectf.left + dstWidth;
//        mDstRectf.top = viewHeight / 2 - dstHeight / 2;
//        mDstRectf.bottom = mDstRectf.top + dstHeight;
//
//        canvas.drawBitmap(mBitmap, null, mDstRectf, null);
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(mDstRectf,mPaint);
    }

    public Path getPath() {
        mPathPoint.add(new PointF(300, 320));
        mPathPoint.add(new PointF(200, 400));
        mPathPoint.add(new PointF(250, 450));
        mPathPoint.add(new PointF(500, 350));
        mPathPoint.add(new PointF(400, 200));

        Path path = new Path();
        path.moveTo(mPathPoint.get(0).x, mPathPoint.get(0).y);
        for (int i = 1; i < mPathPoint.size(); i++) {
            PointF p = mPathPoint.get(i);
            path.lineTo(p.x, p.y);
        }
        path.close();
        return path;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (isPointInArea(new PointF(x, y), mPathPoint)) {
            mDrawView.setBackgroundColor(Color.RED);
        } else {
            mDrawView.setBackgroundColor(Color.WHITE);
        }

//        int x = (int) event.getX();
//        int y = (int) event.getY();
//
//        float dstX = x - viewWidth / 2 + dstWidth / 2;
//        float dstY = y - viewHeight / 2 + dstHeight / 2;
//
//        if (x > mDstRectf.left && x < mDstRectf.right && y > mDstRectf.top && y < mDstRectf.bottom) {
//            int orlx = 0;
//            int orly = 0;
//            orlx = (int) (dstX / scaleRate);
//            orly = (int) (dstY / scaleRate);
//
//            int color = mBitmap.getPixel(orlx, orly);
//            mDrawView.setBackgroundColor(color);
//        }
        return true;
    }
}
