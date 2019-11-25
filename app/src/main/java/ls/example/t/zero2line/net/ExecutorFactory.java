package ls.example.t.zero2line.net;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class ExecutorFactory {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private static final java.util.concurrent.Executor DEFAULT_WORK_EXECUTOR = new java.util.concurrent.ThreadPoolExecutor(2 * CPU_COUNT + 1,
            2 * CPU_COUNT + 1,
            30, java.util.concurrent.TimeUnit.SECONDS,
            new java.util.concurrent.LinkedBlockingQueue<Runnable>(128),
            new java.util.concurrent.ThreadFactory() {
                private final java.util.concurrent.atomic.AtomicInteger mCount = new java.util.concurrent.atomic.AtomicInteger(1);

                public Thread newThread(@android.support.annotation.NonNull Runnable r) {
                    return new Thread(r, "http-pool-" + mCount.getAndIncrement());
                }
            }
    );

    private static final java.util.concurrent.Executor DEFAULT_MAIN_EXECUTOR = new java.util.concurrent.Executor() {
        private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

        @Override
        public void execute(@android.support.annotation.NonNull Runnable command) {
            mHandler.post(command);
        }
    };

    public static java.util.concurrent.Executor getDefaultWorkExecutor() {
        return DEFAULT_WORK_EXECUTOR;
    }

    public static java.util.concurrent.Executor getDefaultMainExecutor() {
        return DEFAULT_MAIN_EXECUTOR;
    }
}
