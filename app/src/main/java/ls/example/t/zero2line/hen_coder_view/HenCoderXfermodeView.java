package ls.example.t.zero2line.hen_coder_view;

import android.graphics.Paint;
import android.view.View;
import android.content.Context;
import android.util.*;
import android.graphics.Color;

public class HenCoderXfermodeView extends View {
    private Paint mPaint;
    private static final int langth=100;
    private static final int langth_small=80;
    private float currentLength;
    private static final float xf=2.7f;//圆角的半径


    public HenCoderXfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint=new Paint();
        //Paint.Style.FILL:仅填充内部
        //Paint.Style.STROKE:仅描边
        //Paint.Style.FILL_AND_STROKE:描边且填充内部
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        currentLength = dp2px(langth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        //设置背景色
        canvas.drawARGB(255, 139, 197, 186);
        mPaint.setColor(0xFFFFCC44);
        canvas.drawCircle(currentLength/2,currentLength/2,currentLength/2,mPaint);

        mPaint.setColor(0xFF66AAFF);
        float bottom = currentLength * 1.5f;
        canvas.drawRoundRect(currentLength/2,currentLength/2,bottom,bottom,xf,xf,mPaint);
    }

    public static float dp2px(float dp) {
        return  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                android.content.res.Resources.getSystem().getDisplayMetrics());
    }
}
