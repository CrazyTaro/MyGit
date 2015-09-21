package us.bestapp.henrytaro;/**
 * Created by xuhaolin on 15/9/18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

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
    }

    public void drawCanvas(Canvas canvas) {
        viewWidth = mDrawView.getWidth();
        viewHeight = mDrawView.getHeight();

        scaleRate = viewWidth / mBitmap.getWidth();
        dstWidth = mBitmap.getWidth() * scaleRate;
        dstHeight = mBitmap.getHeight() * scaleRate;

        mDstRectf.left = viewWidth / 2 - dstWidth / 2;
        mDstRectf.right = mDstRectf.left + dstWidth;
        mDstRectf.top = viewHeight / 2 - dstHeight / 2;
        mDstRectf.bottom = mDstRectf.top + dstHeight;

        canvas.drawBitmap(mBitmap, null, mDstRectf, null);
//        mPaint.setColor(Color.RED);
//        canvas.drawRect(mDstRectf,mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        float dstX = x - viewWidth / 2 + dstWidth / 2;
        float dstY = y - viewHeight / 2 + dstHeight / 2;

        if (x > mDstRectf.left && x < mDstRectf.right && y > mDstRectf.top && y < mDstRectf.bottom) {
            int orlx = 0;
            int orly = 0;
            orlx = (int) (dstX / scaleRate);
            orly = (int) (dstY / scaleRate);

            int color = mBitmap.getPixel(orlx, orly);
            mDrawView.setBackgroundColor(color);
        }
        return true;
    }
}
