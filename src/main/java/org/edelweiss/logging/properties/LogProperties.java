package org.edelweiss.logging.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.edelweiss.logging.annotation.LogExecutorItem;
import org.edelweiss.logging.aspect.executor.LogExecutor;
import org.edelweiss.logging.aspect.processor.ResultPostProcessor;

import java.util.LinkedHashSet;
import java.util.concurrent.*;

@Data
@NoArgsConstructor
public class LogProperties {
    private boolean enable = false;
    private LogExecutorProp executor;
    private LogResultPostProcessorProp processor;
    private LogBizTypeProp bizType;
    private LogTagProp tag;
    private LogResultNameProp resultName;
    private LogThreadPoolProp threadPool;

    @Data
    @NoArgsConstructor
    public static class LogExecutorProp {
        private LogExecutorItemProp global;

    }

    @Data
    @NoArgsConstructor
    public static class LogExecutorItemProp {
        private Class<? extends LogExecutor> clazz;
        private boolean async = true;

        public LogExecutorItemProp(Class<? extends LogExecutor> clazz) {
            this.clazz = clazz;
        }

        public LogExecutorItemProp(Class<? extends LogExecutor> clazz, boolean async) {
            this.clazz = clazz;
            this.async = async;
        }

        public LogExecutorItemProp(LogExecutorItem annotation) {
            this(annotation.clazz(), annotation.async());
        }
    }

    @Data
    @NoArgsConstructor
    public static class LogResultPostProcessorProp {
        private LogResultPostProcessorItemProp global;
    }

    @Data
    @NoArgsConstructor
    public static class LogResultPostProcessorItemProp {
        private Class<? extends ResultPostProcessor> clazz;
    }

    @Data
    @NoArgsConstructor
    public static class LogResultNameProp {
        private String global;
    }

    @Data
    @NoArgsConstructor
    public static class LogTagProp {
        private LinkedHashSet<String> global = new LinkedHashSet<>();
    }

    @Data
    @NoArgsConstructor
    public static class LogBizTypeProp {
        private String global;
    }

    @Data
    @NoArgsConstructor
    public static class LogThreadPoolProp {
        private boolean enable = true;
        private int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        private int maximumPoolSize = 64;
        private long keepAliveTime = 30;
        private TimeUnit timeUnit = TimeUnit.MINUTES;
        private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10000);
        private RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
    }
}
