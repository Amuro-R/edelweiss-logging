package org.edelweiss.logging.aspect.executor;

import org.edelweiss.logging.pojo.po.LogPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleLogExecutor implements LogExecutor {
    @Override
    public Object execute(LogPO logPO) {
        log.info("{}", logPO);
        return null;
    }
}
