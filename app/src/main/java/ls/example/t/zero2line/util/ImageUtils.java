package ls.example.t.zero2line.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/12
 *     desc  : utils about image
 * </pre>
 */
public final class ImageUtils {

    private ImageUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Bitmap to bytes.
     *
     * @param bitmap The bitmap.
     * @param format The format of bitmap.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final android.graphics.Bitmap bitmap, final android.graphics.Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bytes to bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static android.graphics.Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return bitmap
     */
    public static android.graphics.Bitmap drawable2Bitmap(final android.graphics.drawable.Drawable drawable) {
        if (drawable instanceof android.graphics.drawable.BitmapDrawable) {
            android.graphics.drawable.BitmapDrawable bitmapDrawable = (android.graphics.drawable.BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        android.graphics.Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = android.graphics.Bitmap.createBitmap(1, 1,
                    drawable.getOpacity() != android.graphics.PixelFormat.OPAQUE
                            ? android.graphics.Bitmap.Config.ARGB_8888
                            : android.graphics.Bitmap.Config.RGB_565);
        } else {
            bitmap = android.graphics.Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != android.graphics.PixelFormat.OPAQUE
                            ? android.graphics.Bitmap.Config.ARGB_8888
                            : android.graphics.Bitmap.Config.RGB_565);
        }
        android.graphics.Canvas canvas = new android.graphics.Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Bitmap to drawable.
     *
     * @param bitmap The bitmap.
     * @return drawable
     */
    public static android.graphics.drawable.Drawable bitmap2Drawable(final android.graphics.Bitmap bitmap) {
        return bitmap == null ? null : new android.graphics.drawable.BitmapDrawable(Utils.getApp().getResources(), bitmap);
    }

    /**
     * Drawable to bytes.
     *
     * @param drawable The drawable.
     * @param format   The format of bitmap.
     * @return bytes
     */
    public static byte[] drawable2Bytes(final android.graphics.drawable.Drawable drawable, final android.graphics.Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * Bytes to drawable.
     *
     * @param bytes The bytes.
     * @return drawable
     */
    public static android.graphics.drawable.Drawable bytes2Drawable(final byte[] bytes) {
        return bitmap2Drawable(bytes2Bitmap(bytes));
    }

    /**
     * View to bitmap.
     *
     * @param view The view.
     * @return bitmap
     */
    public static android.graphics.Bitmap view2Bitmap(final android.view.View view) {
        if (view == null) return null;
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(view.getWidth(),
                view.getHeight(),
                android.graphics.Bitmap.Config.ARGB_8888);
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        android.graphics.drawable.Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(android.graphics.Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }

    /**
     * Return bitmap.
     *
     * @param file The file.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.File file) {
        if (file == null) return null;
        return android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    /**
     * Return bitmap.
     *
     * @param file      The file.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    /**
     * Return bitmap.
     *
     * @param filePath The path of file.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final String filePath) {
        if (isSpace(filePath)) return null;
        return android.graphics.BitmapFactory.decodeFile(filePath);
    }

    /**
     * Return bitmap.
     *
     * @param filePath  The path of file.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final String filePath, final int maxWidth, final int maxHeight) {
        if (isSpace(filePath)) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return android.graphics.BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * Return bitmap.
     *
     * @param is The input stream.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.InputStream is) {
        if (is == null) return null;
        return android.graphics.BitmapFactory.decodeStream(is);
    }

    /**
     * Return bitmap.
     *
     * @param is        The input stream.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.InputStream is, final int maxWidth, final int maxHeight) {
        if (is == null) return null;
        byte[] bytes = input2Byte(is);
        return getBitmap(bytes, 0, maxWidth, maxHeight);
    }

    /**
     * Return bitmap.
     *
     * @param data   The data.
     * @param offset The offset.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final byte[] data, final int offset) {
        if (data.length == 0) return null;
        return android.graphics.BitmapFactory.decodeByteArray(data, offset, data.length);
    }

    /**
     * Return bitmap.
     *
     * @param data      The data.
     * @param offset    The offset.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final byte[] data,
                                                    final int offset,
                                                    final int maxWidth,
                                                    final int maxHeight) {
        if (data.length == 0) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeByteArray(data, offset, data.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return android.graphics.BitmapFactory.decodeByteArray(data, offset, data.length, options);
    }

    /**
     * Return bitmap.
     *
     * @param resId The resource id.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.DrawableRes final int resId) {
        android.graphics.drawable.Drawable drawable = android.support.v4.content.ContextCompat.getDrawable(Utils.getApp(), resId);
        android.graphics.Canvas canvas = new android.graphics.Canvas();
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                android.graphics.Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * Return bitmap.
     *
     * @param resId     The resource id.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(@android.support.annotation.DrawableRes final int resId,
                                                    final int maxWidth,
                                                    final int maxHeight) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        final android.content.res.Resources resources = Utils.getApp().getResources();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return android.graphics.BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * Return bitmap.
     *
     * @param fd The file descriptor.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.FileDescriptor fd) {
        if (fd == null) return null;
        return android.graphics.BitmapFactory.decodeFileDescriptor(fd);
    }

    /**
     * Return bitmap.
     *
     * @param fd        The file descriptor
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return bitmap
     */
    public static android.graphics.Bitmap getBitmap(final java.io.FileDescriptor fd,
                                                    final int maxWidth,
                                                    final int maxHeight) {
        if (fd == null) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        return android.graphics.BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    /**
     * Return the bitmap with the specified color.
     *
     * @param src   The source of bitmap.
     * @param color The color.
     * @return the bitmap with the specified color
     */
    public static android.graphics.Bitmap drawColor(@android.support.annotation.NonNull final android.graphics.Bitmap src, @android.support.annotation.ColorInt final int color) {
        return drawColor(src, color, false);
    }

    /**
     * Return the bitmap with the specified color.
     *
     * @param src     The source of bitmap.
     * @param color   The color.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the bitmap with the specified color
     */
    public static android.graphics.Bitmap drawColor(@android.support.annotation.NonNull final android.graphics.Bitmap src,
                                                    @android.support.annotation.ColorInt final int color,
                                                    final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        canvas.drawColor(color, android.graphics.PorterDuff.Mode.DARKEN);
        return ret;
    }

    /**
     * Return the scaled bitmap.
     *
     * @param src       The source of bitmap.
     * @param newWidth  The new width.
     * @param newHeight The new height.
     * @return the scaled bitmap
     */
    public static android.graphics.Bitmap scale(final android.graphics.Bitmap src, final int newWidth, final int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    /**
     * Return the scaled bitmap.
     *
     * @param src       The source of bitmap.
     * @param newWidth  The new width.
     * @param newHeight The new height.
     * @param recycle   True to recycle the source of bitmap, false otherwise.
     * @return the scaled bitmap
     */
    public static android.graphics.Bitmap scale(final android.graphics.Bitmap src,
                                                final int newWidth,
                                                final int newHeight,
                                                final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = android.graphics.Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the scaled bitmap
     *
     * @param src         The source of bitmap.
     * @param scaleWidth  The scale of width.
     * @param scaleHeight The scale of height.
     * @return the scaled bitmap
     */
    public static android.graphics.Bitmap scale(final android.graphics.Bitmap src, final float scaleWidth, final float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    /**
     * Return the scaled bitmap
     *
     * @param src         The source of bitmap.
     * @param scaleWidth  The scale of width.
     * @param scaleHeight The scale of height.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @return the scaled bitmap
     */
    public static android.graphics.Bitmap scale(final android.graphics.Bitmap src,
                                                final float scaleWidth,
                                                final float scaleHeight,
                                                final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setScale(scaleWidth, scaleHeight);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the clipped bitmap.
     *
     * @param src    The source of bitmap.
     * @param x      The x coordinate of the first pixel.
     * @param y      The y coordinate of the first pixel.
     * @param width  The width.
     * @param height The height.
     * @return the clipped bitmap
     */
    public static android.graphics.Bitmap clip(final android.graphics.Bitmap src,
                                               final int x,
                                               final int y,
                                               final int width,
                                               final int height) {
        return clip(src, x, y, width, height, false);
    }

    /**
     * Return the clipped bitmap.
     *
     * @param src     The source of bitmap.
     * @param x       The x coordinate of the first pixel.
     * @param y       The y coordinate of the first pixel.
     * @param width   The width.
     * @param height  The height.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the clipped bitmap
     */
    public static android.graphics.Bitmap clip(final android.graphics.Bitmap src,
                                               final int x,
                                               final int y,
                                               final int width,
                                               final int height,
                                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(src, x, y, width, height);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the skewed bitmap.
     *
     * @param src The source of bitmap.
     * @param kx  The skew factor of x.
     * @param ky  The skew factor of y.
     * @return the skewed bitmap
     */
    public static android.graphics.Bitmap skew(final android.graphics.Bitmap src, final float kx, final float ky) {
        return skew(src, kx, ky, 0, 0, false);
    }

    /**
     * Return the skewed bitmap.
     *
     * @param src     The source of bitmap.
     * @param kx      The skew factor of x.
     * @param ky      The skew factor of y.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the skewed bitmap
     */
    public static android.graphics.Bitmap skew(final android.graphics.Bitmap src,
                                               final float kx,
                                               final float ky,
                                               final boolean recycle) {
        return skew(src, kx, ky, 0, 0, recycle);
    }

    /**
     * Return the skewed bitmap.
     *
     * @param src The source of bitmap.
     * @param kx  The skew factor of x.
     * @param ky  The skew factor of y.
     * @param px  The x coordinate of the pivot point.
     * @param py  The y coordinate of the pivot point.
     * @return the skewed bitmap
     */
    public static android.graphics.Bitmap skew(final android.graphics.Bitmap src,
                                               final float kx,
                                               final float ky,
                                               final float px,
                                               final float py) {
        return skew(src, kx, ky, px, py, false);
    }

    /**
     * Return the skewed bitmap.
     *
     * @param src     The source of bitmap.
     * @param kx      The skew factor of x.
     * @param ky      The skew factor of y.
     * @param px      The x coordinate of the pivot point.
     * @param py      The y coordinate of the pivot point.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the skewed bitmap
     */
    public static android.graphics.Bitmap skew(final android.graphics.Bitmap src,
                                               final float kx,
                                               final float ky,
                                               final float px,
                                               final float py,
                                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setSkew(kx, ky, px, py);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the rotated bitmap.
     *
     * @param src     The source of bitmap.
     * @param degrees The number of degrees.
     * @param px      The x coordinate of the pivot point.
     * @param py      The y coordinate of the pivot point.
     * @return the rotated bitmap
     */
    public static android.graphics.Bitmap rotate(final android.graphics.Bitmap src,
                                                 final int degrees,
                                                 final float px,
                                                 final float py) {
        return rotate(src, degrees, px, py, false);
    }

    /**
     * Return the rotated bitmap.
     *
     * @param src     The source of bitmap.
     * @param degrees The number of degrees.
     * @param px      The x coordinate of the pivot point.
     * @param py      The y coordinate of the pivot point.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the rotated bitmap
     */
    public static android.graphics.Bitmap rotate(final android.graphics.Bitmap src,
                                                 final int degrees,
                                                 final float px,
                                                 final float py,
                                                 final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        if (degrees == 0) return src;
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setRotate(degrees, px, py);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the rotated degree.
     *
     * @param filePath The path of file.
     * @return the rotated degree
     */
    public static int getRotateDegree(final String filePath) {
        try {
            android.media.ExifInterface exifInterface = new android.media.ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    android.media.ExifInterface.TAG_ORIENTATION,
                    android.media.ExifInterface.ORIENTATION_NORMAL
            );
            switch (orientation) {
                case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Return the round bitmap.
     *
     * @param src The source of bitmap.
     * @return the round bitmap
     */
    public static android.graphics.Bitmap toRound(final android.graphics.Bitmap src) {
        return toRound(src, 0, 0, false);
    }

    /**
     * Return the round bitmap.
     *
     * @param src     The source of bitmap.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the round bitmap
     */
    public static android.graphics.Bitmap toRound(final android.graphics.Bitmap src, final boolean recycle) {
        return toRound(src, 0, 0, recycle);
    }

    /**
     * Return the round bitmap.
     *
     * @param src         The source of bitmap.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @return the round bitmap
     */
    public static android.graphics.Bitmap toRound(final android.graphics.Bitmap src,
                                                  @android.support.annotation.IntRange(from = 0) int borderSize,
                                                  @android.support.annotation.ColorInt int borderColor) {
        return toRound(src, borderSize, borderColor, false);
    }

    /**
     * Return the round bitmap.
     *
     * @param src         The source of bitmap.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @return the round bitmap
     */
    public static android.graphics.Bitmap toRound(final android.graphics.Bitmap src,
                                                  @android.support.annotation.IntRange(from = 0) int borderSize,
                                                  @android.support.annotation.ColorInt int borderColor,
                                                  final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        int size = Math.min(width, height);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(width, height, src.getConfig());
        float center = size / 2f;
        android.graphics.RectF rectF = new android.graphics.RectF(0, 0, width, height);
        rectF.inset((width - size) / 2f, (height - size) / 2f);
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setTranslate(rectF.left, rectF.top);
        if (width != height) {
            matrix.preScale((float) size / width, (float) size / height);
        }
        android.graphics.BitmapShader shader = new android.graphics.BitmapShader(src, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        shader.setLocalMatrix(matrix);
        paint.setShader(shader);
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        canvas.drawRoundRect(rectF, center, center, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            float radius = center - borderSize / 2f;
            canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the round corner bitmap.
     *
     * @param src    The source of bitmap.
     * @param radius The radius of corner.
     * @return the round corner bitmap
     */
    public static android.graphics.Bitmap toRoundCorner(final android.graphics.Bitmap src, final float radius) {
        return toRoundCorner(src, radius, 0, 0, false);
    }

    /**
     * Return the round corner bitmap.
     *
     * @param src     The source of bitmap.
     * @param radius  The radius of corner.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the round corner bitmap
     */
    public static android.graphics.Bitmap toRoundCorner(final android.graphics.Bitmap src,
                                                        final float radius,
                                                        final boolean recycle) {
        return toRoundCorner(src, radius, 0, 0, recycle);
    }

    /**
     * Return the round corner bitmap.
     *
     * @param src         The source of bitmap.
     * @param radius      The radius of corner.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @return the round corner bitmap
     */
    public static android.graphics.Bitmap toRoundCorner(final android.graphics.Bitmap src,
                                                        final float radius,
                                                        @android.support.annotation.IntRange(from = 0) int borderSize,
                                                        @android.support.annotation.ColorInt int borderColor) {
        return toRoundCorner(src, radius, borderSize, borderColor, false);
    }

    /**
     * Return the round corner bitmap.
     *
     * @param src         The source of bitmap.
     * @param radius      The radius of corner.
     * @param borderSize  The size of border.
     * @param borderColor The color of border.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @return the round corner bitmap
     */
    public static android.graphics.Bitmap toRoundCorner(final android.graphics.Bitmap src,
                                                        final float radius,
                                                        @android.support.annotation.IntRange(from = 0) int borderSize,
                                                        @android.support.annotation.ColorInt int borderColor,
                                                        final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(width, height, src.getConfig());
        android.graphics.BitmapShader shader = new android.graphics.BitmapShader(src, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP);
        paint.setShader(shader);
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        android.graphics.RectF rectF = new android.graphics.RectF(0, 0, width, height);
        float halfBorderSize = borderSize / 2f;
        rectF.inset(halfBorderSize, halfBorderSize);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderSize > 0) {
            paint.setShader(null);
            paint.setColor(borderColor);
            paint.setStyle(android.graphics.Paint.Style.STROKE);
            paint.setStrokeWidth(borderSize);
            paint.setStrokeCap(android.graphics.Paint.Cap.ROUND);
            canvas.drawRoundRect(rectF, radius, radius, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the round corner bitmap with border.
     *
     * @param src          The source of bitmap.
     * @param borderSize   The size of border.
     * @param color        The color of border.
     * @param cornerRadius The radius of corner.
     * @return the round corner bitmap with border
     */
    public static android.graphics.Bitmap addCornerBorder(final android.graphics.Bitmap src,
                                                          @android.support.annotation.IntRange(from = 1) final int borderSize,
                                                          @android.support.annotation.ColorInt final int color,
                                                          @android.support.annotation.FloatRange(from = 0) final float cornerRadius) {
        return addBorder(src, borderSize, color, false, cornerRadius, false);
    }

    /**
     * Return the round corner bitmap with border.
     *
     * @param src          The source of bitmap.
     * @param borderSize   The size of border.
     * @param color        The color of border.
     * @param cornerRadius The radius of corner.
     * @param recycle      True to recycle the source of bitmap, false otherwise.
     * @return the round corner bitmap with border
     */
    public static android.graphics.Bitmap addCornerBorder(final android.graphics.Bitmap src,
                                                          @android.support.annotation.IntRange(from = 1) final int borderSize,
                                                          @android.support.annotation.ColorInt final int color,
                                                          @android.support.annotation.FloatRange(from = 0) final float cornerRadius,
                                                          final boolean recycle) {
        return addBorder(src, borderSize, color, false, cornerRadius, recycle);
    }

    /**
     * Return the round bitmap with border.
     *
     * @param src        The source of bitmap.
     * @param borderSize The size of border.
     * @param color      The color of border.
     * @return the round bitmap with border
     */
    public static android.graphics.Bitmap addCircleBorder(final android.graphics.Bitmap src,
                                                          @android.support.annotation.IntRange(from = 1) final int borderSize,
                                                          @android.support.annotation.ColorInt final int color) {
        return addBorder(src, borderSize, color, true, 0, false);
    }

    /**
     * Return the round bitmap with border.
     *
     * @param src        The source of bitmap.
     * @param borderSize The size of border.
     * @param color      The color of border.
     * @param recycle    True to recycle the source of bitmap, false otherwise.
     * @return the round bitmap with border
     */
    public static android.graphics.Bitmap addCircleBorder(final android.graphics.Bitmap src,
                                                          @android.support.annotation.IntRange(from = 1) final int borderSize,
                                                          @android.support.annotation.ColorInt final int color,
                                                          final boolean recycle) {
        return addBorder(src, borderSize, color, true, 0, recycle);
    }

    /**
     * Return the bitmap with border.
     *
     * @param src          The source of bitmap.
     * @param borderSize   The size of border.
     * @param color        The color of border.
     * @param isCircle     True to draw circle, false to draw corner.
     * @param cornerRadius The radius of corner.
     * @param recycle      True to recycle the source of bitmap, false otherwise.
     * @return the bitmap with border
     */
    private static android.graphics.Bitmap addBorder(final android.graphics.Bitmap src,
                                                     @android.support.annotation.IntRange(from = 1) final int borderSize,
                                                     @android.support.annotation.ColorInt final int color,
                                                     final boolean isCircle,
                                                     final float cornerRadius,
                                                     final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        int width = ret.getWidth();
        int height = ret.getHeight();
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(android.graphics.Paint.Style.STROKE);
        paint.setStrokeWidth(borderSize);
        if (isCircle) {
            float radius = Math.min(width, height) / 2f - borderSize / 2f;
            canvas.drawCircle(width / 2f, height / 2f, radius, paint);
        } else {
            int halfBorderSize = borderSize >> 1;
            android.graphics.RectF rectF = new android.graphics.RectF(halfBorderSize, halfBorderSize,
                    width - halfBorderSize, height - halfBorderSize);
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);
        }
        return ret;
    }

    /**
     * Return the bitmap with reflection.
     *
     * @param src              The source of bitmap.
     * @param reflectionHeight The height of reflection.
     * @return the bitmap with reflection
     */
    public static android.graphics.Bitmap addReflection(final android.graphics.Bitmap src, final int reflectionHeight) {
        return addReflection(src, reflectionHeight, false);
    }

    /**
     * Return the bitmap with reflection.
     *
     * @param src              The source of bitmap.
     * @param reflectionHeight The height of reflection.
     * @param recycle          True to recycle the source of bitmap, false otherwise.
     * @return the bitmap with reflection
     */
    public static android.graphics.Bitmap addReflection(final android.graphics.Bitmap src,
                                                        final int reflectionHeight,
                                                        final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        final int REFLECTION_GAP = 0;
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.preScale(1, -1);
        android.graphics.Bitmap reflectionBitmap = android.graphics.Bitmap.createBitmap(src, 0, srcHeight - reflectionHeight,
                srcWidth, reflectionHeight, matrix, false);
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(srcWidth, srcHeight + reflectionHeight, src.getConfig());
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        canvas.drawBitmap(src, 0, 0, null);
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        android.graphics.LinearGradient shader = new android.graphics.LinearGradient(
                0, srcHeight,
                0, ret.getHeight() + REFLECTION_GAP,
                0x70FFFFFF,
                0x00FFFFFF,
                android.graphics.Shader.TileMode.MIRROR);
        paint.setShader(shader);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
        canvas.drawRect(0, srcHeight + REFLECTION_GAP, srcWidth, ret.getHeight(), paint);
        if (!reflectionBitmap.isRecycled()) reflectionBitmap.recycle();
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the bitmap with text watermarking.
     *
     * @param src      The source of bitmap.
     * @param content  The content of text.
     * @param textSize The size of text.
     * @param color    The color of text.
     * @param x        The x coordinate of the first pixel.
     * @param y        The y coordinate of the first pixel.
     * @return the bitmap with text watermarking
     */
    public static android.graphics.Bitmap addTextWatermark(final android.graphics.Bitmap src,
                                                           final String content,
                                                           final int textSize,
                                                           @android.support.annotation.ColorInt final int color,
                                                           final float x,
                                                           final float y) {
        return addTextWatermark(src, content, textSize, color, x, y, false);
    }

    /**
     * Return the bitmap with text watermarking.
     *
     * @param src      The source of bitmap.
     * @param content  The content of text.
     * @param textSize The size of text.
     * @param color    The color of text.
     * @param x        The x coordinate of the first pixel.
     * @param y        The y coordinate of the first pixel.
     * @param recycle  True to recycle the source of bitmap, false otherwise.
     * @return the bitmap with text watermarking
     */
    public static android.graphics.Bitmap addTextWatermark(final android.graphics.Bitmap src,
                                                           final String content,
                                                           final float textSize,
                                                           @android.support.annotation.ColorInt final int color,
                                                           final float x,
                                                           final float y,
                                                           final boolean recycle) {
        if (isEmptyBitmap(src) || content == null) return null;
        android.graphics.Bitmap ret = src.copy(src.getConfig(), true);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        paint.setColor(color);
        paint.setTextSize(textSize);
        android.graphics.Rect bounds = new android.graphics.Rect();
        paint.getTextBounds(content, 0, content.length(), bounds);
        canvas.drawText(content, x, y + textSize, paint);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the bitmap with image watermarking.
     *
     * @param src       The source of bitmap.
     * @param watermark The image watermarking.
     * @param x         The x coordinate of the first pixel.
     * @param y         The y coordinate of the first pixel.
     * @param alpha     The alpha of watermark.
     * @return the bitmap with image watermarking
     */
    public static android.graphics.Bitmap addImageWatermark(final android.graphics.Bitmap src,
                                                            final android.graphics.Bitmap watermark,
                                                            final int x, final int y,
                                                            final int alpha) {
        return addImageWatermark(src, watermark, x, y, alpha, false);
    }

    /**
     * Return the bitmap with image watermarking.
     *
     * @param src       The source of bitmap.
     * @param watermark The image watermarking.
     * @param x         The x coordinate of the first pixel.
     * @param y         The y coordinate of the first pixel.
     * @param alpha     The alpha of watermark.
     * @param recycle   True to recycle the source of bitmap, false otherwise.
     * @return the bitmap with image watermarking
     */
    public static android.graphics.Bitmap addImageWatermark(final android.graphics.Bitmap src,
                                                            final android.graphics.Bitmap watermark,
                                                            final int x,
                                                            final int y,
                                                            final int alpha,
                                                            final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = src.copy(src.getConfig(), true);
        if (!isEmptyBitmap(watermark)) {
            android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
            paint.setAlpha(alpha);
            canvas.drawBitmap(watermark, x, y, paint);
        }
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the alpha bitmap.
     *
     * @param src The source of bitmap.
     * @return the alpha bitmap
     */
    public static android.graphics.Bitmap toAlpha(final android.graphics.Bitmap src) {
        return toAlpha(src, false);
    }

    /**
     * Return the alpha bitmap.
     *
     * @param src     The source of bitmap.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the alpha bitmap
     */
    public static android.graphics.Bitmap toAlpha(final android.graphics.Bitmap src, final Boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = src.extractAlpha();
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the gray bitmap.
     *
     * @param src The source of bitmap.
     * @return the gray bitmap
     */
    public static android.graphics.Bitmap toGray(final android.graphics.Bitmap src) {
        return toGray(src, false);
    }

    /**
     * Return the gray bitmap.
     *
     * @param src     The source of bitmap.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the gray bitmap
     */
    public static android.graphics.Bitmap toGray(final android.graphics.Bitmap src, final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.Bitmap ret = android.graphics.Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        android.graphics.Canvas canvas = new android.graphics.Canvas(ret);
        android.graphics.Paint paint = new android.graphics.Paint();
        android.graphics.ColorMatrix colorMatrix = new android.graphics.ColorMatrix();
        colorMatrix.setSaturation(0);
        android.graphics.ColorMatrixColorFilter colorMatrixColorFilter = new android.graphics.ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(src, 0, 0, paint);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the blur bitmap fast.
     * <p>zoom out, blur, zoom in</p>
     *
     * @param src    The source of bitmap.
     * @param scale  The scale(0...1).
     * @param radius The radius(0...25).
     * @return the blur bitmap
     */
    public static android.graphics.Bitmap fastBlur(final android.graphics.Bitmap src,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 1, fromInclusive = false
                                  ) final float scale,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 25, fromInclusive = false
                                  ) final float radius) {
        return fastBlur(src, scale, radius, false, false);
    }

    /**
     * Return the blur bitmap fast.
     * <p>zoom out, blur, zoom in</p>
     *
     * @param src    The source of bitmap.
     * @param scale  The scale(0...1).
     * @param radius The radius(0...25).
     * @return the blur bitmap
     */
    public static android.graphics.Bitmap fastBlur(final android.graphics.Bitmap src,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 1, fromInclusive = false
                                  ) final float scale,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 25, fromInclusive = false
                                  ) final float radius,
                                                   final boolean recycle) {
        return fastBlur(src, scale, radius, recycle, false);
    }

    /**
     * Return the blur bitmap fast.
     * <p>zoom out, blur, zoom in</p>
     *
     * @param src           The source of bitmap.
     * @param scale         The scale(0...1).
     * @param radius        The radius(0...25).
     * @param recycle       True to recycle the source of bitmap, false otherwise.
     * @param isReturnScale True to return the scale blur bitmap, false otherwise.
     * @return the blur bitmap
     */
    public static android.graphics.Bitmap fastBlur(final android.graphics.Bitmap src,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 1, fromInclusive = false
                                  ) final float scale,
                                                   @android.support.annotation.FloatRange(
                                          from = 0, to = 25, fromInclusive = false
                                  ) final float radius,
                                                   final boolean recycle,
                                                   final boolean isReturnScale) {
        if (isEmptyBitmap(src)) return null;
        int width = src.getWidth();
        int height = src.getHeight();
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setScale(scale, scale);
        android.graphics.Bitmap scaleBitmap =
                android.graphics.Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        android.graphics.Paint paint = new android.graphics.Paint(android.graphics.Paint.FILTER_BITMAP_FLAG | android.graphics.Paint.ANTI_ALIAS_FLAG);
        android.graphics.Canvas canvas = new android.graphics.Canvas();
        android.graphics.PorterDuffColorFilter filter = new android.graphics.PorterDuffColorFilter(
                android.graphics.Color.TRANSPARENT, android.graphics.PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.scale(scale, scale);
        canvas.drawBitmap(scaleBitmap, 0, 0, paint);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            scaleBitmap = renderScriptBlur(scaleBitmap, radius, recycle);
        } else {
            scaleBitmap = stackBlur(scaleBitmap, (int) radius, recycle);
        }
        if (scale == 1 || isReturnScale) {
            if (recycle && !src.isRecycled() && scaleBitmap != src) src.recycle();
            return scaleBitmap;
        }
        android.graphics.Bitmap ret = android.graphics.Bitmap.createScaledBitmap(scaleBitmap, width, height, true);
        if (!scaleBitmap.isRecycled()) scaleBitmap.recycle();
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }

    /**
     * Return the blur bitmap using render script.
     *
     * @param src    The source of bitmap.
     * @param radius The radius(0...25).
     * @return the blur bitmap
     */
    @android.support.annotation.RequiresApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static android.graphics.Bitmap renderScriptBlur(final android.graphics.Bitmap src,
                                                           @android.support.annotation.FloatRange(
                                                  from = 0, to = 25, fromInclusive = false
                                          ) final float radius) {
        return renderScriptBlur(src, radius, false);
    }

    /**
     * Return the blur bitmap using render script.
     *
     * @param src     The source of bitmap.
     * @param radius  The radius(0...25).
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the blur bitmap
     */
    @android.support.annotation.RequiresApi(android.os.Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static android.graphics.Bitmap renderScriptBlur(final android.graphics.Bitmap src,
                                                           @android.support.annotation.FloatRange(
                                                  from = 0, to = 25, fromInclusive = false
                                          ) final float radius,
                                                           final boolean recycle) {
        android.renderscript.RenderScript rs = null;
        android.graphics.Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        try {
            rs = android.renderscript.RenderScript.create(Utils.getApp());
            rs.setMessageHandler(new android.renderscript.RenderScript.RSMessageHandler());
            android.renderscript.Allocation input = android.renderscript.Allocation.createFromBitmap(rs,
                    ret,
                    android.renderscript.Allocation.MipmapControl.MIPMAP_NONE,
                    android.renderscript.Allocation.USAGE_SCRIPT);
            android.renderscript.Allocation output = android.renderscript.Allocation.createTyped(rs, input.getType());
            android.renderscript.ScriptIntrinsicBlur blurScript = android.renderscript.ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs));
            blurScript.setInput(input);
            blurScript.setRadius(radius);
            blurScript.forEach(output);
            output.copyTo(ret);
        } finally {
            if (rs != null) {
                rs.destroy();
            }
        }
        return ret;
    }

    /**
     * Return the blur bitmap using stack.
     *
     * @param src    The source of bitmap.
     * @param radius The radius(0...25).
     * @return the blur bitmap
     */
    public static android.graphics.Bitmap stackBlur(final android.graphics.Bitmap src, final int radius) {
        return stackBlur(src, radius, false);
    }

    /**
     * Return the blur bitmap using stack.
     *
     * @param src     The source of bitmap.
     * @param radius  The radius(0...25).
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the blur bitmap
     */
    public static android.graphics.Bitmap stackBlur(final android.graphics.Bitmap src, int radius, final boolean recycle) {
        android.graphics.Bitmap ret = recycle ? src : src.copy(src.getConfig(), true);
        if (radius < 1) {
            radius = 1;
        }
        int w = ret.getWidth();
        int h = ret.getHeight();

        int[] pix = new int[w * h];
        ret.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }
        ret.setPixels(pix, 0, w, 0, 0, w, h);
        return ret;
    }

    /**
     * Save the bitmap.
     *
     * @param src      The source of bitmap.
     * @param filePath The path of file.
     * @param format   The format of the image.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final android.graphics.Bitmap src,
                               final String filePath,
                               final android.graphics.Bitmap.CompressFormat format) {
        return save(src, getFileByPath(filePath), format, false);
    }

    /**
     * Save the bitmap.
     *
     * @param src    The source of bitmap.
     * @param file   The file.
     * @param format The format of the image.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final android.graphics.Bitmap src, final java.io.File file, final android.graphics.Bitmap.CompressFormat format) {
        return save(src, file, format, false);
    }

    /**
     * Save the bitmap.
     *
     * @param src      The source of bitmap.
     * @param filePath The path of file.
     * @param format   The format of the image.
     * @param recycle  True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final android.graphics.Bitmap src,
                               final String filePath,
                               final android.graphics.Bitmap.CompressFormat format,
                               final boolean recycle) {
        return save(src, getFileByPath(filePath), format, recycle);
    }

    /**
     * Save the bitmap.
     *
     * @param src     The source of bitmap.
     * @param file    The file.
     * @param format  The format of the image.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean save(final android.graphics.Bitmap src,
                               final java.io.File file,
                               final android.graphics.Bitmap.CompressFormat format,
                               final boolean recycle) {
        if (isEmptyBitmap(src) || !createFileByDeleteOldFile(file)) return false;
        java.io.OutputStream os = null;
        boolean ret = false;
        try {
            os = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file));
            ret = src.compress(format, 100, os);
            if (recycle && !src.isRecycled()) src.recycle();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Return whether it is a image according to the file name.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isImage(final java.io.File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        return isImage(file.getPath());
    }

    /**
     * Return whether it is a image according to the file name.
     *
     * @param filePath The path of file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isImage(final String filePath) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(filePath, options);
            return options.outWidth != -1 && options.outHeight != -1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return the type of image.
     *
     * @param filePath The path of file.
     * @return the type of image
     */
    public static ls.example.t.zero2line.util.ImageUtils.ImageType getImageType(final String filePath) {
        return getImageType(getFileByPath(filePath));
    }

    /**
     * Return the type of image.
     *
     * @param file The file.
     * @return the type of image
     */
    public static ls.example.t.zero2line.util.ImageUtils.ImageType getImageType(final java.io.File file) {
        if (file == null) return null;
        java.io.InputStream is = null;
        try {
            is = new java.io.FileInputStream(file);
            ls.example.t.zero2line.util.ImageUtils.ImageType type = getImageType(is);
            if (type != null) {
                return type;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static ls.example.t.zero2line.util.ImageUtils.ImageType getImageType(final java.io.InputStream is) {
        if (is == null) return null;
        try {
            byte[] bytes = new byte[12];
            return is.read(bytes) != -1 ? getImageType(bytes) : null;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ls.example.t.zero2line.util.ImageUtils.ImageType getImageType(final byte[] bytes) {
        String type = bytes2HexString(bytes).toUpperCase();
        if (type.contains("FFD8FF")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_JPG;
        } else if (type.contains("89504E47")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_PNG;
        } else if (type.contains("47494638")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_GIF;
        } else if (type.contains("49492A00") || type.contains("4D4D002A")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_TIFF;
        } else if (type.contains("424D")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_BMP;
        } else if (type.startsWith("52494646") && type.endsWith("57454250")) {//524946461c57000057454250-12个字节
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_WEBP;
        } else if (type.contains("00000100") || type.contains("00000200")) {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_ICO;
        } else {
            return ls.example.t.zero2line.util.ImageUtils.ImageType.TYPE_UNKNOWN;
        }
    }

    private static final char[] hexDigits =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String bytes2HexString(final byte[] bytes) {
        if (bytes == null) return "";
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


    private static boolean isJPEG(final byte[] b) {
        return b.length >= 2
                && (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(final byte[] b) {
        return b.length >= 6
                && b[0] == 'G' && b[1] == 'I'
                && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(final byte[] b) {
        return b.length >= 8
                && (b[0] == (byte) 137 && b[1] == (byte) 80
                && b[2] == (byte) 78 && b[3] == (byte) 71
                && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(final byte[] b) {
        return b.length >= 2
                && (b[0] == 0x42) && (b[1] == 0x4d);
    }

    private static boolean isEmptyBitmap(final android.graphics.Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

    ///////////////////////////////////////////////////////////////////////////
    // about compress
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the compressed bitmap using scale.
     *
     * @param src       The source of bitmap.
     * @param newWidth  The new width.
     * @param newHeight The new height.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByScale(final android.graphics.Bitmap src,
                                                          final int newWidth,
                                                          final int newHeight) {
        return scale(src, newWidth, newHeight, false);
    }

    /**
     * Return the compressed bitmap using scale.
     *
     * @param src       The source of bitmap.
     * @param newWidth  The new width.
     * @param newHeight The new height.
     * @param recycle   True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByScale(final android.graphics.Bitmap src,
                                                          final int newWidth,
                                                          final int newHeight,
                                                          final boolean recycle) {
        return scale(src, newWidth, newHeight, recycle);
    }

    /**
     * Return the compressed bitmap using scale.
     *
     * @param src         The source of bitmap.
     * @param scaleWidth  The scale of width.
     * @param scaleHeight The scale of height.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByScale(final android.graphics.Bitmap src,
                                                          final float scaleWidth,
                                                          final float scaleHeight) {
        return scale(src, scaleWidth, scaleHeight, false);
    }

    /**
     * Return the compressed bitmap using scale.
     *
     * @param src         The source of bitmap.
     * @param scaleWidth  The scale of width.
     * @param scaleHeight The scale of height.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @return he compressed bitmap
     */
    public static android.graphics.Bitmap compressByScale(final android.graphics.Bitmap src,
                                                          final float scaleWidth,
                                                          final float scaleHeight,
                                                          final boolean recycle) {
        return scale(src, scaleWidth, scaleHeight, recycle);
    }

    /**
     * Return the compressed bitmap using quality.
     *
     * @param src     The source of bitmap.
     * @param quality The quality.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByQuality(final android.graphics.Bitmap src,
                                                            @android.support.annotation.IntRange(from = 0, to = 100) final int quality) {
        return compressByQuality(src, quality, false);
    }

    /**
     * Return the compressed bitmap using quality.
     *
     * @param src     The source of bitmap.
     * @param quality The quality.
     * @param recycle True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByQuality(final android.graphics.Bitmap src,
                                                            @android.support.annotation.IntRange(from = 0, to = 100) final int quality,
                                                            final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        src.compress(android.graphics.Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Return the compressed bitmap using quality.
     *
     * @param src         The source of bitmap.
     * @param maxByteSize The maximum size of byte.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByQuality(final android.graphics.Bitmap src, final long maxByteSize) {
        return compressByQuality(src, maxByteSize, false);
    }

    /**
     * Return the compressed bitmap using quality.
     *
     * @param src         The source of bitmap.
     * @param maxByteSize The maximum size of byte.
     * @param recycle     True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressByQuality(final android.graphics.Bitmap src,
                                                            final long maxByteSize,
                                                            final boolean recycle) {
        if (isEmptyBitmap(src) || maxByteSize <= 0) return null;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes;
        if (baos.size() <= maxByteSize) {
            bytes = baos.toByteArray();
        } else {
            baos.reset();
            src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 0, baos);
            if (baos.size() >= maxByteSize) {
                bytes = baos.toByteArray();
            } else {
                // find the best quality using binary search
                int st = 0;
                int end = 100;
                int mid = 0;
                while (st < end) {
                    mid = (st + end) / 2;
                    baos.reset();
                    src.compress(android.graphics.Bitmap.CompressFormat.JPEG, mid, baos);
                    int len = baos.size();
                    if (len == maxByteSize) {
                        break;
                    } else if (len > maxByteSize) {
                        end = mid - 1;
                    } else {
                        st = mid + 1;
                    }
                }
                if (end == mid - 1) {
                    baos.reset();
                    src.compress(android.graphics.Bitmap.CompressFormat.JPEG, st, baos);
                }
                bytes = baos.toByteArray();
            }
        }
        if (recycle && !src.isRecycled()) src.recycle();
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src        The source of bitmap.
     * @param sampleSize The sample size.
     * @return the compressed bitmap
     */

    public static android.graphics.Bitmap compressBySampleSize(final android.graphics.Bitmap src, final int sampleSize) {
        return compressBySampleSize(src, sampleSize, false);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src        The source of bitmap.
     * @param sampleSize The sample size.
     * @param recycle    True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressBySampleSize(final android.graphics.Bitmap src,
                                                               final int sampleSize,
                                                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        if (recycle && !src.isRecycled()) src.recycle();
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src       The source of bitmap.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressBySampleSize(final android.graphics.Bitmap src,
                                                               final int maxWidth,
                                                               final int maxHeight) {
        return compressBySampleSize(src, maxWidth, maxHeight, false);
    }

    /**
     * Return the compressed bitmap using sample size.
     *
     * @param src       The source of bitmap.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @param recycle   True to recycle the source of bitmap, false otherwise.
     * @return the compressed bitmap
     */
    public static android.graphics.Bitmap compressBySampleSize(final android.graphics.Bitmap src,
                                                               final int maxWidth,
                                                               final int maxHeight,
                                                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        src.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        if (recycle && !src.isRecycled()) src.recycle();
        return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    /**
     * Return the size of bitmap.
     *
     * @param filePath The path of file.
     * @return the size of bitmap
     */
    public static int[] getSize(String filePath) {
        return getSize(getFileByPath(filePath));
    }

    /**
     * Return the size of bitmap.
     *
     * @param file The file.
     * @return the size of bitmap
     */
    public static int[] getSize(java.io.File file) {
        if (file == null) return new int[]{0, 0};
        android.graphics.BitmapFactory.Options opts = new android.graphics.BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        return new int[]{opts.outWidth, opts.outHeight};
    }

    /**
     * Return the sample size.
     *
     * @param options   The options.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     * @return the sample size
     */
    private static int calculateInSampleSize(final android.graphics.BitmapFactory.Options options,
                                             final int maxWidth,
                                             final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
        }
        return inSampleSize;
    }

    ///////////////////////////////////////////////////////////////////////////
    // other utils methods
    ///////////////////////////////////////////////////////////////////////////

    private static java.io.File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new java.io.File(filePath);
    }

    private static boolean createFileByDeleteOldFile(final java.io.File file) {
        if (file == null) return false;
        if (file.exists() && !file.delete()) return false;
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean createOrExistsDir(final java.io.File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static byte[] input2Byte(final java.io.InputStream is) {
        if (is == null) return null;
        try {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum ImageType {
        TYPE_JPG("jpg"),

        TYPE_PNG("png"),

        TYPE_GIF("gif"),

        TYPE_TIFF("tiff"),

        TYPE_BMP("bmp"),

        TYPE_WEBP("webp"),

        TYPE_ICO("ico"),

        TYPE_UNKNOWN("unknown");

        String value;

        ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
