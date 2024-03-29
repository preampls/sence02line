package ls.example.t.zero2line.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/03/13
 *     desc  : constants of memory
 * </pre>
 */
public final class MemoryConstants {

    public static final int BYTE = 1;
    public static final int KB   = 1024;
    public static final int MB   = 1048576;
    public static final int GB   = 1073741824;

    @android.support.annotation.IntDef({BYTE, KB, MB, GB})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
