package org.edelweiss.logging.registry;

import lombok.NoArgsConstructor;
import org.edelweiss.logging.aspect.executor.LogExecutor;

import java.util.List;

/**
 * @author Amuro-R
 * @date 2023/11/20
 **/
@NoArgsConstructor
public class LogExecutorRegistry extends SpringBasedRegistry<LogExecutor> {

    public LogExecutorRegistry(List<LogExecutor> executors) {
        for (LogExecutor executor : executors) {
            this.register(executor.getClass(), executor);
        }
    }
}
