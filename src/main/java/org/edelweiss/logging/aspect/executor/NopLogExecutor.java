package org.edelweiss.logging.aspect.executor;

import org.edelweiss.logging.pojo.po.LogPO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NopLogExecutor implements LogExecutor {
    @Override
    public Object execute(LogPO logOperationPO) {
        return null;
    }
}
