package org.edelweiss.logging.config;

import org.edelweiss.logging.properties.LogProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static org.edelweiss.logging.properties.LogProperties.*;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@Configuration
public class LogThreadConfig {

    @Bean("edelweiss.log.thread.pool.default")
    @ConditionalOnProperty(prefix = "edelweiss.log.prop.thread-pool", name = "enable", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(name = "edelweiss.log.thread.pool.default")
    public ThreadPoolExecutor logThreadPool(LogProperties logProperties) throws InstantiationException, IllegalAccessException {
        LogThreadPoolProp threadPool = logProperties.getThreadPool();
        RejectedExecutionHandler handler = threadPool.getHandler().newInstance();
        return new ThreadPoolExecutor(threadPool.getCorePoolSize(), threadPool.getMaximumPoolSize(), threadPool.getKeepAliveTime(), threadPool.getTimeUnit(), new ArrayBlockingQueue<>(threadPool.getWorkQueueSize()), handler);
    }
}
