package ls.example.t.zero2line.hen_coder_view;
import android.graphics.Path;

public class HenCoderXfermodeView02 extends android.view.View {
    private android.graphics.Paint mPaint;
    private static final int langth=100;
    private static final int langth_small=80;
    private float currentLength;
    private static final float xf=2.7f;
    private Path path=new Path();




    public HenCoderXfermodeView02(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint=new android.graphics.Paint();
        //Paint.Style.FILL:仅填充内部
        //Paint.Style.STROKE:仅描边
        //Paint.Style.FILL_AND_STROKE:描边且填充内部
        mPaint.setStyle(android.graphics.Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        currentLength = dp2px(langth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        path.reset();
        path.addCircle(langth*2,langth*2,langth/2, android.graphics.Path.Direction.CCW);
        path.addArc(0,0,langth,langth,0,360);

        float bottom = currentLength * 1.5f;
        path.addRoundRect(currentLength/2,currentLength/2,bottom,bottom,xf,xf,android.graphics.Path.Direction.CCW);
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
//        canvas.drawCircle(currentLength/2,currentLength/2,currentLength/2,mPaint);

        mPaint.setColor(0xFF66AAFF);
        float bottom = currentLength * 1.5f;
//        canvas.drawRoundRect(currentLength/2,currentLength/2,bottom,bottom,xf,xf,mPaint);

//        mPaint.setColor(0xFFf1a333);
        canvas.drawPath(path,mPaint);
    }

    public static float dp2px(float dp) {
        return  android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dp,
                android.content.res.Resources.getSystem().getDisplayMetrics());
    }
}
