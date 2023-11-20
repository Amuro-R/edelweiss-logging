package org.edelweiss.logging.aspect.executor;

import org.edelweiss.logging.pojo.po.LogPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConsoleLogOperationExecutor implements LogOperationExecutor {
    @Override
    public Object execute(LogPO logOperationPO) {
        log.info("{}", logOperationPO);
        return null;
    }
}
