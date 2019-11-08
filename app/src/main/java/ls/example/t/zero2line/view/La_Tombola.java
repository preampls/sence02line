package ls.example.t.zero2line.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ls.t.zero2line.R;


public class La_Tombola extends View {

    /**
     *  刮刮卡
     */
    private Paint mPaint;
    private Path mPath;
    private float mEventX, mEventY;
    private Bitmap result;
    private Bitmap eraser;
    private Bitmap bitmap;

    public La_Tombola(Context context) {
        super(context);
        init();
    }

    public La_Tombola(Context context, @android.support.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public La_Tombola(Context context, @android.support.annotation.Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);
        mPath=new Path();
        //禁用硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        result = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);
        eraser = BitmapFactory.decodeResource(getResources(), R.drawable.eraser);
        bitmap = Bitmap.createBitmap(eraser.getWidth(), eraser.getHeight(), Bitmap.Config.ARGB_8888);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawBitmap(result,0,0,mPaint);

        int i = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
        //先将路径绘制到 bitmap上
        Canvas dstCanvas = new Canvas(bitmap);
        dstCanvas.drawPath(mPath, mPaint);

        canvas.drawBitmap(bitmap,0,0,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //绘制源图像
        canvas.drawBitmap(eraser, 0, 0, mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(i);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX, mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - mEventX) / 2 + mEventX;
                float endY = (event.getY() - mEventY) / 2 + mEventY;
                //画二阶贝塞尔曲线
                mPath.quadTo(mEventX, mEventY, endX, endY);
                mEventX = event.getX();
                mEventY = event.getY();
                break;
        }
        invalidate();
        return true;
    }
}
