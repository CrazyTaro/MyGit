package us.bestapp.henrytaro.draw;/**
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

import us.bestapp.henrytaro.R;
import us.bestapp.henrytaro.utils.PathUtils;

/**
 * Created by xuhaolin on 15/9/18.
 */
public class AbsDrawUtils extends AbsTouchEventHandle {
    private Context mContext = null;
    private View mDrawView = null;
    private Bitmap mFtBitmap = null;
    private Bitmap mBgBitmap = null;
    private Bitmap mMaskBitmap = null;
    private Rect mSrcRectf = null;
    private RectF mDstRectf = null;
    private Paint mPaint = null;
    private List<PointF> mPathPoint = null;
    private Path mPath = null;
    private boolean mIsSetNewBitmap = false;
    private float scaleRate = 0;
    private float dstWidth = 0;
    private float dstHeight = 0;
    private float viewWidth = 0;
    private float viewHeight = 0;

    private int[] mData = {
            380, 795, 381, 795, 382, 795, 383, 795, 384, 795, 385, 795, 386, 795, 387, 795, 388, 795,
            389, 795, 390, 795, 391, 795, 392, 795, 393, 795, 394, 795, 395, 795, 396, 795, 397, 795,
            398, 795, 399, 795, 400, 795, 401, 795, 402, 795, 403, 795, 404, 795, 405, 795, 406, 795,
            407, 795, 408, 795, 409, 794, 410, 794, 411, 794, 412, 794, 413, 794, 414, 794, 415, 794,
            416, 794, 417, 794, 418, 794, 419, 793, 420, 793, 421, 793, 422, 793, 423, 793, 424, 793,
            425, 793, 426, 793, 427, 793, 428, 793, 429, 793, 430, 793, 431, 793, 432, 793, 433, 793,
            434, 792, 435, 792, 436, 792, 437, 792, 438, 791, 439, 791, 440, 791, 441, 791, 442, 791,
            443, 791, 444, 791, 445, 790, 446, 790, 447, 790, 448, 790, 449, 790, 450, 790, 451, 790,
            452, 790, 453, 789, 454, 789, 455, 789, 456, 789, 457, 789, 458, 788, 459, 788, 460, 788,
            461, 788, 462, 787, 463, 787, 464, 787, 465, 787, 466, 787, 467, 786, 468, 786, 469, 786,
            470, 786, 471, 786, 472, 785, 473, 785, 474, 785, 475, 784, 476, 784, 477, 784, 478, 784,
            479, 784, 480, 783, 481, 783, 482, 783, 483, 783, 484, 782, 485, 782, 486, 781, 487, 781,
            488, 781, 489, 781, 490, 780, 491, 780, 492, 780, 493, 779, 494, 779, 495, 778, 496, 778,
            497, 777, 498, 777, 499, 777, 500, 776, 501, 776, 502, 776, 503, 776, 504, 775, 504, 774,
            504, 773, 503, 772, 503, 771, 502, 770, 502, 769, 501, 768, 501, 767, 500, 766, 500, 765,
            499, 764, 499, 763, 499, 762, 498, 761, 498, 760, 497, 759, 497, 758, 496, 757, 496, 756,
            495, 755, 495, 754, 494, 753, 494, 752, 493, 751, 493, 750, 492, 749, 492, 748, 492, 747,
            491, 746, 491, 745, 490, 744, 490, 743, 489, 742, 489, 741, 488, 740, 488, 739, 488, 738,
            487, 737, 487, 736, 487, 735, 486, 734, 486, 733, 485, 732, 485, 731, 484, 730, 484, 729,
            483, 728, 482, 727, 482, 726, 481, 725, 481, 724, 480, 723, 480, 722, 480, 721, 479, 720,
            479, 719, 479, 718, 478, 717, 478, 716, 477, 715, 476, 714, 475, 714, 474, 715, 473, 715,
            472, 716, 471, 716, 470, 716, 469, 717, 468, 717, 467, 718, 466, 718, 465, 719, 464, 719,
            463, 719, 462, 719, 461, 720, 460, 720, 459, 720, 458, 720, 457, 721, 456, 721, 455, 721,
            454, 721, 453, 722, 452, 722, 451, 722, 450, 723, 449, 723, 448, 723, 447, 723, 446, 724,
            445, 724, 444, 724, 443, 724, 442, 724, 441, 725, 440, 725, 439, 725, 438, 726, 437, 726,
            436, 726, 435, 726, 434, 726, 433, 727, 432, 727, 431, 727, 430, 727, 429, 727, 428, 727,
            427, 727, 426, 727, 425, 728, 424, 728, 423, 728, 422, 728, 421, 728, 420, 728, 419, 728,
            418, 729, 417, 729, 416, 729, 415, 729, 414, 729, 413, 729, 412, 729, 411, 729, 410, 729,
            409, 729, 408, 729, 407, 729, 406, 729, 405, 729, 404, 729, 403, 729, 402, 729, 401, 730,
            400, 730, 399, 730, 398, 730, 397, 730, 396, 730, 395, 730, 394, 730, 393, 730, 392, 729,
            391, 729, 390, 729, 389, 729, 388, 729, 387, 729, 386, 729, 385, 729, 384, 729, 383, 729,
            382, 729, 381, 729, 380, 729, 379, 729, 378, 729, 377, 729, 376, 729, 375, 729, 374, 728,
            373, 728, 372, 728, 371, 728, 370, 728, 369, 728, 368, 728, 367, 727, 366, 727, 365, 727,
            364, 727, 363, 727, 362, 727, 361, 727, 360, 726, 359, 726, 358, 726, 357, 726, 356, 726,
            355, 726, 354, 726, 353, 725, 352, 725, 351, 725, 350, 725, 349, 725, 348, 724, 347, 724,
            346, 724, 345, 723, 344, 723, 343, 723, 342, 723, 341, 722, 340, 722, 339, 722, 338, 722,
            337, 722, 336, 721, 335, 721, 334, 721, 333, 721, 332, 720, 331, 720, 330, 719, 329, 719,
            328, 719, 327, 718, 326, 718, 325, 718, 324, 718, 323, 717, 322, 717, 321, 717, 320, 716,
            319, 716, 318, 715, 317, 715, 316, 716, 316, 717, 315, 718, 315, 719, 314, 720, 314, 721,
            313, 722, 313, 723, 312, 724, 312, 725, 311, 726, 311, 727, 310, 728, 310, 729, 310, 730,
            309, 731, 309, 732, 308, 733, 308, 734, 307, 735, 307, 736, 306, 737, 306, 738, 305, 739,
            305, 740, 304, 741, 304, 742, 303, 743, 303, 744, 302, 745, 302, 746, 301, 747, 301, 748,
            300, 749, 300, 750, 300, 751, 299, 752, 299, 753, 298, 754, 298, 755, 297, 756, 297, 757,
            296, 758, 295, 759, 295, 760, 294, 761, 294, 762, 294, 763, 293, 764, 293, 765, 292, 766,
            292, 767, 291, 768, 291, 769, 290, 770, 290, 771, 290, 772, 289, 773, 289, 774, 288, 775,
            287, 776, 288, 776, 289, 777, 290, 777, 291, 777, 292, 778, 293, 778, 294, 778, 295, 779,
            296, 779, 297, 779, 298, 780, 299, 780, 300, 780, 301, 781, 302, 781, 303, 782, 304, 782,
            305, 782, 306, 782, 307, 783, 308, 783, 309, 783, 310, 783, 311, 784, 312, 784, 313, 785,
            314, 785, 315, 785, 316, 785, 317, 786, 318, 786, 319, 786, 320, 786, 321, 786, 322, 787,
            323, 787, 324, 787, 325, 788, 326, 788, 327, 788, 328, 788, 329, 788, 330, 789, 331, 789,
            332, 789, 333, 789, 334, 789, 335, 789, 336, 789, 337, 790, 338, 790, 339, 790, 340, 790,
            341, 790, 342, 790, 343, 791, 344, 791, 345, 791, 346, 791, 347, 792, 348, 792, 349, 792,
            350, 792, 351, 792, 352, 792, 353, 793, 354, 793, 355, 793, 356, 793, 357, 793, 358, 793,
            359, 793, 360, 793, 361, 793, 362, 794, 363, 794, 364, 794, 365, 794, 366, 794, 367, 794,
            368, 794, 369, 794, 370, 794, 371, 794, 372, 794, 373, 794, 374, 794, 375, 794, 376, 794,
            377, 794, 378, 794, 379, 794};

    public AbsDrawUtils(Context context, View drawView) {
        this.mContext = context;
        this.mDrawView = drawView;

        mDrawView.setOnTouchListener(this);

        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inMutable = true;
        mFtBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_stadium_front);
        mBgBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.img_stadium_bg);
        mMaskBitmap = mFtBitmap.copy(mFtBitmap.getConfig(), true);

        mSrcRectf = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
        mDstRectf = null;

        mPaint = new Paint();
        mPathPoint = new ArrayList<>();

//        mPathPoint.add(new PointF(300, 320));
//        mPathPoint.add(new PointF(200, 400));
//        mPathPoint.add(new PointF(250, 450));
//        mPathPoint.add(new PointF(500, 350));
//        mPathPoint.add(new PointF(400, 200));

        int[] tempData = {300, 320, 200, 400, 250, 450, 500, 350, 400, 200};
        mPathPoint = PathUtils.getPathPoint(mData);

        mPath = getPath(mPathPoint);
    }


    public void drawCanvas(Canvas canvas) {

//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(2);
//        canvas.drawPath(mPath, mPaint);

        if (mDstRectf == null) {
            viewWidth = mDrawView.getWidth();
            viewHeight = mDrawView.getHeight();

            scaleRate = viewWidth / mFtBitmap.getWidth();
            dstWidth = mFtBitmap.getWidth() * scaleRate;
            dstHeight = mFtBitmap.getHeight() * scaleRate;

            mDstRectf = new RectF();
            mDstRectf.left = viewWidth / 2 - dstWidth / 2;
            mDstRectf.right = mDstRectf.left + dstWidth;
            mDstRectf.top = viewHeight / 2 - dstHeight / 2;
            mDstRectf.bottom = mDstRectf.top + dstHeight;
        }

        canvas.drawBitmap(mFtBitmap, null, mDstRectf, null);
        if (mIsSetNewBitmap) {
            canvas.drawBitmap(mMaskBitmap, null, mDstRectf, null);
        }
    }

    public Path getPath(List<PointF> points) {
        if (points == null || points.size() <= 0) {
            return null;
        }

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
    public void onSingleTouchEventHandle(MotionEvent event, int extraMotionEvent) {

    }

    @Override
    public void onMultiTouchEventHandle(MotionEvent event, int extraMotionEvent) {

    }

    @Override
    public void onSingleClickByTime(MotionEvent event) {

    }

    @Override
    public void onSingleClickByDistance(MotionEvent event) {
        //        float x = event.getX();
//        float y = event.getY();
//
//        if (isPointInArea(new PointF(x, y), mPathPoint)) {
//            mDrawView.setBackgroundColor(Color.RED);
//        } else {
//            mDrawView.setBackgroundColor(Color.WHITE);
//        }


        int x = (int) event.getX();
        int y = (int) event.getY();

        float dstX = x - viewWidth / 2 + dstWidth / 2;
        float dstY = y - viewHeight / 2 + dstHeight / 2;

        if (x > mDstRectf.left && x < mDstRectf.right && y > mDstRectf.top && y < mDstRectf.bottom) {
            int orlx = 0;
            int orly = 0;
            orlx = (int) (dstX / scaleRate);
            orly = (int) (dstY / scaleRate);

            int color = mFtBitmap.getPixel(orlx, orly);
            Log.i("color", color + "");

            long time = System.currentTimeMillis();
            for (int i = 0; i < mMaskBitmap.getWidth(); i++) {
                for (int j = 0; j < mMaskBitmap.getHeight(); j++) {
                    if (mMaskBitmap.getPixel(i, j) == color) {
                        mMaskBitmap.setPixel(i, j, Color.BLACK);
                        mIsSetNewBitmap = true;
                    }
                }
            }

            Log.i("time", (System.currentTimeMillis() - time) + "ms");
            mDrawView.postInvalidate();
        }
    }

    @Override
    public void onDoubleClickByTime() {

    }

    @Override
    public void onDoubleClickByDistance() {

    }
}
