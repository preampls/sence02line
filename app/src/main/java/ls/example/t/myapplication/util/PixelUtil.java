package ls.example.t.myapplication.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class PixelUtil {


    //屏幕的宽度
    public static int getScreenWidth(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;  // 返回屏幕的宽度数值
    }

    // 获得屏幕的高度
    public static int getScreenHeight(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;  // 返回屏幕的高度数值
    }

    // 获得屏幕的像素密度
    public static float getScreenDensity(Context ctx) {
        // 从系统服务中获取窗口管理器
        WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        // 从默认显示器中获取显示参数保存到dm对象中
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;  // 返回屏幕的像素密度数值
    }




}
