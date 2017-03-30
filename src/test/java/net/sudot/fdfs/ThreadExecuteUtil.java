package net.sudot.fdfs;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程执行工具
 * Created by tangjialin on 2017-03-22 0022.
 */
public class ThreadExecuteUtil {
    /** 线程池 */
    private static volatile ThreadPoolExecutor THREAD_POOL_EXECUTOR;
    private static byte[] SYNC_THREAD_POOL_EXECUTOR = new byte[0];

    /**
     * 在线程池中执行一个线程
     * @param runnable 需要执行的线程业务
     */
    public static void execute(Runnable runnable) {
        if (THREAD_POOL_EXECUTOR == null) {
            synchronized (SYNC_THREAD_POOL_EXECUTOR) {
                if (THREAD_POOL_EXECUTOR == null) {
                    int corePoolSize = 5; // 最小空闲池数量
                    int maximumPoolSize = 50;
                    long keepAliveTime = 10L;
                    TimeUnit unit = TimeUnit.SECONDS;
                    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(50);
                    THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
                }
            }
        }
        THREAD_POOL_EXECUTOR.execute(runnable);
    }

    public static void close() {
        THREAD_POOL_EXECUTOR.shutdown();
    }
}
