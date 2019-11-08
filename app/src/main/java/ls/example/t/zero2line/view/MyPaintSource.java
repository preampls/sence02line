package ls.example.t.zero2line.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.Xfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import ls.t.zero2line.R;

public class MyPaintSource extends View {

    private Paint mPaint;
    private int mWidth, mHight;
    private Shader mShader;
    private Bitmap mBitmap;

    public MyPaintSource(Context context) {
        this(context, null);
        init();
    }

    public MyPaintSource(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyPaintSource(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyPaintSource(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    
    /**
     * 
     * @Author Administrator
     * @time 2019-11-08 15:26
     */
    
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setARGB(255, 255, 255, 0);
        mPaint.setAlpha(200);
        mPaint.setAntiAlias(true);
//        Paint.Style.STROKE,   Paint.Style.FILL,   Paint.Style.FILL_AND_STROKE
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(4);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 1.线性渲染,LinearGradient(float x0, float y0, float x1, float y1,
         * @NonNull @ColorInt int colors[], @Nullable float positions[], @NonNull TileMode tile)
         * (x0,y0)：渐变起始点坐标
         * (x1,y1):渐变结束点坐标
         * color0:渐变开始点颜色,16进制的颜色表示，必须要带有透明度
         * color1:渐变结束颜色
         * colors:渐变数组
         * positions:位置数组，position的取值范围[0,1],作用是指定某个位置的颜色值，如果传null，渐变就线性变化。
         * tile:用于指定控件区域大于指定的渐变区域时，空白区域的颜色填充方法
         */
//        mShader=new LinearGradient(0,0,500, 500,new int[]{Color.RED, Color.BLUE, Color.GREEN},new float[]{0.f,0.7f,1},Shader.TileMode.MIRROR);
//        mPaint.setShader(mShader);
//        canvas.drawCircle(100,100,100,mPaint);
//        canvas.drawRect(0,0,300,300,mPaint);

        /**
         * 环形渲染，RadialGradient(float centerX, float centerY, float radius, @ColorInt int colors[], @Nullable float stops[], TileMode tileMode)
         * centerX ,centerY：shader的中心坐标，开始渐变的坐标
         * radius:渐变的半径
         * centerColor,edgeColor:中心点渐变颜色，边界的渐变颜色
         * colors:渐变颜色数组
         * stoops:渐变位置数组，类似扫描渐变的positions数组，取值[0,1],中心点为0，半径到达位置为1.0f
         * tileMode:shader未覆盖以外的填充模式。
         */
//        mShader = new RadialGradient(0,0,100,new int []{Color.RED,Color.GREEN},null,Shader.TileMode.CLAMP);
//        mPaint.setShader(mShader);
//        canvas.drawCircle(100,100,100,mPaint);


        /**
         * 扫描渲染，SweepGradient(float cx, float cy, @ColorInt int color0,int color1)
         * cx,cy 渐变中心坐标
         * color0,color1：渐变开始结束颜色
         * colors，positions：类似LinearGradient,用于多颜色渐变,positions为null时，根据颜色线性渐变
         */
//        mShader = new SweepGradient(100, 100, new int[]{Color.GREEN, Color.RED}, null);
//        mPaint.setShader(mShader);
//        canvas.drawCircle(100, 100, 100, mPaint);

        /**
         * 位图渲染，BitmapShader(@NonNull Bitmap bitmap, @NonNull TileMode tileX, @NonNull TileMode tileY)
         * Bitmap:构造shader使用的bitmap
         * tileX：X轴方向的TileMode
         * tileY:Y轴方向的TileMode
         REPEAT, 绘制区域超过渲染区域的部分，重复排版
         CLAMP， 绘制区域超过渲染区域的部分，会以最后一个像素拉伸排版
         MIRROR, 绘制区域超过渲染区域的部分，镜像翻转排版
         */
        mBitmap= BitmapFactory.decodeResource(getResources(),R.drawable.p001);
//
//        mShader=new BitmapShader(mBitmap,Shader.TileMode.MIRROR,Shader.TileMode.REPEAT);
//        mPaint.setShader(mShader);
//        int len = mBitmap.getWidth() * mBitmap.getWidth() + mBitmap.getHeight() * mBitmap.getHeight();
//        double sqrt = Math.sqrt(len);
//        canvas.drawCircle(500,300, (float) sqrt/2,mPaint);
//        canvas.drawRect(mBitmap.getWidth(),mBitmap.getHeight(),mBitmap.getWidth()*5,mBitmap.getHeight()*5,mPaint);

        /**
         * 组合渲染，
         * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, Xfermode mode)
         * ComposeShader(@NonNull Shader shaderA, @NonNull Shader shaderB, PorterDuff.Mode mode)
         * shaderA,shaderB:要混合的两种shader
         * Xfermode mode： 组合两种shader颜色的模式
         * PorterDuff.Mode mode: 组合两种shader颜色的模式
         */
        BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.MIRROR, Shader.TileMode.REPEAT);
        RadialGradient radialGradient = new RadialGradient(0, 0, 100, new int[]{Color.RED, Color.GREEN}, null, Shader.TileMode.CLAMP);
        mShader=new ComposeShader(bitmapShader,radialGradient, PorterDuff.Mode.MULTIPLY);
        mPaint.setShader(mShader);
        canvas.drawCircle(300,300,300,mPaint);


    }
}
