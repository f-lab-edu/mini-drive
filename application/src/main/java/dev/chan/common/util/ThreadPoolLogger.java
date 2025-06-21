package dev.chan.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ThreadPoolLogger {

    public static void logExecutorStats(String fileName, ThreadPoolTaskExecutor executor) {
        ThreadPoolExecutor threadPoolExecutor = executor.getThreadPoolExecutor();
        int poolSize = threadPoolExecutor.getPoolSize();
        int activeCount = threadPoolExecutor.getActiveCount();
        int queueSize = threadPoolExecutor.getQueue().size();
        long completedTaskCount = threadPoolExecutor.getCompletedTaskCount();
        int maxPoolSize = threadPoolExecutor.getMaximumPoolSize();
        log.info("[ThreadPool] active: {}, pool size: {}, queue: {}, completed: {}, max pool size {}", activeCount, poolSize, queueSize, completedTaskCount, maxPoolSize);
    }
}
