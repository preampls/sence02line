package ls.example.t.zero2line.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2018/05/08
 *     desc  : utils about thread
 * </pre>
 */
public final class ThreadUtils {

    private static final java.util.Map<Integer, java.util.Map<Integer, java.util.concurrent.ExecutorService>> TYPE_PRIORITY_POOLS = new java.util.HashMap<>();

    private static final java.util.Map<ls.example.t.zero2line.util.ThreadUtils.Task, ls.example.t.zero2line.util.ThreadUtils.TaskInfo> TASK_TASKINFO_MAP = new java.util.concurrent.ConcurrentHashMap<>();

    private static final int   CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final java.util.Timer TIMER     = new java.util.Timer();

    private static final byte TYPE_SINGLE = -1;
    private static final byte TYPE_CACHED = -2;
    private static final byte TYPE_IO     = -4;
    private static final byte TYPE_CPU    = -8;

    private static java.util.concurrent.Executor sDeliver;

    /**
     * Return whether the thread is the main thread.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isMainThread() {
        return android.os.Looper.myLooper() == android.os.Looper.getMainLooper();
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size The size of thread in the pool.
     * @return a fixed thread pool
     */
    public static java.util.concurrent.ExecutorService getFixedPool(@android.support.annotation.IntRange(from = 1) final int size) {
        return getPoolByTypeAndPriority(size);
    }

    /**
     * Return a thread pool that reuses a fixed number of threads
     * operating off a shared unbounded queue, using the provided
     * ThreadFactory to create new threads when needed.
     *
     * @param size     The size of thread in the pool.
     * @param priority The priority of thread in the poll.
     * @return a fixed thread pool
     */
    public static java.util.concurrent.ExecutorService getFixedPool(@android.support.annotation.IntRange(from = 1) final int size,
                                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(size, priority);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @return a single thread pool
     */
    public static java.util.concurrent.ExecutorService getSinglePool() {
        return getPoolByTypeAndPriority(TYPE_SINGLE);
    }

    /**
     * Return a thread pool that uses a single worker thread operating
     * off an unbounded queue, and uses the provided ThreadFactory to
     * create a new thread when needed.
     *
     * @param priority The priority of thread in the poll.
     * @return a single thread pool
     */
    public static java.util.concurrent.ExecutorService getSinglePool(@android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_SINGLE, priority);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @return a cached thread pool
     */
    public static java.util.concurrent.ExecutorService getCachedPool() {
        return getPoolByTypeAndPriority(TYPE_CACHED);
    }

    /**
     * Return a thread pool that creates new threads as needed, but
     * will reuse previously constructed threads when they are
     * available.
     *
     * @param priority The priority of thread in the poll.
     * @return a cached thread pool
     */
    public static java.util.concurrent.ExecutorService getCachedPool(@android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CACHED, priority);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @return a IO thread pool
     */
    public static java.util.concurrent.ExecutorService getIoPool() {
        return getPoolByTypeAndPriority(TYPE_IO);
    }

    /**
     * Return a thread pool that creates (2 * CPU_COUNT + 1) threads
     * operating off a queue which size is 128.
     *
     * @param priority The priority of thread in the poll.
     * @return a IO thread pool
     */
    public static java.util.concurrent.ExecutorService getIoPool(@android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_IO, priority);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @return a cpu thread pool for
     */
    public static java.util.concurrent.ExecutorService getCpuPool() {
        return getPoolByTypeAndPriority(TYPE_CPU);
    }

    /**
     * Return a thread pool that creates (CPU_COUNT + 1) threads
     * operating off a queue which size is 128 and the maximum
     * number of threads equals (2 * CPU_COUNT + 1).
     *
     * @param priority The priority of thread in the poll.
     * @return a cpu thread pool for
     */
    public static java.util.concurrent.ExecutorService getCpuPool(@android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        return getPoolByTypeAndPriority(TYPE_CPU, priority);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size The size of thread in the fixed thread pool.
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByFixed(@android.support.annotation.IntRange(from = 1) final int size, final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(getPoolByTypeAndPriority(size), task);
    }

    /**
     * Executes the given task in a fixed thread pool.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixed(@android.support.annotation.IntRange(from = 1) final int size,
                                          final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                          @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(size, priority), task);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size  The size of thread in the fixed thread pool.
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   final long delay,
                                                   final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(size), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool after the given delay.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixedWithDelay(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   final long delay,
                                                   final java.util.concurrent.TimeUnit unit,
                                                   @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(size, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size   The size of thread in the fixed thread pool.
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   final long period,
                                                   final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size     The size of thread in the fixed thread pool.
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   final long period,
                                                   final java.util.concurrent.TimeUnit unit,
                                                   @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size         The size of thread in the fixed thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   long initialDelay,
                                                   final long period,
                                                   final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(size), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a fixed thread pool at fix rate.
     *
     * @param size         The size of thread in the fixed thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByFixedAtFixRate(@android.support.annotation.IntRange(from = 1) final int size,
                                                   final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                   long initialDelay,
                                                   final long period,
                                                   final java.util.concurrent.TimeUnit unit,
                                                   @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(size, priority), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeBySingle(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE), task);
    }

    /**
     * Executes the given task in a single thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingle(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                           @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long delay,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingleWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long delay,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    long initialDelay,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_SINGLE), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a single thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeBySingleAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    long initialDelay,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_SINGLE, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCached(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED), task);
    }

    /**
     * Executes the given task in a cached thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCached(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                           @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CACHED, priority), task);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long delay,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCachedWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long delay,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    long initialDelay,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CACHED), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cached thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCachedAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    long initialDelay,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit,
                                                    @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CACHED, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByIo(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_IO), task);
    }

    /**
     * Executes the given task in an IO thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIo(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                       @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_IO, priority), task);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                final long delay,
                                                final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIoWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                final long delay,
                                                final java.util.concurrent.TimeUnit unit,
                                                @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_IO, priority), task, delay, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                final long period,
                                                final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                final long period,
                                                final java.util.concurrent.TimeUnit unit,
                                                @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                long initialDelay,
                                                final long period,
                                                final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_IO), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in an IO thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByIoAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                long initialDelay,
                                                final long period,
                                                final java.util.concurrent.TimeUnit unit,
                                                @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_IO, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCpu(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(getPoolByTypeAndPriority(TYPE_CPU), task);
    }

    /**
     * Executes the given task in a cpu thread pool.
     *
     * @param task     The task to execute.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpu(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                        @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        execute(getPoolByTypeAndPriority(TYPE_CPU, priority), task);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 final long delay,
                                                 final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool after the given delay.
     *
     * @param task     The task to execute.
     * @param delay    The time from now to delay execution.
     * @param unit     The time unit of the delay parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpuWithDelay(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 final long delay,
                                                 final java.util.concurrent.TimeUnit unit,
                                                 @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeWithDelay(getPoolByTypeAndPriority(TYPE_CPU, priority), task, delay, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 final long period,
                                                 final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task     The task to execute.
     * @param period   The period between successive executions.
     * @param unit     The time unit of the period parameter.
     * @param priority The priority of thread in the poll.
     * @param <T>      The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 final long period,
                                                 final java.util.concurrent.TimeUnit unit,
                                                 @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU, priority), task, 0, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 long initialDelay,
                                                 final long period,
                                                 final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(getPoolByTypeAndPriority(TYPE_CPU), task, initialDelay, period, unit);
    }

    /**
     * Executes the given task in a cpu thread pool at fix rate.
     *
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param priority     The priority of thread in the poll.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCpuAtFixRate(final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                 long initialDelay,
                                                 final long period,
                                                 final java.util.concurrent.TimeUnit unit,
                                                 @android.support.annotation.IntRange(from = 1, to = 10) final int priority) {
        executeAtFixedRate(
                getPoolByTypeAndPriority(TYPE_CPU, priority), task, initialDelay, period, unit
        );
    }

    /**
     * Executes the given task in a custom thread pool.
     *
     * @param pool The custom thread pool.
     * @param task The task to execute.
     * @param <T>  The type of the task's result.
     */
    public static <T> void executeByCustom(final java.util.concurrent.ExecutorService pool, final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(pool, task);
    }

    /**
     * Executes the given task in a custom thread pool after the given delay.
     *
     * @param pool  The custom thread pool.
     * @param task  The task to execute.
     * @param delay The time from now to delay execution.
     * @param unit  The time unit of the delay parameter.
     * @param <T>   The type of the task's result.
     */
    public static <T> void executeByCustomWithDelay(final java.util.concurrent.ExecutorService pool,
                                                    final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long delay,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeWithDelay(pool, task, delay, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool   The custom thread pool.
     * @param task   The task to execute.
     * @param period The period between successive executions.
     * @param unit   The time unit of the period parameter.
     * @param <T>    The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(final java.util.concurrent.ExecutorService pool,
                                                    final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(pool, task, 0, period, unit);
    }

    /**
     * Executes the given task in a custom thread pool at fix rate.
     *
     * @param pool         The custom thread pool.
     * @param task         The task to execute.
     * @param initialDelay The time to delay first execution.
     * @param period       The period between successive executions.
     * @param unit         The time unit of the initialDelay and period parameters.
     * @param <T>          The type of the task's result.
     */
    public static <T> void executeByCustomAtFixRate(final java.util.concurrent.ExecutorService pool,
                                                    final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                                    long initialDelay,
                                                    final long period,
                                                    final java.util.concurrent.TimeUnit unit) {
        executeAtFixedRate(pool, task, initialDelay, period, unit);
    }

    /**
     * Cancel the given task.
     *
     * @param task The task to cancel.
     */
    public static void cancel(final ls.example.t.zero2line.util.ThreadUtils.Task task) {
        if (task == null) return;
        task.cancel();
    }

    /**
     * Cancel the given tasks.
     *
     * @param tasks The tasks to cancel.
     */
    public static void cancel(final ls.example.t.zero2line.util.ThreadUtils.Task... tasks) {
        if (tasks == null || tasks.length == 0) return;
        for (ls.example.t.zero2line.util.ThreadUtils.Task task : tasks) {
            if (task == null) continue;
            task.cancel();
        }
    }

    /**
     * Cancel the given tasks.
     *
     * @param tasks The tasks to cancel.
     */
    public static void cancel(final java.util.List<ls.example.t.zero2line.util.ThreadUtils.Task> tasks) {
        if (tasks == null || tasks.size() == 0) return;
        for (ls.example.t.zero2line.util.ThreadUtils.Task task : tasks) {
            if (task == null) continue;
            task.cancel();
        }
    }

    /**
     * Cancel the tasks in pool.
     *
     * @param executorService The pool.
     */
    public static void cancel(java.util.concurrent.ExecutorService executorService) {
        if (executorService instanceof ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util) {
            for (java.util.Map.Entry<ls.example.t.zero2line.util.ThreadUtils.Task, ls.example.t.zero2line.util.ThreadUtils.TaskInfo> taskTaskInfoEntry : TASK_TASKINFO_MAP.entrySet()) {
                if (taskTaskInfoEntry.getValue().mService == executorService) {
                    cancel(taskTaskInfoEntry.getKey());
                }
            }
        } else {
            android.util.Log.e("LogUtils", "The executorService is not ThreadUtils's pool.");
        }
    }

    /**
     * Set the deliver.
     *
     * @param deliver The deliver.
     */
    public static void setDeliver(final java.util.concurrent.Executor deliver) {
        sDeliver = deliver;
    }

    private static <T> void execute(final java.util.concurrent.ExecutorService pool, final ls.example.t.zero2line.util.ThreadUtils.Task<T> task) {
        execute(pool, task, 0, 0, null);
    }

    private static <T> void executeWithDelay(final java.util.concurrent.ExecutorService pool,
                                             final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                             final long delay,
                                             final java.util.concurrent.TimeUnit unit) {
        execute(pool, task, delay, 0, unit);
    }

    private static <T> void executeAtFixedRate(final java.util.concurrent.ExecutorService pool,
                                               final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                               long delay,
                                               final long period,
                                               final java.util.concurrent.TimeUnit unit) {
        execute(pool, task, delay, period, unit);
    }

    private static <T> void execute(final java.util.concurrent.ExecutorService pool, final ls.example.t.zero2line.util.ThreadUtils.Task<T> task,
                                    long delay, final long period, final java.util.concurrent.TimeUnit unit) {
        ls.example.t.zero2line.util.ThreadUtils.TaskInfo taskInfo;
        synchronized (TASK_TASKINFO_MAP) {
            if (TASK_TASKINFO_MAP.get(task) != null) {
                android.util.Log.e("ThreadUtils", "Task can only be executed once.");
                return;
            }
            taskInfo = new ls.example.t.zero2line.util.ThreadUtils.TaskInfo(pool);
            TASK_TASKINFO_MAP.put(task, taskInfo);
        }
        if (period == 0) {
            if (delay == 0) {
                pool.execute(task);
            } else {
                java.util.TimerTask timerTask = new java.util.TimerTask() {
                    @Override
                    public void run() {
                        pool.execute(task);
                    }
                };
                taskInfo.mTimerTask = timerTask;
                TIMER.schedule(timerTask, unit.toMillis(delay));
            }
        } else {
            task.setSchedule(true);
            java.util.TimerTask timerTask = new java.util.TimerTask() {
                @Override
                public void run() {
                    pool.execute(task);
                }
            };
            taskInfo.mTimerTask = timerTask;
            TIMER.scheduleAtFixedRate(timerTask, unit.toMillis(delay), unit.toMillis(period));
        }
    }

    private static java.util.concurrent.ExecutorService getPoolByTypeAndPriority(final int type) {
        return getPoolByTypeAndPriority(type, Thread.NORM_PRIORITY);
    }

    private static java.util.concurrent.ExecutorService getPoolByTypeAndPriority(final int type, final int priority) {
        synchronized (TYPE_PRIORITY_POOLS) {
            java.util.concurrent.ExecutorService pool;
            java.util.Map<Integer, java.util.concurrent.ExecutorService> priorityPools = TYPE_PRIORITY_POOLS.get(type);
            if (priorityPools == null) {
                priorityPools = new java.util.concurrent.ConcurrentHashMap<>();
                pool = ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util.createPool(type, priority);
                priorityPools.put(priority, pool);
                TYPE_PRIORITY_POOLS.put(type, priorityPools);
            } else {
                pool = priorityPools.get(priority);
                if (pool == null) {
                    pool = ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util.createPool(type, priority);
                    priorityPools.put(priority, pool);
                }
            }
            return pool;
        }
    }

    static final class ThreadPoolExecutor4Util extends java.util.concurrent.ThreadPoolExecutor {

        private static java.util.concurrent.ExecutorService createPool(final int type, final int priority) {
            switch (type) {
                case TYPE_SINGLE:
                    return new ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util(1, 1,
                            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
                            new ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util(),
                            new ls.example.t.zero2line.util.ThreadUtils.UtilsThreadFactory("single", priority)
                    );
                case TYPE_CACHED:
                    return new ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util(0, 128,
                            60L, java.util.concurrent.TimeUnit.SECONDS,
                            new ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util(true),
                            new ls.example.t.zero2line.util.ThreadUtils.UtilsThreadFactory("cached", priority)
                    );
                case TYPE_IO:
                    return new ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util(2 * CPU_COUNT + 1, 2 * CPU_COUNT + 1,
                            30, java.util.concurrent.TimeUnit.SECONDS,
                            new ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util(),
                            new ls.example.t.zero2line.util.ThreadUtils.UtilsThreadFactory("io", priority)
                    );
                case TYPE_CPU:
                    return new ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util(CPU_COUNT + 1, 2 * CPU_COUNT + 1,
                            30, java.util.concurrent.TimeUnit.SECONDS,
                            new ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util(true),
                            new ls.example.t.zero2line.util.ThreadUtils.UtilsThreadFactory("cpu", priority)
                    );
                default:
                    return new ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util(type, type,
                            0L, java.util.concurrent.TimeUnit.MILLISECONDS,
                            new ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util(),
                            new ls.example.t.zero2line.util.ThreadUtils.UtilsThreadFactory("fixed(" + type + ")", priority)
                    );
            }
        }

        private final java.util.concurrent.atomic.AtomicInteger mSubmittedCount = new java.util.concurrent.atomic.AtomicInteger();

        private ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util mWorkQueue;

        ThreadPoolExecutor4Util(int corePoolSize, int maximumPoolSize,
                                long keepAliveTime, java.util.concurrent.TimeUnit unit,
                                ls.example.t.zero2line.util.ThreadUtils.LinkedBlockingQueue4Util workQueue,
                                java.util.concurrent.ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize,
                    keepAliveTime, unit,
                    workQueue,
                    threadFactory
            );
            workQueue.mPool = this;
            mWorkQueue = workQueue;
        }

        private int getSubmittedCount() {
            return mSubmittedCount.get();
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            mSubmittedCount.decrementAndGet();
            super.afterExecute(r, t);
        }

        @Override
        public void execute(@android.support.annotation.NonNull Runnable command) {
            if (this.isShutdown()) return;
            mSubmittedCount.incrementAndGet();
            try {
                super.execute(command);
            } catch (java.util.concurrent.RejectedExecutionException ignore) {
                android.util.Log.e("ThreadUtils", "This will not happen!");
                mWorkQueue.offer(command);
            } catch (Throwable t) {
                mSubmittedCount.decrementAndGet();
            }
        }
    }

    private static final class LinkedBlockingQueue4Util extends java.util.concurrent.LinkedBlockingQueue<Runnable> {

        private volatile ls.example.t.zero2line.util.ThreadUtils.ThreadPoolExecutor4Util mPool;

        private int mCapacity = Integer.MAX_VALUE;

        LinkedBlockingQueue4Util() {
            super();
        }

        LinkedBlockingQueue4Util(boolean isAddSubThreadFirstThenAddQueue) {
            super();
            if (isAddSubThreadFirstThenAddQueue) {
                mCapacity = 0;
            }
        }

        LinkedBlockingQueue4Util(int capacity) {
            super();
            mCapacity = capacity;
        }

        @Override
        public boolean offer(@android.support.annotation.NonNull Runnable runnable) {
            if (mCapacity <= size() &&
                    mPool != null && mPool.getPoolSize() < mPool.getMaximumPoolSize()) {
                // create a non-core thread
                return false;
            }
            return super.offer(runnable);
        }
    }

    private static final class UtilsThreadFactory extends java.util.concurrent.atomic.AtomicLong
            implements java.util.concurrent.ThreadFactory {
        private static final java.util.concurrent.atomic.AtomicInteger POOL_NUMBER      = new java.util.concurrent.atomic.AtomicInteger(1);
        private static final long          serialVersionUID = -9209200509960368598L;
        private final        String        namePrefix;
        private final        int           priority;
        private final        boolean       isDaemon;

        UtilsThreadFactory(String prefix, int priority) {
            this(prefix, priority, false);
        }

        UtilsThreadFactory(String prefix, int priority, boolean isDaemon) {
            namePrefix = prefix + "-pool-" +
                    POOL_NUMBER.getAndIncrement() +
                    "-thread-";
            this.priority = priority;
            this.isDaemon = isDaemon;
        }

        @Override
        public Thread newThread(@android.support.annotation.NonNull Runnable r) {
            Thread t = new Thread(r, namePrefix + getAndIncrement()) {
                @Override
                public void run() {
                    try {
                        super.run();
                    } catch (Throwable t) {
                        android.util.Log.e("ThreadUtils", "Request threw uncaught throwable", t);
                    }
                }
            };
            t.setDaemon(isDaemon);
            t.setUncaughtExceptionHandler(new java.lang.Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    System.out.println(e);
                }
            });
            t.setPriority(priority);
            return t;
        }
    }

    public abstract static class SimpleTask<T> extends ls.example.t.zero2line.util.ThreadUtils.Task<T> {

        @Override
        public void onCancel() {
            android.util.Log.e("ThreadUtils", "onCancel: " + Thread.currentThread());
        }

        @Override
        public void onFail(Throwable t) {
            android.util.Log.e("ThreadUtils", "onFail: ", t);
        }

    }

    public abstract static class Task<T> implements Runnable {

        private static final int NEW         = 0;
        private static final int RUNNING     = 1;
        private static final int EXCEPTIONAL = 2;
        private static final int COMPLETING  = 3;
        private static final int CANCELLED   = 4;
        private static final int INTERRUPTED = 5;
        private static final int TIMEOUT     = 6;

        private final java.util.concurrent.atomic.AtomicInteger state = new java.util.concurrent.atomic.AtomicInteger(NEW);

        private volatile boolean isSchedule;
        private volatile Thread  runner;

        private java.util.Timer mTimer;

        private java.util.concurrent.Executor deliver;

        public abstract T doInBackground() throws Throwable;

        public abstract void onSuccess(T result);

        public abstract void onCancel();

        public abstract void onFail(Throwable t);

        @Override
        public void run() {
            if (isSchedule) {
                if (runner == null) {
                    if (!state.compareAndSet(NEW, RUNNING)) return;
                    runner = Thread.currentThread();
                } else {
                    if (state.get() != RUNNING) return;
                }
            } else {
                if (!state.compareAndSet(NEW, RUNNING)) return;
                runner = Thread.currentThread();
            }
            try {
                final T result = doInBackground();
                if (isSchedule) {
                    if (state.get() != RUNNING) return;
                    getDeliver().execute(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(result);
                        }
                    });
                } else {
                    if (!state.compareAndSet(RUNNING, COMPLETING)) return;
                    getDeliver().execute(new Runnable() {
                        @Override
                        public void run() {
                            onSuccess(result);
                            onDone();
                        }
                    });
                }
            } catch (InterruptedException ignore) {
                state.compareAndSet(CANCELLED, INTERRUPTED);
            } catch (final Throwable throwable) {
                if (!state.compareAndSet(RUNNING, EXCEPTIONAL)) return;
                getDeliver().execute(new Runnable() {
                    @Override
                    public void run() {
                        onFail(throwable);
                        onDone();
                    }
                });
            }
        }

        public void cancel() {
            cancel(true);
        }

        public void cancel(boolean mayInterruptIfRunning) {
            synchronized (state) {
                if (state.get() > RUNNING) return;
                state.set(CANCELLED);
            }
            if (mayInterruptIfRunning) {
                if (runner != null) {
                    runner.interrupt();
                }
            }

            getDeliver().execute(new Runnable() {
                @Override
                public void run() {
                    onCancel();
                    onDone();
                }
            });
        }

        private void timeout() {
            synchronized (state) {
                if (state.get() > RUNNING) return;
                state.set(TIMEOUT);
            }
            if (runner != null) {
                runner.interrupt();
            }
            onDone();
        }


        public boolean isCanceled() {
            return state.get() >= CANCELLED;
        }

        public boolean isDone() {
            return state.get() > RUNNING;
        }

        public ls.example.t.zero2line.util.ThreadUtils.Task<T> setDeliver(java.util.concurrent.Executor deliver) {
            this.deliver = deliver;
            return this;
        }

        public void setTimeout(final long timeoutMillis, final ls.example.t.zero2line.util.ThreadUtils.Task.OnTimeoutListener listener) {
            mTimer = new java.util.Timer();
            mTimer.schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    if (!isDone() && listener != null) {
                        timeout();
                        listener.onTimeout();
                    }
                }
            }, timeoutMillis);
        }

        private void setSchedule(boolean isSchedule) {
            this.isSchedule = isSchedule;
        }

        private java.util.concurrent.Executor getDeliver() {
            if (deliver == null) {
                return getGlobalDeliver();
            }
            return deliver;
        }

        @android.support.annotation.CallSuper
        protected void onDone() {
            TASK_TASKINFO_MAP.remove(this);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }

        public interface OnTimeoutListener {
            void onTimeout();
        }
    }

    private static java.util.concurrent.Executor getGlobalDeliver() {
        if (sDeliver == null) {
            sDeliver = new java.util.concurrent.Executor() {
                private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());

                @Override
                public void execute(@android.support.annotation.NonNull Runnable command) {
                    mHandler.post(command);
                }
            };
        }
        return sDeliver;
    }

    private static class TaskInfo {
        private java.util.TimerTask       mTimerTask;
        private java.util.concurrent.ExecutorService mService;

        private TaskInfo(java.util.concurrent.ExecutorService service) {
            mService = service;
        }
    }
}
