package org.edelweiss.logging.log.aspect.executor;

import org.edelweiss.logging.pojo.log.LogPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NopLogOperationExecutor implements LogOperationExecutor {
    @Override
    public Object execute(LogPO logOperationPO) {
        return null;
    }
}

