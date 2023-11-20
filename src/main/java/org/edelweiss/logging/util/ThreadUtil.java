package org.edelweiss.logging.util;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jingyun
 * @date 2022-09-02
 */
@Slf4j
public class ThreadUtil {


    private static final int availableCoreSize = Runtime.getRuntime().availableProcessors();
    // public static final ExecutorService QZT_SYNC_EXECUTOR = new ThreadPoolExecutor(availableCoreSize + 1, 64, 1L, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    // public static final ExecutorService MBS_SYNC_EXECUTOR = new ThreadPoolExecutor(availableCoreSize + 1, 64, 1L, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    // public static final ExecutorService DEVICE_EXECUTOR = new ThreadPoolExecutor(availableCoreSize * 2, 64, 1L, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    // public static final ExecutorService MONITOR_EXECUTOR = new ThreadPoolExecutor(availableCoreSize * 2, 64, 1L, TimeUnit.HOURS, new LinkedBlockingQueue<>(1000), new ThreadPoolExecutor.AbortPolicy());
    // public static final ScheduledExecutorService MONITOR_SCHEDULE;
    // public static final ScheduledExecutorService KAFKA_SCHEDULE = new ScheduledThreadPoolExecutor(16, new ThreadPoolExecutor.AbortPolicy());
    // public static final ScheduledExecutorService DEVICE_SCHEDULE = new ScheduledThreadPoolExecutor(16, new ThreadPoolExecutor.AbortPolicy());

    static {
        // MonitorDeviceProperties monitorDeviceProperties = ApplicationContextUtil.getSingleBean(MonitorDeviceProperties.class);
        // int copyTotal = monitorDeviceProperties.getSchedule().getVideoCopyTotal();
        // log.info("最大复制线程数 {}", copyTotal);
        // MONITOR_SCHEDULE = new ScheduledThreadPoolExecutor(copyTotal, new ThreadPoolExecutor.AbortPolicy());
    }
}
