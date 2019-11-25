package ls.example.t.zero2line.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/10/09
 *     desc  : utils about close
 * </pre>
 */
public final class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Close the io stream.
     *
     * @param closeables The closeables.
     */
    public static void closeIO(final java.io.Closeable... closeables) {
        if (closeables == null) return;
        for (java.io.Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Close the io stream quietly.
     *
     * @param closeables The closeables.
     */
    public static void closeIOQuietly(final java.io.Closeable... closeables) {
        if (closeables == null) return;
        for (java.io.Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (java.io.IOException ignored) {
                }
            }
        }
    }
}
